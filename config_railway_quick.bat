@echo off
echo 🚀 Configuration Rapide Railway - Ado-Transfert
echo ===============================================

echo.
echo 📋 Configuration des variables Railway pour le test local
echo.

echo 🔧 Définition des variables d'environnement Railway...
echo    (Remplacez par vos vraies valeurs Railway)
echo.

:: Variables Railway - REMPLACEZ PAR VOS VRAIES VALEURS
set ADO_DB_HOST=containers-us-west-xxx.railway.app
set ADO_DB_PORT=6543
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
echo ⚠️  IMPORTANT: 
echo    Ces variables sont des exemples. 
echo    Remplacez-les par vos vraies valeurs Railway dans ce fichier.
echo.

echo 🧪 Test de connexion Railway...
echo.

call test_railway_connection.bat

echo.
echo 🏁 Configuration terminée
pause
