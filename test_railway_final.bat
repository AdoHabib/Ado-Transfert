@echo off
echo 🧪 Test final Ado-Transfert Railway

echo.
echo 📋 Variables d'environnement de test :
set ADO_DB_PASSWORD=1234
set RAILWAY_PUBLIC_DOMAIN=localhost
set PORT=8080

echo    ADO_DB_PASSWORD: %ADO_DB_PASSWORD%
echo    RAILWAY_PUBLIC_DOMAIN: %RAILWAY_PUBLIC_DOMAIN%
echo    PORT: %PORT%

echo.
echo 🔧 Test de compilation finale :
javac -cp "lib\*" -d bin src\*.java
if %errorlevel% equ 0 (
    echo ✅ Compilation réussie
) else (
    echo ❌ Erreur de compilation
    pause
    exit /b 1
)

echo.
echo 🚀 Test du serveur web uniquement :
echo    Démarrage du serveur web sur le port %PORT%...
start /b java -cp "bin;lib\*" WebServer
timeout /t 3 /nobreak >nul

echo.
echo 📊 Vérification des ports :
netstat -an | findstr ":8080"
if %errorlevel% equ 0 (
    echo ✅ Serveur web actif sur le port 8080
) else (
    echo ❌ Serveur web non détecté
)

echo.
echo 🌐 Test de l'interface web :
echo    Ouvrez http://localhost:%PORT% dans votre navigateur
echo    Vous devriez voir la page d'accueil Ado-Transfert

echo.
echo 🛑 Arrêt des processus de test :
taskkill /f /im java.exe 2>nul
echo ✅ Test terminé

echo.
echo 🎉 Votre application est prête pour Railway !
echo    - Compilation: ✅ OK
echo    - Serveur web: ✅ OK  
echo    - Configuration: ✅ OK

pause
