# 🌐 Guida Completa per Ottenere un Dominio - Ado-Transfert

## 🚀 **Soluzioni Rapide (Consigliate)**

### **1. 🆓 GitHub Pages (GRATUITO - Migliore Opzione)**

#### **Vantaggi:**
- ✅ Completamente gratuito
- ✅ Integrazione con GitHub
- ✅ HTTPS automatico
- ✅ Facile da configurare
- ✅ Aggiornamenti automatici

#### **Come Configurare:**
```bash
1. Vai su https://github.com
2. Crea repository pubblico: "ado-transfert"
3. Carica i file della cartella 'web/'
4. Settings > Pages > Deploy from branch
5. Scegli "main" branch, "/ (root)" folder
6. Il tuo sito sarà su: https://tuousername.github.io/ado-transfert
```

#### **Aggiorna App.java:**
```java
String resetLink = "https://tuousername.github.io/ado-transfert/reset-password.html?token=" + token + "&user=" + userID;
```

---

### **2. 🆓 Netlify (GRATUITO - Alternativa)**

#### **Vantaggi:**
- ✅ Deploy automatico da GitHub
- ✅ Nome personalizzato
- ✅ HTTPS automatico
- ✅ CDN globale

#### **Come Configurare:**
```bash
1. Vai su https://netlify.com
2. Registrati con GitHub
3. "New site from Git" > Seleziona repository
4. Build command: (vuoto)
5. Publish directory: web
6. Deploy
```

---

### **3. 🆓 Vercel (GRATUITO - Per Sviluppatori)**

#### **Vantaggi:**
- ✅ Ottimizzato per sviluppatori
- ✅ Deploy automatico
- ✅ Performance eccellenti

#### **Come Configurare:**
```bash
1. Vai su https://vercel.com
2. Import Project > Seleziona repository
3. Framework: Other
4. Root Directory: web
5. Deploy
```

---

## 💰 **Domini a Basso Costo**

### **Provider Consigliati:**

| Provider | Prezzo/Anno | Estensione | Note |
|----------|-------------|------------|------|
| **Namecheap** | €8.88 | .com | Migliore rapporto qualità/prezzo |
| **OVH** | €7.99 | .com | Economico e affidabile |
| **Aruba** | €9.99 | .it | Italiano, supporto locale |
| **GoDaddy** | €12.99 | .com | Più costoso ma popolare |

### **Estensioni Economiche:**
- **.xyz**: €1.99/anno
- **.online**: €2.99/anno
- **.site**: €2.99/anno

---

## 🛠️ **Implementazione Passo-Passo**

### **Opzione 1: GitHub Pages (Consigliata)**

#### **Step 1: Prepara i File**
```bash
# I file web sono già creati in:
web/reset-password.html
web/login.html
```

#### **Step 2: Crea Repository GitHub**
1. Vai su [GitHub](https://github.com)
2. Clicca "New repository"
3. Nome: `ado-transfert`
4. Pubblico ✅
5. Crea repository

#### **Step 3: Carica i File**
1. Clicca "uploading an existing file"
2. Trascina la cartella `web/`
3. Commit message: "Add web pages for password reset"
4. Commit

#### **Step 4: Abilita GitHub Pages**
1. Vai su **Settings** del repository
2. Scrolla fino a **Pages**
3. Source: **Deploy from a branch**
4. Branch: **main**
5. Folder: **/ (root)**
6. Save

#### **Step 5: Aggiorna il Codice**
```java
// In src/App.java, linea 1418:
String resetLink = "https://TUOUSername.github.io/ado-transfert/reset-password.html?token=" + token + "&user=" + userID;
```

### **Opzione 2: Test Locale (Sviluppo)**

#### **Per Testare Senza Dominio:**
```bash
# 1. Trova il tuo IP
ipconfig | findstr IPv4

# 2. Avvia server web nella cartella web/
cd web
python -m http.server 8080

# 3. Aggiorna App.java temporaneamente:
String resetLink = "http://TUOIP:8080/reset-password.html?token=" + token + "&user=" + userID;
```

---

## 🔧 **Configurazione Avanzata**

### **Dominio Personalizzato con GitHub Pages**

#### **1. Acquista Dominio**
- Compra da Namecheap, GoDaddy, ecc.
- Esempio: `adotransfert.com`

#### **2. Configura DNS**
```
Tipo: CNAME
Nome: www
Valore: tuousername.github.io

Tipo: A
Nome: @
Valore: 185.199.108.153
```

#### **3. Configura GitHub Pages**
- Settings > Pages > Custom domain
- Inserisci: `www.adotransfert.com`
- Abilita HTTPS

#### **4. Aggiorna App.java**
```java
String resetLink = "https://www.adotransfert.com/reset-password.html?token=" + token + "&user=" + userID;
```

---

## 🚨 **Note Importanti**

### **Sicurezza:**
- ✅ Usa sempre HTTPS in produzione
- ✅ Valida i token lato server
- ✅ Implementa rate limiting
- ✅ Logga tutti i tentativi di reset

### **Backup:**
- ✅ Mantieni backup del codice
- ✅ Documenta le configurazioni
- ✅ Testa prima di andare in produzione

### **Performance:**
- ✅ Usa CDN per file statici
- ✅ Comprimi le immagini
- ✅ Minimizza CSS/JS

---

## 📞 **Supporto**

### **Problemi Comuni:**

#### **"Sito non raggiungibile"**
- Verifica che il repository sia pubblico
- Controlla le impostazioni GitHub Pages
- Aspetta 5-10 minuti per la propagazione

#### **"Email non arriva"**
- Verifica configurazione Gmail
- Controlla cartella spam
- Testa con email diverse

#### **"Link non funziona"**
- Verifica URL nel codice
- Controlla parametri URL
- Testa manualmente il link

### **Risorse Utili:**
- [GitHub Pages Docs](https://docs.github.com/en/pages)
- [Netlify Docs](https://docs.netlify.com)
- [Vercel Docs](https://vercel.com/docs)

---

## 🎯 **Raccomandazione Finale**

**Per iniziare subito:** Usa **GitHub Pages** (gratuito e facile)

**Per produzione:** Acquista un dominio personalizzato (€8-15/anno)

**Per sviluppo:** Usa IP locale con server web

Il sistema è già pronto con tutti i file necessari! 🚀
