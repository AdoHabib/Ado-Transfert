# 🌐 Guida Dominio per Ado-Transfert

## 🚀 **Soluzione Completa Implementata**

Ho creato una soluzione completa per il dominio del tuo sistema Ado-Transfert. Ecco tutto quello che è stato preparato:

## 📁 **File Creati**

### **1. Pagine Web**
- `web/reset-password.html` - Pagina per reset password
- `web/login.html` - Pagina di login web

### **2. Script di Setup**
- `setup_domain.bat` - Menu interattivo per configurazione dominio
- `update_domain.bat` - Aggiorna automaticamente il link nel codice
- `download_javamail.bat` - Download librerie email

### **3. Documentazione**
- `DOMAIN_SETUP.md` - Guida completa per ottenere dominio
- `GMAIL_SETUP.md` - Setup Gmail per invio email
- `README_DOMINIO.md` - Questo file di riepilogo

## 🎯 **Raccomandazione: GitHub Pages (GRATUITO)**

### **Perché GitHub Pages?**
- ✅ **Completamente gratuito**
- ✅ **HTTPS automatico**
- ✅ **Facile da configurare**
- ✅ **Aggiornamenti automatici**
- ✅ **Nessuna configurazione server**

### **Come Configurare (5 minuti):**

#### **Step 1: Crea Repository GitHub**
1. Vai su [GitHub](https://github.com)
2. Clicca "New repository"
3. Nome: `ado-transfert`
4. ✅ Pubblico
5. ✅ Add README
6. Crea repository

#### **Step 2: Carica i File Web**
1. Nel repository, clicca "uploading an existing file"
2. Trascina la cartella `web/` completa
3. Commit: "Add web pages for password reset"
4. Commit changes

#### **Step 3: Abilita GitHub Pages**
1. Settings > Pages
2. Source: "Deploy from a branch"
3. Branch: "main"
4. Folder: "/ (root)"
5. Save

#### **Step 4: Aggiorna il Codice**
```bash
# Usa lo script automatico:
.\update_domain.bat

# Oppure modifica manualmente App.java:
String resetLink = "https://TUOUSername.github.io/ado-transfert/reset-password.html?token=" + token + "&user=" + userID;
```

## 🔧 **Configurazione Gmail**

### **Setup App Password:**
1. Vai su [Google Account Security](https://myaccount.google.com/security)
2. Attiva "Verifica in due passaggi"
3. Genera "Password delle app" per Mail
4. Aggiorna `email_config.properties`:
   ```properties
   email.password=la_tua_app_password_qui
   ```

## 🚀 **Avvio del Sistema**

### **1. Compila con librerie email:**
```bash
.\compile.bat
```

### **2. Avvia server:**
```bash
.\start_server.bat
```

### **3. Testa il sistema:**
```bash
.\start_gui.bat
```

## 📊 **Flusso Completo**

### **1. Utente Richiede Reset Password**
- Inserisce email e userID
- Sistema valida input e rate limiting

### **2. Sistema Genera Token Sicuro**
- Token SHA-256 + timestamp
- Scadenza 2 ore
- Salvataggio nel database

### **3. Invio Email Gmail**
- Email HTML professionale
- Link al tuo dominio GitHub Pages
- Logging completo degli eventi

### **4. Utente Clicca Link**
- Vai alla pagina `reset-password.html`
- Inserisce nuova password
- Conferma password

### **5. Reset Completato**
- Password aggiornata nel database
- Token invalidato
- Utente può accedere con nuova password

## 🛡️ **Sicurezza Implementata**

- ✅ **Rate limiting**: Max 3 tentativi/ora, 5/giorno
- ✅ **Token sicuri**: SHA-256 + timestamp
- ✅ **Validazione input**: Email e userID
- ✅ **Scadenza token**: 2 ore
- ✅ **Logging completo**: Tutti gli eventi di sicurezza
- ✅ **HTTPS**: GitHub Pages con SSL automatico

## 💡 **Alternative Consigliate**

### **Se vuoi un dominio personalizzato:**
- **Namecheap**: €8.88/anno (.com)
- **Aruba**: €9.99/anno (.it)
- **OVH**: €7.99/anno (.com)

### **Per hosting completo:**
- **Netlify**: Gratuito con deploy automatico
- **Vercel**: Gratuito per sviluppatori
- **Heroku**: Gratuito con limitazioni

## 🎉 **Risultato Finale**

Dopo la configurazione avrai:
- ✅ **Sistema di recupero password** completamente funzionante
- ✅ **Email professionali** con design moderno
- ✅ **Pagina web** per reset password
- ✅ **Sicurezza enterprise** con rate limiting
- ✅ **Logging completo** per monitoraggio
- ✅ **HTTPS automatico** con GitHub Pages

## 📞 **Supporto**

Se hai problemi:
1. Controlla i log del server
2. Verifica configurazione Gmail
3. Testa il link manualmente
4. Consulta `DOMAIN_SETUP.md` per dettagli

**Il sistema è pronto per la produzione!** 🚀
