# 🏦 Architettura Sistema Ado-Transfert - Tre Livelli Utenti

## 👥 **Tipi di Utenti**

### 🔹 **Cliente**
- **Funzioni**:
  - 💰 Inviare denaro ad altri clienti
  - 📊 Consultare saldo
  - 📈 Visualizzare storico transazioni
  - 👤 Gestire profilo personale
  - ✏️ Modificare informazioni profilo
- **Limitazioni**:
  - ❌ Non può fare depositi/prelievi diretti
  - ❌ Deve recarsi presso un Collaboratore per depositi/prelievi

### 🔹 **Collaboratore**
- **Funzioni**:
  - 💳 Gestire depositi per clienti
  - 💸 Gestire prelievi per clienti
  - 📋 Visualizzare transazioni gestite
  - 👤 Gestire profilo personale
- **Responsabilità**:
  - ✅ Verifica identità cliente
  - ✅ Registra operazioni deposito/prelievo
  - ✅ Aggiorna saldo cliente

### 🔹 **Admin**
- **Funzioni**:
  - 👥 Gestione completa utenti
  - 🔄 Promuovere Cliente → Collaboratore
  - 📊 Supervisione sistema
  - 💬 Messaggi amministrativi
  - 🔒 Controllo accessi
- **Poteri**:
  - ✅ Approvazione registrazioni
  - ✅ Modifica tipo utente
  - ✅ Blocco/sblocco utenti
  - ✅ Eliminazione utenti

## 🔄 **Flusso Operazioni**

### **Deposito/Prelievo**
```
Cliente → Collaboratore → Sistema → Database
```

### **Trasferimento**
```
Cliente → Sistema → Database → Cliente Destinatario
```

### **Gestione Utenti**
```
Admin → Sistema → Database → Utente
```

## 📋 **Modifiche Necessarie**

### **1. Database**
- Aggiungere campo `tipoUtente` con valori: `cliente`, `collaboratore`, `admin`
- Aggiornare tabelle esistenti

### **2. Interfaccia**
- Dashboard specifici per ogni tipo utente
- Menu differenziati
- Controlli di accesso

### **3. Logica Business**
- Validazione operazioni per tipo utente
- Controlli di autorizzazione
- Flussi operativi specifici

## 🎯 **Implementazione**

### **Fase 1**: Aggiornamento Database
### **Fase 2**: Modifica Logica Business
### **Fase 3**: Aggiornamento Interfaccia
### **Fase 4**: Test e Validazione
