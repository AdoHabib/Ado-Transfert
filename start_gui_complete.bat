@echo off
echo 🏦 Ado-Transfert - Interface Graphique Complete
echo ================================================

:: Configuration des variables d'environnement
set ADO_DB_PASSWORD=1234
set RAILWAY_PUBLIC_DOMAIN=localhost
set PORT=8080

echo.
echo 📋 Configuration:
echo    Base de données: localhost:3306/ado_transfert
echo    Mot de passe DB: %ADO_DB_PASSWORD%
echo    Domaine: %RAILWAY_PUBLIC_DOMAIN%

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

