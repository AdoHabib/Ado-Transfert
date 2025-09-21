
import java.rmi.RemoteException;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Implementazione del server RMI per il sistema Ado-Transfert
 * Gestisce tutte le operazioni bancarie e l'accesso al database
 */
public class App {
    
    // Configurazione database - Caricata da variabili d'ambiente o file
    private String DB_HOST;
    private String DB_PORT;
    private String DB_NAME;
    private String DB_USER;
    private String DB_PASSWORD;
    private String URL;
    
    // Configurazione email Gmail - Caricata da variabili d'ambiente o file
    private String EMAIL_HOST;
    private String EMAIL_PORT;
    private String EMAIL_USERNAME;
    private String EMAIL_PASSWORD;
    private String EMAIL_FROM;
    private String EMAIL_FROM_NAME;
    
    // Mappa per gestire le sessioni utente attive
    private java.util.Map<String, Utente> sessioniAttive = new java.util.concurrent.ConcurrentHashMap<>();
    
    // Rate limiting per il recupero password
    private final ConcurrentHashMap<String, PasswordRecoveryAttempt> recoveryAttempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS_PER_HOUR = 3;
    private static final int MAX_ATTEMPTS_PER_DAY = 5;
    
    // Pattern per validazione email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Classe per tracciare i tentativi di recupero password
    private static class PasswordRecoveryAttempt {
        private final String identifier; // email o IP
        private final LocalDateTime firstAttempt;
        private int attempts;
        
        public PasswordRecoveryAttempt(String identifier) {
            this.identifier = identifier;
            this.firstAttempt = LocalDateTime.now();
            this.attempts = 1;
        }
        
        public boolean canAttempt() {
            LocalDateTime now = LocalDateTime.now();
            long hoursSinceFirst = java.time.Duration.between(firstAttempt, now).toHours();
            long daysSinceFirst = java.time.Duration.between(firstAttempt, now).toDays();
            
            return attempts < MAX_ATTEMPTS_PER_HOUR && attempts < MAX_ATTEMPTS_PER_DAY;
        }
        
        public void incrementAttempts() {
            this.attempts++;
        }
        
        public String getIdentifier() { return identifier; }
        public LocalDateTime getFirstAttempt() { return firstAttempt; }
        public int getAttempts() { return attempts; }
    }

    public App(String dbPassword) {
        try {
            // Carica configurazione database in modo sicuro
            loadDatabaseConfiguration();
            
            // Carica configurazione email in modo sicuro
            loadEmailConfiguration();
            
            // Se viene passata una password, usa quella (per compatibilità)
            if (dbPassword != null && !dbPassword.isEmpty()) {
                this.DB_PASSWORD = dbPassword;
            }
            
            // Costruisci URL database
            buildDatabaseURL();
            
            initializeDatabase();
            System.out.println("✅ Database inizializzato con successo");
        } catch (Exception e) {
            System.err.println("❌ Errore durante l'inizializzazione: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carica la configurazione database in modo sicuro da variabili d'ambiente o file
     */
    private void loadDatabaseConfiguration() {
        try {
            // Priorità 1: Variabili d'ambiente (più sicure)
            DB_HOST = System.getenv("ADO_DB_HOST");
            DB_PORT = System.getenv("ADO_DB_PORT");
            DB_NAME = System.getenv("ADO_DB_NAME");
            DB_USER = System.getenv("ADO_DB_USER");
            DB_PASSWORD = System.getenv("ADO_DB_PASSWORD");
            
            // Se le variabili d'ambiente non sono impostate, usa valori di default per sviluppo locale
            if (DB_HOST == null) DB_HOST = "localhost";
            if (DB_PORT == null) DB_PORT = "3306";
            if (DB_NAME == null) DB_NAME = "ado_transfert";
            if (DB_USER == null) DB_USER = "root";
            if (DB_PASSWORD == null) DB_PASSWORD = "1234"; // Password di default per sviluppo
            
            System.out.println("✅ Configurazione database caricata da: " + 
                (System.getenv("ADO_DB_HOST") != null ? "Variabili d'ambiente" : "Valori di default"));
            System.out.println("   Host: " + DB_HOST + ":" + DB_PORT);
            System.out.println("   Database: " + DB_NAME);
            System.out.println("   User: " + DB_USER);
            
        } catch (Exception e) {
            System.err.println("❌ Errore caricamento configurazione database: " + e.getMessage());
        }
    }
    
    /**
     * Costruisce l'URL del database
     */
    private void buildDatabaseURL() {
        // Supporta sia Railway che MySQL locale
        if (DB_HOST.contains("railway.app") || DB_HOST.contains("containers-")) {
            // Railway MySQL
            URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=true&serverTimezone=UTC";
        } else {
            // MySQL locale
            URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";
        }
        
        System.out.println("🔗 URL Database: " + URL.replace(DB_PASSWORD, "***"));
    }

    /**
     * Carica la configurazione email in modo sicuro da variabili d'ambiente o file
     */
    private void loadEmailConfiguration() {
        try {
            // Priorità 1: Variabili d'ambiente (più sicure)
            EMAIL_HOST = System.getenv("ADO_EMAIL_HOST");
            EMAIL_PORT = System.getenv("ADO_EMAIL_PORT");
            EMAIL_USERNAME = System.getenv("ADO_EMAIL_USERNAME");
            EMAIL_PASSWORD = System.getenv("ADO_EMAIL_PASSWORD");
            EMAIL_FROM = System.getenv("ADO_EMAIL_FROM");
            EMAIL_FROM_NAME = System.getenv("ADO_EMAIL_FROM_NAME");
            
            // Se le variabili d'ambiente non sono impostate, carica dal file
            if (EMAIL_HOST == null || EMAIL_USERNAME == null || EMAIL_PASSWORD == null) {
                loadFromConfigFile();
            }
            
            // Valori di default se tutto è null
            if (EMAIL_HOST == null) EMAIL_HOST = "smtp.gmail.com";
            if (EMAIL_PORT == null) EMAIL_PORT = "587";
            if (EMAIL_FROM == null && EMAIL_USERNAME != null) EMAIL_FROM = EMAIL_USERNAME;
            if (EMAIL_FROM_NAME == null) EMAIL_FROM_NAME = "Ado-Transfert Sistema";
            
            // Verifica configurazione essenziale
            if (EMAIL_USERNAME == null || EMAIL_PASSWORD == null) {
                System.err.println("⚠️ ATTENZIONE: Configurazione email non trovata!");
                System.err.println("Imposta le variabili d'ambiente o crea email_config.properties");
                System.err.println("Variabili richieste: ADO_EMAIL_USERNAME, ADO_EMAIL_PASSWORD");
            } else {
                System.out.println("✅ Configurazione email caricata da: " + 
                    (System.getenv("ADO_EMAIL_USERNAME") != null ? "Variabili d'ambiente" : "File di configurazione"));
            }
            
        } catch (Exception e) {
            System.err.println("❌ Errore caricamento configurazione email: " + e.getMessage());
        }
    }
    
    /**
     * Carica configurazione da file properties
     */
    private void loadFromConfigFile() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("email_config.properties");
            props.load(fis);
            fis.close();
            
            // Carica solo se non già impostato dalle variabili d'ambiente
            if (EMAIL_HOST == null) EMAIL_HOST = props.getProperty("email.host");
            if (EMAIL_PORT == null) EMAIL_PORT = props.getProperty("email.port");
            if (EMAIL_USERNAME == null) EMAIL_USERNAME = props.getProperty("email.username");
            if (EMAIL_PASSWORD == null) EMAIL_PASSWORD = props.getProperty("email.password");
            if (EMAIL_FROM == null) EMAIL_FROM = props.getProperty("email.from");
            if (EMAIL_FROM_NAME == null) EMAIL_FROM_NAME = props.getProperty("email.from.name");
            
        } catch (IOException e) {
            System.err.println("⚠️ File email_config.properties non trovato o non leggibile");
        }
    }

    // === AUTENTICAZIONE E GESTIONE UTENTI ===
    /**
     * Metodo per effettuare il login. Controlla se l'email e la password sono corrette e se l'utente è verificato.
     * @param email
     * @param password
     * @return String
     * @throws RemoteException
     */
    public String login(String email, String password) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM utenti WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    if (hashPassword(password).equals(hashedPassword)) {
                        if (resultSet.getInt("verificato") == 0) {
                            return "ERROR: Il tuo account è in attesa di approvazione.";
                        }

                        // Login riuscito
                        String userID = resultSet.getString("userID");
                        String nome = resultSet.getString("nome");
                        String cognome = resultSet.getString("cognome");
                        String telefono = resultSet.getString("telefono");
                        String tipoUtente = resultSet.getString("tipoUtente");
                        double saldo = resultSet.getDouble("saldo");

                        // Crea l'oggetto utente
                        Utente utente = new Utente(nome, cognome, userID, telefono, email, hashedPassword);
                        utente.setSaldo(saldo);
                        utente.setTipo(tipoUtente);

                        // Carica dati aggiuntivi
                        caricaDatiUtente(connection, utente);
                        
                        // Aggiungi alla sessione attiva
                        sessioniAttive.put(userID, utente);
                        System.out.println("Sessione attiva per l'utente: " + userID);
                        
                        return "SUCCESS: Login effettuato con successo! Benvenuto " + nome + " " + cognome;
                    } else {
                        return "ERROR: Password errata.";
                    }
                } else {
                    return "ERROR: Email non trovata.";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante il login: " + e.getMessage();
        }
    }

    /**
     * Metodo per effettuare il logout.
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String logout(String userID) throws RemoteException {
        if (sessioniAttive.containsKey(userID)) {
            sessioniAttive.remove(userID);
            return "SUCCESS: Logout effettuato con successo!";
        }
        return "ERROR: Nessuna sessione attiva trovata.";
    }

    /**
     * Metodo per creare un nuovo utente.
     * @param nome
     * @param cognome
     * @param userID
     * @param telefono
     * @param email
     * @param password
     * @return String
     * @throws RemoteException
     */
    public String creaUtente(String nome, String cognome, String userID, String telefono, String email, String password) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            // Verifica che l'UserID non esista già
            String sqlCheck = "SELECT userID FROM utenti WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sqlCheck)) {
                statement.setString(1, userID);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return "ERROR: UserID già esistente. Scegli un altro UserID.";
                }
            }

            // Validazione email e password
            if (!Utente.isEmailValida(email)) {
                return "ERROR: Email non valida.";
            }
            if (!Utente.isPasswordValida(password)) {
                return "ERROR: Password non valida (min 8 caratteri, almeno 1 maiuscola, 1 numero).";
            }

            // Hash della password
            String hashedPassword = hashPassword(password);

            // Verifica che l'utente "Sistema" esista
            verificaUtenteSistema(connection);

            // Salvataggio dell'utente nel database
            String sql = "INSERT INTO utenti (nome, cognome, userID, telefono, email, password, tipoUtente) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nome);
                statement.setString(2, cognome);
                statement.setString(3, userID);
                statement.setString(4, telefono);
                statement.setString(5, email);
                statement.setString(6, hashedPassword);
                statement.setString(7, "cliente");
                statement.executeUpdate();

                // Invia notifica all'admin
                String adminID = getAdminID(connection);
                if (adminID != null) {
                    String sqlMessaggio = "INSERT INTO messaggi (mittenteID, destinatarioID, titolo, contenuto) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement statement2 = connection.prepareStatement(sqlMessaggio)) {
                        statement2.setString(1, "Sistema");
                        statement2.setString(2, adminID);
                        statement2.setString(3, "Nuova registrazione!");
                        statement2.setString(4, "Un nuovo utente si è registrato: " + userID + " con telefono " + telefono + " e chiede l'approvazione del suo account.");
                        statement2.executeUpdate();
                    }
                }

                return "SUCCESS: Utente creato con successo! In attesa di approvazione dall'amministratore.";
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante la creazione dell'utente: " + e.getMessage();
        }
    }

    /**
     * Metodo per approvare un utente.
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String approvaUtente(String userID) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sqlApprovazione = "UPDATE utenti SET verificato = 1 WHERE userID = ?";
            try (PreparedStatement approvaStmt = connection.prepareStatement(sqlApprovazione)) {
                approvaStmt.setString(1, userID);
                int rowsUpdated = approvaStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    // Invia notifica all'utente
                    String sqlNotifica = "INSERT INTO messaggi (mittenteID, destinatarioID, titolo, contenuto) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement notificaStmt = connection.prepareStatement(sqlNotifica)) {
                        notificaStmt.setString(1, "Sistema");
                        notificaStmt.setString(2, userID);
                        notificaStmt.setString(3, "Account approvato");
                        notificaStmt.setString(4, "Il tuo account è stato approvato. Ora puoi accedere a tutte le funzionalità.");
                        notificaStmt.executeUpdate();
                    }
                    return "SUCCESS: Utente approvato con successo.";
                } else {
                    return "ERROR: Utente non trovato o già approvato.";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante l'approvazione: " + e.getMessage();
        }
    }

    // === GESTIONE PROFILO ===
    
    /**
     * Metodo per visualizzare il profilo di un utente.
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String visualizzaProfilo(String userID) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== DETTAGLI DEL PROFILO ===\n");
        sb.append("Nome: ").append(utente.getNome()).append("\n");
        sb.append("Cognome: ").append(utente.getCognome()).append("\n");
        sb.append("UserID: ").append(utente.getUserID()).append("\n");
        sb.append("Email: ").append(utente.getEmail()).append("\n");
        sb.append("Telefono: ").append(utente.getTelefono()).append("\n");
        sb.append("Tipo Utente: ").append(utente.getTipo()).append("\n");

        if (utente.getIndirizzo() != null) {
            Indirizzo indirizzo = utente.getIndirizzo();
            sb.append("Indirizzo:\n");
            sb.append(indirizzo.getVia()).append(" ").append(indirizzo.getNumeroCivico()).append("\n");
            sb.append(indirizzo.getCap()).append(" ").append(indirizzo.getCitta()).append(" (").append(indirizzo.getProvincia()).append(")\n");
            if (utente.getNazione() != null && !utente.getNazione().isEmpty()) {
                sb.append(utente.getNazione()).append("\n");
            }
        } else {
            sb.append("Nessun indirizzo registrato.\n");
        }

        return "SUCCESS: " + sb.toString();
    }

    /**
     * Metodo per modificare il profilo di un utente.
     * @param userID
     * @param nome
     * @param cognome
     * @param telefono
     * @return String
     * @throws RemoteException
     */
    public String modificaProfilo(String userID, String nome, String cognome, String telefono) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            // Aggiorna l'oggetto utente
            if (!nome.isEmpty()) utente.setNome(nome);
            if (!cognome.isEmpty()) utente.setCognome(cognome);
            if (!telefono.isEmpty()) utente.setTelefono(telefono);

            // Aggiorna il database
            String sql = "UPDATE utenti SET nome = ?, cognome = ?, telefono = ? WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, utente.getNome());
                statement.setString(2, utente.getCognome());
                statement.setString(3, utente.getTelefono());
                statement.setString(4, userID);
                statement.executeUpdate();
                return "SUCCESS: Profilo modificato con successo!";
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante la modifica del profilo: " + e.getMessage();
        }
    }

    /**
     * Metodo per visualizzare i dettagli del conto di un utente.
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String visualizzaDettagliConto(String userID) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== DETTAGLI DEL CONTO ===\n");
        sb.append("Proprietario: ").append(utente.getNome()).append(" ").append(utente.getCognome()).append("\n");
        sb.append("UserID: ").append(utente.getUserID()).append("\n");
        sb.append("Telefono: ").append(utente.getTelefono()).append("\n");
        sb.append(String.format("Saldo attuale: %.2f EUR\n", utente.getSaldo()));

        // Mostra indirizzo se disponibile
        if (utente.getIndirizzo() != null) {
            Indirizzo indirizzo = utente.getIndirizzo();
            sb.append("Indirizzo:\n");
            sb.append(indirizzo.getVia()).append(" ").append(indirizzo.getNumeroCivico()).append("\n");
            sb.append(indirizzo.getCap()).append(" ").append(indirizzo.getCitta()).append(" (").append(indirizzo.getProvincia()).append(")\n");
            if (utente.getNazione() != null && !utente.getNazione().isEmpty()) {
                sb.append(utente.getNazione()).append("\n");
            }
        } else {
            sb.append("Nessun indirizzo registrato.\n");
        }

        return "SUCCESS: " + sb.toString();
    }

    // === GESTIONE INDIRIZZI ===
    
    /**
     * Metodo per creare un nuovo indirizzo.
     * @param userID
     * @param via
     * @param numeroCivico
     * @param citta
     * @param provincia
     * @param cap
     * @param nazione
     * @return String
     * @throws RemoteException
     */
    public String creaIndirizzo(String userID, String via, String numeroCivico, String citta, String provincia, String cap, String nazione) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        if (utente.getIndirizzo() != null) {
            return "ERROR: Hai già un indirizzo registrato. Usa l'opzione modifica.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            Indirizzo indirizzo = new Indirizzo(via, numeroCivico, citta, provincia, cap);
            utente.setIndirizzo(indirizzo);
            utente.setNazione(nazione);

            String sql = "INSERT INTO indirizzi (userID, via, numeroCivico, citta, provincia, cap, nazione) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userID);
                statement.setString(2, via);
                statement.setString(3, numeroCivico);
                statement.setString(4, citta);
                statement.setString(5, provincia);
                statement.setString(6, cap);
                statement.setString(7, nazione);
                statement.executeUpdate();
                return "SUCCESS: Indirizzo creato con successo!";
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante la creazione dell'indirizzo: " + e.getMessage();
        }
    }

    /**
     * Metodo per modificare un indirizzo.
     * @param userID
     * @param via
     * @param numeroCivico
     * @param citta
     * @param provincia
     * @param cap
     * @param nazione
     * @return String
     * @throws RemoteException
     */
    public String modificaIndirizzo(String userID, String via, String numeroCivico, String citta, String provincia, String cap, String nazione) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        if (utente.getIndirizzo() == null) {
            return "ERROR: Nessun indirizzo registrato. Creane uno nuovo.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            // Aggiorna l'oggetto indirizzo
            if (!via.isEmpty()) utente.getIndirizzo().setVia(via);
            if (!numeroCivico.isEmpty()) utente.getIndirizzo().setNumeroCivico(numeroCivico);
            if (!citta.isEmpty()) utente.getIndirizzo().setCitta(citta);
            if (!provincia.isEmpty()) utente.getIndirizzo().setProvincia(provincia);
            if (!cap.isEmpty()) utente.getIndirizzo().setCap(cap);
            if (!nazione.isEmpty()) utente.setNazione(nazione);

            // Aggiorna il database
            String sql = "UPDATE indirizzi SET via = ?, numeroCivico = ?, citta = ?, provincia = ?, cap = ?, nazione = ? WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, utente.getIndirizzo().getVia());
                statement.setString(2, utente.getIndirizzo().getNumeroCivico());
                statement.setString(3, utente.getIndirizzo().getCitta());
                statement.setString(4, utente.getIndirizzo().getProvincia());
                statement.setString(5, utente.getIndirizzo().getCap());
                statement.setString(6, utente.getNazione());
                statement.setString(7, userID);
                statement.executeUpdate();
                return "SUCCESS: Indirizzo modificato con successo!";
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante la modifica dell'indirizzo: " + e.getMessage();
        }
    }

    // === TRANSazioni FINANZIARIE ===
    
    /**
     * Metodo per effettuare un versamento.
     * @param userID
     * @param importo
     * @return String
     * @throws RemoteException
     */
    public String faiVersamento(String userID, double importo) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        if (importo <= 0) {
            return "ERROR: Importo non valido.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE utenti SET saldo = saldo + ? WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, importo);
                statement.setString(2, userID);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    utente.setSaldo(utente.getSaldo() + importo);
                    registraTransazione(connection, "VERSAMENTO", importo, userID, userID);
                    return String.format("SUCCESS: Versamento di %.2f EUR effettuato con successo. Nuovo saldo: %.2f EUR", importo, utente.getSaldo());
                } else {
                    return "ERROR: Errore durante il versamento.";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante il versamento: " + e.getMessage();
        }
    }

    /**
     * Metodo per effettuare un prelievo.
     * @param userID
     * @param importo
     * @return String
     * @throws RemoteException
     */
    public String faiPrelievo(String userID, double importo) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        if (importo <= 0) {
            return "ERROR: Importo non valido.";
        }

        if (utente.getSaldo() < importo) {
            return "ERROR: Saldo insufficiente.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE utenti SET saldo = saldo - ? WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, importo);
                statement.setString(2, userID);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    utente.setSaldo(utente.getSaldo() - importo);
                    registraTransazione(connection, "PRELIEVO", importo, userID, userID);
                    return String.format("SUCCESS: Prelievo di %.2f EUR effettuato con successo. Nuovo saldo: %.2f EUR", importo, utente.getSaldo());
                } else {
                    return "ERROR: Errore durante il prelievo.";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante il prelievo: " + e.getMessage();
        }
    }

    /**
     * Metodo per effettuare un trasferimento.
     * @param userID
     * @param telefonoDestinatario
     * @param importo
     * @return String
     * @throws RemoteException
     */
    public String faiTrasferimento(String userID, String telefonoDestinatario, double importo) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        if (importo <= 0) {
            return "ERROR: Importo non valido.";
        }

        if (utente.getSaldo() < importo) {
            return "ERROR: Saldo insufficiente.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try {
                // Verifica che il telefono destinatario esista
                String sqlVerifica = "SELECT userID FROM utenti WHERE telefono = ? AND userID != ? AND verificato = 1";
                String destinatarioID = null;

                try (PreparedStatement verificaStmt = connection.prepareStatement(sqlVerifica)) {
                    verificaStmt.setString(1, telefonoDestinatario);
                    verificaStmt.setString(2, userID);
                    try (ResultSet rs = verificaStmt.executeQuery()) {
                        if (rs.next()) {
                            destinatarioID = rs.getString("userID");
                        } else {
                            return "ERROR: Destinatario non trovato o non verificato.";
                        }
                    }
                }

                // Prelievo dal mittente
                String sqlPrelievo = "UPDATE utenti SET saldo = saldo - ? WHERE userID = ?";
                try (PreparedStatement prelievoStmt = connection.prepareStatement(sqlPrelievo)) {
                    prelievoStmt.setDouble(1, importo);
                    prelievoStmt.setString(2, userID);
                    int rowsUpdated = prelievoStmt.executeUpdate();
                    if (rowsUpdated == 0) {
                        throw new SQLException("Errore durante il prelievo.");
                    }
                }

                // Versamento al destinatario
                String sqlVersamento = "UPDATE utenti SET saldo = saldo + ? WHERE userID = ?";
                try (PreparedStatement versamentoStmt = connection.prepareStatement(sqlVersamento)) {
                    versamentoStmt.setDouble(1, importo);
                    versamentoStmt.setString(2, destinatarioID);
                    int rowsUpdated = versamentoStmt.executeUpdate();
                    if (rowsUpdated == 0) {
                        throw new SQLException("Errore durante il versamento.");
                    }
                }

                // Aggiorna il saldo locale
                utente.setSaldo(utente.getSaldo() - importo);

                // Registra le transazioni
                registraTransazione(connection, "TRASFERIMENTO_OUT", importo, userID, destinatarioID);
                registraTransazione(connection, "TRASFERIMENTO_IN", importo, userID, destinatarioID);

                connection.commit();
                return String.format("SUCCESS: Trasferimento di %.2f EUR effettuato con successo. Nuovo saldo: %.2f EUR", importo, utente.getSaldo());
            } catch (SQLException e) {
                connection.rollback();
                return "ERROR: Errore durante il trasferimento: " + e.getMessage();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return "ERROR: Errore di connessione: " + e.getMessage();
        }
    }

    /**
     * Metodo per visualizzare lo storico delle transazioni
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String visualizzaStoricoTransazioni(String userID) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM transazioni WHERE mittenteID = ? OR destinatarioID = ? ORDER BY data_transazione DESC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userID);
                statement.setString(2, userID);
                ResultSet resultSet = statement.executeQuery();

                StringBuilder sb = new StringBuilder();
                sb.append("=== STORICO TRANSazioni ===\n");
                sb.append("Data\t\tTipo\t\tImporto\t\tMittente\tDestinatario\n");
                sb.append("---------------------------------------------------------------\n");

                boolean hasTransactions = false;
                while (resultSet.next()) {
                    hasTransactions = true;
                    String tipo = resultSet.getString("tipo");
                    double importo = resultSet.getDouble("importo");
                    String mittente = resultSet.getString("mittenteID");
                    String destinatario = resultSet.getString("destinatarioID");
                    String data = resultSet.getString("data_transazione");

                    sb.append(String.format("%-15s %-15s %-10.2f %-15s %-15s\n",
                            data.substring(0, 16),
                            tipo,
                            importo,
                            mittente.equals(userID) ? "Tu" : mittente,
                            destinatario.equals(userID) ? "Tu" : destinatario));
                }

                if (!hasTransactions) {
                    sb.append("Nessuna transazione trovata.\n");
                }

                return "SUCCESS: " + sb.toString();
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante il recupero delle transazioni: " + e.getMessage();
        }
    }

    // === SISTEMA MESSAGGISTICA ===
    
    /**
     * Metodo per visualizzare i messaggi ricevuti.
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String visualizzaMessaggi(String userID) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        if (utente.getMessaggi().isEmpty()) {
            return "SUCCESS: Nessun messaggio trovato.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== MESSAGGI RICEVUTI ===\n");
        for (Messaggio messaggio : utente.getMessaggi()) {
            sb.append("ID: ").append(messaggio.getId()).append("\n");
            sb.append("Mittente: ").append(messaggio.getMittenteID()).append("\n");
            sb.append("Data: ").append(messaggio.getDataInvio()).append("\n");
            sb.append("Titolo: ").append(messaggio.getTitolo()).append("\n");
            sb.append("Contenuto: ").append(messaggio.getContenuto()).append("\n");
            sb.append("------------------------------\n");
        }

        return "SUCCESS: " + sb.toString();
    }

    /**
     * Metodo per creare un nuovo messaggio.
     * @param userID
     * @param destinatarioID
     * @param titolo
     * @param contenuto
     * @return String
     * @throws RemoteException
     */
    public String creaMessaggio(String userID, String destinatarioID, String titolo, String contenuto) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            // Verifica esistenza destinatario
            String sql = "SELECT userID FROM utenti WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, destinatarioID);
                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) {
                    return "ERROR: Destinatario non trovato.";
                }

                String sqlInsert = "INSERT INTO messaggi (mittenteID, destinatarioID, titolo, contenuto) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                    insertStatement.setString(1, userID);
                    insertStatement.setString(2, destinatarioID);
                    insertStatement.setString(3, titolo);
                    insertStatement.setString(4, contenuto);
                    insertStatement.executeUpdate();
                    return "SUCCESS: Messaggio inviato con successo!";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante l'invio del messaggio: " + e.getMessage();
        }
    }

    /**
     * Metodo per eliminare un messaggio.
     * @param userID
     * @param idMessaggio
     * @return String
     * @throws RemoteException
     */
    public String eliminaMessaggio(String userID, int idMessaggio) throws RemoteException {
        Utente utente = sessioniAttive.get(userID);
        if (utente == null) {
            return "ERROR: Sessione non valida. Effettua il login.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM messaggi WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idMessaggio);
                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    return "SUCCESS: Messaggio eliminato con successo.";
                } else {
                    return "ERROR: Messaggio non trovato.";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante l'eliminazione: " + e.getMessage();
        }
    }

    // === AMMINISTRAZIONE (SOLO ADMIN) ===
    
    /**
     * Metodo per visualizzare tutti gli utenti (solo admin).
     * 
     * @param adminID
     * @return String
     * @throws RemoteException
     */
    public String visualizzaUtenti(String adminID) throws RemoteException {
        Utente admin = sessioniAttive.get(adminID);
        if (admin == null || !"admin".equalsIgnoreCase(admin.getTipo())) {
            return "ERROR: Accesso negato. Solo gli amministratori possono accedere a questa funzione.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT userID, nome, cognome, email, tipoUtente, verificato, telefono, saldo FROM utenti";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                StringBuilder sb = new StringBuilder();
                sb.append("=== ELENCO UTENTI ===\n");
                sb.append("ID\tNome\tCognome\tEmail\tTipo\tTelefono\tSaldo\tApprovato\n");
                while (resultSet.next()) {
                    sb.append(resultSet.getString("userID")).append("\t")
                      .append(resultSet.getString("nome")).append("\t")
                      .append(resultSet.getString("cognome")).append("\t")
                      .append(resultSet.getString("email")).append("\t")
                      .append(resultSet.getString("tipoUtente")).append("\t")
                      .append(resultSet.getString("telefono")).append("\t")
                      .append(resultSet.getDouble("saldo")).append("\t")
                      .append(resultSet.getInt("verificato") == 1 ? "Sì" : "No").append("\n");
                }
                return "SUCCESS: " + sb.toString();
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante il recupero degli utenti: " + e.getMessage();
        }
    }

    /**
     * Metodo per modificare il tipo di un utente (admin o cliente).
     * @param adminID
     * @param userID
     * @param nuovoTipo
     * @return String
     * @throws RemoteException
     */
    public String modificaTipoUtente(String adminID, String userID, String nuovoTipo) throws RemoteException {
        Utente admin = sessioniAttive.get(adminID);
        if (admin == null || !"admin".equalsIgnoreCase(admin.getTipo())) {
            return "ERROR: Accesso negato. Solo gli amministratori possono accedere a questa funzione.";
        }

        if (!nuovoTipo.equals("ADMIN") && !nuovoTipo.equals("CLIENTE")) {
            return "ERROR: Tipo non valido.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE utenti SET tipoUtente = ? WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nuovoTipo);
                statement.setString(2, userID);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    return "SUCCESS: Tipo utente modificato con successo.";
                } else {
                    return "ERROR: Utente non trovato.";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante la modifica: " + e.getMessage();
        }
    }

    /**
     * Metodo per bloccare un utente.
     * @param adminID
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String bloccaUtente(String adminID, String userID) throws RemoteException {
        Utente admin = sessioniAttive.get(adminID);
        if (admin == null || !"admin".equalsIgnoreCase(admin.getTipo())) {
            return "ERROR: Accesso negato. Solo gli amministratori possono accedere a questa funzione.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE utenti SET verificato = 0 WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userID);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    // Rimuovi dalla sessione se attiva
                    sessioniAttive.remove(userID);
                    return "SUCCESS: Utente bloccato con successo.";
                } else {
                    return "ERROR: Utente non trovato.";
                }
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante il blocco: " + e.getMessage();
        }
    }

    /**
     * Metodo per eliminare un utente.
     * @param adminID
     * @param userID
     * @return String
     * @throws RemoteException
     */
    public String eliminaUtente(String adminID, String userID) throws RemoteException {
        Utente admin = sessioniAttive.get(adminID);
        if (admin == null || !"admin".equalsIgnoreCase(admin.getTipo())) {
            return "ERROR: Accesso negato. Solo gli amministratori possono accedere a questa funzione.";
        }

        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try {
                // Elimina i messaggi dell'utente
                String sqlMessaggi = "DELETE FROM messaggi WHERE mittenteID = ? OR destinatarioID = ?";
                try (PreparedStatement stmtMessaggi = connection.prepareStatement(sqlMessaggi)) {
                    stmtMessaggi.setString(1, userID);
                    stmtMessaggi.setString(2, userID);
                    stmtMessaggi.executeUpdate();
                }

                // Elimina le transazioni dell'utente
                String sqlTransazioni = "DELETE FROM transazioni WHERE mittenteID = ? OR destinatarioID = ?";
                try (PreparedStatement stmtTransazioni = connection.prepareStatement(sqlTransazioni)) {
                    stmtTransazioni.setString(1, userID);
                    stmtTransazioni.setString(2, userID);
                    stmtTransazioni.executeUpdate();
                }

                // Elimina l'indirizzo dell'utente
                String sqlIndirizzo = "DELETE FROM indirizzi WHERE userID = ?";
                try (PreparedStatement stmtIndirizzo = connection.prepareStatement(sqlIndirizzo)) {
                    stmtIndirizzo.setString(1, userID);
                    stmtIndirizzo.executeUpdate();
                }

                // Elimina l'utente
                String sqlUtente = "DELETE FROM utenti WHERE userID = ?";
                try (PreparedStatement stmtUtente = connection.prepareStatement(sqlUtente)) {
                    stmtUtente.setString(1, userID);
                    int rowsDeleted = stmtUtente.executeUpdate();

                    if (rowsDeleted > 0) {
                        connection.commit();
                        // Rimuovi dalla sessione se attiva
                        sessioniAttive.remove(userID);
                        return "SUCCESS: Utente e tutti i dati correlati eliminati con successo.";
                    } else {
                        return "ERROR: Utente non trovato.";
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                return "ERROR: Errore durante l'eliminazione: " + e.getMessage();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return "ERROR: Errore di connessione: " + e.getMessage();
        }
    }

    // === UTILITÀ ===
    
    /**
     * Metodo per testare la connessione al database.
     * @return String
     * @throws RemoteException
     */
    public String testConnection() throws RemoteException {
        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
            if (connection != null) {
                return "SUCCESS: Connessione al database riuscita!";
            }
        } catch (SQLException e) {
            return "ERROR: Errore durante la connessione al database: " + e.getMessage();
        }
        return "ERROR: Connessione fallita.";
    }

    /**
     * Metodo per ottenere lo stato del server.
     * @return String
     * @throws RemoteException
     */
    public String getServerStatus() throws RemoteException {
        return "SUCCESS: Server RMI Ado-Transfert attivo. Sessioni attive: " + sessioniAttive.size();
    }

    // === METODI PRIVATI DI SUPPORTO ===
    
    private void caricaDatiUtente(Connection connection, Utente utente) throws SQLException {
        String userID = utente.getUserID();
        
        // Carica indirizzo
        String sqlIndirizzi = "SELECT * FROM indirizzi WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlIndirizzi)) {
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Indirizzo indirizzo = new Indirizzo(
                        resultSet.getString("via"),
                        resultSet.getString("numeroCivico"),
                        resultSet.getString("citta"),
                        resultSet.getString("provincia"),
                        resultSet.getString("cap"));
                utente.setIndirizzo(indirizzo);
                utente.setNazione(resultSet.getString("nazione"));
            }
        }

        // Carica messaggi
        String sqlMessaggi = "SELECT * FROM messaggi WHERE destinatarioID = ? ORDER BY dataInvio DESC";
        try (PreparedStatement statement = connection.prepareStatement(sqlMessaggi)) {
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Messaggio messaggio = new Messaggio(
                        resultSet.getString("mittenteID"),
                        userID,
                        resultSet.getString("titolo"),
                        resultSet.getString("contenuto"));
                messaggio.setId(resultSet.getInt("id"));
                messaggio.setDataInvio(resultSet.getString("dataInvio"));
                utente.aggiungiMessaggio(messaggio);
            }
        }
    }

    private void verificaUtenteSistema(Connection connection) throws SQLException {
        String sql = "SELECT userID FROM utenti WHERE userID = 'Sistema'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                // Crea l'utente "Sistema" se non esiste
                String sqlInsert = "INSERT INTO utenti (userID, nome, cognome, email, password, tipoUtente, verificato) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                    insertStatement.setString(1, "Sistema");
                    insertStatement.setString(2, "Habib");
                    insertStatement.setString(3, "Ado");
                    insertStatement.setString(4, "sistema@ado_transfert.com");
                    insertStatement.setString(5, hashPassword("123Lover"));
                    insertStatement.setString(6, "admin");
                    insertStatement.setInt(7, 1);
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    private String getAdminID(Connection connection) throws SQLException {
        String sql = "SELECT userID FROM utenti WHERE tipoUtente = 'admin' LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("userID");
            }
        }
        return null;
    }

    private synchronized void registraTransazione(Connection connection, String tipo, double importo, String mittenteID, String destinatarioID) throws SQLException {
        String sql = "INSERT INTO transazioni (tipo, importo, mittenteID, destinatarioID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tipo);
            statement.setDouble(2, importo);
            statement.setString(3, mittenteID);
            statement.setString(4, destinatarioID);
            statement.executeUpdate();
        }
    }

    private synchronized String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            return "Errore durante l'hashing della password" + e.getMessage();
        }
    }

     /**
     * Inizializza il database e crea tutte le tabelle necessarie
     */
    private synchronized void initializeDatabase() {
        try {
            System.out.println("Inizializzazione database...");
            
            // Test de connexion avec gestion d'erreur améliorée
            try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
                System.out.println("✅ Connesso al database MySQL");
                
                // Per Railway, il database esiste già, per MySQL locale crealo se necessario
                if (!DB_HOST.contains("railway.app") && !DB_HOST.contains("containers-")) {
                    // Solo per MySQL locale: crea il database se non esiste
                    String serverUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/";
                    try (Connection serverConnection = DriverManager.getConnection(serverUrl, DB_USER, DB_PASSWORD)) {
                        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
                        try (Statement statement = serverConnection.createStatement()) {
                            statement.executeUpdate(createDatabaseSQL);
                            System.out.println("Database '" + DB_NAME + "' creato/verificato");
                        }
                    }
                } else {
                    System.out.println("Database Railway '" + DB_NAME + "' già disponibile");
                }
                
                // Usa il database
                String useDatabaseSQL = "USE " + DB_NAME;
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(useDatabaseSQL);
                    System.out.println("Database 'ado_transfert' selezionato");
                }
                
                // Crea la tabella utenti
                String createUtentiSQL = 
                    "CREATE TABLE IF NOT EXISTS utenti (" +
                    "    id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "    tipoUtente ENUM('admin', 'cliente') NOT NULL, " +
                    "    nome VARCHAR(100) NOT NULL, " +
                    "    cognome VARCHAR(100) NOT NULL, " +
                    "    userID varchar(220) not null unique," +
                    "    email VARCHAR(255), " +
                    "    password VARCHAR(255) NOT NULL, " +
                    "    indirizzo TEXT, " +
                    "    telefono VARCHAR(20) not null unique default 'N/A', " +
                    "    saldo decimal(15, 2) default 0.0 check(saldo >= 0)," +
                    "    data_registrazione DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "    verificato BOOLEAN DEFAULT FALSE, " +
                    "    token_verifica VARCHAR(255), " +
                    "    token_reset_password VARCHAR(255), " +
                    "    token_reset_scadenza DATETIME" +
                    ")";
                
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createUtentiSQL);
                    System.out.println("Tabella 'utenti' creata/verificata");
                }
                
                // Crea la tabella messaggi
                String createMessaggiSQL = 
                    "CREATE TABLE IF NOT EXISTS messaggi(" +
                    "    id int auto_increment primary key," +
                    "    mittenteID varchar(220) not null," +
                    "    destinatarioID varchar(220) not null," +
                    "    titolo varchar(225) not null," +
                    "    contenuto text not null," +
                    "    dataInvio datetime default current_timestamp," +
                    "    foreign key (mittenteID) references utenti(userID) on delete cascade," +
                    "    foreign key (destinatarioID) references utenti(userID) on delete cascade" +
                    ")";
                
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createMessaggiSQL);
                    System.out.println("Tabella 'messaggi' creata/verificata");
                }
                
                // Crea la tabella indirizzi
                String createIndirizziSQL = 
                    "CREATE TABLE IF NOT EXISTS indirizzi(" +
                    "    id int auto_increment primary key," +
                    "    userID varchar(220) not null, " +
                    "    via varchar(220)," +
                    "    numeroCivico varchar(100)," +
                    "    citta varchar(220) not null," +
                    "    provincia varchar(220)," +
                    "    cap varchar(220)," +
                    "    nazione varchar(220) not null default 'Sénégal'," +
                    "    foreign key(userID) references utenti(userID) on delete cascade" +
                    ")";
                
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createIndirizziSQL);
                    System.out.println("Tabella 'indirizzi' creata/verificata");
                }
                
                // Crea la tabella transazioni
                String createTransazioniSQL = 
                    "CREATE TABLE IF NOT EXISTS transazioni(" +
                    "    id int auto_increment primary key," +
                    "    tipo varchar(220) not null," +
                    "    importo double not null check (importo > 0)," +
                    "    mittenteID varchar(220) not null," +
                    "    destinatarioID varchar(220) not null," +
                    "    data_transazione timestamp default current_timestamp," +
                    "    foreign key(mittenteID) references utenti(userID) on delete cascade," +
                    "    foreign key(destinatarioID) references utenti(userID) on delete cascade" +
                    ")";
                
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createTransazioniSQL);
                    System.out.println("Tabella 'transazioni' creata/verificata");
                }
                
                // Crea l'utente amministratore di sistema se non esiste
                createSystemAdmin(connection);
                
                System.out.println("✅ Database inizializzato con successo!");
                
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Errore durante l'inizializzazione del database: " + e.getMessage());
            
            // Messages d'erreur plus informatifs
            if (e.getMessage().contains("Access denied")) {
                System.err.println("🔐 Problème d'authentification MySQL:");
                System.err.println("   - Vérifiez que l'utilisateur '" + DB_USER + "' existe");
                System.err.println("   - Vérifiez que le mot de passe est correct");
                System.err.println("   - Vérifiez que l'utilisateur a les permissions sur la base '" + DB_NAME + "'");
                System.err.println("   - Pour MySQL local, essayez: mysql -u root -p");
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("🌐 Problème de connexion réseau:");
                System.err.println("   - Vérifiez que MySQL est démarré");
                System.err.println("   - Vérifiez que le port " + DB_PORT + " est accessible");
                System.err.println("   - Vérifiez l'adresse " + DB_HOST);
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("📊 Base de données introuvable:");
                System.err.println("   - La base '" + DB_NAME + "' n'existe pas");
                System.err.println("   - Créez-la avec: CREATE DATABASE " + DB_NAME + ";");
            }
            
            System.err.println("🔧 Configuration actuelle:");
            System.err.println("   Host: " + DB_HOST + ":" + DB_PORT);
            System.err.println("   Database: " + DB_NAME);
            System.err.println("   User: " + DB_USER);
            System.err.println("   URL: " + URL.replace(DB_PASSWORD, "***"));
            
            e.printStackTrace();
        }
    }

    /**
     * Metodo About per la visualizzazione delle informazioni dell'app
     * Permette anche di comunicare con un admin
     * 
     */
    public String about() throws RemoteException {
        return "Ado-Transfert v1.0\n" +
               "Un sistema di trasferimento denaro sicuro e affidabile.\n" +
               "Facile da comprendere e utilizzare.\n" +
               "Sviluppato da Habib Ado\n" +
               "Contatto: sistema@ado_transfert.com\n" +
               "Per assistenza, invia un messaggio tramite la funzione di messaggistica.";
    }

    /**
     * Metodo sicuro per il recupero della password - Versione Produzione
     * @param userID ID utente
     * @param email Email dell'utente
     * @return String con risultato dell'operazione
     * @throws RemoteException
     */
    public synchronized String recuperoPassword(String userID, String email) throws RemoteException {
        // Log dell'attività per sicurezza
        logSecurityEvent("PASSWORD_RECOVERY_ATTEMPT", userID, "Attempt from email: " + email);
        
        try {
            // 1. Validazione input
            String validationResult = validateRecoveryInput(userID, email);
            if (!validationResult.equals("VALID")) {
                return validationResult;
            }
            
            // 2. Rate limiting
            String rateLimitResult = checkRateLimit(email);
            if (!rateLimitResult.equals("ALLOWED")) {
                return rateLimitResult;
            }
            
            // 3. Verifica esistenza utente
        try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT userID, nome, cognome, verificato, token_reset_scadenza FROM utenti WHERE userID = ? AND email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userID);
                statement.setString(2, email);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                        // Verifica che l'utente sia verificato
                        if (resultSet.getInt("verificato") != 1) {
                            logSecurityEvent("PASSWORD_RECOVERY_DENIED", userID, "Unverified account");
                            return "ERROR: Account non verificato. Contatta l'amministratore.";
                        }
                        
                        // Verifica se esiste già un token valido
                        Timestamp existingTokenExpiry = resultSet.getTimestamp("token_reset_scadenza");
                        if (existingTokenExpiry != null && existingTokenExpiry.after(Timestamp.valueOf(LocalDateTime.now()))) {
                            logSecurityEvent("PASSWORD_RECOVERY_DENIED", userID, "Valid token already exists");
                            return "ERROR: È già stato inviato un link di reset. Controlla la tua email o riprova tra qualche minuto.";
                        }

                        // 4. Genera token sicuro
                        String secureToken = generateSecureToken();
                        LocalDateTime scadenza = LocalDateTime.now().plusHours(2); // Token valido per 2 ore
                        
                        // 5. Salva token nel database
                    String updateSQL = "UPDATE utenti SET token_reset_password = ?, token_reset_scadenza = ? WHERE userID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                            updateStatement.setString(1, secureToken);
                        updateStatement.setTimestamp(2, Timestamp.valueOf(scadenza));
                        updateStatement.setString(3, userID);
                            int rowsUpdated = updateStatement.executeUpdate();
                            
                            if (rowsUpdated > 0) {
                                // 6. Invia email (in produzione, integreresti un servizio email reale)
                                boolean emailSent = sendPasswordResetEmail(email, userID, secureToken);
                                
                                if (emailSent) {
                                    logSecurityEvent("PASSWORD_RECOVERY_SUCCESS", userID, "Token generated and email sent");
                                    return "SUCCESS: Link di reset della password inviato alla tua email. Il link è valido per 2 ore.";
                                } else {
                                    logSecurityEvent("PASSWORD_RECOVERY_EMAIL_FAILED", userID, "Token generated but email failed");
                                    return "ERROR: Token generato ma errore nell'invio email. Riprova più tardi.";
                                }
                            } else {
                                logSecurityEvent("PASSWORD_RECOVERY_DB_ERROR", userID, "Failed to update token");
                                return "ERROR: Errore interno. Riprova più tardi.";
                            }
                        }
                    } else {
                        // Non rivelare se l'utente esiste o meno per sicurezza
                        logSecurityEvent("PASSWORD_RECOVERY_DENIED", userID, "Invalid credentials");
                        return "ERROR: Se l'account esiste, riceverai un'email con le istruzioni per il reset della password.";
                    }
                }
            }
        } catch (SQLException e) {
            logSecurityEvent("PASSWORD_RECOVERY_ERROR", userID, "Database error: " + e.getMessage());
            return "ERROR: Errore interno del sistema. Riprova più tardi.";
        }
    }
    
    /**
     * Valida l'input per il recupero password
     */
    private String validateRecoveryInput(String userID, String email) {
        if (userID == null || userID.trim().isEmpty()) {
            return "ERROR: UserID non può essere vuoto.";
        }
        
        if (email == null || email.trim().isEmpty()) {
            return "ERROR: Email non può essere vuota.";
        }
        
        // Validazione formato email
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "ERROR: Formato email non valido.";
        }
        
        // Validazione lunghezza userID
        if (userID.length() < 3 || userID.length() > 50) {
            return "ERROR: UserID deve essere tra 3 e 50 caratteri.";
        }
        
        // Validazione caratteri speciali userID
        if (!userID.matches("^[a-zA-Z0-9_-]+$")) {
            return "ERROR: UserID può contenere solo lettere, numeri, underscore e trattini.";
        }
        
        return "VALID";
    }
    
    /**
     * Controlla il rate limiting per prevenire abusi
     */
    private String checkRateLimit(String email) {
        String key = email.toLowerCase().trim();
        PasswordRecoveryAttempt attempt = recoveryAttempts.get(key);
        
        if (attempt == null) {
            // Primo tentativo
            recoveryAttempts.put(key, new PasswordRecoveryAttempt(key));
            return "ALLOWED";
        }
        
        // Verifica se può tentare
        if (!attempt.canAttempt()) {
            long hoursSinceFirst = java.time.Duration.between(attempt.getFirstAttempt(), LocalDateTime.now()).toHours();
            if (hoursSinceFirst < 24) {
                return "ERROR: Troppi tentativi. Riprova tra " + (24 - hoursSinceFirst) + " ore.";
                } else {
                // Reset dopo 24 ore
                recoveryAttempts.remove(key);
                recoveryAttempts.put(key, new PasswordRecoveryAttempt(key));
                return "ALLOWED";
            }
        }
        
        // Incrementa tentativi
        attempt.incrementAttempts();
        return "ALLOWED";
    }
    
    /**
     * Genera un token sicuro per il reset password
     */
    private String generateSecureToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32]; // 256 bit
        secureRandom.nextBytes(randomBytes);
        
        // Combina con timestamp per maggiore sicurezza
        long timestamp = System.currentTimeMillis();
        String timestampStr = String.valueOf(timestamp);
        
        // Crea hash sicuro
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(randomBytes);
            digest.update(timestampStr.getBytes());
            byte[] hash = digest.digest();
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // Fallback a UUID se SHA-256 non disponibile
            return UUID.randomUUID().toString().replace("-", "");
        }
    }
    
    /**
     * Invia email di reset password usando Gmail SMTP
     */
    private boolean sendPasswordResetEmail(String email, String userID, String token) {
        try {
            // Configurazione proprietà SMTP per Gmail
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);
            
            // Configurazione per Gmail App Password
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.checkserveridentity", "false");
            
            // Creazione sessione
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            // Creazione messaggio
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM, EMAIL_FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Reset Password - Ado-Transfert");
            
            // Link di reset (in produzione, sostituisci con il tuo dominio)
            String resetLink = "https://ado-transfer.up.railway.app/reset-password.html?token=" + token + "&user=" + userID;
            
            // Contenuto HTML dell'email
            String htmlContent = createPasswordResetEmailHTML(userID, resetLink);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            // Invio email
            Transport.send(message);
            
            logSecurityEvent("EMAIL_SENT_SUCCESS", userID, "Password reset email sent to: " + email);
            System.out.println("✅ Email di reset password inviata con successo a: " + email);
            
            return true;
            
        } catch (MessagingException e) {
            logSecurityEvent("EMAIL_SEND_ERROR", userID, "Failed to send email: " + e.getMessage());
            System.err.println("❌ Errore invio email: " + e.getMessage());
            
            // Log dettagliato per debug
            if (e instanceof AuthenticationFailedException) {
                System.err.println("🔐 Errore autenticazione Gmail. Verifica App Password.");
            } else if (e instanceof SendFailedException) {
                System.err.println("📧 Errore invio email. Verifica indirizzo destinatario.");
            }
            
            return false;
            
        } catch (Exception e) {
            logSecurityEvent("EMAIL_SEND_ERROR", userID, "Unexpected error: " + e.getMessage());
            System.err.println("❌ Errore imprevisto invio email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Crea il contenuto HTML per l'email di reset password
     */
    private String createPasswordResetEmailHTML(String userID, String resetLink) {
        return String.format(
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <style>\n" +
            "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }\n" +
            "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
            "        .header { background: linear-gradient(135deg, #0066cc, #004499); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }\n" +
            "        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }\n" +
            "        .button { display: inline-block; background: #0066cc; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; }\n" +
            "        .button:hover { background: #004499; }\n" +
            "        .warning { background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0; }\n" +
            "        .footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>🏦 Ado-Transfert</h1>\n" +
            "            <h2>Reset Password</h2>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"content\">\n" +
            "            <h3>Ciao %s,</h3>\n" +
            "            \n" +
            "            <p>Abbiamo ricevuto una richiesta per reimpostare la password del tuo account Ado-Transfert.</p>\n" +
            "            \n" +
            "            <p>Per procedere con il reset della password, clicca sul pulsante qui sotto:</p>\n" +
            "            \n" +
            "            <p style=\"text-align: center;\">\n" +
            "                <a href=\"%s\" class=\"button\">🔑 Reimposta Password</a>\n" +
            "            </p>\n" +
            "            \n" +
            "            <div class=\"warning\">\n" +
            "                <strong>⚠️ Importante:</strong>\n" +
            "                <ul>\n" +
            "                    <li>Questo link è valido per <strong>2 ore</strong> dalla ricezione di questa email</li>\n" +
            "                    <li>Se non hai richiesto il reset della password, ignora questa email</li>\n" +
            "                    <li>Per motivi di sicurezza, non condividere questo link con altri</li>\n" +
            "                </ul>\n" +
            "            </div>\n" +
            "            \n" +
            "            <p>Se il pulsante non funziona, copia e incolla questo link nel tuo browser:</p>\n" +
            "            <p style=\"word-break: break-all; background: #e9ecef; padding: 10px; border-radius: 3px; font-family: monospace;\">\n" +
            "                %s\n" +
            "            </p>\n" +
            "            \n" +
            "            <p>Se hai domande o problemi, contatta il nostro supporto tecnico.</p>\n" +
            "            \n" +
            "            <p>Cordiali saluti,<br>\n" +
            "            <strong>Team Ado-Transfert</strong></p>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"footer\">\n" +
            "            <p>Questa è un'email automatica, non rispondere a questo messaggio.</p>\n" +
            "            <p>© 2024 Ado-Transfert. Tutti i diritti riservati.</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>", userID, resetLink, resetLink);
    }
    
    /**
     * Log degli eventi di sicurezza
     */
    private void logSecurityEvent(String eventType, String userID, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format("[%s] SECURITY_EVENT: %s | UserID: %s | Details: %s", 
            timestamp, eventType, userID, details);
        
        // In produzione, useresti un logger professionale come Log4j o SLF4J
        System.out.println(logMessage);
        
        // Potresti anche scrivere su file di log dedicato
        // LoggerFactory.getLogger(App.class).info(logMessage);
    }
    
    /**
     * Metodo per validare e utilizzare un token di reset password
     * @param token Token di reset
     * @param newPassword Nuova password
     * @return String con risultato dell'operazione
     */
    public synchronized String resetPasswordWithToken(String token, String newPassword) throws RemoteException {
        logSecurityEvent("PASSWORD_RESET_ATTEMPT", "UNKNOWN", "Token: " + token.substring(0, Math.min(8, token.length())) + "...");
        
        try {
            // Validazione input
            if (token == null || token.trim().isEmpty()) {
                return "ERROR: Token non valido.";
            }
            
            if (newPassword == null || newPassword.length() < 8) {
                return "ERROR: La nuova password deve essere di almeno 8 caratteri.";
            }
            
            // Validazione password
            if (!Utente.isPasswordValida(newPassword)) {
                return "ERROR: Password non valida (min 8 caratteri, almeno 1 maiuscola, 1 numero).";
            }
            
            try (Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
                // Verifica token valido e non scaduto
                String sql = "SELECT userID, token_reset_scadenza FROM utenti WHERE token_reset_password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, token);
                    ResultSet resultSet = statement.executeQuery();
                    
                    if (resultSet.next()) {
                        String userID = resultSet.getString("userID");
                        Timestamp expiry = resultSet.getTimestamp("token_reset_scadenza");
                        
                        // Verifica scadenza
                        if (expiry == null || expiry.before(Timestamp.valueOf(LocalDateTime.now()))) {
                            logSecurityEvent("PASSWORD_RESET_DENIED", userID, "Token expired");
                            return "ERROR: Token scaduto. Richiedi un nuovo link di reset.";
                        }
                        
                        // Aggiorna password e rimuovi token
                        String hashedPassword = hashPassword(newPassword);
                        String updateSQL = "UPDATE utenti SET password = ?, token_reset_password = NULL, token_reset_scadenza = NULL WHERE userID = ?";
                        
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                            updateStatement.setString(1, hashedPassword);
                            updateStatement.setString(2, userID);
                            int rowsUpdated = updateStatement.executeUpdate();
                            
                            if (rowsUpdated > 0) {
                                // Invalida eventuali sessioni attive
                                sessioniAttive.remove(userID);
                                
                                logSecurityEvent("PASSWORD_RESET_SUCCESS", userID, "Password successfully reset");
                                return "SUCCESS: Password aggiornata con successo. Effettua il login con la nuova password.";
                            } else {
                                logSecurityEvent("PASSWORD_RESET_ERROR", userID, "Failed to update password");
                                return "ERROR: Errore nell'aggiornamento della password. Riprova.";
                            }
                        }
                    } else {
                        logSecurityEvent("PASSWORD_RESET_DENIED", "UNKNOWN", "Invalid token");
                        return "ERROR: Token non valido.";
                    }
                }
            }
        } catch (SQLException e) {
            logSecurityEvent("PASSWORD_RESET_ERROR", "UNKNOWN", "Database error: " + e.getMessage());
            return "ERROR: Errore interno del sistema. Riprova più tardi.";
        }
    }
    
    /**
     * Crea l'utente amministratore di sistema se non esiste
     */
    private void createSystemAdmin(Connection connection) throws SQLException {
        String checkAdminSQL = "SELECT COUNT(*) FROM utenti WHERE userID = 'Sistema'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(checkAdminSQL)) {
            
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                // Crea l'utente amministratore di sistema
                String createAdminSQL = 
                    "INSERT INTO utenti (userID, nome, cognome, email, password, tipoUtente, verificato, telefono) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement preparedStatement = connection.prepareStatement(createAdminSQL)) {
                    preparedStatement.setString(1, "Sistema");
                    preparedStatement.setString(2, "Habib");
                    preparedStatement.setString(3, "Ado");
                    preparedStatement.setString(4, "sistema@ado_transfert.com");
                    preparedStatement.setString(5, hashPassword("123Lover"));
                    preparedStatement.setString(6, "admin");
                    preparedStatement.setBoolean(7, true);
                    preparedStatement.setString(8, "0000000000");
                    
                    preparedStatement.executeUpdate();
                    System.out.println("✅ Utente amministratore 'Sistema' creato");
                }
            } else {
                System.out.println("ℹ️ Utente amministratore 'Sistema' già esistente");
            }
        }
    }
    
}
