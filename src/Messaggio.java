

import java.text.SimpleDateFormat;
import java.util.Date;

public class Messaggio {
    private int id;
    private String mittenteID;
    private String destinatarioID;
    private String titolo;
    private String contenuto;
    private String dataInvio;

    public Messaggio(String mittenteID, String destinatarioID, String titolo, String contenuto) {
        this.mittenteID = mittenteID;
        this.destinatarioID = destinatarioID;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.dataInvio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getMittenteID() { return mittenteID; }
    public String getDestinatarioID() { return destinatarioID; }
    public String getTitolo() { return titolo; }
    public String getContenuto() { return contenuto; }
    public String getDataInvio() { return dataInvio; }

    public void setId(int id) { this.id = id; }
    public void setDataInvio(String dataInvio) { this.dataInvio = dataInvio; }
    public void setMittenteID(String mittenteID) {
        this.mittenteID = mittenteID;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public void setDestinatarioID(String destinatario){
        this.destinatarioID = destinatario;
    }
    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    @Override
    public String toString() {
        return "Messaggio{" +
                "id=" + id +
                ", mittenteID='" + mittenteID + '\'' +
                ", destinatarioID='" + destinatarioID + '\'' +
                ", titolo='" + titolo + '\'' +
                ", contenuto='" + contenuto + '\'' +
                ", dataInvio='" + dataInvio + '\'' +
                '}';
    }

    public String toStringShort() {
        return "Messaggio{" +
                "\nid=" + id +
                "\nmittenteID='" + mittenteID + '\'' +
                "\ntitolo='" + titolo + '\'' +
                "\ncontenuto='" + contenuto + '\'' +
                "\ndataInvio='" + dataInvio + '\'' +
                '}';
    }
}
// Messaggio.java
// Questa classe rappresenta un messaggio con i suoi attributi e metodi di accesso.