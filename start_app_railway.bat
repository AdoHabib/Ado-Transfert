@echo off
echo 🚀 Ado-Transfert - Application Railway
echo =====================================

echo.
echo 📋 Configuration Railway complète
echo.

:: Configuration Railway pour l'application
set ADO_DB_HOST=trolley.proxy.rlwy.net
set ADO_DB_PORT=System.getenv().getOrDefault("ADO_DB_PORT");
set ADO_DB_NAME=System.getenv().getOrDefault("ADO_DB_NAME");
set ADO_DB_USER=System.getenv().getOrDefault("ADO_DB_USER");
set ADO_DB_PASSWORD=System.getenv().getOrDefault("ADO_DB_PASSWORD");
set RAILWAY_PUBLIC_DOMAIN=System.getenv().getOrDefault("RAILWAY_PUBLIC_DOMAIN");

echo ✅ Configuration Railway:
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
echo 🚀 Démarrage du serveur Railway...
echo    Le serveur va démarrer en arrière-plan
echo.

:: Démarre le serveur Railway en arrière-plan
start "Serveur Railway" cmd /k "java -cp bin;lib\* -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway"

:: Attendre que le serveur démarre
echo ⏳ Attente du démarrage du serveur...
timeout /t 10 /nobreak > nul

echo.
echo 🖥️ Démarrage de l'interface graphique...
echo    L'interface va se connecter à: %RAILWAY_PUBLIC_DOMAIN%:1099
echo.

:: Démarre l'interface graphique
java -cp "bin;lib\*" AppGUI

echo.
echo 🛑 Application fermée
pause
