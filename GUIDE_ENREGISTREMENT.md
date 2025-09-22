# 📝 Guide d'Enregistrement - Ado-Transfert

## 🚀 **Démarrage de l'Application**

### **Option 1: Démarrage Complet (Recommandé)**
```cmd
start_complete_railway.bat
```

### **Option 2: Démarrage Manuel**
1. **Serveur Railway** (Terminal 1):
   ```cmd
   config_railway_final.bat
   ```

2. **Interface Graphique** (Terminal 2):
   ```cmd
   java -cp "bin;lib\*" AppGUI
   ```

## 📋 **Processus d'Enregistrement**

### **1. Ouvrir l'Interface Graphique**
- L'interface AppGUI va s'ouvrir
- Cherchez le bouton **"Registrati"** ou **"S'inscrire"**

### **2. Remplir le Formulaire d'Enregistrement**
- **Nome** : Votre prénom
- **Cognome** : Votre nom de famille
- **UserID** : Identifiant unique (ex: votre email ou nom d'utilisateur)
- **Telefono** : Numéro de téléphone
- **Email** : Adresse email valide
- **Password** : Mot de passe sécurisé

### **3. Soumettre l'Enregistrement**
- Cliquez sur **"Registrati"** ou **"Créer un compte"**
- Attendez la confirmation

## ⚠️ **Problèmes Courants**

### **"Erreur de connexion au serveur"**
- Vérifiez que le serveur Railway est démarré
- Vérifiez que le port 1099 n'est pas bloqué

### **"Email déjà utilisé"**
- Utilisez une autre adresse email
- Ou connectez-vous avec l'email existant

### **"UserID déjà existant"**
- Choisissez un autre UserID
- Utilisez votre email comme UserID

### **"Erreur de validation"**
- Vérifiez que tous les champs sont remplis
- Vérifiez le format de l'email
- Vérifiez que le mot de passe est assez fort

## 🔧 **Dépannage**

### **Si l'interface ne se lance pas**
```cmd
# Arrêter tous les processus Java
taskkill /f /im java.exe

# Relancer l'application
start_complete_railway.bat
```

### **Si le serveur ne démarre pas**
```cmd
# Vérifier que le port 1099 est libre
netstat -an | findstr :1099

# Si occupé, arrêter le processus
taskkill /f /im java.exe
```

### **Si la connexion à Railway échoue**
- Vérifiez vos credentials Railway
- Vérifiez que votre projet Railway est actif
- Vérifiez votre connexion internet

## 📞 **Support**

### **Informations de Connexion**
- **Serveur** : Railway (trolley.proxy.rlwy.net:43111)
- **Base de données** : railway
- **Utilisateur** : root
- **Port RMI** : 1099

### **Logs Utiles**
- Vérifiez la console du serveur pour les erreurs
- Vérifiez la console de l'interface graphique
- Les erreurs de base de données apparaissent dans le serveur

## ✅ **Vérification du Succès**

Après enregistrement, vous devriez voir :
- Message de confirmation
- Possibilité de vous connecter
- Interface utilisateur complète

## 🎯 **Prochaines Étapes**

1. **Enregistrez-vous** avec l'interface graphique
2. **Connectez-vous** avec vos credentials
3. **Explorez** les fonctionnalités de l'application
4. **Testez** les transactions financières

---

**💡 Conseil** : Gardez vos credentials en sécurité et notez votre UserID pour la connexion future.
