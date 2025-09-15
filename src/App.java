

import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class App {
    // JDBC URL, username and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/ado_transfert";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private static Utente utente = null;
    private static String tipoUtente = null;
    private static String userID = null;
    // sistema@ado_transfert.com
    // 123Lover

    public static void main(String[] args) throws Exception {
        // Connessione al database
        testConnection();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (tipoUtente == null) {
                System.out.println("===================================");
                System.out.println("Benvenuto nel sistema di gestione delle transazioni!");
                System.out.println("1. Creare un conto utente");
                System.out.println("2. Effettuare il login");
                System.out.println("0. Uscire");
                System.out.print("Scelta: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        creaUtente(scanner);
                        break;
                    case 2:
                        login(scanner);
                        break;
                    case 0:
                        System.out.println("Uscita dal programma...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            } else if (tipoUtente.equalsIgnoreCase("cliente")) {
                System.out.println("===================================");
                System.out.println("Benvenuto " + utente.getNome() + " " + utente.getCognome() + "!");
                System.out.println("1. Visualizza messaggi");
                System.out.println("2. Crea messaggio");
                System.out.println("3. Crea indirizzo");
                System.out.println("4. Fai transazione");
                System.out.println("5. Area profilo");
                System.out.println("6. Elimina messaggio");
                System.out.println("7. Modifica indirizzo");
                System.out.println("8. Visualizza storico transazioni");
                System.out.println("9. Visualizza dettagli conto");
                System.out.println("0. Logout");
                System.out.print("Scelta: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        visualizzaMessaggi();
                        break;
                    case 2:
                        creaMessaggio(scanner);
                        break;
                    case 3:
                        creaIndirizzo(scanner);
                        break;
                    case 4:
                        faiTransazione(scanner);
                        break;
                    case 5:
                        AreaProfilo(scanner);
                        break;
                    case 6:
                        eliminaMessaggio(scanner);
                        break;
                    case 7:
                        modificaIndirizzo(scanner);
                        break;
                    case 8:
                        visualizzaStoricoTransazioni();
                        break;
                    case 9:
                        visualizzaDettagliConto();
                        break;
                    case 0:
                        logout();
                        break;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            } else if (tipoUtente.equalsIgnoreCase("admin")) {
                System.out.println("===================================");
                System.out.println("Benvenuto " + utente.getNome() + " " + utente.getCognome() + "!");
                System.out.println("1. Visualizza messaggi");
                System.out.println("2. Crea messaggio");
                System.out.println("3. Approva utente");
                System.out.println("4. Gestione utenti");
                System.out.println("5. Elimina messaggio");
                System.out.println("0. Logout");
                System.out.print("Scelta: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        visualizzaMessaggi();
                        break;
                    case 2:
                        creaMessaggio(scanner);
                        break;
                    case 3:
                        approvaUtente(scanner);
                        break;
                    case 4:
                        gestioneUtenti(scanner);
                        break;
                    case 5:
                        eliminaMessaggio(scanner);
                        break;
                    case 0:
                        logout();
                        break;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            }
        }
    }

    public static void testConnection() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                System.out.println("Connessione al database riuscita!");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
        }
    }

    public static void AreaProfilo(Scanner scanner) {
        while (true) {
            System.out.println("===================================");
            System.out.println("Area profilo:");
            System.out.println("1. Modifica profilo");
            System.out.println("2. Visualizza dettagli profilo");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    modificaProfilo(scanner);
                    break;
                case 2:
                    visualizzaProfilo();
                    break;
                case 0:
                    System.out.println("Tornando al menu principale...");
                    return;
                default:
                    System.out.println("Scelta non valida, riprova.");
            }
        }
    }

    public static void faiTransazione(Scanner scanner) {
        while (true) {
            System.out.println("===================================");
            System.out.println("1. Fai versamento");
            System.out.println("2. Fai prelievo");
            System.out.println("3. Fai trasferimento");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    faiVersamento(scanner);
                    break;
                case 2:
                    faiPrelievo(scanner);
                    break;
                case 3:
                    faiTrasferimento(scanner);
                    break;
                case 0:
                    System.out.println("Tornando al menu principale...");
                    return;
                default:
                    System.out.println("Scelta non valida, riprova.");
            }
        }
    }

    public static void modificaProfilo(Scanner scanner) {
        System.out.println("Modifica profilo:");
        System.out.print("Nuovo Nome (" + utente.getNome() + "): ");
        String nome = scanner.nextLine();
        System.out.print("Nuovo Cognome (" + utente.getCognome() + "): ");
        String cognome = scanner.nextLine();
        System.out.print("Nuovo Telefono (" + utente.getTelefono() + "): ");
        String telefono = scanner.nextLine();

        // Aggiorna l'oggetto utente
        utente.setNome(nome.isEmpty() ? utente.getNome() : nome);
        utente.setCognome(cognome.isEmpty() ? utente.getCognome() : cognome);
        utente.setTelefono(telefono.isEmpty() ? utente.getTelefono() : telefono);

        // Aggiorna il database
        String sql = "UPDATE utenti SET nome = ?, cognome = ?, telefono = ? WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, utente.getNome());
            statement.setString(2, utente.getCognome());
            statement.setString(3, utente.getTelefono());
            statement.setString(4, userID);
            statement.executeUpdate();
            System.out.println("Profilo modificato con successo!");
        } catch (SQLException e) {
            System.out.println("Errore durante la modifica del profilo: " + e.getMessage());
        }
    }

    public static void creaUtente(Scanner scanner) {
        System.out.println("Creazione conto utente:");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("UserID: ");
        String userID = scanner.nextLine();
        // Verifica che l'UserID non esista già
        String sqlCheck = "SELECT userID FROM utenti WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sqlCheck)) {
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("UserID già esistente. Scegli un altro UserID.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la verifica dell'UserID: " + e.getMessage());
        }
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();

        String email;
        do {
            System.out.print("Email: ");
            email = scanner.nextLine();
        } while (!Utente.isEmailValida(email));

        String password;
        do {
            System.out.print("Password (min 8 caratteri, almeno 1 maiuscola, 1 numero): ");
            password = scanner.nextLine();
        } while (!Utente.isPasswordValida(password));

        // Hash della password prima di salvarla
        String hashedPassword = hashPassword(password);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
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
                statement.setString(7, "cliente"); // Default type
                statement.executeUpdate();

                System.out.println("Utente creato con successo! In attesa di approvazione dall'amministratore.");
                utente = new Utente(nome, cognome, userID, telefono, email, hashedPassword);
                utente.setTipo("cliente"); // Imposta il tipo di utente come CLIENTE

                // Invia un messaggio di benvenuto all'admin
                String adminID = getAdminID();
                if (adminID != null) {
                    Messaggio messaggio = new Messaggio("Sistema", adminID, "Nuova registrazione!",
                            "Un nuovo utente si è registrato: " + userID + " con telefono " + telefono
                                    + " e chiede l'approvazione del suo account.");

                    String sqlMessaggio = "INSERT INTO messaggi (mittenteID, destinatarioID, titolo, contenuto) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement statement2 = connection.prepareStatement(sqlMessaggio)) {
                        statement2.setString(1, "Sistema");
                        statement2.setString(2, adminID);
                        statement2.setString(3, messaggio.getTitolo());
                        statement2.setString(4, messaggio.getContenuto());
                        statement2.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la creazione dell'utente: " + e.getMessage());
        }
    }

    private static void verificaUtenteSistema(Connection connection) throws SQLException {
        String sql = "SELECT userID FROM utenti WHERE userID = 'Sistema'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                // Crea l'utente "Sistema" se non esiste
                String sqlInsert = "INSERT INTO utenti (userID, nome, cognome, email, password, tipoUtente, verificato) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                    insertStatement.setString(1, "Sistema");
                    insertStatement.setString(2, "Habib");
                    insertStatement.setString(3, "Ado");
                    insertStatement.setString(4, "sistema@ado_transfert.com");
                    insertStatement.setString(5, hashPassword("123Lover")); // Usa una password hashata
                    insertStatement.setString(6, "admin");
                    insertStatement.setInt(7, 1); // Verificato
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore durante l'hashing della password", e);
        }
    }

    public static String getAdminID() {
        String adminID = null;
        String sql = "SELECT userID FROM utenti WHERE tipoUtente = 'ADMIN' LIMIT 1";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                adminID = resultSet.getString("userID");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dell'adminID: " + e.getMessage());
        }
        return adminID;
    }

    public static void login(Scanner scanner) {
        System.out.println("Effettuare il login:");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM utenti WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if (hashPassword(password).equals(hashedPassword)) {
                    // Verifica se l'utente è verificato
                    if (resultSet.getInt("verificato") == 0) {
                        System.out.println("Il tuo account è in attesa di approvazione.");
                        return;
                    }

                    // Login riuscito
                    System.out.println("Login effettuato con successo!");
                    userID = resultSet.getString("userID");
                    String nome = resultSet.getString("nome");
                    String cognome = resultSet.getString("cognome");
                    String telefono = resultSet.getString("telefono");
                    tipoUtente = resultSet.getString("tipoUtente");
                    double saldo = resultSet.getDouble("saldo");

                    // Crea l'oggetto utente
                    utente = new Utente(nome, cognome, userID, telefono, email, hashedPassword);
                    utente.setSaldo(saldo);
                    utente.setTipo(tipoUtente);

                    // Carica dati aggiuntivi
                    caricaDatiUtente(connection);
                } else {
                    System.out.println("Password errata. Riprova.");
                }
            } else {
                System.out.println("Email non trovata. Verifica i dati inseriti.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il login: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore imprevisto: " + e.getMessage());
        }
    }

    private static void caricaDatiUtente(Connection connection) throws SQLException {
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

    public static void logout() {
        utente = null;
        tipoUtente = null;
        userID = null;
        System.out.println("Logout effettuato con successo!");
    }

    public static void visualizzaMessaggi() {
        if (utente.getMessaggi().isEmpty()) {
            System.out.println("Nessun messaggio trovato.");
            return;
        }

        System.out.println("Messaggi ricevuti:");
        for (Messaggio messaggio : utente.getMessaggi()) {
            System.out.println("ID: " + messaggio.getId());
            System.out.println("Mittente: " + messaggio.getMittenteID());
            System.out.println("Data: " + messaggio.getDataInvio());
            System.out.println("Titolo: " + messaggio.getTitolo());
            System.out.println("Contenuto: " + messaggio.getContenuto());
            System.out.println("------------------------------");
        }
    }

    public static void creaMessaggio(Scanner scanner) {
        System.out.println("Creazione messaggio:");
        System.out.print("Destinatario ID: ");
        String destinatarioID = scanner.nextLine();

        // Verifica esistenza destinatario
        String sql = "SELECT userID FROM utenti WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, destinatarioID);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Destinatario non trovato.");
                return;
            }

            System.out.print("Titolo: ");
            String titolo = scanner.nextLine();
            System.out.print("Contenuto: ");
            String contenuto = scanner.nextLine();

            String sqlInsert = "INSERT INTO messaggi (mittenteID, destinatarioID, titolo, contenuto) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                insertStatement.setString(1, userID);
                insertStatement.setString(2, destinatarioID);
                insertStatement.setString(3, titolo);
                insertStatement.setString(4, contenuto);
                insertStatement.executeUpdate();
                System.out.println("Messaggio inviato con successo!");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante l'invio del messaggio: " + e.getMessage());
        }
    }

    public static void creaIndirizzo(Scanner scanner) {
        if (utente.getIndirizzo() != null) {
            System.out.println("Hai già un indirizzo registrato. Usa l'opzione modifica.");
            return;
        }

        System.out.println("Creazione indirizzo:");
        System.out.print("Via: ");
        String via = scanner.nextLine();
        System.out.print("Numero Civico: ");
        String numeroCivico = scanner.nextLine();
        System.out.print("Città: ");
        String citta = scanner.nextLine();
        System.out.print("Provincia: ");
        String provincia = scanner.nextLine();
        System.out.print("CAP: ");
        String cap = scanner.nextLine();
        System.out.print("Nazione: ");
        String nazione = scanner.nextLine();

        Indirizzo indirizzo = new Indirizzo(via, numeroCivico, citta, provincia, cap);
        utente.setIndirizzo(indirizzo);
        utente.setNazione(nazione);

        String sql = "INSERT INTO indirizzi (userID, via, numeroCivico, citta, provincia, cap, nazione) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userID);
            statement.setString(2, via);
            statement.setString(3, numeroCivico);
            statement.setString(4, citta);
            statement.setString(5, provincia);
            statement.setString(6, cap);
            statement.setString(7, nazione);
            statement.executeUpdate();
            System.out.println("Indirizzo creato con successo!");
        } catch (SQLException e) {
            System.out.println("Errore durante la creazione dell'indirizzo: " + e.getMessage());
        }
    }

    public static void modificaIndirizzo(Scanner scanner) {
        if (utente.getIndirizzo() == null) {
            System.out.println("Nessun indirizzo registrato. Creane uno nuovo.");
            return;
        }

        System.out.println("Modifica indirizzo:");
        System.out.print("Nuova Via (" + utente.getIndirizzo().getVia() + "): ");
        String via = scanner.nextLine();
        System.out.print("Nuovo Numero Civico (" + utente.getIndirizzo().getNumeroCivico() + "): ");
        String numeroCivico = scanner.nextLine();
        System.out.print("Nuova Città (" + utente.getIndirizzo().getCitta() + "): ");
        String citta = scanner.nextLine();
        System.out.print("Nuova Provincia (" + utente.getIndirizzo().getProvincia() + "): ");
        String provincia = scanner.nextLine();
        System.out.print("Nuovo CAP (" + utente.getIndirizzo().getCap() + "): ");
        String cap = scanner.nextLine();
        System.out.print("Nuova Nazione (" + utente.getNazione() + "): ");
        String nazione = scanner.nextLine();

        // Aggiorna l'oggetto indirizzo
        utente.getIndirizzo().setVia(via.isEmpty() ? utente.getIndirizzo().getVia() : via);
        utente.getIndirizzo()
                .setNumeroCivico(numeroCivico.isEmpty() ? utente.getIndirizzo().getNumeroCivico() : numeroCivico);
        utente.getIndirizzo().setCitta(citta.isEmpty() ? utente.getIndirizzo().getCitta() : citta);
        utente.getIndirizzo().setProvincia(provincia.isEmpty() ? utente.getIndirizzo().getProvincia() : provincia);
        utente.getIndirizzo().setCap(cap.isEmpty() ? utente.getIndirizzo().getCap() : cap);
        utente.setNazione(nazione.isEmpty() ? utente.getNazione() : nazione);

        // Aggiorna il database
        String sql = "UPDATE indirizzi SET via = ?, numeroCivico = ?, citta = ?, provincia = ?, cap = ?, nazione = ? WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, utente.getIndirizzo().getVia());
            statement.setString(2, utente.getIndirizzo().getNumeroCivico());
            statement.setString(3, utente.getIndirizzo().getCitta());
            statement.setString(4, utente.getIndirizzo().getProvincia());
            statement.setString(5, utente.getIndirizzo().getCap());
            statement.setString(6, utente.getNazione());
            statement.setString(7, userID);
            statement.executeUpdate();
            System.out.println("Indirizzo modificato con successo!");
        } catch (SQLException e) {
            System.out.println("Errore durante la modifica dell'indirizzo: " + e.getMessage());
        }
    }

    public static void faiVersamento(Scanner scanner) {
        System.out.print("Inserisci l'importo da versare: ");
        double importo = 0;
        // Gestione dell'input non valido
        try {
            importo = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Input non valido. Inserisci un numero.");
            scanner.nextLine(); // Pulisce l'input
        }

        if (importo <= 0) {
            System.out.println("Importo non valido.");
            return;
        }

        String sql = "UPDATE utenti SET saldo = saldo + ? WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, importo);
            statement.setString(2, userID);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                utente.setSaldo(utente.getSaldo() + importo);
                System.out.println("Versamento di " + importo + " effettuato con successo.");
                System.out.println("Nuovo saldo: " + utente.getSaldo());

                // Registra la transazione
                registraTransazione(connection, "VERSAMENTO", importo, userID, userID);
            } else {
                System.out.println("Errore durante il versamento.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il versamento: " + e.getMessage());
        }
    }

    public static void faiPrelievo(Scanner scanner) {
        System.out.print("Inserisci l'importo da prelevare: ");
        double importo = 0;
        try {
            importo = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Input non valido. Inserisci un numero.");
            scanner.nextLine(); // Pulisce l'input
        }

        if (importo <= 0) {
            System.out.println("Importo non valido.");
            return;
        }

        if (utente.getSaldo() < importo) {
            System.out.println("Saldo insufficiente.");
            return;
        }

        String sql = "UPDATE utenti SET saldo = saldo - ? WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, importo);
            statement.setString(2, userID);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                utente.setSaldo(utente.getSaldo() - importo);
                System.out.println("Prelievo di " + importo + " effettuato con successo.");
                System.out.println("Nuovo saldo: " + utente.getSaldo());

                // Registra la transazione
                registraTransazione(connection, "PRELIEVO", importo, userID, userID);
            } else {
                System.out.println("Errore durante il prelievo.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il prelievo: " + e.getMessage());
        }
    }

    public static void faiTrasferimento(Scanner scanner) {
        System.out.print("Inserisci il telefono del destinatario: ");
        String telefonoDestinatario = scanner.nextLine();
        System.out.print("Inserisci l'importo da trasferire: ");
        double importo = 0;
        // Gestione dell'input non valido
        try {
            importo = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Input non valido. Inserisci un numero.");
            scanner.nextLine(); // Pulisce l'input
        }


        if (importo <= 0) {
            System.out.println("Importo non valido.");
            return;
        }

        if (utente.getSaldo() < importo) {
            System.out.println("Saldo insufficiente.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // Inizia la transazione

            try {
                // Verifica che il telefono destinatario esista e sia diverso dal mittente
                String sqlVerifica = "SELECT userID FROM utenti WHERE telefono = ? AND userID != ? AND verificato = 1";
                String destinatarioID = null;

                try (PreparedStatement verificaStmt = connection.prepareStatement(sqlVerifica)) {
                    verificaStmt.setString(1, telefonoDestinatario);
                    verificaStmt.setString(2, userID);
                    try (ResultSet rs = verificaStmt.executeQuery()) {
                        if (rs.next()) {
                            destinatarioID = rs.getString("userID");
                        } else {
                            System.out.println("Destinatario non trovato o non verificato.");
                            return;
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Errore durante la verifica del destinatario: " + e.getMessage());
                    return;
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

                connection.commit(); // Conferma la transazione
                System.out.println("Trasferimento di " + importo + " effettuato con successo.");
                System.out.println("Nuovo saldo: " + utente.getSaldo());
            } catch (SQLException e) {
                connection.rollback(); // Annulla la transazione in caso di errore
                System.out.println("Errore durante il trasferimento: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true); // Ripristina l'autocommit
            }
        } catch (SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }

    private static void registraTransazione(Connection connection, String tipo, double importo, String mittenteID,
            String destinatarioID) throws SQLException {
        String sql = "INSERT INTO transazioni (tipo, importo, mittenteID, destinatarioID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tipo);
            statement.setDouble(2, importo);
            statement.setString(3, mittenteID);
            statement.setString(4, destinatarioID);
            statement.executeUpdate();
        }
    }

    // Metodi per l'amministratore
    public static void approvaUtente(Scanner scanner) {
        System.out.println("Utenti in attesa di approvazione:");
        String sql = "SELECT userID, nome, cognome, email FROM utenti WHERE verificato = 0";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Nessun utente in attesa di approvazione.");
                return;
            }

            do {
                System.out.println("ID: " + resultSet.getString("userID") +
                        " - Nome: " + resultSet.getString("nome") +
                        " " + resultSet.getString("cognome") +
                        " - Email: " + resultSet.getString("email"));
            } while (resultSet.next());

            System.out.print("Inserisci l'ID dell'utente da approvare: ");
            String userID = scanner.nextLine();

            String sqlApprovazione = "UPDATE utenti SET verificato = 1 WHERE userID = ?";
            try (PreparedStatement approvaStmt = connection.prepareStatement(sqlApprovazione)) {
                approvaStmt.setString(1, userID);
                int rowsUpdated = approvaStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Utente approvato con successo.");

                    // Invia notifica all'utente
                    String sqlNotifica = "INSERT INTO messaggi (mittenteID, destinatarioID, titolo, contenuto) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement notificaStmt = connection.prepareStatement(sqlNotifica)) {
                        notificaStmt.setString(1, "Sistema");
                        notificaStmt.setString(2, userID);
                        notificaStmt.setString(3, "Account approvato");
                        notificaStmt.setString(4,
                                "Il tuo account è stato approvato. Ora puoi accedere a tutte le funzionalità.");
                        notificaStmt.executeUpdate();
                    }
                } else {
                    System.out.println("Utente non trovato o già approvato.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante l'approvazione: " + e.getMessage());
        }
    }

    public static void visualizzaStoricoTransazioni() {
        System.out.println("Storico transazioni:");

        String sql = "SELECT * FROM transazioni WHERE mittenteID = ? OR destinatarioID = ? ORDER BY data_transazione DESC";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userID);
            statement.setString(2, userID);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Data\t\tTipo\t\tImporto\t\tMittente\tDestinatario");
            System.out.println("---------------------------------------------------------------");

            while (resultSet.next()) {
                String tipo = resultSet.getString("tipo");
                double importo = resultSet.getDouble("importo");
                String mittente = resultSet.getString("mittenteID");
                String destinatario = resultSet.getString("destinatarioID");
                String data = resultSet.getString("data_transazione");

                // Format the output nicely
                System.out.printf("%-15s %-15s %-10.2f %-15s %-15s%n",
                        data.substring(0, 16), // Just show date and time (first 16 chars)
                        tipo,
                        importo,
                        mittente.equals(userID) ? "Tu" : mittente,
                        destinatario.equals(userID) ? "Tu" : destinatario);
            }

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Nessuna transazione trovata.");
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero delle transazioni: " + e.getMessage());
        }
    }

    // Visualizza i dettagli del profilo
    public static void visualizzaProfilo() {
        System.out.println("Dettagli del profilo:");
        System.out.println("Nome: " + utente.getNome());
        System.out.println("Cognome: " + utente.getCognome());
        System.out.println("UserID: " + utente.getUserID());
        System.out.println("Email: " + utente.getEmail());
        System.out.println("Telefono: " + utente.getTelefono());
        System.out.println("Tipo Utente: " + utente.getTipo());

        // Mostra l'indirizzo se disponibile
        if (utente.getIndirizzo() != null) {
            Indirizzo indirizzo = utente.getIndirizzo();
            System.out.println("Indirizzo:");
            System.out.println(indirizzo.getVia() + " " + indirizzo.getNumeroCivico());
            System.out.println(indirizzo.getCap() + " " + indirizzo.getCitta() + " (" + indirizzo.getProvincia() + ")");
            if (utente.getNazione() != null && !utente.getNazione().isEmpty()) {
                System.out.println(utente.getNazione());
            }
        } else {
            System.out.println("Nessun indirizzo registrato.");
        }
    }

    public static void visualizzaDettagliConto() {
        System.out.println("Dettagli del conto:");
        System.out.println("Proprietario: " + utente.getNome() + " " + utente.getCognome());
        System.out.println("UserID: " + utente.getUserID());
        System.out.println("Telefono: " + utente.getTelefono());
        System.out.printf("Saldo attuale: %.2f EUR%n", utente.getSaldo());

        // Show address if available
        if (utente.getIndirizzo() != null) {
            Indirizzo indirizzo = utente.getIndirizzo();
            System.out.println("Indirizzo:");
            System.out.println(indirizzo.getVia() + " " + indirizzo.getNumeroCivico());
            System.out.println(indirizzo.getCap() + " " + indirizzo.getCitta() + " (" + indirizzo.getProvincia() + ")");
            if (utente.getNazione() != null && !utente.getNazione().isEmpty()) {
                System.out.println(utente.getNazione());
            }
        } else {
            System.out.println("Nessun indirizzo registrato.");
        }

        // Show last 3 transactions
        System.out.println("\nUltime 3 transazioni:");
        String sql = "SELECT * FROM transazioni WHERE mittenteID = ? OR destinatarioID = ? " +
                "ORDER BY data_transazione DESC LIMIT 3";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userID);
            statement.setString(2, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String tipo = resultSet.getString("tipo");
                double importo = resultSet.getDouble("importo");
                String data = resultSet.getString("data_transazione");

                System.out.printf("[%s] %s: %.2f EUR%n",
                        data.substring(0, 16),
                        tipo,
                        importo);
            }

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Nessuna transazione recente.");
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero delle transazioni recenti: " + e.getMessage());
        }
    }

    public static void gestioneUtenti(Scanner scanner) {
        while (true) {
            System.out.println("Gestione utenti:");
            System.out.println("1. Visualizza tutti gli utenti");
            System.out.println("2. Modifica tipo utente");
            System.out.println("3. Blocca utente");
            System.out.println("4. Elimina utente");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (scelta) {
                case 1:
                    visualizzaUtenti();
                    break;
                case 2:
                    modificaTipoUtente(scanner);
                    break;
                case 3:
                    bloccaUtente(scanner);
                    break;
                case 4:
                    eliminaUtente(scanner);
                    break;
                case 0:
                    System.out.println("Tornando al menu principale...");
                    return; // Torna al menu principale
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    private static void visualizzaUtenti() {
        String sql = "SELECT userID, nome, cognome, email, tipoUtente, verificato, telefono, saldo FROM utenti";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Elenco utenti:");
            System.out.println("ID\tNome\tCognome\tEmail\tTipo\tTelefono\tSaldo\tApprovato");
            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString("userID") + "\t" +
                                resultSet.getString("nome") + "\t" +
                                resultSet.getString("cognome") + "\t" +
                                resultSet.getString("email") + "\t" +
                                resultSet.getString("tipoUtente") + "\t" +
                                resultSet.getString("telefono") + "\t" +
                                resultSet.getDouble("saldo") + "\t" +
                                (resultSet.getInt("verificato") == 1 ? "Sì" : "No"));
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero degli utenti: " + e.getMessage());
        }
    }

    private static void modificaTipoUtente(Scanner scanner) {
        System.out.print("Inserisci l'ID dell'utente da modificare: ");
        String userID = scanner.nextLine();
        System.out.print("Nuovo tipo (ADMIN/CLIENTE): ");
        String nuovoTipo = scanner.nextLine().toUpperCase();

        if (!nuovoTipo.equals("ADMIN") && !nuovoTipo.equals("CLIENTE")) {
            System.out.println("Tipo non valido.");
            return;
        }

        String sql = "UPDATE utenti SET tipoUtente = ? WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nuovoTipo);
            statement.setString(2, userID);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Tipo utente modificato con successo.");
            } else {
                System.out.println("Utente non trovato.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la modifica: " + e.getMessage());
        }
    }

    private static void bloccaUtente(Scanner scanner) {
        System.out.print("Inserisci l'ID dell'utente da bloccare: ");
        String userID = scanner.nextLine();

        String sql = "UPDATE utenti SET verificato = 0 WHERE userID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userID);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Utente bloccato con successo.");
            } else {
                System.out.println("Utente non trovato.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il blocco: " + e.getMessage());
        }
    }

    public static void eliminaMessaggio(Scanner scanner) {
        System.out.print("Inserisci l'ID del messaggio da eliminare: ");
        int idMessaggio = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM messaggi WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idMessaggio);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Messaggio eliminato con successo.");
            } else {
                System.out.println("Messaggio non trovato.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    public static void eliminaUtente(Scanner scanner) {
        System.out.print("Inserisci l'ID dell'utente da eliminare: ");
        String userID = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // Inizia la transazione

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
                        connection.commit(); // Conferma la transazione
                        System.out.println("Utente e tutti i dati correlati eliminati con successo.");
                    } else {
                        System.out.println("Utente non trovato.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback(); // Annulla la transazione in caso di errore
                System.out.println("Errore durante l'eliminazione: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true); // Ripristina l'autocommit
            }
        } catch (SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }

}