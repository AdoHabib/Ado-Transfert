@echo off
echo ========================================
echo    SETUP VARIABILI D'AMBIENTE EMAIL
echo ========================================
echo.
echo Questo script imposta le variabili d'ambiente
echo per la configurazione email in modo sicuro.
echo.

echo ⚠️ IMPORTANTE: Queste variabili saranno visibili nel sistema
echo ma NON nel codice sorgente (più sicuro).
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
echo Impostazione variabili d'ambiente...

REM Imposta variabili d'ambiente per l'utente corrente
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
    echo Le variabili sono state salvate e saranno disponibili
    echo per tutte le nuove sessioni del prompt dei comandi.
    echo.
    echo ⚠️ IMPORTANTE: Riavvia il prompt dei comandi per
    echo utilizzare le nuove variabili d'ambiente.
    echo.
    echo Puoi ora usare start_server.bat normalmente.
) else (
    echo.
    echo ❌ Errore durante l'impostazione delle variabili.
    echo Riprova o usa start_server_secure.bat
)

echo.
pause
