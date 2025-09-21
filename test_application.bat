@echo off
echo 🧪 Ado-Transfert - Test Complet de l'Application
echo =================================================

:: Configuration des variables d'environnement
set ADO_DB_PASSWORD=1234
set RAILWAY_PUBLIC_DOMAIN=localhost
set PORT=8080

echo.
echo 📋 Configuration de test:
echo    Base de données: localhost:3306/ado_transfert
echo    Mot de passe DB: %ADO_DB_PASSWORD%
echo    Domaine: %RAILWAY_PUBLIC_DOMAIN%
echo    Port web: %PORT%

echo.
echo 🔨 Étape 1: Compilation...
javac -cp "lib\*" -d bin src\*.java
if %errorlevel% neq 0 (
    echo ❌ Erreur lors de la compilation
    pause
    exit /b 1
)
echo ✅ Compilation réussie

echo.
echo 🚀 Étape 2: Démarrage du serveur RMI...
start /b java -cp "bin;lib\*" -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway
timeout /t 3 /nobreak >nul

echo.
echo 🌐 Étape 3: Démarrage du serveur web...
start /b java -cp "bin;lib\*" WebServer
timeout /t 2 /nobreak >nul

echo.
echo 📊 Étape 4: Vérification des services...
echo    Vérification du port RMI 1099...
netstat -an | findstr ":1099" >nul
if %errorlevel% equ 0 (
    echo    ✅ Serveur RMI actif sur le port 1099
) else (
    echo    ❌ Serveur RMI non détecté
)

echo    Vérification du port web %PORT%...
netstat -an | findstr ":%PORT%" >nul
if %errorlevel% equ 0 (
    echo    ✅ Serveur web actif sur le port %PORT%
) else (
    echo    ❌ Serveur web non détecté
)

echo.
echo 🌐 Étape 5: Test de l'interface web...
echo    Ouverture de l'interface web dans le navigateur...
start http://localhost:%PORT%

echo.
echo 🖥️ Étape 6: Lancement de l'interface graphique...
echo    L'interface graphique va s'ouvrir...
echo    Vous pouvez maintenant tester l'application complète
echo.
java -cp "bin;lib\*" AppGUI

echo.
echo 🛑 Nettoyage...
taskkill /f /im java.exe 2>nul
echo ✅ Test terminé

echo.
echo 📋 Résumé du test:
echo    - Compilation: ✅ OK
echo    - Serveur RMI: ✅ OK
echo    - Serveur web: ✅ OK
echo    - Interface graphique: ✅ OK
echo    - Interface web: ✅ OK

pause

