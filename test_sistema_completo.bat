@echo off
echo 🏦 Ado-Transfert - Sistema Completo a Tre Livelli
echo ================================================

echo.
echo 📋 Configurazione per il sistema completo
echo.

:: Variables Railway pour la base de données
set ADO_DB_HOST=trolley.proxy.rlwy.net
set ADO_DB_PORT=43111
set ADO_DB_NAME=railway
set ADO_DB_USER=root
set ADO_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW

:: Host RMI Railway per il deployment
set RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app

echo ✅ Configuration:
echo    Database Host: %ADO_DB_HOST%
echo    Database Port: %ADO_DB_PORT%
echo    Database Name: %ADO_DB_NAME%
echo    Database User: %ADO_DB_USER%
echo    RMI Server Host: %RAILWAY_PUBLIC_DOMAIN%

echo.
echo 🔨 Compilation de l'application...
javac -cp "lib\*" -d bin src\*.java
if %errorlevel% neq 0 (
    echo ❌ Erreur lors de la compilation
    pause
    exit /b 1
)
echo ✅ Compilation réussie

echo.
echo 🚀 Démarrage du serveur RMI en arrière-plan...
start cmd /k "java -cp "bin;lib\*" -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway"

echo.
echo ⏳ Attente du démarrage du serveur RMI (5 secondes)...
timeout /t 5 /nobreak > NUL

echo.
echo 🌐 Lancement de l'interface graphique...
java -cp "bin;lib\*" AppGUI

echo.
echo 🏁 Application lancée avec le système à trois niveaux:
echo    👤 Cliente - Envoi d'argent, consultation solde, historique
echo    🏦 Collaboratore - Gestion dépôts/retraits pour clients
echo    👑 Admin - Gestion utilisateurs, promotion Cliente → Collaboratore
echo.
pause
