@echo off
echo ========================================
echo    SISTEMA RMI ADO-TRANSFERT
echo ========================================
echo.
echo Avvio completo del sistema...
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
    echo Compilazione delle classi...
    call compile.bat
    if %errorlevel% neq 0 (
        echo ERRORE: Compilazione fallita!
        pause
        exit /b 1
    )
)

echo.
echo ========================================
echo    AVVIO SERVER RMI
echo ========================================
echo.
echo Avvio del server in background...
echo.

REM Avvia il server in background
start "Server RMI Ado-Transfert" /min cmd /c "java -cp bin Server"

REM Attendi che il server si avvii
echo Attesa avvio server (5 secondi)...
timeout /t 5 /nobreak >nul

echo.
echo ========================================
echo    AVVIO CLIENT RMI
echo ========================================
echo.
echo Avvio del client...
echo.

REM Avvia il client
java -cp bin Cliente

echo.
echo ========================================
echo    SISTEMA FERMATO
echo ========================================
echo.
echo Il client e' stato chiuso.
echo Il server potrebbe essere ancora in esecuzione.
echo.
echo Per fermare completamente il server:
echo 1. Chiudi la finestra del server
echo 2. Oppure esegui: taskkill /f /im java.exe
echo.

pause
