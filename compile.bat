@echo off
echo 🔨 Ado-Transfert - Compilation
echo ===============================

echo.
echo 📁 Nettoyage des anciennes classes...
if exist bin rmdir /s /q bin
mkdir bin

echo.
echo 🔨 Compilation des fichiers source...
javac -cp "lib\*" -d bin src\*.java

if %errorlevel% equ 0 (
    echo.
    echo ✅ Compilation réussie !
    echo.
    echo 📋 Fichiers compilés dans le dossier 'bin':
    dir /b bin\*.class
) else (
    echo.
    echo ❌ Erreur lors de la compilation
    echo    Vérifiez les erreurs ci-dessus
)

echo.
pause

