@echo off
echo 🚀 Ado-Transfert - Serveur RMI
echo ===============================

:: Configuration des variables d'environnement
set ADO_DB_PASSWORD=System.getenv("ADO_DB_PASSWORD")
set RAILWAY_PUBLIC_DOMAIN=System.getenv("RAILWAY_PUBLIC_DOMAIN")

echo.
echo 📋 Configuration:
echo    Base de données: %ADO_DB_HOST%:%ADO_DB_PORT%/%ADO_DB_NAME%
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
echo 🚀 Démarrage du serveur RMI...
echo    Le serveur va démarrer sur le port 1099
echo    Appuyez sur Ctrl+C pour arrêter le serveur
echo.
java -cp "bin;lib\*" -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway

echo.
echo 🛑 Serveur arrêté
pause

