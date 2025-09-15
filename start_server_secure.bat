@echo off
echo ========================================
echo    SERVER RMI ADO-TRANSFERT SICURO
echo ========================================
echo.
echo Avvio del server con configurazione sicura...
echo.

REM Verifica che Java sia installato
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRORE: Java non trovato nel sistema!
    echo Installa Java 8 o superiore e riprova.
    pause
    exit /b 1
)

REM Verifica che le classi siano compilate
if not exist "bin\Server.class" (
    echo ERRORE: Classi non compilate!
    echo Esegui prima: compile.bat
    pause
    exit /b 1
)

echo ========================================
echo    CONFIGURAZIONE SICURA EMAIL
echo ========================================
echo.

REM Richiedi configurazione email in modo sicuro
echo Configurazione email Gmail:
echo.

set /p email_username="Inserisci la tua email Gmail (es. adobinesse@gmail.com): "
set /p email_password="Inserisci la tua App Password Gmail (non la password normale!): "

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
echo ✅ Configurazione email caricata in modo sicuro
echo.

REM Imposta variabili d'ambiente per questa sessione
set ADO_EMAIL_HOST=smtp.gmail.com
set ADO_EMAIL_PORT=587
set ADO_EMAIL_USERNAME=%email_username%
set ADO_EMAIL_PASSWORD=%email_password%
set ADO_EMAIL_FROM=%email_username%
set ADO_EMAIL_FROM_NAME=Ado-Transfert Sistema

echo ========================================
echo    AVVIO SERVER
echo ========================================
echo.

echo Il server richiederà la password del database MySQL
echo (premi INVIO per usare la password di default: 1234)
echo.
echo Premi Ctrl+C per fermare il server
echo.

REM Avvia il server con le variabili d'ambiente impostate
java -cp "bin;lib\mysql-connector-j-9.2.0.jar;lib\mysql-connector-java.jar;lib\javax.mail.jar;lib\jakarta.activation.jar" Server

echo.
echo Server fermato.
echo Le credenziali email sono state rimosse dalla memoria.
pause
