@echo off
echo ========================================
echo    SERVER RMI ADO-TRANSFERT
echo ========================================
echo.
echo Avvio del server RMI...
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
    echo Esegui prima: javac -d bin src\*.java
    pause
    exit /b 1
)

REM Verifica che MySQL sia in esecuzione
echo Verifica connessione MySQL...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo AVVISO: MySQL non trovato nel PATH
    echo Assicurati che MySQL sia in esecuzione
    echo.
)

echo.
echo ========================================
echo    AVVIO SERVER RMI
echo ========================================
echo.
echo Il server richiedera' la password del database MySQL
echo (premi INVIO per usare la password di default: 1234)
echo.
echo Premi Ctrl+C per fermare il server
echo.

REM Avvia il server
java -cp "bin;lib\mysql-connector-j-9.2.0.jar;lib\mysql-connector-java.jar;lib\javax.mail.jar;lib\jakarta.activation.jar" Server

echo.
echo Server fermato.
pause
