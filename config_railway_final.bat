@echo off
echo 🚀 Configuration Railway Finale - Ado-Transfert
echo ===============================================

echo.
echo 📋 Configuration avec vos vraies valeurs Railway
echo.

echo 🔧 Définition des variables d'environnement Railway...
echo.

:: Variables Railway - VOS VRAIES VALEURS
set ADO_DB_HOST=trolley.proxy.rlwy.net
set ADO_DB_PORT=43111
set ADO_DB_NAME=railway
set ADO_DB_USER=root
set ADO_DB_PASSWORD=jkeYsrFEDPTZouJvuiqDjnafVmSVlamW
set RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app

echo ✅ Variables configurées:
echo    ADO_DB_HOST=%ADO_DB_HOST%
echo    ADO_DB_PORT=%ADO_DB_PORT%
echo    ADO_DB_NAME=%ADO_DB_NAME%
echo    ADO_DB_USER=%ADO_DB_USER%
echo    ADO_DB_PASSWORD=***masqué***
echo    RAILWAY_PUBLIC_DOMAIN=%RAILWAY_PUBLIC_DOMAIN%

echo.
echo 🧪 Test de connexion Railway...
echo.

call test_railway_connection.bat

echo.
echo 🏁 Configuration terminée
pause
