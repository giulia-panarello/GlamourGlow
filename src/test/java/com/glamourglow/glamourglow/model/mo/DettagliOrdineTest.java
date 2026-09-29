package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DettagliOrdineTest {

    @Provide
    Arbitrary<Long> idDettaglioOrdineTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<Long> idOrdineDettaglioTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<Long> idProdottoDettaglioTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<Integer> quantitaDettaglioTest() {
        return Arbitraries.integers()
                .between(1, 1000);
    }

    @Provide
    Arbitrary<Double> prezzoUnitarioDettaglioTest() {
        return Arbitraries.doubles()
                .between(0.01, 1000.0);
    }

    @Provide
    Arbitrary<String> codiceCouponDettaglioTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Property
    void testSetGetIdDettaglio(
            @ForAll("idDettaglioOrdineTest") long idDettaglio) {

        DettagliOrdine dettaglio = new DettagliOrdine();

        dettaglio.setIdDettaglio(idDettaglio);

        assertEquals(idDettaglio, dettaglio.getIdDettaglio());
    }

    @Property
    void testSetGetOrdine(
            @ForAll("idOrdineDettaglioTest") long idOrdine) {

        DettagliOrdine dettaglio = new DettagliOrdine();

        Ordine ordine = new Ordine();
        ordine.setIdOrdine(idOrdine);

        dettaglio.setOrdine(ordine);

        assertEquals(ordine, dettaglio.getOrdine());
    }

    @Property
    void testSetGetProdotto(
            @ForAll("idProdottoDettaglioTest") long idProdotto) {

        DettagliOrdine dettaglio = new DettagliOrdine();

        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(idProdotto);

        dettaglio.setProdotto(prodotto);

        assertEquals(prodotto, dettaglio.getProdotto());
    }

    @Property
    void testSetGetQuantita(
            @ForAll("quantitaDettaglioTest") int quantita) {

        DettagliOrdine dettaglio = new DettagliOrdine();

        dettaglio.setQuantita(quantita);

        assertEquals(quantita, dettaglio.getQuantita());
    }

    @Property
    void testSetGetPrezzoUnitario(
            @ForAll("prezzoUnitarioDettaglioTest") double prezzoUnitario) {

        DettagliOrdine dettaglio = new DettagliOrdine();

        dettaglio.setPrezzoUnitario(prezzoUnitario);

        assertEquals(prezzoUnitario, dettaglio.getPrezzoUnitario(), 0.001);
    }

    @Property
    void testSetGetCoupon(
            @ForAll("codiceCouponDettaglioTest") String codice) {

        DettagliOrdine dettaglio = new DettagliOrdine();

        Coupon coupon = new Coupon();
        coupon.setCodice(codice);

        dettaglio.setCoupon(coupon);

        assertEquals(coupon, dettaglio.getCoupon());
    }
}