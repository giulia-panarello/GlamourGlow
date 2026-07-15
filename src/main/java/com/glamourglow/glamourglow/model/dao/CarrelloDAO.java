package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.mo.Carrello;

import java.util.List;

public interface CarrelloDAO {


    List<Carrello> findById(long id_utente);
    public List<Carrello> findAll();
    public void delete(long id_ut, long id_prod);
    public void create( long id_ut, long id_prod, int qta);
}
