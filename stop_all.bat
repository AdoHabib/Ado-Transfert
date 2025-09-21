@echo off
echo 🛑 Ado-Transfert - Arrêt de tous les services
echo =============================================

echo.
echo 🔍 Recherche des processus Java en cours...
tasklist /fi "imagename eq java.exe" 2>nul | find /i "java.exe" >nul
if %errorlevel% equ 0 (
    echo    Processus Java trouvés, arrêt en cours...
    taskkill /f /im java.exe
    echo    ✅ Tous les processus Java ont été arrêtés
) else (
    echo    ℹ️ Aucun processus Java en cours d'exécution
)

echo.
echo 🔍 Vérification des ports...
echo    Port 1099 (RMI):
netstat -an | findstr ":1099" >nul
if %errorlevel% equ 0 (
    echo    ⚠️ Le port 1099 est encore en cours d'utilisation
) else (
    echo    ✅ Port 1099 libéré
)

echo    Port 8080 (Web):
netstat -an | findstr ":8080" >nul
if %errorlevel% equ 0 (
    echo    ⚠️ Le port 8080 est encore en cours d'utilisation
) else (
    echo    ✅ Port 8080 libéré
)

echo.
echo ✅ Arrêt complet des services Ado-Transfert
pause

