@echo off
echo ========================================
echo    SETUP DOMINIO PER ADO-TRANSFERT
echo ========================================
echo.
echo Questo script ti aiuta a configurare un dominio
echo per il sistema Ado-Transfert.
echo.

:menu
echo ========================================
echo    OPZIONI DISPONIBILI
echo ========================================
echo.
echo 1. GitHub Pages (GRATUITO) - Consigliato
echo 2. Netlify (GRATUITO) - Alternativa
echo 3. Vercel (GRATUITO) - Per sviluppatori
echo 4. Dominio personalizzato (A PAGAMENTO)
echo 5. Test locale con IP
echo 6. Esci
echo.

set /p choice="Scegli un'opzione (1-6): "

if "%choice%"=="1" goto github
if "%choice%"=="2" goto netlify
if "%choice%"=="3" goto vercel
if "%choice%"=="4" goto domain
if "%choice%"=="5" goto local
if "%choice%"=="6" goto exit
goto menu

:github
echo.
echo ========================================
echo    SETUP GITHUB PAGES
echo ========================================
echo.
echo Passaggi per GitHub Pages:
echo.
echo 1. Vai su https://github.com
echo 2. Crea un nuovo repository pubblico chiamato "ado-transfert"
echo 3. Carica i file della cartella 'web' nella root del repository
echo 4. Vai su Settings ^> Pages
echo 5. Seleziona "Deploy from a branch"
echo 6. Scegli "main" branch e "/ (root)" folder
echo 7. Il tuo sito sarà disponibile su:
echo    https://tuousername.github.io/ado-transfert
echo.
echo 8. Aggiorna il file App.java con il tuo URL:
echo    String resetLink = "https://tuousername.github.io/ado-transfert/reset-password.html?token=" + token + "&user=" + userID;
echo.
pause
goto menu

:netlify
echo.
echo ========================================
echo    SETUP NETLIFY
echo ========================================
echo.
echo Passaggi per Netlify:
echo.
echo 1. Vai su https://netlify.com
echo 2. Registrati con GitHub
echo 3. Clicca "New site from Git"
echo 4. Connetti il tuo repository GitHub
echo 5. Build command: (lascia vuoto)
echo 6. Publish directory: web
echo 7. Clicca "Deploy site"
echo 8. Il tuo sito sarà disponibile su:
echo    https://random-name.netlify.app
echo.
echo 9. Puoi cambiare il nome in Site Settings
echo 10. Aggiorna App.java con il tuo URL Netlify
echo.
pause
goto menu

:vercel
echo.
echo ========================================
echo    SETUP VERCEL
echo ========================================
echo.
echo Passaggi per Vercel:
echo.
echo 1. Vai su https://vercel.com
echo 2. Registrati con GitHub
echo 3. Clicca "Import Project"
echo 4. Seleziona il tuo repository
echo 5. Framework Preset: Other
echo 6. Root Directory: web
echo 7. Clicca "Deploy"
echo 8. Il tuo sito sarà disponibile su:
echo    https://tuo-progetto.vercel.app
echo.
echo 9. Aggiorna App.java con il tuo URL Vercel
echo.
pause
goto menu

:domain
echo.
echo ========================================
echo    DOMINIO PERSONALIZZATO
echo ========================================
echo.
echo Provider consigliati per domini:
echo.
echo 1. Namecheap - .com a €8.88/anno
echo 2. GoDaddy - .com a €12.99/anno
echo 3. Aruba - .it a €9.99/anno
echo 4. OVH - .com a €7.99/anno
echo.
echo Dopo aver acquistato il dominio:
echo 1. Configura i DNS per puntare al tuo hosting
echo 2. Aggiorna App.java con il tuo dominio
echo 3. Configura SSL/HTTPS per sicurezza
echo.
echo Esempio di configurazione:
echo String resetLink = "https://tuodominio.com/reset-password?token=" + token + "&user=" + userID;
echo.
pause
goto menu

:local
echo.
echo ========================================
echo    TEST LOCALE CON IP
echo ========================================
echo.
echo Per testare localmente senza dominio:
echo.
echo 1. Trova il tuo IP locale:
for /f "tokens=2 delims=:" %%i in ('ipconfig ^| findstr /c:"IPv4"') do (
    set ip=%%i
    goto :ip_found
)
:ip_found
echo    Il tuo IP locale è: %ip%
echo.
echo 2. Avvia un server web locale nella cartella 'web':
echo    python -m http.server 8080
echo    oppure
echo    npx serve web
echo.
echo 3. Aggiorna App.java temporaneamente:
echo    String resetLink = "http://%ip%:8080/reset-password.html?token=" + token + "&user=" + userID;
echo.
echo 4. Testa l'invio email con l'IP locale
echo.
pause
goto menu

:exit
echo.
echo ========================================
echo    SETUP COMPLETATO
echo ========================================
echo.
echo Ricorda di:
echo 1. Aggiornare il link in App.java con il tuo URL
echo 2. Configurare Gmail con App Password
echo 3. Testare l'invio email
echo 4. Riavviare il server dopo le modifiche
echo.
echo Buon lavoro con Ado-Transfert!
echo.
pause
exit
