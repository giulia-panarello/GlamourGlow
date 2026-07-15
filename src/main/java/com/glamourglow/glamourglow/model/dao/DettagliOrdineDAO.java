package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.mo.DettagliOrdine;

import java.util.List;

public interface DettagliOrdineDAO {

    boolean usoCoupon(long idNome, String s);

    DettagliOrdine create(Long idDettagli, Long idOrdine, Long idProdotto, int qta, Double prezzo, String coupon);
    DettagliOrdine findById(long Id);

    List<DettagliOrdine> findByIdOrdine(Long idOrdine);
}
