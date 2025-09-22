@echo off
echo 🚀 Configuration Railway - Ado-Transfert
echo ========================================

echo.
echo 📋 Ce script configure les variables d'environnement pour Railway
echo.

echo 🔧 Variables d'environnement Railway requises:
echo.
echo 1. ADO_DB_HOST - Host de la base de données Railway
echo 2. ADO_DB_PORT - Port de la base de données Railway  
echo 3. ADO_DB_NAME - Nom de la base de données Railway
echo 4. ADO_DB_USER - Utilisateur de la base de données Railway
echo 5. ADO_DB_PASSWORD - Mot de passe de la base de données Railway
echo 6. RAILWAY_PUBLIC_DOMAIN - Domaine public Railway
echo 7. ADO_EMAIL_USERNAME - Email pour l'envoi de notifications
echo.

echo 📝 Saisissez les informations de votre projet Railway:
echo.

set /p RAILWAY_DB_HOST=mysql.railway.internal
set /p RAILWAY_DB_PORT=3306
set /p RAILWAY_DB_NAME=railway
set /p RAILWAY_DB_USER=root
set /p RAILWAY_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW
set /p RAILWAY_DOMAIN=ado-transfer.up.railway.app
set /p ADO_EMAIL_USERNAME=adobinesse@gmail.com
set /p ADO_EMAIL_PASSWORD=xxpc vsgl mnli ufbw

echo.
echo 🔧 Configuration des variables d'environnement...
set ADO_DB_HOST=%RAILWAY_DB_HOST%
set ADO_DB_PORT=%RAILWAY_DB_PORT%
set ADO_DB_NAME=%RAILWAY_DB_NAME%
set ADO_DB_USER=%RAILWAY_DB_USER%
set ADO_DB_PASSWORD=%RAILWAY_DB_PASSWORD%
set RAILWAY_PUBLIC_DOMAIN=%RAILWAY_DOMAIN%
set ADO_EMAIL_USERNAME=%ADO_EMAIL_USERNAME%
set ADO_EMAIL_PASSWORD=%ADO_EMAIL_PASSWORD%

echo.
echo ✅ Variables configurées:
echo    ADO_DB_HOST=%ADO_DB_HOST%
echo    ADO_DB_PORT=%ADO_DB_PORT%
echo    ADO_DB_NAME=%ADO_DB_NAME%
echo    ADO_DB_USER=%ADO_DB_USER%
echo    ADO_DB_PASSWORD=***masqué***
echo    RAILWAY_PUBLIC_DOMAIN=%RAILWAY_PUBLIC_DOMAIN%

echo.
echo 🚀 Démarrage du serveur avec la configuration Railway...
echo.

call start_server.bat

echo.
echo 🏁 Configuration terminée
pause
