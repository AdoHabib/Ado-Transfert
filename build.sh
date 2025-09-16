#!/bin/bash

# Build script per Railway - Ado-Transfert
echo "🔨 Build Ado-Transfert per Railway..."

# Verifica che Java sia installato
if ! command -v java &> /dev/null; then
    echo "❌ Java non trovato! Installando OpenJDK 11..."
    apt-get update
    apt-get install -y openjdk-11-jdk
fi

# Mostra versione Java
echo "☕ Java version:"
java -version

# Crea directory bin se non esiste
mkdir -p bin

# Compila le classi Java
echo "🔨 Compilazione delle classi Java..."
javac -cp "lib/*" -d bin src/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilazione completata con successo"
else
    echo "❌ Errore durante la compilazione"
    exit 1
fi

# Verifica che le classi siano state create
if [ -f "bin/Server.class" ]; then
    echo "✅ Server.class creato con successo"
else
    echo "❌ Server.class non trovato"
    exit 1
fi

echo "🎉 Build completato con successo!"
