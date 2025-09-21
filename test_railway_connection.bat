@echo off
echo 🧪 Test Connexion Railway - Ado-Transfert
echo ========================================

echo.
echo 📋 Ce script teste la connexion à votre base de données Railway
echo.

echo 🔧 Vérification des variables d'environnement Railway...
echo.

if "%ADO_DB_HOST%"=="" (
    echo ❌ ADO_DB_HOST non défini
    echo    Définissez-le avec: set ADO_DB_HOST=votre-host-railway
) else (
    echo ✅ ADO_DB_HOST: %ADO_DB_HOST%
)

if "%ADO_DB_PORT%"=="" (
    echo ❌ ADO_DB_PORT non défini
    echo    Définissez-le avec: set ADO_DB_PORT=votre-port
) else (
    echo ✅ ADO_DB_PORT: %ADO_DB_PORT%
)

if "%ADO_DB_NAME%"=="" (
    echo ❌ ADO_DB_NAME non défini
    echo    Définissez-le avec: set ADO_DB_NAME=votre-database
) else (
    echo ✅ ADO_DB_NAME: %ADO_DB_NAME%
)

if "%ADO_DB_USER%"=="" (
    echo ❌ ADO_DB_USER non défini
    echo    Définissez-le avec: set ADO_DB_USER=votre-user
) else (
    echo ✅ ADO_DB_USER: %ADO_DB_USER%
)

if "%ADO_DB_PASSWORD%"=="" (
    echo ❌ ADO_DB_PASSWORD non défini
    echo    Définissez-le avec: set ADO_DB_PASSWORD=votre-password
) else (
    echo ✅ ADO_DB_PASSWORD: ***défini***
)

if "%RAILWAY_PUBLIC_DOMAIN%"=="" (
    echo ❌ RAILWAY_PUBLIC_DOMAIN non défini
    echo    Définissez-le avec: set RAILWAY_PUBLIC_DOMAIN=votre-domaine
) else (
    echo ✅ RAILWAY_PUBLIC_DOMAIN: %RAILWAY_PUBLIC_DOMAIN%
)

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
echo 🧪 Test de connexion à Railway...
echo    URL: jdbc:mysql://%ADO_DB_HOST%:%ADO_DB_PORT%/%ADO_DB_NAME%
echo    User: %ADO_DB_USER%
echo.

java -cp "bin;lib\*" -Djava.rmi.server.hostname=%RAILWAY_PUBLIC_DOMAIN% ServerRailway

echo.
echo 🏁 Test terminé
pause
