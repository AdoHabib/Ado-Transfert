@echo off
echo 🚀 Ado-Transfert - Serveur RMI
echo ===============================

:: Configuration des variables d'environnement pour Railway
:: Ces variables doivent être définies dans Railway ou localement pour le test
if "%ADO_DB_PASSWORD%"=="" set ADO_DB_PASSWORD=1234
if "%RAILWAY_PUBLIC_DOMAIN%"=="" set RAILWAY_PUBLIC_DOMAIN=localhost

echo.
echo 📋 Configuration:
echo    Base de données: Railway MySQL (variables d'environnement)
echo    Mot de passe DB: %ADO_DB_PASSWORD%
echo    Domaine: %RAILWAY_PUBLIC_DOMAIN%
echo.
echo ⚠️  IMPORTANT: Pour utiliser Railway, définissez les variables d'environnement:
echo    - ADO_DB_HOST (ex: containers-us-west-xxx.railway.app)
echo    - ADO_DB_PORT (ex: 6543)
echo    - ADO_DB_NAME (ex: railway)
echo    - ADO_DB_USER (ex: root)
echo    - ADO_DB_PASSWORD (votre mot de passe Railway)
echo    - RAILWAY_PUBLIC_DOMAIN (votre domaine Railway)

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

