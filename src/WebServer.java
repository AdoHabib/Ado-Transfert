import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.Executors;

/**
 * Serveur web simple pour Railway - Affiche les informations de l'application
 * et sert les fichiers statiques
 */
public class WebServer {
    private static final int WEB_PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    private static final String PUBLIC_DOMAIN = System.getenv("RAILWAY_PUBLIC_DOMAIN");
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(WEB_PORT)) {
            System.out.println("🌐 Serveur web démarré sur le port: " + WEB_PORT);
            System.out.println("🔗 URL publique: https://" + PUBLIC_DOMAIN);
            
            var executor = Executors.newFixedThreadPool(10);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleRequest(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur web: " + e.getMessage());
        }
    }
    
    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts.length > 1 ? parts[1] : "/";
            
            System.out.println("Requête: " + method + " " + path);
            
            if (method.equals("GET")) {
                handleGetRequest(path, out);
            } else {
                sendResponse(out, "405 Method Not Allowed", "text/plain", "Méthode non supportée");
            }
            
        } catch (IOException e) {
            System.err.println("Erreur traitement requête: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignorer
            }
        }
    }
    
    private static void handleGetRequest(String path, PrintWriter out) {
        switch (path) {
            case "/":
            case "/index.html":
                serveHomePage(out);
                break;
            case "/login.html":
                serveLoginPage(out);
                break;
            case "/status":
                serveStatus(out);
                break;
            default:
                sendResponse(out, "404 Not Found", "text/html", 
                    "<html><body><h1>404 - Page non trouvée</h1>" +
                    "<p><a href='/'>Retour à l'accueil</a></p></body></html>");
        }
    }
    
    private static void serveHomePage(PrintWriter out) {
        String html = "<!DOCTYPE html>" +
            "<html><head>" +
            "<title>Ado-Transfert - Système de Transfert d'Argent</title>" +
            "<meta charset='UTF-8'>" +
            "<style>" +
            "body { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }" +
            ".header { background: linear-gradient(135deg, #0066cc, #004499); color: white; padding: 30px; text-align: center; border-radius: 10px; }" +
            ".content { padding: 20px; }" +
            ".button { background: #0066cc; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; display: inline-block; margin: 10px; }" +
            ".status { background: #f0f8ff; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
            "</style></head><body>" +
            "<div class='header'>" +
            "<h1>🏦 Ado-Transfert</h1>" +
            "<h2>Système de Transfert d'Argent Sécurisé</h2>" +
            "</div>" +
            "<div class='content'>" +
            "<h3>Bienvenue sur Ado-Transfert</h3>" +
            "<p>Un système de transfert d'argent sûr et fiable développé par Habib Ado.</p>" +
            "<div class='status'>" +
            "<h4>📊 Statut du Service</h4>" +
            "<p><strong>Serveur RMI:</strong> Actif sur le port 1099</p>" +
            "<p><strong>Domaine:</strong> " + PUBLIC_DOMAIN + "</p>" +
            "<p><strong>Statut:</strong> <span style='color: green;'>✅ Opérationnel</span></p>" +
            "</div>" +
            "<h4>🔗 Accès à l'Application</h4>" +
            "<a href='/login.html' class='button'>🔑 Interface de Connexion</a>" +
            "<a href='/status' class='button'>📈 Statut Détaillé</a>" +
            "<h4>ℹ️ Informations</h4>" +
            "<ul>" +
            "<li>Version: 1.0</li>" +
            "<li>Type: Serveur RMI Java</li>" +
            "<li>Base de données: MySQL (Railway)</li>" +
            "<li>Développeur: Habib Ado</li>" +
            "</ul>" +
            "</div></body></html>";
        
        sendResponse(out, "200 OK", "text/html", html);
    }
    
    private static void serveLoginPage(PrintWriter out) {
        try {
            Path loginPath = Paths.get("web/login.html");
            if (Files.exists(loginPath)) {
                String content = Files.readString(loginPath);
                sendResponse(out, "200 OK", "text/html", content);
            } else {
                sendResponse(out, "404 Not Found", "text/html", 
                    "<html><body><h1>404 - Page de connexion non trouvée</h1>" +
                    "<p><a href='/'>Retour à l'accueil</a></p></body></html>");
            }
        } catch (IOException e) {
            sendResponse(out, "500 Internal Server Error", "text/html", 
                "<html><body><h1>Erreur serveur</h1><p>" + e.getMessage() + "</p></body></html>");
        }
    }
    
    private static void serveStatus(PrintWriter out) {
        String status = "{" +
            "\"service\": \"Ado-Transfert\"," +
            "\"version\": \"1.0\"," +
            "\"status\": \"operational\"," +
            "\"rmi_port\": 1099," +
            "\"web_port\": " + WEB_PORT + "," +
            "\"domain\": \"" + PUBLIC_DOMAIN + "\"," +
            "\"database\": \"MySQL (Railway)\"," +
            "\"timestamp\": \"" + java.time.Instant.now().toString() + "\"" +
            "}";
        
        sendResponse(out, "200 OK", "application/json", status);
    }
    
    private static void sendResponse(PrintWriter out, String status, String contentType, String body) {
        out.println("HTTP/1.1 " + status);
        out.println("Content-Type: " + contentType + "; charset=UTF-8");
        out.println("Content-Length: " + body.getBytes().length);
        out.println("Connection: close");
        out.println();
        out.println(body);
    }
}
