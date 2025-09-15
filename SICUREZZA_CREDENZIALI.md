# 🔐 Guida Sicurezza Credenziali - Ado-Transfert

## ⚠️ **IMPORTANTE: Mai Inserire Password nel Codice!**

Ho implementato un sistema sicuro per gestire le credenziali senza esporre password nel codice sorgente.

## 🛡️ **Metodi Sicuri Implementati**

### **1. 🏆 Variabili d'Ambiente (CONSIGLIATO)**

#### **Vantaggi:**
- ✅ **Password mai nel codice**
- ✅ **Sicurezza massima**
- ✅ **Facile da gestire**
- ✅ **Standard di sicurezza enterprise**

#### **Come Configurare:**

**Opzione A: Script Automatico**
```bash
.\setup_email_env.bat
# Segui le istruzioni per inserire le credenziali
```

**Opzione B: Manuale**
```bash
# Imposta variabili d'ambiente Windows
setx ADO_EMAIL_USERNAME "la_tua_email@gmail.com"
setx ADO_EMAIL_PASSWORD "la_tua_app_password"
setx ADO_EMAIL_HOST "smtp.gmail.com"
setx ADO_EMAIL_PORT "587"
setx ADO_EMAIL_FROM "la_tua_email@gmail.com"
setx ADO_EMAIL_FROM_NAME "Ado-Transfert Sistema"
```

### **2. 🔒 Script di Avvio Sicuro**

#### **Per Sessioni Temporanee:**
```bash
.\start_server_secure.bat
# Inserisci credenziali al momento dell'avvio
# Le credenziali sono rimosse quando chiudi il server
```

### **3. 📁 File di Configurazione (Fallback)**

#### **File Pubblico (Sicuro):**
```properties
# email_config.properties (può essere committato)
email.host=smtp.gmail.com
email.port=587
email.from.name=Ado-Transfert Sistema
# ⚠️ NON inserire username e password qui!
```

#### **File Privato (Non Committato):**
```properties
# email_secrets.properties (aggiunto a .gitignore)
email.username=la_tua_email@gmail.com
email.password=la_tua_app_password
```

## 🚀 **Come Usare il Sistema Sicuro**

### **Metodo 1: Variabili d'Ambiente (Raccomandato)**

#### **Step 1: Configura le Variabili**
```bash
.\setup_email_env.bat
```

#### **Step 2: Riavvia il Prompt**
```bash
# Chiudi e riapri il prompt dei comandi
```

#### **Step 3: Avvia il Server**
```bash
.\start_server.bat
# Le credenziali saranno caricate automaticamente
```

### **Metodo 2: Avvio Sicuro (Per Test)**

```bash
.\start_server_secure.bat
# Inserisci credenziali quando richiesto
```

### **Metodo 3: File di Configurazione**

#### **Crea file privato:**
```bash
# Crea email_secrets.properties con le tue credenziali
# Il file è automaticamente ignorato da Git
```

## 🔧 **Configurazione Gmail Sicura**

### **1. Genera App Password**
1. Vai su [Google Account Security](https://myaccount.google.com/security)
2. Attiva "Verifica in due passaggi"
3. Genera "Password delle app" per Mail
4. **USA SOLO QUELLA PASSWORD** (non la password normale!)

### **2. Testa la Configurazione**
```bash
# Avvia il server
.\start_server_secure.bat

# Controlla i log per:
# ✅ Configurazione email caricata da: Variabili d'ambiente
# ✅ Email di reset password inviata con successo
```

## 🛡️ **Protezioni Implementate**

### **1. File .gitignore**
```bash
# Protegge automaticamente:
email_config.properties
email_secrets.properties
*.env
*password*
*secret*
*credential*
```

### **2. Validazione Configurazione**
```java
// Il sistema controlla automaticamente:
if (EMAIL_USERNAME == null || EMAIL_PASSWORD == null) {
    System.err.println("⚠️ ATTENZIONE: Configurazione email non trovata!");
    // Mostra istruzioni per configurazione sicura
}
```

### **3. Logging Sicuro**
```java
// Le password non vengono mai loggate:
System.out.println("✅ Configurazione email caricata da: " + 
    (System.getenv("ADO_EMAIL_USERNAME") != null ? "Variabili d'ambiente" : "File di configurazione"));
// ✅ Mostra solo la fonte, non le credenziali
```

## 📊 **Priorità di Caricamento**

Il sistema carica le credenziali in questo ordine:

1. **🥇 Variabili d'ambiente** (più sicure)
2. **🥈 File email_secrets.properties** (privato)
3. **🥉 File email_config.properties** (pubblico)
4. **❌ Valori di default** (solo per test)

## 🚨 **Best Practices di Sicurezza**

### **✅ Cosa Fare:**
- ✅ Usa sempre variabili d'ambiente in produzione
- ✅ Usa App Password di Gmail (non password normale)
- ✅ Testa sempre la configurazione
- ✅ Mantieni aggiornato il .gitignore
- ✅ Usa script di avvio sicuri per test

### **❌ Cosa NON Fare:**
- ❌ Mai inserire password nel codice sorgente
- ❌ Mai committare file con credenziali
- ❌ Mai usare password normale di Gmail
- ❌ Mai condividere App Password
- ❌ Mai loggare password o token

## 🔍 **Verifica Sicurezza**

### **Controlla che non ci siano credenziali nel codice:**
```bash
# Cerca password nel codice (dovrebbe essere vuoto)
grep -r "password" src/ --exclude="*.md"
grep -r "@gmail.com" src/
grep -r "adobinesse" src/
```

### **Controlla che il .gitignore funzioni:**
```bash
# Verifica che i file sensibili siano ignorati
git status
# Non dovrebbero apparire file con credenziali
```

## 🆘 **Risoluzione Problemi**

### **"Configurazione email non trovata"**
```bash
# Soluzione: Imposta variabili d'ambiente
.\setup_email_env.bat
# Riavvia il prompt e riprova
```

### **"Authentication failed"**
```bash
# Soluzione: Verifica App Password
# 1. Usa App Password (non password normale)
# 2. Verifica che la verifica in due passaggi sia attiva
# 3. Controlla che non ci siano spazi nella password
```

### **"File non trovato"**
```bash
# Soluzione: Crea file di configurazione
# email_config.properties per configurazione base
# email_secrets.properties per credenziali (opzionale)
```

## 🎯 **Raccomandazione Finale**

**Per Sviluppo:** Usa `start_server_secure.bat`

**Per Produzione:** Usa variabili d'ambiente con `setup_email_env.bat`

**Per Team:** Usa file di configurazione separati per ogni ambiente

Il sistema è ora **completamente sicuro** e le tue credenziali non sono mai esposte nel codice! 🛡️
