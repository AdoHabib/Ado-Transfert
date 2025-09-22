# 🔧 Résolution - Erreur de Connexion Railway

## ❌ **Erreur Actuelle**
```
Errore di connezione al server: error during JRMP connection establishment
```

## 🔍 **Cause du Problème**
L'interface graphique (AppGUI) essaie de se connecter à `localhost:1099` au lieu de `ado-transfer.up.railway.app:1099`.

## ✅ **Solution Appliquée**

### **1. Correction du Code**
- Modifié `AppGUI.java` pour utiliser la variable d'environnement `RAILWAY_PUBLIC_DOMAIN`
- L'interface se connecte maintenant automatiquement au bon serveur Railway

### **2. Configuration des Variables**
```batch
set RAILWAY_PUBLIC_DOMAIN=System.getenv().getOrDefault("RAILWAY_PUBLIC_DOMAIN");
set ADO_DB_HOST=System.getenv().getOrDefault("ADO_DB_HOST");
set ADO_DB_PORT=System.getenv().getOrDefault("ADO_DB_PORT");
set ADO_DB_NAME=System.getenv().getOrDefault("ADO_DB_NAME");
set ADO_DB_USER=System.getenv().getOrDefault("ADO_DB_USER");
set ADO_DB_PASSWORD=System.getenv().getOrDefault("ADO_DB_PASSWORD");
```

## 🚀 **Démarrage Correct**

### **Option 1: Script Automatique (Recommandé)**
```cmd
start_app_railway.bat
```

### **Option 2: Démarrage Manuel**
1. **Terminal 1 - Serveur Railway**:
   ```cmd
   set RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app
   set ADO_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW
   java -cp "bin;lib\*" -Djava.rmi.server.hostname=ado-transfer.up.railway.app ServerRailway
   ```

2. **Terminal 2 - Interface Graphique**:
   ```cmd
   set RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app
   java -cp "bin;lib\*" AppGUI
   ```

## 🔧 **Vérifications**

### **Serveur Railway**
Le serveur doit afficher :
```
=== SERVER AVVIATO CON SUCCESSO ===
Il server è in ascolto per connessioni client...
Hostname Railway: ado-transfer.up.railway.app
```

### **Interface Graphique**
L'interface doit afficher :
```
✅ Connesso al server
```

## 🐛 **Dépannage**

### **Si l'erreur persiste**
1. **Vérifiez que le serveur Railway est démarré**
2. **Vérifiez les variables d'environnement**
3. **Vérifiez que le port 1099 n'est pas bloqué**
4. **Redémarrez l'application**

### **Commandes de Vérification**
```cmd
# Vérifier les processus Java
tasklist | findstr java

# Vérifier le port 1099
netstat -an | findstr :1099

# Arrêter tous les processus Java
taskkill /f /im java.exe
```

## 📋 **Configuration Finale**

### **Variables d'Environnement Requises**
- `RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app`
- `ADO_DB_HOST=trolley.proxy.rlwy.net`
- `ADO_DB_PORT=43111`
- `ADO_DB_NAME=railway`
- `ADO_DB_USER=root`
- `ADO_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW`

### **Ports Utilisés**
- **RMI Server**: 1099
- **Database**: 43111
- **Web Server**: 8080 (optionnel)

## ✅ **Test de Fonctionnement**

Après démarrage correct :
1. L'interface graphique s'ouvre
2. Le statut affiche "✅ Connesso al server"
3. Vous pouvez vous enregistrer et vous connecter
4. Les fonctionnalités bancaires sont disponibles

---

**💡 Conseil**: Utilisez toujours `start_app_railway.bat` pour un démarrage automatique et correct.
