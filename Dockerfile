# Dockerfile per Ado-Transfert su Railway
FROM openjdk:11-jre-slim

# Installa dipendenze necessarie
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Crea directory di lavoro
WORKDIR /app

# Copia i file dell'applicazione
COPY bin/ /app/bin/
COPY lib/ /app/lib/
COPY src/ /app/src/
COPY web/ /app/web/
COPY *.properties /app/
COPY *.md /app/
COPY start.sh /app/

# Rendi eseguibile lo script di avvio
RUN chmod +x start.sh

# Esponi la porta RMI
EXPOSE 1099

# Esponi la porta HTTP per il web (opzionale)
EXPOSE 8080

# Comando di avvio
CMD ["./start.sh"]
