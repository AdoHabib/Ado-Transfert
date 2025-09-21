# Configuration des Variables d'Environnement Railway

## Variables de Base de Données (OBLIGATOIRES)

Ces variables sont automatiquement configurées par Railway MySQL :

```bash
ADO_DB_HOST=containers-us-west-xxx.railway.app
ADO_DB_PORT=3306
ADO_DB_NAME=railway
ADO_DB_USER=root
ADO_DB_PASSWORD=<password_automatique_railway>
```

## Variables Email (OPTIONNELLES)

Pour activer l'envoi d'emails (récupération mot de passe, notifications) :

```bash
ADO_EMAIL_USERNAME=votre_email@gmail.com
ADO_EMAIL_PASSWORD=votre_app_password
ADO_EMAIL_SMTP_HOST=smtp.gmail.com
ADO_EMAIL_SMTP_PORT=587
```

## Variables Railway (AUTOMATIQUES)

Railway configure automatiquement :

```bash
RAILWAY_PUBLIC_DOMAIN=ado-transfer.up.railway.app
PORT=1099
```

## Comment configurer sur Railway

1. Allez dans votre projet Railway
2. Cliquez sur l'onglet "Variables"
3. Ajoutez les variables suivantes :

### Variables obligatoires :
- `ADO_DB_PASSWORD` : Utilisez la password MySQL fournie par Railway

### Variables optionnelles (pour email) :
- `ADO_EMAIL_USERNAME` : Votre adresse Gmail
- `ADO_EMAIL_PASSWORD` : Mot de passe d'application Gmail

## Notes importantes

- Railway configure automatiquement `ADO_DB_HOST`, `ADO_DB_PORT`, `ADO_DB_NAME`, `ADO_DB_USER`
- Vous devez seulement configurer `ADO_DB_PASSWORD` avec la valeur fournie par Railway
- Les variables email sont optionnelles - l'app fonctionne sans elles
- Le serveur Railway (`ServerRailway.java`) fonctionne sans interaction utilisateur
