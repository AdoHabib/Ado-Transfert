# 🚀 File Batch per Sistema RMI Ado-Transfert

Questo documento descrive i file batch creati per automatizzare l'avvio e la gestione del sistema RMI Ado-Transfert.

## 📁 File Batch Disponibili

### 🔧 **compile.bat**
- **Scopo**: Compila tutte le classi Java del sistema
- **Uso**: Esegui questo file per compilare il progetto
- **Verifiche**: Controlla che Java sia installato e crea la directory `bin`

### 🖥️ **start_server.bat**
- **Scopo**: Avvia il server RMI
- **Uso**: Esegui questo file per avviare solo il server
- **Verifiche**: Controlla Java, classi compilate e MySQL
- **Interattivo**: Richiede la password del database MySQL

### 👤 **start_client.bat**
- **Scopo**: Avvia il client RMI (interfaccia a riga di comando)
- **Uso**: Esegui questo file per avviare solo il client

### 🖼️ **start_gui.bat**
- **Scopo**: Avvia l'interfaccia grafica moderna
- **Uso**: Esegui questo file per avviare la GUI del sistema
- **Verifiche**: Controlla Java, classi compilate e connessione server
- **Verifiche**: Controlla Java, classi compilate e server RMI
- **Prerequisito**: Il server deve essere già in esecuzione

### 🚀 **start_system.bat**
- **Scopo**: Avvia l'intero sistema (server + client)
- **Uso**: Esegui questo file per avviare tutto automaticamente
- **Funzionalità**: 
  - Compila se necessario
  - Avvia il server in background
  - Attende 5 secondi
  - Avvia il client

### 🛑 **stop_server.bat**
- **Scopo**: Ferma tutti i processi Java
- **Uso**: Esegui questo file per fermare il server
- **Funzionalità**: Termina forzatamente tutti i processi Java

## 🎯 **Sequenza di Avvio Consigliata**

### **Opzione 1: Avvio Manuale**
1. Esegui `compile.bat` (se non già fatto)
2. Esegui `start_server.bat`
3. Inserisci la password del database MySQL
4. Scegli l'interfaccia:
   - **GUI moderna**: `start_gui.bat` (consigliato)
   - **Terminale**: `start_client.bat`

### **Opzione 2: Avvio Automatico**
1. Esegui `start_system.bat`
2. Inserisci la password del database MySQL quando richiesto
3. Il sistema si avvierà automaticamente

## 🖼️ **Interfaccia Grafica**

### **Caratteristiche della GUI**
- **Design moderno** con tema blu professionale
- **Navigazione intuitiva** con menu laterali
- **Tre opzioni principali** nella schermata iniziale:
  1. **Accedi** - Login con email e password
  2. **Registrati** - Creazione nuovo account
  3. **Informazioni App** - Dettagli completi sul sistema

### **Dashboard Cliente**
- 💰 **Transazioni**: Versamenti, prelievi, trasferimenti
- 👤 **Profilo**: Gestione dati personali
- 📧 **Messaggi**: Sistema di messaggistica
- 🏠 **Indirizzo**: Gestione indirizzi
- 📊 **Storico**: Cronologia transazioni

### **Pannello Admin**
- 👥 **Gestione Utenti**: Approvazione, blocco, eliminazione
- 📧 **Messaggi**: Sistema messaggistica amministrativa

## 🔧 **Configurazione**

### **Prerequisiti**
- Java 8 o superiore installato
- MySQL Server in esecuzione
- Directory `bin` con le classi compilate

### **Password Database**
- Il server richiederà la password del database MySQL
- Premi INVIO per usare la password di default: `1234`
- La password non viene mostrata durante l'inserimento

## 🚨 **Risoluzione Problemi**

### **Errore: "Java non trovato"**
- Installa Java 8 o superiore
- Assicurati che Java sia nel PATH di sistema

### **Errore: "Classi non compilate"**
- Esegui `compile.bat` prima di avviare il sistema

### **Errore: "Port already in use"**
- Esegui `stop_server.bat` per fermare i processi esistenti
- Oppure riavvia il computer

### **Errore: "No suitable driver found"**
- Installa MySQL Connector/J nella directory `lib/`
- Scarica da: https://dev.mysql.com/downloads/connector/j/

## 📋 **Note Importanti**

- **Ordine di avvio**: Sempre server prima, poi client
- **Password**: La password del database è richiesta solo al server
- **Fermata**: Usa `stop_server.bat` per fermare tutto
- **Background**: Il server può rimanere in esecuzione in background

## 🎉 **Utilizzo Rapido**

Per iniziare subito:
1. Doppio click su `start_system.bat`
2. Inserisci la password MySQL (o premi INVIO per default)
3. Il sistema si avvierà automaticamente!

---
**Sviluppato da**: Habib Ado  
**Versione**: 1.0  
**Sistema**: RMI Server-Client per Ado-Transfert
