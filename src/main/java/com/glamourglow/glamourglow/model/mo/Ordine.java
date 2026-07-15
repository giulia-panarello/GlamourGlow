package com.glamourglow.glamourglow.model.mo;
import java.util.List;
public class Ordine {

    private Long idOrdine;
    private Utente utente;
    private List<DettagliOrdine> dettagliOrdine;
    private Double totaleOrdine;
    private String indirizzoConsegna;
    private String statoOrdine;
    private String citta;
    private String stato;
    private String data;

    public List<DettagliOrdine> getDettagliOrdine(){
        return dettagliOrdine;
    }
    public void setDettagliOrdine(List<DettagliOrdine> dettagliOrdine){
        this.dettagliOrdine = dettagliOrdine;
    }


    public Long getIdOrdine(){
        return idOrdine;
    }
    public void setIdOrdine(Long idOrdine){
        this.idOrdine = idOrdine;
    }
    public Utente getUtente(){
        return utente;
    }
    public void setUtente(Utente utente){
        this.utente = utente;
    }

    public Double getTotaleOrdine(){
        return totaleOrdine;
    }
    public void setTotaleOrdine(Double totaleOrdine){
        this.totaleOrdine = totaleOrdine;
    }
    public String getIndirizzoConsegna(){
        return indirizzoConsegna;
    }
    public void setIndirizzoConsegna(String indirizzoConsegna){
        this.indirizzoConsegna = indirizzoConsegna;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setStatoOrdine(String statoOrdine) {
        this.statoOrdine = statoOrdine;
    }


    public String getStatoOrdine() {
        return statoOrdine;
    }
}
