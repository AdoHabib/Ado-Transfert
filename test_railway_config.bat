@echo off
echo 🧪 Test de la configuration Railway Ado-Transfert

echo.
echo 📋 Variables d'environnement requises :
if defined ADO_DB_HOST (
    echo    ADO_DB_HOST: %ADO_DB_HOST%
) else (
    echo    ADO_DB_HOST: NON CONFIGURÉ
)

if defined ADO_DB_PORT (
    echo    ADO_DB_PORT: %ADO_DB_PORT%
) else (
    echo    ADO_DB_PORT: NON CONFIGURÉ
)

if defined ADO_DB_NAME (
    echo    ADO_DB_NAME: %ADO_DB_NAME%
) else (
    echo    ADO_DB_NAME: NON CONFIGURÉ
)

if defined ADO_DB_USER (
    echo    ADO_DB_USER: %ADO_DB_USER%
) else (
    echo    ADO_DB_USER: NON CONFIGURÉ
)

if defined ADO_DB_PASSWORD (
    echo    ADO_DB_PASSWORD: CONFIGURÉ
) else (
    echo    ADO_DB_PASSWORD: NON CONFIGURÉ
)

if defined RAILWAY_PUBLIC_DOMAIN (
    echo    RAILWAY_PUBLIC_DOMAIN: %RAILWAY_PUBLIC_DOMAIN%
) else (
    echo    RAILWAY_PUBLIC_DOMAIN: NON CONFIGURÉ
)

echo.
echo 📧 Variables email optionnelles :
if defined ADO_EMAIL_USERNAME (
    echo    ADO_EMAIL_USERNAME: %ADO_EMAIL_USERNAME%
) else (
    echo    ADO_EMAIL_USERNAME: NON CONFIGURÉ
)

if defined ADO_EMAIL_PASSWORD (
    echo    ADO_EMAIL_PASSWORD: CONFIGURÉ
) else (
    echo    ADO_EMAIL_PASSWORD: NON CONFIGURÉ
)

echo.
echo 🔧 Test de compilation :
javac -cp "lib\*" -d bin src\*.java
if %errorlevel% equ 0 (
    echo ✅ Compilation réussie
) else (
    echo ❌ Erreur de compilation
    pause
    exit /b 1
)

echo.
echo 🚀 Test du serveur Railway :
echo    Pour tester localement avec variables Railway :
echo    set ADO_DB_PASSWORD=votre_password_railway
echo    set RAILWAY_PUBLIC_DOMAIN=localhost
echo    java -cp "bin;lib\*" ServerRailway

pause
