@echo off
echo 🚀 Ado-Transfert - Démarrage Simple
echo ===================================

echo.
echo 📋 Ce script démarre l'application sans base de données
echo    (Pour tester l'interface utilisateur uniquement)
echo.

echo 🔨 Compilation de l'application...
javac -cp "lib\*" -d bin src\*.java
if %errorlevel% neq 0 (
    echo ❌ Erreur lors de la compilation
    pause
    exit /b 1
)
echo ✅ Compilation réussie

echo.
echo 🚀 Démarrage de l'interface graphique...
echo    Note: La connexion à la base de données échouera
echo    mais l'interface utilisateur se lancera
echo.

java -cp "bin;lib\*" AppGUI

echo.
echo 🛑 Application fermée
pause
