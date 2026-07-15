package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.mo.Ordine;
import java.util.List;

public interface OrdineDAO {

   Ordine findById(long Id);
    Ordine create(long idOrdine, long idNome, String formattedDateTime, String StatoOrdine, double totalecarrello, String indirizzo, String citta, String stato);

    List<Ordine> findByUser(long idNome);

    List<Ordine> findAll();

    void updateordine(long Id_ordine, String Stato);
}
