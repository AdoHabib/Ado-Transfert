@echo off
echo ========================================
echo    AGGIORNA DOMINIO NEL CODICE
echo ========================================
echo.

set /p domain="Inserisci il tuo dominio completo (es. https://AdoHabib.github.io/ado-transfert): "

if "%domain%"=="" (
    echo ERRORE: Dominio non inserito!
    pause
    exit /b 1
)

echo.
echo Aggiornamento in corso...
echo Dominio: %domain%

REM Aggiorna App.java
echo Aggiornamento App.java...
powershell -Command "(Get-Content 'src\App.java') -replace 'https://yourdomain.com/reset-password\?token=', '%domain%/reset-password.html?token=' | Set-Content 'src\App.java'"

echo ✅ App.java aggiornato!

echo.
echo ========================================
echo    AGGIORNAMENTO COMPLETATO
echo ========================================
echo.
echo Il dominio è stato aggiornato in:
echo - src/App.java
echo.
echo Nuovo link di reset:
echo %domain%/reset-password.html?token=TOKEN&user=USERID
echo.
echo Prossimi passi:
echo 1. Compila il progetto: .\compile.bat
echo 2. Riavvia il server: .\start_server.bat
echo 3. Testa il recupero password
echo.
pause
