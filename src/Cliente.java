import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Client RMI per il sistema Ado-Transfert
 * Si connette al server RMI e fornisce l'interfaccia utente
 */
public class Cliente {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1099;
    private static final String SERVICE_NAME = "AdoTransfertService";
    
    private InterfaceTransfer serverStub;
    private String currentUserID = null;
    private String currentUserType = null;

    public static void main(String[] args) {
        Cliente client = new Cliente();
        client.start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Connessione al server RMI
            System.out.println("=== CLIENT RMI ADO-TRANSFERT ===");
            System.out.println("Connessione al server...");
            
            Registry registry = LocateRegistry.getRegistry(SERVER_HOST, SERVER_PORT);
            serverStub = (InterfaceTransfer) registry.lookup(SERVICE_NAME);
            
            System.out.println("Connesso al server RMI con successo!");
            
            // Test connessione database
            String testResult = serverStub.testConnection();
            System.out.println(testResult);
            
            // Menu principale
            while (true) {
                if (currentUserID == null) {
                    showMainMenu(scanner);
                } else if ("cliente".equalsIgnoreCase(currentUserType)) {
                    showClientMenu(scanner);
                } else if ("admin".equalsIgnoreCase(currentUserType)) {
                    showAdminMenu(scanner);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Errore di connessione al server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private void showMainMenu(Scanner scanner) {
        System.out.println("\n===================================");
        System.out.println("Benvenuto nel sistema di gestione delle transazioni!");
        System.out.println("1. Creare un conto utente");
        System.out.println("2. Effettuare il login");
        System.out.println("3. Informazioni sull'applicazione");
        System.out.println("0. Uscire");
        System.out.print("Scelta: ");
        
        try {
            int choice = leggiIntero(scanner.nextLine());
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createUser(scanner);
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
        } catch (Exception e) {
            System.out.println("Errore nell'input. Riprova.");
            scanner.nextLine(); // Pulisce l'input
        }
    }

    private void showClientMenu(Scanner scanner) {
        System.out.println("\n===================================");
        System.out.println("Benvenuto nel sistema bancario!");
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
        
        try {
            int choice = leggiIntero(scanner.nextLine());
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    showMessages();
                    break;
                case 2:
                    createMessage(scanner);
                    break;
                case 3:
                    createAddress(scanner);
                    break;
                case 4:
                    showTransactionMenu(scanner);
                    break;
                case 5:
                    showProfileMenu(scanner);
                    break;
                case 6:
                    deleteMessage(scanner);
                    break;
                case 7:
                    modifyAddress(scanner);
                    break;
                case 8:
                    showTransactionHistory();
                    break;
                case 9:
                    showAccountDetails();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.println("Scelta non valida, riprova.");
            }
        } catch (Exception e) {
            System.out.println("Errore nell'input. Riprova.");
            scanner.nextLine(); // Pulisce l'input
        }
    }

    private void showAdminMenu(Scanner scanner) {
        System.out.println("\n===================================");
        System.out.println("Benvenuto nel pannello amministratore!");
        System.out.println("1. Visualizza messaggi");
        System.out.println("2. Crea messaggio");
        System.out.println("3. Approva utente");
        System.out.println("4. Gestione utenti");
        System.out.println("5. Elimina messaggio");
        System.out.println("0. Logout");
        System.out.print("Scelta: ");
        
        try {
            int choice = leggiIntero(scanner.nextLine());
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    showMessages();
                    break;
                case 2:
                    createMessage(scanner);
                    break;
                case 3:
                    approveUser(scanner);
                    break;
                case 4:
                    showUserManagementMenu(scanner);
                    break;
                case 5:
                    deleteMessage(scanner);
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.println("Scelta non valida, riprova.");
            }
        } catch (Exception e) {
            System.out.println("Errore nell'input. Riprova.");
            scanner.nextLine(); // Pulisce l'input
        }
    }

    // === METODI DI AUTENTICAZIONE ===
    
    private void createUser(Scanner scanner) {
        System.out.println("\n==== Creazione di conto =====");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("UserID: ");
        String userID = scanner.nextLine();
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        try {
            String result = serverStub.creaUtente(nome, cognome, userID, telefono, email, password);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante la creazione dell'utente: " + e.getMessage());
        }
    }

    private void login(Scanner scanner) {
        System.out.println("\n==== Login =====");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        try {
            String result = serverStub.login(email, password);
            System.out.println(result);
            
            if (result.startsWith("SUCCESS:")) {
                // Estrai userID e tipo dal messaggio di successo
                // Per semplicità, assumiamo che l'utente sia un cliente
                // In una versione più avanzata, il server dovrebbe restituire questi dati
                currentUserID = email; // Usiamo l'email come identificatore temporaneo
                currentUserType = "cliente"; // Default, dovrebbe essere determinato dal server
                System.out.println("Login effettuato con successo!");
            }
        } catch (Exception e) {
            System.out.println("Errore durante il login: " + e.getMessage());
        }
    }

    private void logout() {
        try {
            if (currentUserID != null) {
                String result = serverStub.logout(currentUserID);
                System.out.println(result);
            }
            currentUserID = null;
            currentUserType = null;
            System.out.println("Logout effettuato con successo!");
        } catch (Exception e) {
            System.out.println("Errore durante il logout: " + e.getMessage());
        }
    }

    // === METODI PER MESSAGGI ===
    
    private void showMessages() {
        try {
            String result = serverStub.visualizzaMessaggi(currentUserID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei messaggi: " + e.getMessage());
        }
    }

    private void createMessage(Scanner scanner) {
        System.out.println("\n==== Creazione messaggio =====");
        System.out.print("Destinatario ID: ");
        String destinatarioID = scanner.nextLine();
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Contenuto: ");
        String contenuto = scanner.nextLine();
        
        try {
            String result = serverStub.creaMessaggio(currentUserID, destinatarioID, titolo, contenuto);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante l'invio del messaggio: " + e.getMessage());
        }
    }

    private void deleteMessage(Scanner scanner) {
        System.out.println("\n==== Eliminazione messaggio =====");
        System.out.print("ID del messaggio da eliminare: ");
        try {
            int idMessaggio = leggiIntero(scanner.nextLine());
            scanner.nextLine(); // Consume newline
            
            String result = serverStub.eliminaMessaggio(currentUserID, idMessaggio);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante l'eliminazione del messaggio: " + e.getMessage());
            scanner.nextLine(); // Pulisce l'input
        }
    }

    // === METODI PER INDIRIZZI ===
    
    private void createAddress(Scanner scanner) {
        System.out.println("\n==== Creazione indirizzo =====");
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
        
        try {
            String result = serverStub.creaIndirizzo(currentUserID, via, numeroCivico, citta, provincia, cap, nazione);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante la creazione dell'indirizzo: " + e.getMessage());
        }
    }

    private void modifyAddress(Scanner scanner) {
        System.out.println("\n==== Modifica indirizzo =====");
        System.out.print("Nuova Via: ");
        String via = scanner.nextLine();
        System.out.print("Nuovo Numero Civico: ");
        String numeroCivico = scanner.nextLine();
        System.out.print("Nuova Città: ");
        String citta = scanner.nextLine();
        System.out.print("Nuova Provincia: ");
        String provincia = scanner.nextLine();
        System.out.print("Nuovo CAP: ");
        String cap = scanner.nextLine();
        System.out.print("Nuova Nazione: ");
        String nazione = scanner.nextLine();
        
        try {
            String result = serverStub.modificaIndirizzo(currentUserID, via, numeroCivico, citta, provincia, cap, nazione);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante la modifica dell'indirizzo: " + e.getMessage());
        }
    }

    // === METODI PER TRANSazioni ===
    
    private void showTransactionMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n==== Transazioni =====");
            System.out.println("1. Fai versamento");
            System.out.println("2. Fai prelievo");
            System.out.println("3. Fai trasferimento");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            
            try {
                int choice = leggiIntero(scanner.nextLine());
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        makeDeposit(scanner);
                        break;
                    case 2:
                        makeWithdrawal(scanner);
                        break;
                    case 3:
                        makeTransfer(scanner);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            } catch (Exception e) {
                System.out.println("Errore nell'input. Riprova.");
                scanner.nextLine(); // Pulisce l'input
            }
        }
    }

    private void makeDeposit(Scanner scanner) {
        System.out.print("Inserisci l'importo da versare: ");
        try {
            double importo = leggiDouble(scanner.nextLine());
            scanner.nextLine(); // Consume newline
            
            String result = serverStub.faiVersamento(currentUserID, importo);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il versamento: " + e.getMessage());
            scanner.nextLine(); // Pulisce l'input
        }
    }

    private void makeWithdrawal(Scanner scanner) {
        System.out.print("Inserisci l'importo da prelevare: ");
        try {
            double importo = leggiDouble(scanner.nextLine());
            scanner.nextLine(); // Consume newline
            
            String result = serverStub.faiPrelievo(currentUserID, importo);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il prelievo: " + e.getMessage());
            scanner.nextLine(); // Pulisce l'input
        }
    }

    private void makeTransfer(Scanner scanner) {
        System.out.print("Inserisci il telefono del destinatario: ");
        String telefonoDestinatario = scanner.nextLine();
        System.out.print("Inserisci l'importo da trasferire: ");
        try {
            double importo = leggiDouble(scanner.nextLine());
            scanner.nextLine(); // Consume newline
            
            String result = serverStub.faiTrasferimento(currentUserID, telefonoDestinatario, importo);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il trasferimento: " + e.getMessage());
            scanner.nextLine(); // Pulisce l'input
        }
    }

    private void showTransactionHistory() {
        try {
            String result = serverStub.visualizzaStoricoTransazioni(currentUserID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dello storico: " + e.getMessage());
        }
    }

    // === METODI PER PROFILO ===
    
    private void showProfileMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n==== Area profilo =====");
            System.out.println("1. Modifica profilo");
            System.out.println("2. Visualizza dettagli profilo");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            
            try {
                int choice = leggiIntero(scanner.nextLine());
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        modifyProfile(scanner);
                        break;
                    case 2:
                        showProfile();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            } catch (Exception e) {
                System.out.println("Errore nell'input. Riprova.");
                scanner.nextLine(); // Pulisce l'input
            }
        }
    }

    private void showProfile() {
        try {
            String result = serverStub.visualizzaProfilo(currentUserID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero del profilo: " + e.getMessage());
        }
    }

    private void modifyProfile(Scanner scanner) {
        System.out.println("\n==== Modifica profilo =====");
        System.out.print("Nuovo Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nuovo Cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("Nuovo Telefono: ");
        String telefono = scanner.nextLine();
        
        try {
            String result = serverStub.modificaProfilo(currentUserID, nome, cognome, telefono);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante la modifica del profilo: " + e.getMessage());
        }
    }

    private void showAccountDetails() {
        try {
            String result = serverStub.visualizzaDettagliConto(currentUserID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei dettagli del conto: " + e.getMessage());
        }
    }

    // === METODI PER AMMINISTRAZIONE ===
    
    private void approveUser(Scanner scanner) {
        System.out.println("\n==== Approvazione utente =====");
        System.out.print("UserID dell'utente da approvare: ");
        String userID = scanner.nextLine();
        
        try {
            String result = serverStub.approvaUtente(userID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante l'approvazione: " + e.getMessage());
        }
    }

    private void showUserManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n==== Gestione utenti =====");
            System.out.println("1. Visualizza tutti gli utenti");
            System.out.println("2. Modifica tipo utente");
            System.out.println("3. Blocca utente");
            System.out.println("4. Elimina utente");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            
            try {
                int choice = leggiIntero(scanner.nextLine());
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        showAllUsers();
                        break;
                    case 2:
                        modifyUserType(scanner);
                        break;
                    case 3:
                        blockUser(scanner);
                        break;
                    case 4:
                        deleteUser(scanner);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Scelta non valida, riprova.");
                }
            } catch (Exception e) {
                System.out.println("Errore nell'input. Riprova.");
                scanner.nextLine(); // Pulisce l'input
            }
        }
    }

    private void showAllUsers() {
        try {
            String result = serverStub.visualizzaUtenti(currentUserID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero degli utenti: " + e.getMessage());
        }
    }

    private void modifyUserType(Scanner scanner) {
        System.out.println("\n==== Modifica tipo utente =====");
        System.out.print("UserID dell'utente da modificare: ");
        String userID = scanner.nextLine();
        System.out.print("Nuovo tipo (ADMIN/CLIENTE): ");
        String nuovoTipo = scanner.nextLine();
        
        try {
            String result = serverStub.modificaTipoUtente(currentUserID, userID, nuovoTipo);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante la modifica del tipo utente: " + e.getMessage());
        }
    }

    private void blockUser(Scanner scanner) {
        System.out.println("\n==== Blocco utente =====");
        System.out.print("UserID dell'utente da bloccare: ");
        String userID = scanner.nextLine();
        
        try {
            String result = serverStub.bloccaUtente(currentUserID, userID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante il blocco dell'utente: " + e.getMessage());
        }
    }

    private void deleteUser(Scanner scanner) {
        System.out.println("\n==== Eliminazione utente =====");
        System.out.print("UserID dell'utente da eliminare: ");
        String userID = scanner.nextLine();
        
        try {
            String result = serverStub.eliminaUtente(currentUserID, userID);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Errore durante l'eliminazione dell'utente: " + e.getMessage());
        }
    }

    private int leggiIntero(String input){
        try{
            return Integer.parseInt(input);
        } catch (NumberFormatException e){
            return -1;
        }
    }

    private double leggiDouble(String input){
        try{
            return Double.parseDouble(input);
        } catch (NumberFormatException e){
            return -1;
        }
    }

}