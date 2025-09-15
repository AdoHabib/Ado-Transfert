@echo off
echo ========================================
echo    SETUP RAILWAY + MYSQL
echo ========================================
echo.
echo Configurazione dell'app per Railway...
echo.

REM Verifica che Java sia installato
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRORE: Java non trovato nel sistema!
    echo Installa Java 8 o superiore e riprova.
    pause
    exit /b 1
)

echo ========================================
echo    CONFIGURAZIONE DATABASE RAILWAY
echo ========================================
echo.

echo Inserisci le credenziali del database Railway:
echo.

set /p railway_host="Host MySQL Railway (es. containers-us-west-xxx.railway.app): "
set /p railway_port="Porta MySQL (default: 3306): "
set /p railway_database="Nome Database (default: railway): "
set /p railway_user="Username (default: root): "
set /p railway_password="Password MySQL Railway: "

if "%railway_host%"=="" (
    echo ERRORE: Host non inserito!
    pause
    exit /b 1
)

if "%railway_password%"=="" (
    echo ERRORE: Password non inserita!
    pause
    exit /b 1
)

REM Imposta valori di default se vuoti
if "%railway_port%"=="" set railway_port=3306
if "%railway_database%"=="" set railway_database=railway
if "%railway_user%"=="" set railway_user=root

echo.
echo ========================================
echo    CONFIGURAZIONE EMAIL
echo ========================================
echo.

set /p email_username="Inserisci la tua email Gmail: "
set /p email_password="Inserisci la tua App Password Gmail: "

if "%email_username%"=="" (
    echo ERRORE: Email non inserita!
    pause
    exit /b 1
)

if "%email_password%"=="" (
    echo ERRORE: App Password non inserita!
    pause
    exit /b 1
)

echo.
echo ========================================
echo    IMPOSTAZIONE VARIABILI D'AMBIENTE
echo ========================================
echo.

echo Impostazione variabili d'ambiente per Railway...

REM Imposta variabili d'ambiente per l'utente corrente
setx ADO_DB_HOST "%railway_host%"
setx ADO_DB_PORT "%railway_port%"
setx ADO_DB_NAME "%railway_database%"
setx ADO_DB_USER "%railway_user%"
setx ADO_DB_PASSWORD "%railway_password%"

setx ADO_EMAIL_HOST "smtp.gmail.com"
setx ADO_EMAIL_PORT "587"
setx ADO_EMAIL_USERNAME "%email_username%"
setx ADO_EMAIL_PASSWORD "%email_password%"
setx ADO_EMAIL_FROM "%email_username%"
setx ADO_EMAIL_FROM_NAME "Ado-Transfert Sistema"

if %errorlevel% equ 0 (
    echo.
    echo ✅ Variabili d'ambiente impostate con successo!
    echo.
    echo Configurazione Railway:
    echo - Host: %railway_host%
    echo - Porta: %railway_port%
    echo - Database: %railway_database%
    echo - User: %railway_user%
    echo.
    echo Configurazione Email:
    echo - Email: %email_username%
    echo.
    echo ⚠️ IMPORTANTE: Riavvia il prompt dei comandi per
    echo utilizzare le nuove variabili d'ambiente.
    echo.
    echo Prossimi passi:
    echo 1. Riavvia il prompt dei comandi
    echo 2. Esegui: .\compile.bat
    echo 3. Esegui: .\start_server.bat
) else (
    echo.
    echo ❌ Errore durante l'impostazione delle variabili.
    echo Riprova o configura manualmente.
)

echo.
pause
