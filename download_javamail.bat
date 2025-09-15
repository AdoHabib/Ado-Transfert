@echo off
echo ========================================
echo    DOWNLOAD JAVAMAIL LIBRARIES
echo ========================================
echo.
echo Download delle librerie JavaMail per l'invio email...
echo.

REM Crea directory lib se non esiste
if not exist "lib" (
    echo Creazione directory lib...
    mkdir lib
)

REM Download JavaMail API
echo Download JavaMail API...
curl -L -o lib/javax.mail.jar "https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar"

REM Download Jakarta Activation (richiesto per JavaMail)
echo Download Jakarta Activation...
curl -L -o lib/jakarta.activation.jar "https://repo1.maven.org/maven2/jakarta/activation/jakarta.activation-api/2.1.0/jakarta.activation-api-2.1.0.jar"

echo.
echo ========================================
echo    DOWNLOAD COMPLETATO
echo ========================================
echo.
echo Le librerie JavaMail sono state scaricate in:
echo - lib/javax.mail.jar
echo - lib/jakarta.activation.jar
echo.
echo Ora puoi compilare il progetto con le librerie email.
echo.

pause
