package com.glamourglow.glamourglow.model.mo;

public class Prodotto {
    private Long idProdotto;
    private String nomeProdotto;
    private String descrizione;
    private Double prezzo;
    private Integer quantitaDispo;
    private Categoria categoria;
    private Marchio marchio;
    private Boolean inPromo;
    private String immagine;
    private Double prezzoSconto;
    private boolean statoprodotto;


    public Long getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Long idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantitaDispo() {
        return quantitaDispo;
    }

    public void setQuantitaDispo(Integer quantitaDispo) {
        this.quantitaDispo = quantitaDispo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marchio getMarchio() {
        return marchio;
    }

    public void setMarchio(Marchio marchio) {
        this.marchio = marchio;
    }

    public Boolean getInPromo() {
        return inPromo;
    }

    public void setInPromo(Boolean inPromo) {
        this.inPromo = inPromo;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public Double getPrezzoSconto() {
        return prezzoSconto;
    }

    public void setPrezzoSconto(Double prezzoSconto) {
        this.prezzoSconto = prezzoSconto;
    }

    public boolean isStatoprodotto() {
        return statoprodotto;
    }

    public void setStatoprodotto(boolean statoprodotto) {
        this.statoprodotto = statoprodotto;
    }
}
