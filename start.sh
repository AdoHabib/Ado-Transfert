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
if [ -z "$ADO_DB_HOST" ]; then
    echo "⚠️ ADO_DB_HOST non impostato, usando localhost"
    export ADO_DB_HOST="localhost"
fi

if [ -z "$ADO_DB_PASSWORD" ]; then
    echo "⚠️ ADO_DB_PASSWORD non impostato, usando default"
    export ADO_DB_PASSWORD="1234"
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

# Avvia il server
java -cp "bin:lib/*" -Djava.rmi.server.hostname=$RAILWAY_PUBLIC_DOMAIN Server
