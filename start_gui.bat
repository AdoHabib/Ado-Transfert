@echo off
echo ========================================
echo    GUI ADO-TRANSFERT
echo ========================================
echo.
echo Avvio dell'interfaccia grafica...
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
if not exist "bin\AppGUI.class" (
    echo ERRORE: Classi non compilate!
    echo Esegui prima: compile.bat
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
echo    AVVIO INTERFACCIA GRAFICA
echo ========================================
echo.
echo L'interfaccia grafica si connettera' al server RMI
echo Assicurati che il server sia in esecuzione
echo.

REM Avvia l'interfaccia grafica
java -cp "bin;lib\mysql-connector-j-9.2.0.jar;lib\mysql-connector-java.jar;lib\javax.mail.jar;lib\jakarta.activation.jar" AppGUI

echo.
echo Applicazione chiusa.
pause
