package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdineTest {

    @Provide
    Arbitrary<Long> idOrdineTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<Long> idUtenteOrdineTest() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<Double> totaleOrdineTest() {
        return Arbitraries.doubles()
                .between(0.01, 10000.0);
    }

    @Provide
    Arbitrary<String> indirizzoConsegnaTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(50);
    }

    @Provide
    Arbitrary<String> statoOrdineTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Provide
    Arbitrary<String> cittaOrdineTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Provide
    Arbitrary<String> statoOrdinePaeseTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Provide
    Arbitrary<String> dataOrdineTest() {
        return Arbitraries.of(
                "2025-01-24 17:22:42",
                "2026-09-07 12:45:15",
                "2026-09-28 10:30:00"
        );
    }

    @Property
    void testSetGetIdOrdine(
            @ForAll("idOrdineTest") long idOrdine) {

        Ordine ordine = new Ordine();

        ordine.setIdOrdine(idOrdine);

        assertEquals(idOrdine, ordine.getIdOrdine());
    }

    @Property
    void testSetGetUtente(
            @ForAll("idUtenteOrdineTest") long idUtente) {

        Ordine ordine = new Ordine();

        Utente utente = new Utente();
        utente.setIdNome(idUtente);

        ordine.setUtente(utente);

        assertEquals(utente, ordine.getUtente());
    }

    @Test
    void testSetGetDettagliOrdine() {
        Ordine ordine = new Ordine();

        List<DettagliOrdine> dettagli = new ArrayList<>();
        DettagliOrdine dettaglio = new DettagliOrdine();
        dettagli.add(dettaglio);

        ordine.setDettagliOrdine(dettagli);

        assertEquals(dettagli, ordine.getDettagliOrdine());
    }

    @Property
    void testSetGetTotaleOrdine(
            @ForAll("totaleOrdineTest") double totaleOrdine) {

        Ordine ordine = new Ordine();

        ordine.setTotaleOrdine(totaleOrdine);

        assertEquals(totaleOrdine, ordine.getTotaleOrdine());
    }

    @Property
    void testSetGetIndirizzoConsegna(
            @ForAll("indirizzoConsegnaTest") String indirizzo) {

        Ordine ordine = new Ordine();

        ordine.setIndirizzoConsegna(indirizzo);

        assertEquals(indirizzo, ordine.getIndirizzoConsegna());
    }

    @Property
    void testSetGetStatoOrdine(
            @ForAll("statoOrdineTest") String statoOrdine) {

        Ordine ordine = new Ordine();

        ordine.setStatoOrdine(statoOrdine);

        assertEquals(statoOrdine, ordine.getStatoOrdine());
    }

    @Property
    void testSetGetCitta(
            @ForAll("cittaOrdineTest") String citta) {

        Ordine ordine = new Ordine();

        ordine.setCitta(citta);

        assertEquals(citta, ordine.getCitta());
    }

    @Property
    void testSetGetStato(
            @ForAll("statoOrdinePaeseTest") String stato) {

        Ordine ordine = new Ordine();

        ordine.setStato(stato);

        assertEquals(stato, ordine.getStato());
    }

    @Property
    void testSetGetData(
            @ForAll("dataOrdineTest") String data) {

        Ordine ordine = new Ordine();

        ordine.setData(data);

        assertEquals(data, ordine.getData());
    }
}