@echo off
echo ========================================
echo    COMPILAZIONE SISTEMA RMI
echo ========================================
echo.
echo Compilazione delle classi Java...
echo.

REM Verifica che Java sia installato
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRORE: Java non trovato nel sistema!
    echo Installa Java 8 o superiore e riprova.
    pause
    exit /b 1
)

REM Crea la directory bin se non esiste
if not exist "bin" (
    echo Creazione directory bin...
    mkdir bin
)

REM Compila le classi
echo Compilazione in corso...
javac -cp "lib\mysql-connector-j-9.2.0.jar;lib\mysql-connector-java.jar;lib\javax.mail.jar;lib\jakarta.activation.jar" -d bin src\InterfaceTransfer.java src\Utente.java src\Messaggio.java src\Indirizzo.java src\App.java src\InterfaceImpl.java src\Server.java src\Cliente.java src\AppGUI.java

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo    COMPILAZIONE COMPLETATA
    echo ========================================
    echo.
    echo Tutte le classi sono state compilate con successo!
    echo.
    echo Per avviare il sistema:
    echo 1. Esegui start_server.bat per avviare il server
    echo 2. Esegui start_client.bat per avviare il client
    echo.
) else (
    echo.
    echo ========================================
    echo    ERRORE DI COMPILAZIONE
    echo ========================================
    echo.
    echo Si sono verificati errori durante la compilazione.
    echo Controlla i messaggi di errore sopra.
    echo.
)

pause
