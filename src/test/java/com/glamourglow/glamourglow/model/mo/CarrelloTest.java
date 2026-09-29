package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarrelloTest {

    @Property
    void testSetGetQta(@ForAll int qta) {
        Carrello carrello = new Carrello();

        carrello.setQta(qta);

        assertEquals(qta, carrello.getQta());
    }

    @Property
    void testSetGetProdotto(@ForAll long idProdotto) {
        Carrello carrello = new Carrello();

        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(idProdotto);

        carrello.setProdotto(prodotto);

        assertEquals(prodotto, carrello.getProdotto());
    }

    @Property
    void testSetGetUtente(@ForAll long idUtente) {
        Carrello carrello = new Carrello();

        Utente utente = new Utente();
        utente.setIdNome(idUtente);

        carrello.setUtente(utente);

        assertEquals(utente, carrello.getUtente());
    }
}