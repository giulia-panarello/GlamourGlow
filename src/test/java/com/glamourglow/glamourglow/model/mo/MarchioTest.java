package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarchioTest {

    @Provide
    Arbitrary<Long> idMarchioTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<String> nomeMarchioTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<Long> idProdottoMarchioTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Property
    void testSetGetIdMarchio(
            @ForAll("idMarchioTest") long idMarchio) {

        Marchio marchio = new Marchio();

        marchio.setIdMarchio(idMarchio);

        assertEquals(idMarchio, marchio.getIdMarchio());
    }

    @Property
    void testSetGetNomeMarchio(
            @ForAll("nomeMarchioTest") String nomeMarchio) {

        Marchio marchio = new Marchio();

        marchio.setNomeMarchio(nomeMarchio);

        assertEquals(nomeMarchio, marchio.getNomeMarchio());
    }

    @Property
    void testSetGetProdotto(
            @ForAll("idProdottoMarchioTest") long idProdotto) {

        Marchio marchio = new Marchio();

        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(idProdotto);

        List<Prodotto> prodotti = new ArrayList<>();
        prodotti.add(prodotto);

        marchio.setProdotto(prodotti);

        assertEquals(prodotti, marchio.getProdotto());
    }
}