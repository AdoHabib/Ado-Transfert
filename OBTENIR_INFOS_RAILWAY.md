# 🚀 Comment Obtenir les Informations Railway

## 📋 **Étapes pour Récupérer les Variables Railway**

### 1. **Connectez-vous à Railway**
- Allez sur : https://railway.app
- Connectez-vous à votre compte

### 2. **Sélectionnez votre Projet**
- Cliquez sur votre projet "Ado-Transfert"
- Ou créez un nouveau projet si nécessaire

### 3. **Accédez à la Base de Données**
- Dans le dashboard Railway, cherchez la section "Database"
- Cliquez sur votre base de données MySQL

### 4. **Récupérez les Informations de Connexion**
- Cliquez sur l'onglet "Connect"
- Copiez les informations suivantes :

```
Host: containers-us-west-xxx.railway.app
Port: 6543
Database: railway
User: root
Password: [votre-mot-de-passe]
```

### 5. **Récupérez le Domaine Public**
- Dans le dashboard, cherchez "Settings" ou "Domains"
- Copiez votre domaine public (ex: votre-app.railway.app)

## 🔧 **Configuration Locale**

### **Option A: Modifier le Script**
Éditez le fichier `config_railway_quick.bat` et remplacez :

```batch
set ADO_DB_HOST=containers-us-west-xxx.railway.app
set ADO_DB_PORT=6543
set ADO_DB_NAME=railway
set ADO_DB_USER=root
set ADO_DB_PASSWORD=votre-mot-de-passe-railway
set RAILWAY_PUBLIC_DOMAIN=votre-app.railway.app
```

### **Option B: Définir Manuellement**
Dans PowerShell/CMD :

```cmd
set ADO_DB_HOST=containers-us-west-xxx.railway.app
set ADO_DB_PORT=6543
set ADO_DB_NAME=railway
set ADO_DB_USER=root
set ADO_DB_PASSWORD=votre-mot-de-passe
set RAILWAY_PUBLIC_DOMAIN=votre-app.railway.app
```

## 🧪 **Test de Connexion**

Après configuration :

```cmd
config_railway_quick.bat
```

Ou directement :

```cmd
test_railway_connection.bat
```

## ❓ **Si Vous N'Avez Pas de Projet Railway**

### **Créer un Nouveau Projet**
1. Allez sur https://railway.app
2. Cliquez sur "New Project"
3. Sélectionnez "Deploy from GitHub repo"
4. Choisissez votre repository "Ado-Transfert"
5. Railway créera automatiquement une base MySQL

### **Variables Automatiques Railway**
Railway définit automatiquement :
- `MYSQL_HOST` → `ADO_DB_HOST`
- `MYSQL_PORT` → `ADO_DB_PORT`
- `MYSQL_DATABASE` → `ADO_DB_NAME`
- `MYSQL_USER` → `ADO_DB_USER`
- `MYSQL_PASSWORD` → `ADO_DB_PASSWORD`
- `RAILWAY_PUBLIC_DOMAIN` → `RAILWAY_PUBLIC_DOMAIN`

## 🆘 **Problèmes Courants**

### **"Variables non définies"**
- Vérifiez que vous avez bien copié les valeurs Railway
- Assurez-vous que les variables sont définies dans la même session

### **"Connexion refusée"**
- Vérifiez que votre projet Railway est actif
- Vérifiez que la base de données est démarrée
- Vérifiez les credentials

### **"Host introuvable"**
- Vérifiez l'URL du host Railway
- Assurez-vous que c'est bien l'URL de la base de données, pas de l'app

## 💡 **Conseils**

- **Sauvegardez** vos credentials Railway
- **Testez** d'abord avec `test_railway_connection.bat`
- **Utilisez** les variables d'environnement pour la sécurité
- **Vérifiez** que votre projet Railway est déployé et actif
