import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Server principale per il sistema RMI Ado-Transfert
 * Avvia il registry RMI e registra il servizio bancario
 */
public class Server {
    private static final int PORT = 1099; // Porta standard RMI
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("=== SERVER RMI ADO-TRANSFERT ===");
            System.out.println("Avvio del server...");
            
            // Richiedi la password del database
            System.out.print("Inserisci la password del database MySQL (root): ");
            String dbPassword = scanner.nextLine();
            
            // Se la password è vuota, usa quella di default
            if (dbPassword.trim().isEmpty()) {
                dbPassword = "1234";
                System.out.println("Password non inserita, uso password di default: 1234");
            }
            
            System.out.println("Connessione al database con le credenziali fornite...");
            
            // Esporta l'oggetto remoto con la password del database
            InterfaceImpl server = new InterfaceImpl(dbPassword);
            
            // Crea il registry sulla porta 1099 (porta standard RMI)
            Registry registry = LocateRegistry.createRegistry(PORT);
            System.out.println("Registry RMI creato sulla porta: "+ PORT);
            
            // Registra il servizio con il nome "AdoTransfertService"
            registry.rebind("AdoTransfertService", server);
            System.out.println("Servizio 'AdoTransfertService' registrato nel registry");
            
            System.out.println("=== SERVER AVVIATO CON SUCCESSO ===");
            System.out.println("Il server è in ascolto per connessioni client...");
            System.out.println("Premi Ctrl+C per fermare il server");
            
            // Mantiene il server in esecuzione
            /*while (true) {
                Thread.sleep(1000);
            }*/
            
        } catch (RemoteException e) {
            System.err.println("Errore RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Errore generico: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
