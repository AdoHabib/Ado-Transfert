@echo off
echo 🚀 Ado-Transfert - Développement Local
echo ======================================

echo.
echo 📋 Configuration pour développement local
echo.

:: Configuration Railway pour la base de données uniquement
set ADO_DB_HOST=trolley.proxy.rlwy.net
set ADO_DB_PORT=43111
set ADO_DB_NAME=railway
set ADO_DB_USER=root
set ADO_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW
set RAILWAY_PUBLIC_DOMAIN=localhost

echo ✅ Configuration:
echo    Database: %ADO_DB_HOST%:%ADO_DB_PORT%/%ADO_DB_NAME%
echo    RMI Server: %RAILWAY_PUBLIC_DOMAIN%:1099
echo    User: %ADO_DB_USER%

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
echo 🚀 Démarrage du serveur local...
echo    Le serveur va démarrer en arrière-plan
echo.

:: Démarre le serveur Railway en arrière-plan
start "Serveur Local" cmd /k "java -cp bin;lib\* -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway"

:: Attendre que le serveur démarre
echo ⏳ Attente du démarrage du serveur...
timeout /t 8 /nobreak > nul

echo.
echo 🖥️ Démarrage de l'interface graphique...
echo    L'interface va se connecter à: %RAILWAY_PUBLIC_DOMAIN%:1099
echo.

:: Démarre l'interface graphique
java -cp "bin;lib\*" AppGUI

echo.
echo 🛑 Application fermée
pause
