package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProdottoTest {

    @Provide
    Arbitrary<String> nomeProdottoTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Property
    void testNomeProdotto(
            @ForAll("nomeProdottoTest") String nomeProdotto) {

        Prodotto prodotto = new Prodotto();

        prodotto.setNomeProdotto(nomeProdotto);

        assertEquals(nomeProdotto, prodotto.getNomeProdotto());
    }
}