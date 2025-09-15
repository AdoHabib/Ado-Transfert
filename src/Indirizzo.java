
// Classe Indirizzo

public class Indirizzo {
    private String via;
    private String numeroCivico;
    private String citta;
    private String provincia;
    private String cap;
    private String nazione; 

    public Indirizzo(String via, String numeroCivico, String citta, String provincia, String cap) {
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.citta = citta;
        this.provincia = provincia;
        this.cap = cap;
        this.nazione = "Italia"; // Default value
    }

    // Getters e Setters
    public String getVia() { return via; }
    public String getNumeroCivico() { return numeroCivico; }
    public String getCitta() { return citta; }
    public String getProvincia() { return provincia; }
    public String getCap() { return cap; }
    public String getNazione() { return nazione; }

    public void setVia(String via) { this.via = via; }
    public void setNumeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; }
    public void setCitta(String citta) { this.citta = citta; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public void setCap(String cap) { this.cap = cap; }
    public void setNazione(String nazione) { this.nazione = nazione; }

    @Override
    public String toString() {
        return "Indirizzo{" +
                ", via='" + via + '\'' +
                ", numeroCivico='" + numeroCivico + '\'' +
                ", citta='" + citta + '\'' +
                ", provincia='" + provincia + '\'' +
                ", cap='" + cap + '\'' +
                ", nazione='" + nazione + '\'' +
                '}';
    }
}
// Indirizzo.java
// Questa classe rappresenta un indirizzo con i suoi attributi e metodi di accesso.