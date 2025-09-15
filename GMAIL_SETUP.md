# 📧 Configurazione Gmail per Ado-Transfert

## 🔐 Setup Gmail App Password

Per utilizzare l'invio email con Gmail, devi configurare una **App Password** (Password dell'app).

### 📋 Passaggi per configurare Gmail:

#### 1. **Attiva la Verifica in Due Passaggi**
- Vai su [Google Account Security](https://myaccount.google.com/security)
- Nella sezione "Accedi a Google", clicca su **"Verifica in due passaggi"**
- Segui le istruzioni per attivarla (se non è già attiva)

#### 2. **Genera App Password**
- Sempre nella pagina [Google Account Security](https://myaccount.google.com/security)
- Nella sezione "Accedi a Google", clicca su **"Password delle app"**
- Seleziona **"Mail"** come app
- Scegli **"Windows Computer"** come dispositivo
- Clicca **"Genera"**
- **COPIA** la password generata (16 caratteri senza spazi)

#### 3. **Configura il Sistema**
- Apri il file `email_config.properties`
- Sostituisci `your_app_password_here` con la password generata:
  ```properties
  email.password=abcd efgh ijkl mnop
  ```
- **IMPORTANTE**: Rimuovi gli spazi dalla password!

#### 4. **Esempio di Configurazione Corretta**
```properties
# Configurazione Email per Ado-Transfert
email.host=smtp.gmail.com
email.port=587
email.username=adobinesse@gmail.com
email.password=abcdefghijklmnop
email.from=adobinesse@gmail.com
email.from.name=Ado-Transfert Sistema
```

## 🚨 Note di Sicurezza

### ⚠️ **IMPORTANTE**:
- **NON** usare la password normale del tuo account Gmail
- **NON** committare mai il file `email_config.properties` con password reali
- **SEMPRE** usa App Password per applicazioni
- **MANTIENI** segreta la tua App Password

### 🔒 **Per Produzione**:
- Usa variabili d'ambiente per le password
- Implementa un sistema di configurazione sicuro
- Considera l'uso di servizi email dedicati (SendGrid, AWS SES, ecc.)

## 🧪 Test dell'Invio Email

### 1. **Avvia il Server**
```bash
.\start_server.bat
```

### 2. **Testa il Recupero Password**
- Usa l'interfaccia grafica o il client
- Richiedi il reset password per un utente esistente
- Controlla la console del server per i log di invio

### 3. **Verifica Email**
- Controlla la casella email del destinatario
- Verifica che l'email sia arrivata nella cartella "Posta in arrivo" o "Spam"

## 📊 Log di Debug

Il sistema genera log dettagliati per il debug:

```
✅ Email di reset password inviata con successo a: user@example.com
❌ Errore invio email: Authentication failed
🔐 Errore autenticazione Gmail. Verifica App Password.
📧 Errore invio email. Verifica indirizzo destinatario.
```

## 🆘 Risoluzione Problemi

### **Errore: Authentication Failed**
- Verifica che l'App Password sia corretta
- Assicurati che la verifica in due passaggi sia attiva
- Controlla che non ci siano spazi nella password

### **Errore: Connection Timeout**
- Verifica la connessione internet
- Controlla che la porta 587 non sia bloccata dal firewall
- Prova a cambiare porta (465 per SSL)

### **Email non Arriva**
- Controlla la cartella spam
- Verifica l'indirizzo email destinatario
- Controlla i log del server per errori

## 📞 Supporto

Per problemi con la configurazione email:
- Controlla i log del server
- Verifica la configurazione Gmail
- Consulta la documentazione Gmail per App Passwords
