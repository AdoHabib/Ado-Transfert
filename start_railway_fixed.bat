@echo off
echo 🚀 Ado-Transfert - Démarrage Railway Corrigé
echo =============================================

echo.
echo 📋 Configuration Railway forcée
echo.

:: Force les variables Railway
set ADO_DB_HOST=trolley.proxy.rlwy.net
set ADO_DB_PORT=43111
set ADO_DB_NAME=railway
set ADO_DB_USER=root
set ADO_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW
set RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app

echo ✅ Variables Railway configurées:
echo    Host: %ADO_DB_HOST%:%ADO_DB_PORT%
echo    Database: %ADO_DB_NAME%
echo    User: %ADO_DB_USER%
echo    Domain: %RAILWAY_PUBLIC_DOMAIN%

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

:: Attendre un peu pour que le serveur démarre
timeout /t 8 /nobreak > nul

echo.
echo 🖥️ Démarrage de l'interface graphique...
echo    L'interface va se connecter au serveur Railway
echo.

:: Démarre l'interface graphique
java -cp "bin;lib\*" AppGUI

echo.
echo 🛑 Application fermée
pause
