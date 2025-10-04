#!/bin/bash

# Start script per Railway - Ado-Transfert
echo "🚀 Avvio Ado-Transfert su Railway..."

# Verifica che Java sia installato
if ! command -v java &> /dev/null; then
    echo "❌ Java non trovato!"
    exit 1
fi

# Mostra versione Java
echo "☕ Java version:"
java -version

# Compila l'applicazione se necessario
if [ ! -d "bin" ] || [ ! -f "bin/Server.class" ]; then
    echo "🔨 Compilazione dell'applicazione..."
    
    # Crea directory bin se non esiste
    mkdir -p bin
    
    # Compila le classi Java
    javac -cp "lib/*" -d bin src/*.java
    
    if [ $? -eq 0 ]; then
        echo "✅ Compilazione completata con successo"
    else
        echo "❌ Errore durante la compilazione"
        exit 1
    fi
fi

# Verifica configurazione database
echo "🔧 Verifica configurazione database..."

# Utilizza le variabili Railway se disponibili, altrimenti usa i default
if [ -z "$ADO_DB_HOST" ]; then
    if [ -n "$MYSQL_HOST" ]; then
        echo "✅ Usando MYSQL_HOST da Railway: $MYSQL_HOST"
        export ADO_DB_HOST="$MYSQL_HOST"
    else
        echo "⚠️ ADO_DB_HOST non impostato, usando localhost"
        export ADO_DB_HOST="localhost"
    fi
fi

if [ -z "$ADO_DB_PASSWORD" ]; then
    if [ -n "$MYSQL_PASSWORD" ]; then
        echo "✅ Usando MYSQL_PASSWORD da Railway"
        export ADO_DB_PASSWORD="$MYSQL_PASSWORD"
    else
        echo "⚠️ ADO_DB_PASSWORD non impostato, usando default"
        export ADO_DB_PASSWORD="1234"
    fi
fi

# Configurazione automatica Railway
if [ -n "$MYSQL_PORT" ]; then
    export ADO_DB_PORT="$MYSQL_PORT"
fi

if [ -n "$MYSQL_DATABASE" ]; then
    export ADO_DB_NAME="$MYSQL_DATABASE"
fi

if [ -n "$MYSQL_USER" ]; then
    export ADO_DB_USER="$MYSQL_USER"
fi

# Mostra configurazione (senza password)
echo "📊 Configurazione:"
echo "   Host: $ADO_DB_HOST"
echo "   Port: ${ADO_DB_PORT:-3306}"
echo "   Database: ${ADO_DB_NAME:-railway}"
echo "   User: ${ADO_DB_USER:-root}"

# Avvia il server RMI
echo "🚀 Avvio server RMI Ado-Transfert..."
echo "   Porta RMI: 1099"
echo "   Database: $ADO_DB_HOST:${ADO_DB_PORT:-3306}/${ADO_DB_NAME:-railway}"

# Avvia il server web in background
echo "🌐 Avvio server web..."
java -cp "bin:lib/*" WebServer &
WEB_PID=$!

# Avvia il server Railway RMI (senza interazione utente)
echo "🚀 Avvio server RMI..."
java -cp "bin:lib/*" -Djava.rmi.server.hostname=$RAILWAY_PUBLIC_DOMAIN ServerRailway &
RMI_PID=$!

# Mantieni entrambi i processi attivi
wait $WEB_PID $RMI_PID
