# Ado-Transfert

Un sistema di gestione delle transazioni bancarie sviluppato in Java con interfaccia a riga di comando.

## 📋 Panoramica

Ado-Transfert è un'applicazione bancaria completa che permette agli utenti di gestire le proprie transazioni finanziarie, inclusi versamenti, prelievi e trasferimenti tra utenti. Il sistema include funzionalità di messaggistica, gestione profili e amministrazione utenti.

## ✨ Funzionalità Principali

### 👤 Gestione Utenti
- **Registrazione**: Creazione di nuovi account con validazione email e password
- **Autenticazione**: Sistema di login sicuro con hash delle password (SHA-256)
- **Profilo**: Gestione completa del profilo utente e indirizzo
- **Verifica**: Sistema di approvazione utenti da parte degli amministratori

### 💰 Transazioni Finanziarie
- **Versamenti**: Aggiunta di fondi al proprio conto
- **Prelievi**: Rimozione di fondi dal proprio conto
- **Trasferimenti**: Invio di denaro ad altri utenti tramite numero di telefono
- **Storico**: Visualizzazione completa delle transazioni effettuate

### 💬 Sistema di Messaggistica
- **Invio messaggi**: Comunicazione tra utenti
- **Ricezione**: Visualizzazione dei messaggi ricevuti
- **Notifiche**: Messaggi automatici del sistema per eventi importanti

### 🔧 Amministrazione
- **Gestione utenti**: Visualizzazione, modifica e eliminazione utenti
- **Approvazione**: Sistema di verifica per nuovi account
- **Controllo accessi**: Gestione dei permessi utente (Admin/Cliente)

### 📱 Funzionalità Aggiuntive
- **Generazione QR Code**: Creazione di codici QR per contenuti personalizzati
- **Validazione dati**: Controlli di sicurezza per email, password e numeri di telefono
- **Gestione indirizzi**: Sistema completo per la gestione degli indirizzi utente

## 🏗️ Architettura del Sistema

### Classi Principali

#### `App.java`
- Classe principale dell'applicazione
- Gestisce il menu principale e la logica di navigazione
- Contiene tutti i metodi per le operazioni bancarie e la gestione utenti

#### `Utente.java`
- Modello dati per gli utenti del sistema
- Include validazione email/password e gestione hash
- Gestisce saldo, indirizzo e messaggi associati

#### `Messaggio.java`
- Rappresenta i messaggi del sistema di comunicazione
- Include timestamp automatico e gestione mittente/destinatario

#### `Indirizzo.java`
- Modello per gli indirizzi degli utenti
- Supporta indirizzi completi con validazione

#### `QRCodeGenerator.java`
- Generatore di codici QR utilizzando la libreria ZXing
- Supporta personalizzazione dimensioni e contenuto

#### `AppGUI.java`
- Classe per l'interfaccia grafica (attualmente vuota)

## 🗄️ Database

Il sistema utilizza MySQL con le seguenti tabelle principali:

### Tabelle del Database
- **`utenti`**: Informazioni degli utenti (nome, cognome, email, password, saldo, tipo)
- **`messaggi`**: Sistema di messaggistica interno
- **`transazioni`**: Storico completo delle operazioni finanziarie
- **`indirizzi`**: Indirizzi associati agli utenti

### Configurazione Database
```java
URL: jdbc:mysql://localhost:3306/ado_transfert
Username: root
Password: 1234
```

## 🚀 Installazione e Configurazione

### Prerequisiti
- Java 8 o superiore
- MySQL Server
- IDE Java (Eclipse, IntelliJ IDEA, VS Code)

### Dipendenze
- MySQL Connector/J
- ZXing Core e ZXing Java SE (per la generazione QR Code)

### Setup
1. **Clona il repository**
   ```bash
   git clone [repository-url]
   cd Ado-Transfert
   ```

2. **Configura il database MySQL**
   - Crea un database chiamato `ado_transfert`
   - Aggiorna le credenziali in `App.java` se necessario

3. **Compila il progetto**
   ```bash
   javac -cp "lib/*" src/*.java -d bin
   ```

4. **Esegui l'applicazione**
   ```bash
   java -cp "bin:lib/*" App
   ```

## 📁 Struttura del Progetto

```
Ado-Transfert/
├── src/                    # Codice sorgente
│   ├── App.java           # Classe principale
│   ├── AppGUI.java        # Interfaccia grafica (da implementare)
│   ├── Utente.java        # Modello utente
│   ├── Messaggio.java     # Modello messaggio
│   ├── Indirizzo.java     # Modello indirizzo
│   └── QRCodeGenerator.java # Generatore QR Code
├── bin/                   # File compilati
├── lib/                   # Librerie esterne
└── README.md             # Documentazione
```

## 🔐 Sicurezza

- **Password**: Hash SHA-256 con codifica Base64
- **Validazione**: Controlli rigorosi su email, password e numeri di telefono
- **Transazioni**: Gestione transazionale per operazioni critiche
- **Accesso**: Sistema di verifica utenti per prevenire accessi non autorizzati

## 👥 Tipi di Utente

### Cliente
- Gestione del proprio profilo
- Operazioni bancarie (versamenti, prelievi, trasferimenti)
- Sistema di messaggistica
- Visualizzazione storico transazioni

### Amministratore
- Tutte le funzionalità del cliente
- Gestione e approvazione utenti
- Controllo completo del sistema
- Eliminazione utenti e messaggi

## 🎯 Account di Sistema

Il sistema include un account amministratore predefinito:
- **Email**: sistema@ado_transfert.com
- **Password**: 123Lover
- **Tipo**: Admin

## 🔧 Personalizzazione

### Modifica Credenziali Database
Aggiorna le costanti in `App.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/ado_transfert";
private static final String USER = "root";
private static final String PASSWORD = "1234";
```

### Configurazione QR Code
Modifica i parametri in `QRCodeGenerator.java`:
```java
int larghezza = 300;  // Larghezza del QR Code
int altezza = 300;    // Altezza del QR Code
```

## 🐛 Risoluzione Problemi

### Errori di Connessione Database
- **Errore**: `No suitable driver found for jdbc:mysql://localhost:3306/ado_transfert`
- **Soluzione**: Scarica e aggiungi il MySQL Connector/J alla cartella `lib/`
  1. Scarica `mysql-connector-java-8.0.33.jar` da [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/)
  2. Copia il file JAR nella cartella `lib/`
  3. Ricompila con: `javac -cp "lib/*" -d bin src/*.java`
  4. Esegui con: `java -cp "bin:lib/*" App`

### Problemi di Compilazione
- **Errore**: `NoClassDefFoundError: Messaggio`
- **Soluzione**: Compila tutte le classi insieme:
  ```bash
  javac -d bin src/App.java src/Utente.java src/Messaggio.java src/Indirizzo.java src/AppGUI.java
  ```

- **Errore**: `package com.google.zxing does not exist`
- **Soluzione**: Per utilizzare QRCodeGenerator, scarica le librerie ZXing:
  1. Scarica `core-3.5.1.jar` e `javase-3.5.1.jar` da [ZXing Releases](https://github.com/zxing/zxing/releases)
  2. Copia i file JAR nella cartella `lib/`
  3. Compila con: `javac -cp "lib/*" -d bin src/*.java`

### Comandi di Compilazione e Esecuzione

#### Senza Dipendenze Esterne (Solo funzionalità base)
```bash
# Compila le classi principali
javac -d bin src/App.java src/Utente.java src/Messaggio.java src/Indirizzo.java src/AppGUI.java

# Esegui l'applicazione
java -cp bin App
```

#### Con Tutte le Dipendenze
```bash
# Compila tutto (richiede MySQL Connector e ZXing in lib/)
javac -cp "lib/*" -d bin src/*.java

# Esegui con tutte le dipendenze
java -cp "bin:lib/*" App
```

### Verifica Prerequisiti
- Verifica che MySQL sia in esecuzione
- Controlla le credenziali di connessione in `App.java`
- Assicurati che il database `ado_transfert` esista
- Controlla la versione di Java (richiede Java 8+)

## 📝 Note di Sviluppo

- Il progetto è sviluppato in Java puro senza framework esterni
- L'interfaccia è attualmente a riga di comando
- `AppGUI.java` è preparato per l'implementazione di un'interfaccia grafica
- Il sistema è progettato per essere estensibile e modulare

## 🤝 Contributi

Per contribuire al progetto:
1. Fork del repository
2. Crea un branch per la tua feature
3. Implementa le modifiche
4. Testa accuratamente
5. Crea una pull request

## 📄 Licenza

Questo progetto è sviluppato per scopi educativi e di apprendimento.

---

🏦 Ado-Transfert                    [Stato Connessione]

┌─────────────────────────────────────────────────────────┐
│                                                         │
│              Accedi al tuo Account                      │
│                                                         │
│  Email:     [________________]                          │
│  Password:  [________________]                          │
│                                                         │
│  [🔵 Accedi]  [⚪ Registrati]  [🟡 Informazioni App]  │
│                                                         │
└─────────────────────────────────────────────────────────┘

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

**Sviluppato da**: Habib Ado  
**Versione**: 1.0  
**Ultimo aggiornamento**: 2024
