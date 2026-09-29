package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoriaTest {

    @Provide
    Arbitrary<Long> idCategoriaTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<String> nomeCategoriaTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> descrizioneCategoriaTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(50);
    }

    @Provide
    Arbitrary<Long> idProdottoCategoriaTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Property
    void testSetGetIdCategoria(
            @ForAll("idCategoriaTest") long idCategoria) {

        Categoria categoria = new Categoria();

        categoria.setIdCategoria(idCategoria);

        assertEquals(idCategoria, categoria.getIdCategoria());
    }

    @Property
    void testSetGetNomeCategoria(
            @ForAll("nomeCategoriaTest") String nomeCategoria) {

        Categoria categoria = new Categoria();

        categoria.setNomeCategoria(nomeCategoria);

        assertEquals(nomeCategoria, categoria.getNomeCategoria());
    }

    @Property
    void testSetGetDescrizione(
            @ForAll("descrizioneCategoriaTest") String descrizione) {

        Categoria categoria = new Categoria();

        categoria.setDescrizione(descrizione);

        assertEquals(descrizione, categoria.getDescrizione());
    }

    @Property
    void testSetGetProdotto(
            @ForAll("idProdottoCategoriaTest") long idProdotto) {

        Categoria categoria = new Categoria();

        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(idProdotto);

        List<Prodotto> prodotti = new ArrayList<>();
        prodotti.add(prodotto);

        categoria.setProdotto(prodotti);

        assertEquals(prodotti, categoria.getProdotto());
    }
}