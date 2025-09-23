import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia RMI per il sistema Ado-Transfert
 * Definisce tutti i metodi remoti disponibili per i client
 */
public interface InterfaceTransfer extends Remote {
    
    // === AUTENTICAZIONE E GESTIONE UTENTI ===
    public String login(String email, String password) throws RemoteException;
    public String logout(String userID) throws RemoteException;
    public String creaUtente(String nome, String cognome, String userID, String telefono, String email, String password) throws RemoteException;
    public String approvaUtente(String userID) throws RemoteException;
    
    // === RECUPERO PASSWORD ===
    public String recuperoPassword(String userID, String email) throws RemoteException;
    public String resetPasswordWithToken(String token, String newPassword) throws RemoteException;
    
    // === GESTIONE PROFILO ===
    public String visualizzaProfilo(String userID) throws RemoteException;
    public String modificaProfilo(String userID, String nome, String cognome, String telefono) throws RemoteException;
    public String visualizzaDettagliConto(String userID) throws RemoteException;
    
    // === GESTIONE INDIRIZZI ===
    public String creaIndirizzo(String userID, String via, String numeroCivico, String citta, String provincia, String cap, String nazione) throws RemoteException;
    public String modificaIndirizzo(String userID, String via, String numeroCivico, String citta, String provincia, String cap, String nazione) throws RemoteException;
    
    // === TRANSazioni FINANZIARIE ===
    public String faiVersamento(String userID, double importo) throws RemoteException;
    public String faiPrelievo(String userID, double importo) throws RemoteException;
    public String faiTrasferimento(String userID, String telefonoDestinatario, double importo) throws RemoteException;
    public String visualizzaStoricoTransazioni(String userID) throws RemoteException;
    
    // === SISTEMA MESSAGGISTICA ===
    public String visualizzaMessaggi(String userID) throws RemoteException;
    public String creaMessaggio(String userID, String destinatarioID, String titolo, String contenuto) throws RemoteException;
    public String eliminaMessaggio(String userID, int idMessaggio) throws RemoteException;
    
    // === AMMINISTRAZIONE (SOLO ADMIN) ===
    public String visualizzaUtenti(String adminID) throws RemoteException;
    public String modificaTipoUtente(String adminID, String userID, String nuovoTipo) throws RemoteException;
    public String bloccaUtente(String adminID, String userID) throws RemoteException;
    public String eliminaUtente(String adminID, String userID) throws RemoteException;
    
    // === METODI COLLABORATORE ===
    public String gestisciDeposito(String collaboratoreID, String clienteID, double importo) throws RemoteException;
    public String gestisciPrelievo(String collaboratoreID, String clienteID, double importo) throws RemoteException;
    public String visualizzaTransazioniGestite(String collaboratoreID) throws RemoteException;
    
    // === UTILITÀ ===
    public String testConnection() throws RemoteException;
    public String getServerStatus() throws RemoteException;
    public String about() throws RemoteException;
}