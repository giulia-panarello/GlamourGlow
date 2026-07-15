package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.mo.Prodotto;
import java.util.List;

public interface ProdottoDAO {

    public List<Prodotto> cerca(String Nomeprodotto, String Marchioprodotto, String Categoria, boolean promo);

    Prodotto findById(long id);

    List<Prodotto> findPromo();

    void modifica(Prodotto prodotto);

    void create(
            Prodotto prodotto

    );

    List<Prodotto> cercaadmin(String Nomeprodotto, String Marchioprodotto, String categoria);
}
