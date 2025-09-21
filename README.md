# 🏦 Ado-Transfert

Un sistema completo di gestione transazioni finanziarie sviluppato in Java con tecnologia RMI e interfaccia grafica moderna.

## 📋 Panoramica

Ado-Transfert è un'applicazione bancaria completa che permette agli utenti di gestire le proprie transazioni finanziarie, inclusi versamenti, prelievi e trasferimenti tra utenti. Il sistema include funzionalità di messaggistica, gestione profili e amministrazione utenti.

**🌐 Versione Web**: Disponibile su Railway - [ado-transfer.up.railway.app](https://ado-transfer.up.railway.app)

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
- **Interfaccia Web**: Accesso via browser su Railway
- **Generazione QR Code**: Creazione di codici QR per contenuti personalizzati
- **Validazione dati**: Controlli di sicurezza per email, password e numeri di telefono
- **Gestione indirizzi**: Sistema completo per la gestione degli indirizzi utente

## 🏗️ Architettura del Sistema

### Tecnologie Utilizzate
- **Java 11**: Linguaggio di programmazione principale
- **Java RMI**: Comunicazione client-server distribuita
- **Java Swing**: Interfaccia grafica desktop
- **MySQL**: Database per persistenza dati
- **Railway**: Piattaforma di hosting cloud
- **Docker**: Containerizzazione dell'applicazione

### Classi Principali

#### `ServerRailway.java`
- Server principale per il deployment su Railway
- Gestisce le connessioni RMI senza interazione utente
- Utilizza variabili d'ambiente per la configurazione

#### `WebServer.java`
- Serveur web HTTP per Railway
- Serve le pagine web e l'API di statut
- Gestisce l'interfaccia web de l'application

#### `App.java`
- Logique métier principale de l'application
- Gère les opérations bancaires et la gestion des utilisateurs
- Interface avec la base de données MySQL

#### `AppGUI.java`
- Interface graphique desktop avec Java Swing
- Interface utilisateur moderne et intuitive
- Gestion des événements et navigation

#### `InterfaceImpl.java`
- Implémentation des méthodes RMI
- Pont entre l'interface et la logique métier
- Gestion des appels distants

## 🚀 Installation et Démarrage

### Prérequis
- Java 11 ou supérieur
- MySQL (pour le développement local)
- Git

### Démarrage Local

#### 1. Cloner le projet
```bash
git clone <repository-url>
cd Ado-Transfert
```

#### 2. Configuration de la base de données
- Installer MySQL
- Créer une base de données nommée `ado_transfert`
- Configurer les variables d'environnement ou modifier la configuration dans `App.java`

#### 3. Compilation
```bash
# Windows
compile.bat

# Linux/Mac
javac -cp "lib/*" -d bin src/*.java
```

#### 4. Démarrage des serveurs

**Serveur RMI (Terminal 1):**
```bash
# Windows
start_server.bat

# Linux/Mac
java -cp "bin:lib/*" -Djava.rmi.server.hostname=localhost Server
```

**Interface Graphique (Terminal 2):**
```bash
# Windows
start_gui.bat

# Linux/Mac
java -cp "bin:lib/*" AppGUI
```

### Démarrage avec Interface Graphique (Recommandé)

**Windows:**
```bash
start_gui_complete.bat
```

**Linux/Mac:**
```bash
./start_gui_complete.sh
```

## 🌐 Déploiement Railway

### Configuration Automatique
Le projet est configuré pour se déployer automatiquement sur Railway :

1. **Variables d'environnement requises :**
   - `ADO_DB_PASSWORD` : Mot de passe MySQL Railway

2. **Variables optionnelles :**
   - `ADO_EMAIL_USERNAME` : Email pour l'envoi de notifications
   - `ADO_EMAIL_PASSWORD` : Mot de passe d'application email

### Accès Web
- **URL principale** : [ado-transfer.up.railway.app](https://ado-transfer.up.railway.app)
- **Interface de connexion** : `/login.html`
- **API de statut** : `/status`

## 📁 Structure du Projet

```
Ado-Transfert/
├── Dockerfile              # Configuration Railway
├── start.sh               # Script de démarrage Railway
├── Procfile               # Configuration Railway
├── railway.json           # Configuration Railway
├── src/                   # Code source
│   ├── App.java           # Logique métier
│   ├── AppGUI.java        # Interface graphique
│   ├── ServerRailway.java # Serveur Railway
│   ├── WebServer.java     # Serveur web
│   ├── InterfaceImpl.java # Implémentation RMI
│   └── ...                # Autres classes
├── lib/                   # Bibliothèques Java
├── web/                   # Interface web
└── bin/                   # Classes compilées
```

## 🔧 Configuration

### Variables d'Environnement (Railway)
- `ADO_DB_HOST` : Host de la base de données (automatique)
- `ADO_DB_PORT` : Port de la base de données (automatique)
- `ADO_DB_NAME` : Nom de la base de données (automatique)
- `ADO_DB_USER` : Utilisateur de la base de données (automatique)
- `ADO_DB_PASSWORD` : **Obligatoire** - Mot de passe MySQL Railway
- `RAILWAY_PUBLIC_DOMAIN` : Domaine public Railway (automatique)
- `PORT` : Port du serveur web (automatique)

### Configuration Email (Optionnelle)
Pour activer l'envoi d'emails :
- `ADO_EMAIL_USERNAME` : Adresse email Gmail
- `ADO_EMAIL_PASSWORD` : Mot de passe d'application Gmail

## 👥 Types d'Utilisateur

### 🔹 Cliente
- Effettua transazioni finanziarie
- Gestisce il proprio profilo
- Invia e riceve messaggi
- Visualizza lo storico delle transazioni

### 🔹 Amministratore
- Gestisce tutti gli utenti del sistema
- Approva le registrazioni
- Supervisiona le transazioni
- Ha accesso completo alle funzionalità

## 📞 Support

### Informazioni di Contatto
- **Sviluppatore**: Habib Ado
- **Email**: sistema@ado_transfert.com
- **Versione**: 1.0.0
- **Piattaforma**: Railway Cloud

### Assistenza
- Per assistenza tecnica, utilizza il sistema di messaggistica integrato
- Segnala problemi tramite l'interfaccia web
- Consulta la documentazione Railway per il deployment

## 📄 Licenza

© 2024 Ado-Transfert Team. Tutti i diritti riservati.

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
                       
**🚀 Déployé avec succès sur Railway** - [ado-transfer.up.railway.app](https://ado-transfer.up.railway.app)