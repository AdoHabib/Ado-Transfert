#!/bin/bash

# Script de test pour la configuration Railway
echo "🧪 Test de la configuration Railway Ado-Transfert"

# Test des variables d'environnement
echo "📋 Variables d'environnement requises :"
echo "   ADO_DB_HOST: ${ADO_DB_HOST:-'NON CONFIGURÉ'}"
echo "   ADO_DB_PORT: ${ADO_DB_PORT:-'NON CONFIGURÉ'}"
echo "   ADO_DB_NAME: ${ADO_DB_NAME:-'NON CONFIGURÉ'}"
echo "   ADO_DB_USER: ${ADO_DB_USER:-'NON CONFIGURÉ'}"
echo "   ADO_DB_PASSWORD: ${ADO_DB_PASSWORD:+'CONFIGURÉ'}"
echo "   RAILWAY_PUBLIC_DOMAIN: ${RAILWAY_PUBLIC_DOMAIN:-'NON CONFIGURÉ'}"

echo ""
echo "📧 Variables email optionnelles :"
echo "   ADO_EMAIL_USERNAME: ${ADO_EMAIL_USERNAME:-'NON CONFIGURÉ'}"
echo "   ADO_EMAIL_PASSWORD: ${ADO_EMAIL_PASSWORD:+'CONFIGURÉ'}"

echo ""
echo "🔧 Test de compilation :"
if javac -cp "lib/*" -d bin src/*.java; then
    echo "✅ Compilation réussie"
else
    echo "❌ Erreur de compilation"
    exit 1
fi

echo ""
echo "🚀 Test du serveur Railway :"
echo "   Pour tester localement avec variables Railway :"
echo "   export ADO_DB_PASSWORD='votre_password_railway'"
echo "   export RAILWAY_PUBLIC_DOMAIN='localhost'"
echo "   java -cp 'bin:lib/*' ServerRailway"
