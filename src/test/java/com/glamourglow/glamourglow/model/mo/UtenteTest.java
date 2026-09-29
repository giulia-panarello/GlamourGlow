package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtenteTest {

    @Provide
    Arbitrary<String> nomeUtenteTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Provide
    Arbitrary<String> emailUtenteTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> nome + "@email.com");
    }

    @Provide
    Arbitrary<String> passwordUtenteTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Provide
    Arbitrary<String> telefonoUtenteTest() {
        return Arbitraries.strings()
                .numeric()
                .ofMinLength(6)
                .ofMaxLength(15);
    }

    @Provide
    Arbitrary<Double> walletUtenteTest() {
        return Arbitraries.integers()
                .between(0, 1_000_000)
                .map(centesimi -> centesimi / 100.0);
    }

    @Provide
    Arbitrary<String> statoAccountUtenteTest() {
        return Arbitraries.of("attivo", "bloccato");
    }

    @Provide
    Arbitrary<Boolean> ruoloUtenteTest() {
        return Arbitraries.of(true, false);
    }

    @Provide
    Arbitrary<String> cognomeUtenteTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Property
    void testNomeUtente(
            @ForAll("nomeUtenteTest") String nome) {

        Utente utente = new Utente();

        utente.setNome(nome);

        assertEquals(nome, utente.getNome());
    }

    @Property
    void testEmailUtente(
            @ForAll("emailUtenteTest") String email) {

        Utente utente = new Utente();

        utente.setEmail(email);

        assertEquals(email, utente.getEmail());
    }

    @Property
    void testPasswordUtente(
            @ForAll("passwordUtenteTest") String password) {

        Utente utente = new Utente();

        utente.setPassword(password);

        assertEquals(password, utente.getPassword());
    }

    @Property
    void testTelefonoUtente(
            @ForAll("telefonoUtenteTest") String telefono) {

        Utente utente = new Utente();

        utente.setTelefono(telefono);

        assertEquals(telefono, utente.getTelefono());
    }

    @Property
    void testWalletUtente(
            @ForAll("walletUtenteTest") double wallet) {

        Utente utente = new Utente();

        utente.setWallet(wallet);

        assertEquals(wallet, utente.getWallet());
    }

    @Property
    void testStatoAccountUtente(
            @ForAll("statoAccountUtenteTest") String statoAccount) {

        Utente utente = new Utente();

        utente.setStatoAccount(statoAccount);

        assertEquals(statoAccount, utente.getStatoAccount());
    }

    @Property
    void testRuoloUtente(
            @ForAll("ruoloUtenteTest") boolean ruolo) {

        Utente utente = new Utente();

        utente.setRuolo(ruolo);

        assertEquals(ruolo, utente.getRuolo());
    }

    @Property
    void testCognomeUtente(
            @ForAll("cognomeUtenteTest") String cognome) {

        Utente utente = new Utente();

        utente.setCognome(cognome);

        assertEquals(cognome, utente.getCognome());
    }

    @Property
    void testIsRuoloUtente(
            @ForAll("ruoloUtenteTest") boolean ruolo) {

        Utente utente = new Utente();

        utente.setRuolo(ruolo);

        assertEquals(ruolo, utente.isRuolo());
    }
}