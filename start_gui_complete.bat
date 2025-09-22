@echo off
echo 🏦 Ado-Transfert - Interface Graphique Complete
echo ================================================

:: Configuration des variables d'environnement
set ADO_DB_PASSWORD=System.getenv("ADO_DB_PASSWORD")
set RAILWAY_PUBLIC_DOMAIN=System.getenv("RAILWAY_PUBLIC_DOMAIN")
set ADO_DB_HOST=System.getenv("ADO_DB_HOST")
set ADO_DB_PORT=System.getenv("ADO_DB_PORT")
set ADO_DB_NAME=System.getenv("ADO_DB_NAME")
set PORT=System.getenv("PORT")
set ADO_DB_USER=System.getenv("ADO_DB_USER")
set ADO_EMAIL_HOST=System.getenv("ADO_EMAIL_HOST")
set ADO_EMAIL_PORT=System.getenv("ADO_EMAIL_PORT")
set ADO_EMAIL_USERNAME=System.getenv("ADO_EMAIL_USERNAME")
set ADO_EMAIL_PASSWORD=System.getenv("ADO_EMAIL_PASSWORD")
set ADO_EMAIL_FROM=System.getenv("ADO_EMAIL_FROM")
set ADO_EMAIL_FROM_NAME=System.getenv("ADO_EMAIL_FROM_NAME")

echo.
echo 📋 Configuration:
echo    Base de données: %ADO_DB_HOST%:%ADO_DB_PORT%/%ADO_DB_NAME%
echo    Mot de passe DB: %ADO_DB_PASSWORD%
echo    Domaine: %RAILWAY_PUBLIC_DOMAIN%
echo    Port: %PORT%
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
start /b java -cp "bin;lib\*" -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway
timeout /t 3 /nobreak >nul

echo.
echo 🌐 Démarrage du serveur web en arrière-plan...
start /b java -cp "bin;lib\*" WebServer
timeout /t 2 /nobreak >nul

echo.
echo 🖥️ Lancement de l'interface graphique...
echo    L'interface graphique va s'ouvrir dans quelques secondes...
java -cp "bin;lib\*" AppGUI

echo.
echo 🛑 Arrêt des serveurs...
taskkill /f /im java.exe 2>nul
echo ✅ Application fermée

pause

