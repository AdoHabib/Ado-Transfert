@echo off
echo ========================================
echo    CLIENT RMI ADO-TRANSFERT
echo ========================================
echo.
echo Avvio del client RMI...
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
if not exist "bin\Cliente.class" (
    echo ERRORE: Classi non compilate!
    echo Esegui prima: javac -d bin src\*.java
    pause
    exit /b 1
)

REM Verifica che il server sia in esecuzione
echo Verifica connessione al server RMI...
netstat -an | find "1099" >nul 2>&1
if %errorlevel% neq 0 (
    echo AVVISO: Server RMI non trovato sulla porta 1099
    echo Assicurati che il server sia in esecuzione
    echo.
)

echo.
echo ========================================
echo    AVVIO CLIENT RMI
echo ========================================
echo.
echo Il client si connettera' al server RMI
echo Assicurati che il server sia in esecuzione
echo.

REM Avvia il client
java -cp "bin;lib\mysql-connector-j-9.2.0.jar;lib\mysql-connector-java.jar" Cliente

echo.
echo Client fermato.
pause
