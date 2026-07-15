package com.glamourglow.glamourglow.model.mo;
import java.util.List;
public class DettagliOrdine {
    private Long idDettaglio;
    private Ordine ordine;
    private Prodotto prodotto;
    private Integer quantita;
    private Double prezzoUnitario;
    private Coupon coupon;

    public Long getIdDettaglio(){
        return idDettaglio;
    }
    public void setIdDettaglio(Long idDettaglio){
        this.idDettaglio = idDettaglio;
    }
    public Ordine getOrdine(){
        return ordine;
    }
    public void setOrdine(Ordine ordine){
        this.ordine = ordine;
    }
    public Prodotto getProdotto(){
        return prodotto;
    }
    public void setProdotto(Prodotto prodotto){
        this.prodotto = prodotto;
    }
    public Integer getQuantita(){
        return quantita;
    }
    public void setQuantita(Integer quantita){
        this.quantita = quantita;
    }
    public Double getPrezzoUnitario(){
        return prezzoUnitario;
    }
    public void setPrezzoUnitario(Double prezzoUnitario){
        this.prezzoUnitario = prezzoUnitario;
    }


    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
