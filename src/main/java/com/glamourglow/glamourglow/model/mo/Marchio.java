package com.glamourglow.glamourglow.model.mo;
import java.util.List;

public class Marchio {
    private Long idMarchio;
    private String nomeMarchio;
    private List<Prodotto> prodotto;

    public List<Prodotto> getProdotto(){
        return prodotto;
    }

    public void setProdotto(List<Prodotto> prodotto){
        this.prodotto = prodotto;
    }

    public Long getIdMarchio(){
        return idMarchio;
    }

    public void setIdMarchio(Long idMarchio){
        this.idMarchio = idMarchio;
    }

    public String getNomeMarchio(){
        return nomeMarchio;
    }

    public void setNomeMarchio(String nomeMarchio){
        this.nomeMarchio = nomeMarchio;
    }


}

