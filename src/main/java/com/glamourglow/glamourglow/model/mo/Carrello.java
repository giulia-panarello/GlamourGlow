package com.glamourglow.glamourglow.model.mo;

public class Carrello {

    int qta;
    Prodotto prod;
    Utente ut;



    public int getQta() {
        return qta;
    }

    public void setQta(int qta) {
        this.qta = qta;
    }

    public Prodotto getProdotto() {
        return prod;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prod = prodotto;
    }

    public Utente getUtente() {
        return ut;
    }

    public void setUtente(Utente utente) {
        this.ut = utente;
    }
}
