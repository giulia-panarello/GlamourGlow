package com.glamourglow.glamourglow.model.mo;
import java.util.List;

public class Categoria {

    private Long idCategoria;
    private String nomeCategoria;
    private String descrizione;
    private List<Prodotto> prodotto;

    public List<Prodotto> getProdotto(){
        return prodotto;
    }

    public void setProdotto(List<Prodotto> prodotto){
        this.prodotto = prodotto;
    }


    public Long getIdCategoria(){
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria){
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria(){
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria){
        this.nomeCategoria = nomeCategoria;
    }

    public String getDescrizione(){
        return descrizione;
    }

    public void setDescrizione(String descrizione){
        this.descrizione = descrizione;
    }
}
