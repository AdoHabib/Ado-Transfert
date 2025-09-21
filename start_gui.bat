@echo off
echo 🖥️ Ado-Transfert - Interface Graphique
echo =====================================

:: Configuration des variables d'environnement
set ADO_DB_PASSWORD=1234
set RAILWAY_PUBLIC_DOMAIN=localhost

echo.
echo 📋 Configuration:
echo    Base de données: localhost:3306/ado_transfert
echo    Mot de passe DB: %ADO_DB_PASSWORD%

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
echo    Assurez-vous que le serveur RMI est déjà démarré ou
echo    utilisez start_gui_complete.bat pour un démarrage automatique
echo.
echo 🖥️ Lancement de l'interface graphique...
java -cp "bin;lib\*" AppGUI

pause

