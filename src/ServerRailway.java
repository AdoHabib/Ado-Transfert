import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server Railway per il sistema RMI Ado-Transfert
 * Versione senza interazione utente per il deployment su Railway
 */
public class ServerRailway {
    private static final int PORT = 1099; // Porta standard RMI
    
    public static void main(String[] args) {
        try {
            System.out.println("=== SERVER RMI ADO-TRANSFERT (RAILWAY) ===");
            System.out.println("Avvio del server...");
            
            // Per Railway, usa le variabili d'ambiente per la password del database
            String dbPassword = System.getenv("ADO_DB_PASSWORD");
            if (dbPassword == null || dbPassword.trim().isEmpty()) {
                System.err.println("❌ ADO_DB_PASSWORD non impostata!");
                System.exit(1);
            }
            
            System.out.println("Connessione al database con credenziali Railway...");
            
            // Esporta l'oggetto remoto con la password del database da variabili d'ambiente
            InterfaceImpl server = new InterfaceImpl(dbPassword);
            
            // Crea il registry sulla porta 1099 (porta standard RMI)
            Registry registry = LocateRegistry.createRegistry(PORT);
            System.out.println("Registry RMI creato sulla porta: "+ PORT);
            
            // Registra il servizio con il nome "AdoTransfertService"
            registry.rebind("AdoTransfertService", server);
            System.out.println("Servizio 'AdoTransfertService' registrato nel registry");
            
            System.out.println("=== SERVER AVVIATO CON SUCCESSO ===");
            System.out.println("Il server è in ascolto per connessioni client...");
            System.out.println("Hostname Railway: " + System.getenv("RAILWAY_PUBLIC_DOMAIN"));
            
            // Mantiene il server in esecuzione
            while (true) {
                Thread.sleep(1000);
            }
            
        } catch (RemoteException e) {
            System.err.println("Errore RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Errore generico: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
