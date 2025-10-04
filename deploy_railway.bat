@echo off
echo 🚀 Ado-Transfert - Deployment Railway
echo ====================================

echo.
echo 📋 Configuration Railway per deployment
echo.

:: Variables Railway pour la base de données
set ADO_DB_HOST=trolley.proxy.rlwy.net
set ADO_DB_PORT=43111
set ADO_DB_NAME=railway
set ADO_DB_USER=root
set ADO_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW

:: Host RMI Railway per il deployment pubblico
set RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app

echo ✅ Configuration Railway:
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
echo 🚀 Démarrage du serveur Railway...
echo    Le serveur va démarrer avec l'hostname Railway
echo    Domaine public: %RAILWAY_PUBLIC_DOMAIN%
echo.
echo ⚠️  IMPORTANT: Pour le deployment Railway:
echo    1. Le serveur doit essere avviato su Railway
echo    2. L'interface graphique deve connettersi a: %RAILWAY_PUBLIC_DOMAIN%
echo    3. Assicurati che le porte siano aperte su Railway
echo.

java -cp "bin;lib\*" -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway

echo.
echo 🏁 Server Railway avviato.
pause

