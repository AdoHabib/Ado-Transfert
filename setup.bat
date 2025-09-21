@echo off
echo 🏦 Ado-Transfert - Installation et Configuration
echo ================================================

echo.
echo 📋 Vérification des prérequis...

:: Vérification de Java
echo 🔍 Vérification de Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java n'est pas installé ou n'est pas dans le PATH
    echo    Veuillez installer Java 11 ou supérieur
    pause
    exit /b 1
) else (
    echo ✅ Java détecté
    java -version
)

echo.
echo 🔍 Vérification de MySQL...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️ MySQL n'est pas installé ou n'est pas dans le PATH
    echo    L'application utilisera les paramètres par défaut
) else (
    echo ✅ MySQL détecté
    mysql --version
)

echo.
echo 📁 Création des dossiers nécessaires...
if not exist bin mkdir bin
if not exist lib (
    echo ❌ Dossier 'lib' manquant
    echo    Veuillez télécharger les bibliothèques nécessaires
    pause
    exit /b 1
)
echo ✅ Structure des dossiers OK

echo.
echo 🔨 Compilation de l'application...
javac -cp "lib\*" -d bin src\*.java
if %errorlevel% neq 0 (
    echo ❌ Erreur lors de la compilation
    echo    Vérifiez que toutes les bibliothèques sont présentes dans le dossier 'lib'
    pause
    exit /b 1
)
echo ✅ Compilation réussie

echo.
echo 📊 Résumé de l'installation:
echo    ✅ Java: OK
echo    ✅ Bibliothèques: OK
echo    ✅ Compilation: OK
echo    ✅ Structure: OK

echo.
echo 🚀 Installation terminée !
echo.
echo 📋 Scripts disponibles:
echo    - start_gui_complete.bat : Démarrage complet (recommandé)
echo    - start_gui.bat : Interface graphique uniquement
echo    - start_server.bat : Serveur RMI uniquement
echo    - test_application.bat : Test complet
echo    - stop_all.bat : Arrêt de tous les services

echo.
echo 💡 Pour commencer, utilisez: start_gui_complete.bat

pause

