


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Utente {
    private String nome;
    private String cognome;
    private String userID;
    private String telefono;
    private String email;
    private String password;
    private String tipo;
    private double saldo;
    private Indirizzo indirizzo;
    private String nazione;
    private List<Messaggio> messaggi;
    private boolean verificato;
    private String dataCrezione;
    private String dataUltimoAccesso;

    public Utente(String nome, String cognome, String userID, String telefono, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.userID = userID;
        this.telefono = (telefono != null && !telefono.isEmpty()) ? telefono : "N/A";
        this.indirizzo = null; // Inizialmente l'indirizzo è nullo
        this.email = email;
        this.password = password;
        this.messaggi = new ArrayList<>();
        this.saldo = 0.0; // Saldo iniziale
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email, boolean isValid) {
        if (isValid) {
            this.email = email; // Se l'email è già valida, la impostiamo direttamente
        } else {
            if (isEmailValida(email)) {
                this.email = email; // Altrimenti controlliamo se è valida
            } else {
                throw new IllegalArgumentException("Email non valida");
            }
        }
    }

    public String getTipo() {
        return tipo;
    }

    public double getSaldo() {
        return saldo;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public String getNazione() {
        return nazione;
    }

    public List<Messaggio> getMessaggi() {
        return messaggi;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public void aggiungiMessaggio(Messaggio messaggio) {
        this.messaggi.add(messaggio);
    }

    // Metodi di validazione
    public static boolean isEmailValida(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isPasswordValida(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*");
    }

    public String getDataCreazione() {
        return dataCrezione;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCrezione = dataCreazione;
    }

    public String getDataUltimoAccesso() {
        return dataUltimoAccesso;
    }

    public void setDataUltimoAccesso(String dataUltimoAccesso) {
        this.dataUltimoAccesso = dataUltimoAccesso;
    }

    public String getVerificato() {
        return verificato ? "Utente verificato" : "Utente non verificato";
    }

    // Metodo per fare l'hash della password
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash); // Codifica in Base64
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore durante l'hashing della password", e);
        }
    }

    // Metodo per verificare se la password è corretta
    public boolean verificaPassword(String password) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(this.password); // Confronta l'hash della password inserita con quello memorizzato
    }

    // Metodo per impostare la password (fa l'hash automaticamente)
    public void setPassword(String password, boolean isHashed) {
        if (isHashed) {
            this.password = password; // Se la password è già hashata, la impostiamo direttamente
        } else {
            this.password = hashPassword(password); // Altrimenti facciamo l'hash
        }
    }

    public String getPassword() {
        throw new UnsupportedOperationException("Accesso diretto alla password non consentito.");
    }

    public void setPassword(String password) {
        this.password = hashPassword(password); // Hash della password
    }

    public boolean isApprovato() {
        return verificato;
    }

    public void setApprovato(boolean approvato) {
        this.verificato = approvato;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "tipo='" + tipo + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", userID='" + userID + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", indirizzo=" + indirizzo +
                ", approvato=" + verificato +
                ", saldo=" + getSaldo() +
                '}';
    }

    public boolean isTelefonoValido(String telefono) {
        String telefonoRegex = "^\\+?[0-9]{10,15}$"; // Modifica il regex in base al formato desiderato
        return telefono.matches(telefonoRegex);
    }

    public boolean isIbanValido(String iban) {
        // Controlla se l'IBAN ha 27 caratteri e inizia con "IT"
        String ibanRegex = "^IT[0-9]{2}[A-Z]{1}[0-9]{10}[A-Z]{1}[0-9]{10}$";
        return iban.matches(ibanRegex);
    }

    public String generaIban() {
        String countryCode = "IT"; // Codice paese (es. IT per Italia)
        int checkDigits = new Random().nextInt(90) + 10; // Due cifre di controllo casuali
        String bankCode = "12345"; // Codice banca (esempio)
        String branchCode = "67890"; // Codice filiale (esempio)
        String accountNumber = String.format("%012d", new Random().nextLong() & Long.MAX_VALUE); // Numero conto a 12
                                                                                                 // cifre

        // Combina i componenti per formare l'IBAN
        return countryCode + checkDigits + bankCode + branchCode + accountNumber;
    }

}
