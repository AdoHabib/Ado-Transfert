# 🚀 Guida Completa Railway + MySQL - Ado-Transfert

## 🎯 **Configurazione Railway + MySQL**

Railway è perfetto per l'hosting del tuo sistema Ado-Transfert con database MySQL. Ecco la guida completa.

## 📋 **Step 1: Setup Railway**

### **1.1 Crea Account Railway**
1. Vai su [Railway.app](https://railway.app)
2. Registrati con GitHub
3. Verifica l'email

### **1.2 Crea Progetto**
1. Clicca "New Project"
2. Scegli "Provision MySQL"
3. Railway creerà automaticamente un database MySQL

### **1.3 Ottieni Credenziali**
Railway ti fornirà queste informazioni nella dashboard:

```bash
# Credenziali Database Railway
MYSQL_HOST=containers-us-west-xxx.railway.app
MYSQL_PORT=3306
MYSQL_DATABASE=railway
MYSQL_USER=root
MYSQL_PASSWORD=la_password_generata_automaticamente
MYSQL_URL=mysql://root:password@containers-us-west-xxx.railway.app:3306/railway
```

## 🔧 **Step 2: Configurazione App**

### **2.1 Configurazione Automatica (CONSIGLIATO)**

```bash
# Usa lo script di configurazione automatica
.\setup_railway.bat

# Inserisci le credenziali Railway quando richiesto
# Lo script imposterà automaticamente le variabili d'ambiente
```

### **2.2 Configurazione Manuale**

#### **Opzione A: Variabili d'Ambiente**
```bash
# Imposta le variabili d'ambiente Windows
setx ADO_DB_HOST "containers-us-west-xxx.railway.app"
setx ADO_DB_PORT "3306"
setx ADO_DB_NAME "railway"
setx ADO_DB_USER "root"
setx ADO_DB_PASSWORD "la_tua_password_railway"

# Configura anche l'email
setx ADO_EMAIL_USERNAME "la_tua_email@gmail.com"
setx ADO_EMAIL_PASSWORD "la_tua_app_password"
```

#### **Opzione B: File di Configurazione**
```bash
# Crea file railway_secrets.properties (NON COMMITTARE)
echo railway.host=containers-us-west-xxx.railway.app > railway_secrets.properties
echo railway.port=3306 >> railway_secrets.properties
echo railway.database=railway >> railway_secrets.properties
echo railway.user=root >> railway_secrets.properties
echo railway.password=la_tua_password_railway >> railway_secrets.properties
```

## 🚀 **Step 3: Avvio del Sistema**

### **3.1 Compila l'App**
```bash
.\compile.bat
```

### **3.2 Avvia il Server**
```bash
# Opzione A: Con variabili d'ambiente (se configurate)
.\start_server.bat

# Opzione B: Con configurazione sicura
.\start_server_secure.bat
```

### **3.3 Testa la Connessione**
```bash
# Avvia l'interfaccia grafica
.\start_gui.bat
```

## 🔍 **Step 4: Verifica Configurazione**

### **4.1 Controlla i Log del Server**
Dovresti vedere:
```
✅ Configurazione database caricata da: Variabili d'ambiente
   Host: containers-us-west-xxx.railway.app:3306
   Database: railway
   User: root
🔗 URL Database: jdbc:mysql://containers-us-west-xxx.railway.app:3306/railway?useSSL=true&serverTimezone=UTC
Connesso al database MySQL
Database Railway 'railway' già disponibile
✅ Database inizializzato con successo
```

### **4.2 Testa le Funzionalità**
1. **Registrazione utente** - Dovrebbe funzionare
2. **Login** - Dovrebbe funzionare
3. **Recupero password** - Dovrebbe inviare email
4. **Transazioni** - Dovrebbero essere salvate nel database Railway

## 🛡️ **Step 5: Configurazione Email**

### **5.1 Setup Gmail**
1. Vai su [Google Account Security](https://myaccount.google.com/security)
2. Attiva "Verifica in due passaggi"
3. Genera "Password delle app" per Mail
4. **USA SOLO QUELLA PASSWORD** (non la password normale!)

### **5.2 Configura Email nell'App**
```bash
# Usa lo script di configurazione
.\setup_email_env.bat

# Oppure imposta manualmente
setx ADO_EMAIL_USERNAME "la_tua_email@gmail.com"
setx ADO_EMAIL_PASSWORD "la_tua_app_password"
```

## 🌐 **Step 6: Configurazione Dominio**

### **6.1 Setup Dominio per Reset Password**
```bash
# Usa lo script di configurazione dominio
.\setup_domain.bat

# Scegli GitHub Pages (gratuito) o altro
# Aggiorna il link nel codice
.\update_domain.bat
```

## 📊 **Architettura Finale**

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client GUI    │    │   Server RMI     │    │  Railway MySQL  │
│                 │◄──►│                  │◄──►│                 │
│  - Login        │    │  - App.java      │    │  - Database     │
│  - Registrazione│    │  - InterfaceImpl │    │  - Tabelle      │
│  - Transazioni  │    │  - Server.java   │    │  - Dati         │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │   Gmail SMTP     │
                       │                  │
                       │  - Invio email   │
                       │  - Reset password│
                       └──────────────────┘
```

## 🔧 **Configurazioni Avanzate**

### **Railway Environment Variables**
Puoi anche impostare le variabili direttamente in Railway:

1. Vai alla dashboard del tuo progetto
2. Clicca su "Variables"
3. Aggiungi:
   - `ADO_DB_HOST` = `containers-us-west-xxx.railway.app`
   - `ADO_DB_PORT` = `3306`
   - `ADO_DB_NAME` = `railway`
   - `ADO_DB_USER` = `root`
   - `ADO_DB_PASSWORD` = `la_tua_password`
   - `ADO_EMAIL_USERNAME` = `la_tua_email@gmail.com`
   - `ADO_EMAIL_PASSWORD` = `la_tua_app_password`

### **Railway Deployment**
Per deployare l'app su Railway:

1. Crea un `Dockerfile`:
```dockerfile
FROM openjdk:11-jre-slim
COPY bin/ /app/bin/
COPY lib/ /app/lib/
WORKDIR /app
EXPOSE 1099
CMD ["java", "-cp", "bin:lib/*", "Server"]
```

2. Connetti il repository GitHub a Railway
3. Railway farà il deploy automatico

## 🚨 **Risoluzione Problemi**

### **"Connection refused"**
```bash
# Verifica che le credenziali Railway siano corrette
# Controlla che il database sia attivo nella dashboard Railway
```

### **"Authentication failed"**
```bash
# Verifica la password del database Railway
# Controlla che l'utente sia 'root'
```

### **"Database not found"**
```bash
# Railway crea automaticamente il database 'railway'
# Verifica che stai usando il nome corretto
```

### **"Email not sent"**
```bash
# Verifica configurazione Gmail
# Controlla App Password (non password normale)
# Verifica variabili d'ambiente email
```

## 📈 **Monitoraggio Railway**

### **Dashboard Railway**
- **Metrics**: CPU, RAM, Network
- **Logs**: Log dell'applicazione
- **Database**: Query e performance
- **Variables**: Variabili d'ambiente

### **Log dell'App**
```bash
# I log mostrano:
✅ Configurazione database caricata da: Variabili d'ambiente
🔗 URL Database: jdbc:mysql://containers-us-west-xxx.railway.app:3306/railway
✅ Database inizializzato con successo
✅ Email di reset password inviata con successo
```

## 🎯 **Vantaggi Railway**

- ✅ **Database MySQL gestito** automaticamente
- ✅ **Scaling automatico** in base al traffico
- ✅ **Backup automatici** del database
- ✅ **SSL/TLS** incluso
- ✅ **Monitoring** integrato
- ✅ **Deploy automatico** da GitHub

## 🎉 **Risultato Finale**

Dopo la configurazione avrai:
- ✅ **Database MySQL** su Railway (gestito automaticamente)
- ✅ **App RMI** che si connette al database cloud
- ✅ **Sistema di email** funzionante con Gmail
- ✅ **Dominio** per reset password
- ✅ **Sicurezza** enterprise-grade
- ✅ **Scalabilità** automatica

## 📞 **Supporto**

Se hai problemi:
1. Controlla i log del server
2. Verifica le credenziali Railway
3. Testa la connessione database
4. Controlla configurazione email
5. Consulta la dashboard Railway

**Il sistema è ora pronto per la produzione su Railway!** 🚀
