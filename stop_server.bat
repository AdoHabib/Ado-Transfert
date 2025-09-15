@echo off
echo ========================================
echo    FERMATA SERVER RMI
echo ========================================
echo.
echo Fermata di tutti i processi Java...
echo.

REM Ferma tutti i processi Java
taskkill /f /im java.exe >nul 2>&1

if %errorlevel% equ 0 (
    echo Server RMI fermato con successo!
) else (
    echo Nessun processo Java trovato in esecuzione.
)

echo.
echo Verifica processi Java rimanenti...
tasklist | find "java.exe" >nul 2>&1
if %errorlevel% equ 0 (
    echo ATTENZIONE: Alcuni processi Java sono ancora in esecuzione!
    tasklist | find "java.exe"
) else (
    echo Tutti i processi Java sono stati fermati.
)

echo.
pause
