import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;


/**
 * Implementazione del server RMI per il sistema Ado-Transfert
 * Gestisce tutte le operazioni bancarie e l'accesso al database
 */
public class InterfaceImpl extends UnicastRemoteObject implements InterfaceTransfer {
    private static final long serialVersionUID = 1L;
    private App app;
    
    public InterfaceImpl(String dbPassword) throws RemoteException {
        super();
        app = new App(dbPassword);
        System.out.println("Server RMI Ado-Transfert inizializzato");

    }

    // === AUTENTICAZIONE E GESTIONE UTENTI ===
    

    public String login(String email, String password) throws RemoteException {
       return app.login(email, password);
    }

    
    public String logout(String userID) throws RemoteException {        
        return app.logout(userID);
    }

    
    public String creaUtente(String nome, String cognome, String userID, String telefono, String email, String password) throws RemoteException {
        return app.creaUtente(nome, cognome, userID, telefono, email, password);
    }

 
    public String approvaUtente(String userID) throws RemoteException {
        return app.approvaUtente(userID);
    }

    // === RECUPERO PASSWORD ===
    
    public String recuperoPassword(String userID, String email) throws RemoteException {
        return app.recuperoPassword(userID, email);
    }

    public String resetPasswordWithToken(String token, String newPassword) throws RemoteException {
        return app.resetPasswordWithToken(token, newPassword);
    }

    // === GESTIONE PROFILO ===
    
    
    public String visualizzaProfilo(String userID) throws RemoteException {       
        return app.visualizzaProfilo(userID);
    }

   
    public String modificaProfilo(String userID, String nome, String cognome, String telefono) throws RemoteException {
       return app.modificaProfilo(userID, nome, cognome, telefono);
    }

    
    public String visualizzaDettagliConto(String userID) throws RemoteException {
       return app.visualizzaDettagliConto(userID);
    }

    // === GESTIONE INDIRIZZI ===
    
    
    public String creaIndirizzo(String userID, String via, String numeroCivico, String citta, String provincia, String cap, String nazione) throws RemoteException {
       return app.creaIndirizzo(userID, via, numeroCivico, citta, provincia, cap, nazione);
    }

    
    public String modificaIndirizzo(String userID, String via, String numeroCivico, String citta, String provincia, String cap, String nazione) throws RemoteException {
         return app.modificaIndirizzo(userID, via, numeroCivico, citta, provincia, cap, nazione);
    }

    // === TRANSazioni FINANZIARIE ===
    
    
    public String faiVersamento(String userID, double importo) throws RemoteException {
        return app.faiVersamento(userID, importo);
    }

    
    public String faiPrelievo(String userID, double importo) throws RemoteException {
        return app.faiPrelievo(userID, importo);
    }

    
    public String faiTrasferimento(String userID, String telefonoDestinatario, double importo) throws RemoteException {
        return app.faiTrasferimento(userID, telefonoDestinatario, importo);
    }

    
    public String visualizzaStoricoTransazioni(String userID) throws RemoteException {
        return app.visualizzaStoricoTransazioni(userID);
    }

    // === SISTEMA MESSAGGISTICA ===
    
    
    public String visualizzaMessaggi(String userID) throws RemoteException {
        return app.visualizzaMessaggi(userID);
    }

    
    public String creaMessaggio(String userID, String destinatarioID, String titolo, String contenuto) throws RemoteException {
        return app.creaMessaggio(userID, destinatarioID, titolo, contenuto);
    }

    
    public String eliminaMessaggio(String userID, int idMessaggio) throws RemoteException {
        return app.eliminaMessaggio(userID, idMessaggio);
    }

    // === AMMINISTRAZIONE (SOLO ADMIN) ===
    
    
    public String visualizzaUtenti(String adminID) throws RemoteException {
        return app.visualizzaUtenti(adminID);
    }

    
    public String modificaTipoUtente(String adminID, String userID, String nuovoTipo) throws RemoteException {
        return app.modificaTipoUtente(adminID, userID, nuovoTipo);
    }

    
    public String bloccaUtente(String adminID, String userID) throws RemoteException {
        return app.bloccaUtente(adminID, userID);
    }

    
    public String eliminaUtente(String adminID, String userID) throws RemoteException {
        return app.eliminaUtente(adminID, userID);
    }

    // === UTILITÀ ===
    
    
    public String testConnection() throws RemoteException {
        return app.testConnection();
    }

    
    public String getServerStatus() throws RemoteException {
        return app.getServerStatus();
    }


    public String about() throws RemoteException {
        return app.about();
    }

}
