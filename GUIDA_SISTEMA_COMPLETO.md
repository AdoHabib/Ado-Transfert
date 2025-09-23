# 🏦 Guida Sistema Ado-Transfert - Tre Livelli Utenti

## 🎯 **Panoramica Sistema**

Il sistema Ado-Transfert ora supporta **tre tipi di utenti** con funzionalità specifiche:

### 👤 **CLIENTE**
- **Funzioni Principali**:
  - 💰 Inviare denaro ad altri clienti
  - 📊 Consultare saldo personale
  - 📈 Visualizzare storico transazioni
  - 👤 Gestire profilo personale
  - ✏️ Modificare informazioni profilo

- **Limitazioni**:
  - ❌ **NON può fare depositi/prelievi diretti**
  - ✅ **Deve recarsi presso un Collaboratore** per depositi/prelievi

### 🏦 **COLLABORATORE**
- **Funzioni Principali**:
  - 💳 **Gestire depositi per clienti**
  - 💸 **Gestire prelievi per clienti**
  - 📋 Visualizzare transazioni gestite
  - 👤 Gestire profilo personale

- **Responsabilità**:
  - ✅ Verifica identità del cliente
  - ✅ Registra operazioni deposito/prelievo
  - ✅ Aggiorna saldo del cliente

### 👑 **ADMIN**
- **Funzioni Principali**:
  - 👥 Gestione completa utenti
  - 🔄 **Promuovere Cliente → Collaboratore**
  - 📊 Supervisione sistema
  - 💬 Messaggi amministrativi
  - 🔒 Controllo accessi

- **Poteri Speciali**:
  - ✅ Approvazione registrazioni
  - ✅ **Modifica tipo utente** (cliente → collaboratore)
  - ✅ Blocco/sblocco utenti
  - ✅ Eliminazione utenti

## 🚀 **Come Utilizzare il Sistema**

### **1. Avvio del Sistema**
```bash
.\test_sistema_completo.bat
```

### **2. Registrazione Utenti**

#### **Registrazione Cliente** (Normale)
1. Aprire l'applicazione
2. Cliccare "Registrati"
3. Compilare il form con i propri dati
4. **Il cliente rimane in attesa di approvazione**

#### **Registrazione Collaboratore** (Solo Admin)
1. L'Admin deve prima creare l'utente come "cliente"
2. Poi promuoverlo a "collaboratore" tramite gestione utenti

### **3. Flusso Operativo**

#### **Per i Clienti**:
1. **Login** → Dashboard Cliente
2. **Consultare saldo** → Sezione "Profilo"
3. **Inviare denaro** → Sezione "Transazioni"
4. **Per depositi/prelievi** → Recarsi presso un Collaboratore

#### **Per i Collaboratori**:
1. **Login** → Dashboard Collaboratore
2. **Gestire deposito** → Sezione "Deposito"
   - Inserire ID cliente
   - Inserire importo
   - Processare operazione
3. **Gestire prelievo** → Sezione "Prelievo"
   - Inserire ID cliente
   - Inserire importo
   - Verificare saldo sufficiente
   - Processare operazione
4. **Visualizzare transazioni gestite** → Sezione "Transazioni Gestite"

#### **Per gli Admin**:
1. **Login** → Dashboard Admin
2. **Gestire utenti** → Sezione "Gestione Utenti"
3. **Promuovere utenti** → Modifica tipo da "cliente" a "collaboratore"
4. **Supervisionare sistema** → Tutte le funzioni disponibili

## 🔄 **Flusso Depositi/Prelievi**

### **Scenario Tipico**:
```
1. Cliente si reca presso Collaboratore
2. Cliente fornisce ID e importo
3. Collaboratore verifica identità
4. Collaboratore inserisce dati nel sistema
5. Sistema aggiorna saldo cliente
6. Transazione registrata
```

## 📋 **Nuove Funzionalità**

### **Metodi Collaboratore**:
- `gestisciDeposito(collaboratoreID, clienteID, importo)`
- `gestisciPrelievo(collaboratoreID, clienteID, importo)`
- `visualizzaTransazioniGestite(collaboratoreID)`

### **Dashboard Specifici**:
- **Cliente**: Transazioni, Profilo, Messaggi, Indirizzo, Storico
- **Collaboratore**: Deposito, Prelievo, Transazioni Gestite, Profilo
- **Admin**: Gestione Utenti, Messaggi Admin

## 🎯 **Vantaggi del Nuovo Sistema**

1. **Sicurezza**: Depositi/prelievi solo tramite collaboratori autorizzati
2. **Controllo**: Admin può gestire promozioni e accessi
3. **Tracciabilità**: Tutte le operazioni sono registrate
4. **Flessibilità**: Sistema modulare per diversi tipi di utenti
5. **Scalabilità**: Facile aggiunta di nuovi tipi di utenti

## 🚨 **Note Importanti**

- **Solo gli Admin possono promuovere** clienti a collaboratori
- **I collaboratori devono essere verificati** prima di gestire operazioni
- **Tutte le operazioni sono tracciate** nel database
- **Il sistema mantiene la compatibilità** con le funzionalità esistenti

## 🎉 **Sistema Pronto!**

Il sistema Ado-Transfert è ora completamente funzionale con il supporto per tre livelli di utenti, offrendo un controllo completo e sicuro delle operazioni finanziarie.
