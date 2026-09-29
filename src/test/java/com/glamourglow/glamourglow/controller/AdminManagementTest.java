package com.glamourglow.glamourglow.controller;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;
import com.glamourglow.glamourglow.services.config.Configuration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AdminManagementTest {

    @Provide
    Arbitrary<List<Prodotto>> prodotti() {
        return Arbitraries
                .strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> {
                    Prodotto prodotto = new Prodotto();
                    prodotto.setIdProdotto(1L);
                    prodotto.setNomeProdotto(nome);
                    prodotto.setDescrizione("Descrizione");
                    prodotto.setPrezzo(10.0);
                    prodotto.setQuantitaDispo(10);
                    prodotto.setInPromo(true);
                    prodotto.setImmagine("immagine.jpg");
                    prodotto.setPrezzoSconto(8.0);
                    prodotto.setStatoprodotto(true);
                    return prodotto;
                })
                .list()
                .ofMaxSize(10);
    }

    @Provide
    Arbitrary<List<Marchio>> marchi() {
        return Arbitraries
                .strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> {
                    Marchio marchio = new Marchio();
                    marchio.setIdMarchio(1L);
                    marchio.setNomeMarchio(nome);
                    return marchio;
                })
                .list()
                .ofMaxSize(10);
    }


    @Provide
    Arbitrary<List<Utente>> utenti() {
        return Arbitraries
                .longs()
                .between(1L, 1000L)
                .list()
                .ofMaxSize(10)
                .map(ids -> {
                    List<Utente> utenti = new ArrayList<>();
                    Set<Long> idUnici = new HashSet<>(ids);

                    for (Long id : idUnici) {
                        Utente utente = mock(Utente.class);
                        when(utente.getIdNome()).thenReturn(id);
                        utenti.add(utente);
                    }

                    return utenti;
                });
    }




    @Provide
    Arbitrary<Long> idUtente() {
        return Arbitraries
                .longs()
                .between(1L, 1000L);
    }



    @Provide
    Arbitrary<String> email() {
        return Arbitraries
                .strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> nome + "@test.it");
    }

    @Provide
    Arbitrary<Long> idUtenteDiverso() {
        return Arbitraries
                .longs()
                .between(2L, 1000L);
    }

    @Provide
    Arbitrary<Double> wallet() {
        return Arbitraries.doubles()
                .between(0.0, 10000.0);
    }



    @Provide
    Arbitrary<List<Ordine>> ordini() {
        return Arbitraries
                .longs()
                .between(1L, 1000L)
                .list()
                .ofMaxSize(10)
                .map(ids -> {
                    List<Ordine> ordini = new ArrayList<>();

                    long id = 1L;

                    for (Long ignored : ids) {
                        Ordine ordine = mock(Ordine.class);
                        when(ordine.getIdOrdine()).thenReturn(id++);
                        when(ordine.getUtente()).thenReturn(mock(Utente.class));
                        when(ordine.getDettagliOrdine())
                                .thenReturn(Collections.emptyList());

                        ordini.add(ordine);
                    }

                    return ordini;
                });
    }


    @Provide
    Arbitrary<Double> prezzoUnitario() {
        return Arbitraries.doubles()
                .between(0.01, 1000.0);
    }

    @Provide
    Arbitrary<Integer> percentualeSconto() {
        return Arbitraries.integers()
                .between(0, 100);
    }

    @Provide
    Arbitrary<String> codiceCoupon() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }


    @Provide
    Arbitrary<List<DettagliOrdine>> dettagliOrdineMultipli() {
        return Arbitraries.integers()
                .between(1, 5)
                .flatMap(numeroDettagli ->
                        Combinators.combine(
                                prezzoUnitario(),
                                percentualeSconto()
                        ).as((prezzo, sconto) -> {

                            List<DettagliOrdine> dettagli =
                                    new ArrayList<>();

                            for (int i = 0; i < numeroDettagli; i++) {

                                DettagliOrdine dettaglio =
                                        mock(DettagliOrdine.class);

                                Coupon coupon =
                                        mock(Coupon.class);

                                Prodotto prodotto =
                                        mock(Prodotto.class);

                                long idProdotto = i + 1L;
                                String codiceCoupon = "C" + (i + 1);

                                when(coupon.getCodice())
                                        .thenReturn(codiceCoupon);

                                when(coupon.getSconto())
                                        .thenReturn(sconto);

                                when(dettaglio.getCoupon())
                                        .thenReturn(coupon);

                                when(dettaglio.getPrezzoUnitario())
                                        .thenReturn(prezzo);

                                when(dettaglio.getProdotto())
                                        .thenReturn(prodotto);

                                when(prodotto.getIdProdotto())
                                        .thenReturn(idProdotto);

                                dettagli.add(dettaglio);
                            }

                            return dettagli;
                        }));
    }


    @Provide
    Arbitrary<Long> idOrdine() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }


    @Provide
    Arbitrary<String> scontiNonNumerici() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }


    @Provide
    Arbitrary<List<Categoria>> categorie() {
        return Arbitraries
                .strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> {
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(1L);
                    categoria.setNomeCategoria(nome);
                    return categoria;
                })
                .list()
                .ofMaxSize(10);
    }

    @Provide
    Arbitrary<String> nomeProdotto() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<Integer> quantitaProdotto() {
        return Arbitraries.integers()
                .between(1, 1000);
    }

    @Provide
    Arbitrary<Double> prezzoScontato() {
        return Arbitraries.doubles()
                .between(0.01, 1000.0);
    }

    @Provide
    Arbitrary<Long> idProdotto() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

    @Provide
    Arbitrary<List<Coupon>> coupons() {
        return Arbitraries
                .strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(codice -> {
                    Coupon coupon = mock(Coupon.class);
                    when(coupon.getCodice()).thenReturn(codice);
                    return coupon;
                })
                .list()
                .ofMaxSize(10);
    }

    @Provide
    Arbitrary<RuntimeException> eccezioniRuntime() {
        return Arbitraries
                .strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(50)
                .map(RuntimeException::new);
    }

    @Property
    void gestioneutente_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);


        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(null);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getMarchioDAO()).thenReturn(marchioDAO);

        when(prodottoDAO.findPromo()).thenReturn(prodotti);
        when(marchioDAO.findAll()).thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenReturn(sessionFactory);

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()))
                    .thenReturn(daoFactory);

            AdminManagement.gestioneutente(request, response);
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(request).setAttribute("Prodotti", prodotti);
        verify(request).setAttribute("marchi", marchi);
        verify(request).setAttribute("promo", true);

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", false);
        verify(request).setAttribute("loggedUser", null);
        verify(request).setAttribute("applicationMessage", null);

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void gestioneutente_utenteLoggatoNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente loggedUser = mock(Utente.class);
        when(loggedUser.getRuolo()).thenReturn(false);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getMarchioDAO()).thenReturn(marchioDAO);

        when(prodottoDAO.findPromo()).thenReturn(prodotti);
        when(marchioDAO.findAll()).thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenReturn(sessionFactory);

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()))
                    .thenReturn(daoFactory);

            AdminManagement.gestioneutente(request, response);
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(loggedUser).getRuolo();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();
        verify(request).setAttribute("Prodotti", prodotti);
        verify(request).setAttribute("marchi", marchi);
        verify(request).setAttribute("promo", true);

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(request).setAttribute("loggedOn", true);
        verify(request).setAttribute("loggedUser", loggedUser);
        verify(request).setAttribute("applicationMessage", null);

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }

    @Property
    void gestioneutente_admin(
            @ForAll("utenti") List<Utente> utenti) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente admin = mock(Utente.class);

        when(admin.getRuolo()).thenReturn(true);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(admin);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);

        when(utenteDAO.findSearch()).thenReturn(utenti);
        List<List<Ordine>> ordiniAttesi = new ArrayList<>();

        for (Utente utente : utenti) {
            List<Ordine> ordiniUtente = new ArrayList<>();

            when(ordineDAO.findByUser(utente.getIdNome()))
                    .thenReturn(ordiniUtente);

            ordiniAttesi.add(ordiniUtente);
        }

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenReturn(sessionFactory);

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()))
                    .thenReturn(daoFactory);

            AdminManagement.gestioneutente(request, response);
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(admin).getRuolo();

        verify(utenteDAO).findSearch();

        for (Utente utente : utenti) {
            verify(ordineDAO).findByUser(utente.getIdNome());
        }

        verify(request).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneutente"
        );

        verify(request).setAttribute(
                "utenteList",
                utenti
        );

        verify(request).setAttribute(
                "ordini",
                ordiniAttesi
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", true);
        verify(request).setAttribute("loggedUser", admin);
        verify(request).setAttribute("applicationMessage", null);

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void gestioneutente_eccezioneDuranteFindLoggedUser(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenThrow(eccezione);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenReturn(sessionFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneutente(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezione,
                    exception.getCause()
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(sessionFactory).rollbackTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void gestioneutente_eccezionePrimaCreazioneFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenThrow(eccezione);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneutente(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezione,
                    exception.getCause()
            );
        }
    }




    @Property
    void gestioneutente_erroreDuranteRollbackSessionFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezione,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente admin = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(utenteDAO.findSearch())
                .thenThrow(eccezione);

        doThrow(eccezioneRollback)
                .when(sessionFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenReturn(sessionFactory);

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()))
                    .thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneutente(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezione,
                    exception.getCause()
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();
        verify(sessionUserDAO).findLoggedUser();
        verify(admin).getRuolo();
        verify(daoFactory).getUtenteDAO();
        verify(utenteDAO).findSearch();
        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void gestioneutente_erroreDuranteCloseSessionFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        List<Prodotto> prodotti = new ArrayList<>();
        List<Marchio> marchi = new ArrayList<>();

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        doThrow(eccezione)
                .when(sessionFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenReturn(sessionFactory);

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()))
                    .thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    AdminManagement.gestioneutente(
                            request,
                            response
                    )
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).getProdottoDAO();
        verify(daoFactory).getMarchioDAO();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(request).setAttribute(
                "Prodotti",
                prodotti
        );

        verify(request).setAttribute(
                "marchi",
                marchi
        );

        verify(request).setAttribute(
                "promo",
                true
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute(
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void modificautente_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);


        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()))
                    .thenReturn(sessionFactory);

            mockedDAOFactory
                    .when(() -> DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()))
                    .thenReturn(daoFactory);

            AdminManagement.modificautente(request, response);
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();


        verify(daoFactory).getProdottoDAO();
        verify(daoFactory).getMarchioDAO();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();


        verify(request).setAttribute(
                "Prodotti",
                prodotti
        );

        verify(request).setAttribute(
                "marchi",
                marchi
        );

        verify(request).setAttribute(
                "promo",
                true
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();


        verify(request).setAttribute(
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );


        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void modificautente_utenteLoggatoNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(loggedUser.getRuolo())
                .thenReturn(false);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificautente(request, response);
        }

        verify(sessionUserDAO).findLoggedUser();
        verify(loggedUser).getRuolo();
        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(request).setAttribute(
                "Prodotti",
                prodotti
        );

        verify(request).setAttribute(
                "marchi",
                marchi
        );

        verify(request).setAttribute(
                "promo",
                true
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_adminEmailDuplicata(
            @ForAll("email") String email,
            @ForAll("idUtenteDiverso") Long idAltroUtente) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente admin = mock(Utente.class);
        Utente altroUtente = mock(Utente.class);

       final Long idAdmin = 1L;

        when(admin.getRuolo())
                .thenReturn(true);

        when(admin.getStatoAccount())
                .thenReturn("attivo");

        when(admin.getIdNome())
                .thenReturn(idAdmin);

        when(altroUtente.getIdNome())
                .thenReturn(idAltroUtente);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(request.getParameter("email"))
                .thenReturn(email);

        when(request.getParameter("ruolo"))
                .thenReturn("true");

        when(request.getParameter("stato"))
                .thenReturn("attivo");

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idAdmin));

        when(utenteDAO.findEmail(email))
                .thenReturn(altroUtente);

        when(utenteDAO.findSearch())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificautente(request, response);
        }


        verify(admin, times(2)).getRuolo();
        verify(request).getParameter("email");
        verify(request).getParameter("ruolo");
        verify(request).getParameter("stato");
        verify(request).getParameter("id");

        verify(utenteDAO).findEmail(email);
        verify(utenteDAO, never())
                .update(any(Utente.class));

        verify(sessionUserDAO, never()).create(
                anyLong(),
                any(),
                any(),
                any(),
                any(),
                isNull(),
                any(),
                any(),
                anyBoolean()
        );

        verify(utenteDAO).findSearch();

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Impossibile modificare email"
        );

        verify(request, times(2)).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneutente"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_adminModificaAltroUtenteAccountAttivo(
            @ForAll("email") String email,
            @ForAll("idUtenteDiverso") Long idUser) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente admin = mock(Utente.class);
        Utente user = mock(Utente.class);

        final Long idAdmin = 1L;

        List<Utente> utenti = Collections.singletonList(user);
        List<Ordine> ordiniUser = new ArrayList<>();


        when(admin.getRuolo())
                .thenReturn(true);

        when(admin.getIdNome())
                .thenReturn(idAdmin);

        when(admin.getStatoAccount())
                .thenReturn("attivo");

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);


        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);


        when(request.getParameter("email"))
                .thenReturn(email);

        when(request.getParameter("ruolo"))
                .thenReturn("false");

        when(request.getParameter("stato"))
                .thenReturn("attivo");

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idUser));


        when(utenteDAO.findEmail(email))
                .thenReturn(null);


        when(utenteDAO.findSearch())
                .thenReturn(utenti);


        when(user.getIdNome())
                .thenReturn(idUser);

        when(ordineDAO.findByUser(idUser))
                .thenReturn(ordiniUser);


        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);


            AdminManagement.modificautente(request, response);
        }


        verify(utenteDAO).findEmail(email);
        verify(utenteDAO).update(any(Utente.class));

        verify(sessionUserDAO, never()).create(
                anyLong(),
                any(),
                any(),
                any(),
                any(),
                isNull(),
                any(),
                any(),
                anyBoolean()
        );


        verify(utenteDAO).findSearch();
        verify(ordineDAO).findByUser(idUser);

        verify(request).setAttribute(
                "ordini",
                Collections.singletonList(ordiniUser)
        );

        verify(request).setAttribute(
                "utenteList",
                utenti
        );


        verify(request, times(2)).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneutente"
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                null
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                admin
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_adminModificaProprioAccount(
            @ForAll("email") String email,
            @ForAll("idUtente") Long idAdmin) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente admin = mock(Utente.class);
        Utente stessoUtente = mock(Utente.class);

        when(admin.getRuolo())
                .thenReturn(true);

        when(admin.getIdNome())
                .thenReturn(idAdmin);

        when(admin.getStatoAccount())
                .thenReturn("attivo");

        when(admin.getNome())
                .thenReturn("Mario");

        when(admin.getCognome())
                .thenReturn("Rossi");

        when(admin.getPassword())
                .thenReturn("password");

        when(admin.getWallet())
                .thenReturn(null);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);


        when(request.getParameter("email"))
                .thenReturn(email);

        when(request.getParameter("ruolo"))
                .thenReturn("true");

        when(request.getParameter("stato"))
                .thenReturn("attivo");

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idAdmin));


        when(utenteDAO.findEmail(email))
                .thenReturn(stessoUtente);

        when(stessoUtente.getIdNome())
                .thenReturn(idAdmin);


        when(utenteDAO.findSearch())
                .thenReturn(Collections.emptyList());


        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);


            AdminManagement.modificautente(request, response);
        }

        verify(utenteDAO).findEmail(email);


        ArgumentCaptor<Utente> utenteCaptor =
                ArgumentCaptor.forClass(Utente.class);

        verify(utenteDAO).update(utenteCaptor.capture());

        Utente utenteAggiornato =
                utenteCaptor.getValue();

        assertEquals(
                idAdmin,
                utenteAggiornato.getIdNome()
        );

        assertEquals(
                email,
                utenteAggiornato.getEmail()
        );

        assertEquals(
                "attivo",
                utenteAggiornato.getStatoAccount()
        );

        assertTrue(
                utenteAggiornato.getRuolo()
        );

        assertEquals(
                "Mario",
                utenteAggiornato.getNome()
        );

        assertEquals(
                "Rossi",
                utenteAggiornato.getCognome()
        );

        assertEquals(
                "password",
                utenteAggiornato.getPassword()
        );


        verify(sessionUserDAO).create(
                eq(idAdmin),
                eq("password"),
                eq("Mario"),
                eq("Rossi"),
                eq(email),
                isNull(),
                isNull(),
                eq("attivo"),
                eq(true)
        );

        verify(utenteDAO).findSearch();

        verify(ordineDAO, never())
                .findByUser(anyLong());


        verify(request, times(2)).setAttribute(
                eq("viewUrl"),
                eq("AdminManagement/gestioneutente")
        );

        verify(request).setAttribute(
                eq("ordini"),
                eq(Collections.emptyList())
        );

        verify(request).setAttribute(
                eq("utenteList"),
                eq(Collections.emptyList())
        );


        verify(request).setAttribute(
                eq("loggedOn"),
                eq(true)
        );

        verify(request, times(2)).setAttribute(
                eq("applicationMessage"),
                isNull()
        );

        verify(request).setAttribute(
                eq("loggedUser"),
                any(Utente.class)
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_proprioAccountRuoloDisabilitato(
            @ForAll("email") String email,
            @ForAll("idUtente") Long idAdmin,
            @ForAll("wallet") Double wallet) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente admin = mock(Utente.class);
        Utente stessoUtente = mock(Utente.class);

        List<Prodotto> prodotti = new ArrayList<>();
        List<Marchio> marchi = new ArrayList<>();


        when(admin.getRuolo())
                .thenReturn(true);

        when(admin.getIdNome())
                .thenReturn(idAdmin);

        when(admin.getStatoAccount())
                .thenReturn("attivo");

        when(admin.getNome())
                .thenReturn("Mario");

        when(admin.getCognome())
                .thenReturn("Rossi");

        when(admin.getPassword())
                .thenReturn("password");

        when(admin.getWallet())
                .thenReturn(wallet);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);


        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);


        when(request.getParameter("email"))
                .thenReturn(email);

        when(request.getParameter("ruolo"))
                .thenReturn("false");

        when(request.getParameter("stato"))
                .thenReturn("attivo");

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idAdmin));


        when(utenteDAO.findEmail(email))
                .thenReturn(stessoUtente);

        when(stessoUtente.getIdNome())
                .thenReturn(idAdmin);


        when(utenteDAO.findSearch())
                .thenReturn(Collections.emptyList());


        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificautente(request, response);
        }


        verify(utenteDAO).findEmail(email);

        verify(utenteDAO).update(any(Utente.class));

        verify(sessionUserDAO).create(
                eq(idAdmin),
                eq("password"),
                eq("Mario"),
                eq("Rossi"),
                eq(email),
                isNull(),
                eq(wallet),
                eq("attivo"),
                eq(false)
        );


        verify(utenteDAO).findSearch();
        verify(daoFactory).getProdottoDAO();
        verify(daoFactory).getMarchioDAO();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(request).setAttribute(
                "Prodotti",
                prodotti
        );

        verify(request).setAttribute(
                "marchi",
                marchi
        );

        verify(request).setAttribute(
                "promo",
                true
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );


        verify(ordineDAO, never())
                .findByUser(anyLong());

        verify(request).setAttribute(
                "ordini",
                Collections.emptyList()
        );

        verify(request).setAttribute(
                "utenteList",
                Collections.emptyList()
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();


        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                eq("loggedUser"),
                any(Utente.class)
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                null
        );

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_accountBloccato(
            @ForAll("email") String email,
            @ForAll("idUtente") Long idAdmin) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente admin = mock(Utente.class);
        Utente stessoUtente = mock(Utente.class);

        List<Prodotto> prodotti = Collections.emptyList();
        List<Marchio> marchi = Collections.emptyList();

        when(admin.getRuolo()).thenReturn(true);
        when(admin.getIdNome()).thenReturn(idAdmin);
        when(admin.getStatoAccount()).thenReturn("attivo");

        when(admin.getNome()).thenReturn("Mario");
        when(admin.getCognome()).thenReturn("Rossi");
        when(admin.getPassword()).thenReturn("password");

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(admin);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getMarchioDAO()).thenReturn(marchioDAO);

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("ruolo")).thenReturn("true");
        when(request.getParameter("stato")).thenReturn("bloccato");
        when(request.getParameter("id")).thenReturn(String.valueOf(idAdmin));

        when(utenteDAO.findEmail(email)).thenReturn(stessoUtente);
        when(stessoUtente.getIdNome()).thenReturn(idAdmin);

        when(utenteDAO.findSearch()).thenReturn(Collections.emptyList());

        when(prodottoDAO.findPromo()).thenReturn(prodotti);
        when(marchioDAO.findAll()).thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificautente(request, response);
        }

        verify(utenteDAO).update(any(Utente.class));

        verify(sessionUserDAO).create(
                eq(idAdmin),
                eq("password"),
                eq("Mario"),
                eq("Rossi"),
                eq(email),
                isNull(),
                eq(0.0),
                eq("bloccato"),
                eq(true)
        );

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(request).setAttribute("Prodotti", prodotti);
        verify(request).setAttribute("marchi", marchi);
        verify(request).setAttribute("promo", true);
        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(sessionUserDAO).delete(any(Utente.class));

        verify(ordineDAO, never()).findByUser(anyLong());

        verify(request).setAttribute(
                "ordini",
                Collections.emptyList()
        );

        verify(request).setAttribute(
                "utenteList",
                Collections.emptyList()
        );


        verify(request).setAttribute("loggedOn", false);
        verify(request).setAttribute("loggedUser", null);
        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_adminListaVuota(
            @ForAll("email") String email,
            @ForAll("idUtenteDiverso") Long idUser) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente admin = mock(Utente.class);

        final Long idAdmin = 1L;

        when(admin.getRuolo()).thenReturn(true);
        when(admin.getIdNome()).thenReturn(idAdmin);
        when(admin.getStatoAccount()).thenReturn("attivo");

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(admin);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);

        when(request.getParameter("email"))
                .thenReturn(email);

        when(request.getParameter("ruolo"))
                .thenReturn("true");

        when(request.getParameter("stato"))
                .thenReturn("attivo");

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idUser));

        when(utenteDAO.findEmail(email))
                .thenReturn(null);

        when(utenteDAO.findSearch())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificautente(request, response);
        }

        verify(utenteDAO).update(any(Utente.class));
        verify(utenteDAO).findEmail(email);
        verify(utenteDAO).findSearch();
        verify(ordineDAO, never()).findByUser(anyLong());

        verify(request).setAttribute(
                "ordini",
                Collections.emptyList()
        );

        verify(request).setAttribute(
                "utenteList",
                Collections.emptyList()
        );


        verify(request, times(2)).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneutente"
        );


        verify(daoFactory, never()).getProdottoDAO();
        verify(daoFactory, never()).getMarchioDAO();

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                admin
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                null
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_eccezionePrimaCreazioneFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezione);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificautente(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezione,
                    exception.getCause()
            );
        }
    }



    @Property
    void modificautente_eccezioneConEntrambiFactoryCreati(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenThrow(eccezione);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificautente(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezione,
                    exception.getCause()
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionFactory, never()).commitTransaction();
    }


    @Property
    void modificautente_erroreDuranteRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezionePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenThrow(eccezionePrincipale);

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificautente(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezionePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory).rollbackTransaction();

        verify(sessionFactory, never()).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificautente_erroreDuranteClose(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(Collections.emptyList());

        when(marchioDAO.findAll())
                .thenReturn(Collections.emptyList());

        doThrow(eccezione)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    AdminManagement.modificautente(
                            request,
                            response
                    )
            );
        }

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory, never()).closeTransaction();
    }


    @Test
    void gestioneordini_utenteNonLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestioneordini(request, response);
        }

        verify(request).setAttribute(
                eq("viewUrl"),
                eq("HomeManagement/login")
        );

        verify(daoFactory, never()).getOrdineDAO();
        verify(daoFactory, never()).getDettagliOrdineDAO();
        verify(daoFactory, never()).getProdottoDAO();
        verify(daoFactory, never()).getUtenteDAO();
        verify(daoFactory, never()).getCouponDAO();

        verify(request).setAttribute(
                eq("loggedOn"),
                eq(false)
        );

        verify(request).setAttribute(
                eq("loggedUser"),
                isNull()
        );

        verify(request).setAttribute(
                eq("applicationMessage"),
                isNull()
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void gestioneordini_utenteLoggatoListaOrdini(
            @ForAll("ordini") List<Ordine> ordini) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente utente = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(utente);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(ordineDAO.findAll())
                .thenReturn(ordini);

        for (Ordine ordine : ordini) {
            when(dettagliOrdineDAO.findByIdOrdine(
                    ordine.getIdOrdine()
            )).thenReturn(Collections.emptyList());
        }

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestioneordini(request, response);
        }

        verify(ordineDAO).findAll();

        for (Ordine ordine : ordini) {
            verify(dettagliOrdineDAO)
                    .findByIdOrdine(ordine.getIdOrdine());
        }

        verify(couponDAO, never())
                .findByCode(anyString());

        verify(prodottoDAO, never())
                .findById(anyLong());

        verify(utenteDAO, never())
                .findById(anyLong());

        verify(request).setAttribute(
                eq("viewUrl"),
                eq("AdminManagement/gestioneordini")
        );

        verify(request).setAttribute(
                eq("ordini"),
                eq(ordini)
        );

        verify(request).setAttribute(
                eq("loggedOn"),
                eq(true)
        );

        verify(request).setAttribute(
                eq("loggedUser"),
                eq(utente)
        );

        verify(request).setAttribute(
                eq("applicationMessage"),
                isNull()
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void gestioneordini_ordineConCoupon(
            @ForAll("prezzoUnitario") double prezzo,
            @ForAll("percentualeSconto") int sconto) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteOrdine = mock(Utente.class);

        Ordine ordine = mock(Ordine.class);
        DettagliOrdine dettaglio = mock(DettagliOrdine.class);

        Coupon couponOriginale = mock(Coupon.class);
        Coupon couponTrovato = mock(Coupon.class);

        Prodotto prodotto = mock(Prodotto.class);

        List<Ordine> ordini = new ArrayList<>();
        List<DettagliOrdine> dettagli = new ArrayList<>();

        ordini.add(ordine);
        dettagli.add(dettaglio);


        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);


        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);



        when(ordineDAO.findAll())
                .thenReturn(ordini);

        when(ordine.getIdOrdine())
                .thenReturn(100L);

        when(ordine.getUtente())
                .thenReturn(utenteOrdine);

        when(utenteOrdine.getIdNome())
                .thenReturn(5L);


        when(dettagliOrdineDAO.findByIdOrdine(100L))
                .thenReturn(dettagli);

        when(ordine.getDettagliOrdine())
                .thenReturn(dettagli);


        when(dettaglio.getCoupon())
                .thenReturn(couponOriginale);

        when(couponOriginale.getCodice())
                .thenReturn("SCONTO");

        when(couponDAO.findByCode("SCONTO"))
                .thenReturn(couponTrovato);

        when(couponTrovato.getSconto())
                .thenReturn(sconto);


        when(dettaglio.getPrezzoUnitario())
                .thenReturn(prezzo);


        when(dettaglio.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);


        when(utenteDAO.findById(5L))
                .thenReturn(utenteOrdine);


        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);



            AdminManagement.gestioneordini(request, response);
        }



        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(ordineDAO).findAll();

        verify(dettagliOrdineDAO)
                .findByIdOrdine(100L);

        verify(couponDAO)
                .findByCode("SCONTO");

        verify(prodottoDAO)
                .findById(50L);

        verify(utenteDAO)
                .findById(5L);


        double prezzoAtteso =
                prezzo - (prezzo * ((double) sconto / 100));

        verify(dettaglio)
                .setPrezzoUnitario(prezzoAtteso);


        verify(request).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneordini"
        );

        verify(request).setAttribute(
                "ordini",
                ordini
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void gestioneordini_ordineConCouponNonTrovato(
            @ForAll("codiceCoupon") String codiceCoupon) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteOrdine = mock(Utente.class);

        Ordine ordine = mock(Ordine.class);
        DettagliOrdine dettaglio = mock(DettagliOrdine.class);

        Coupon couponAssociato = mock(Coupon.class);
        Prodotto prodotto = mock(Prodotto.class);

        List<Ordine> ordini = new ArrayList<>();
        List<DettagliOrdine> dettagli = new ArrayList<>();

        ordini.add(ordine);
        dettagli.add(dettaglio);


        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);


        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);


        when(ordineDAO.findAll())
                .thenReturn(ordini);

        when(ordine.getIdOrdine())
                .thenReturn(100L);

        when(ordine.getUtente())
                .thenReturn(utenteOrdine);

        when(utenteOrdine.getIdNome())
                .thenReturn(5L);


        when(dettagliOrdineDAO.findByIdOrdine(100L))
                .thenReturn(dettagli);

        when(ordine.getDettagliOrdine())
                .thenReturn(dettagli);


        when(dettaglio.getCoupon())
                .thenReturn(couponAssociato);

        when(couponAssociato.getCodice())
                .thenReturn(codiceCoupon);

        when(couponDAO.findByCode(codiceCoupon))
                .thenReturn(null);


        when(dettaglio.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);


        when(utenteDAO.findById(5L))
                .thenReturn(utenteOrdine);


        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestioneordini(request, response);
        }


        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(ordineDAO).findAll();

        verify(dettagliOrdineDAO)
                .findByIdOrdine(100L);

        verify(couponDAO)
                .findByCode(codiceCoupon);

        verify(dettaglio, never())
                .setPrezzoUnitario(anyDouble());

        verify(prodottoDAO)
                .findById(50L);

        verify(utenteDAO)
                .findById(5L);


        verify(request).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneordini"
        );

        verify(request).setAttribute(
                "ordini",
                ordini
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void gestioneordini_eccezioneRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente utente = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(utente);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(ordineDAO.findAll())
                .thenThrow(eccezioneOriginale);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneordini(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionFactory, never())
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void gestioneordini_ordineConPiuDettagli(
            @ForAll("dettagliOrdineMultipli")
            List<DettagliOrdine> dettagli) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente utenteLoggato = mock(Utente.class);
        Utente utente = mock(Utente.class);

        Ordine ordine = mock(Ordine.class);

        List<Ordine> ordini = new ArrayList<>();
        ordini.add(ordine);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(utenteLoggato);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(ordineDAO.findAll())
                .thenReturn(ordini);

        when(ordine.getIdOrdine())
                .thenReturn(300L);

        when(ordine.getUtente())
                .thenReturn(utente);

        when(utente.getIdNome())
                .thenReturn(20L);

        when(dettagliOrdineDAO.findByIdOrdine(300L))
                .thenReturn(dettagli);

        when(ordine.getDettagliOrdine())
                .thenReturn(dettagli);

        for (DettagliOrdine dettaglio : dettagli) {

            Coupon coupon = dettaglio.getCoupon();
            Prodotto prodotto = dettaglio.getProdotto();

            String codiceCoupon = coupon.getCodice();
            int sconto = coupon.getSconto();
            double prezzo = dettaglio.getPrezzoUnitario();

            when(couponDAO.findByCode(codiceCoupon))
                    .thenReturn(coupon);

            when(prodottoDAO.findById(prodotto.getIdProdotto()))
                    .thenReturn(prodotto);
        }

        when(utenteDAO.findById(20L))
                .thenReturn(utente);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestioneordini(request, response);
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(daoFactory)
                .beginTransaction();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(ordineDAO)
                .findAll();

        verify(dettagliOrdineDAO)
                .findByIdOrdine(300L);

        for (DettagliOrdine dettaglio : dettagli) {

            Coupon coupon = dettaglio.getCoupon();
            Prodotto prodotto = dettaglio.getProdotto();

            String codiceCoupon = coupon.getCodice();
            int sconto = coupon.getSconto();
            double prezzo = dettaglio.getPrezzoUnitario();

            verify(couponDAO)
                    .findByCode(codiceCoupon);

            double prezzoAtteso =
                    prezzo - (prezzo * ((double) sconto / 100));

            verify(dettaglio)
                    .setPrezzoUnitario(prezzoAtteso);

            verify(prodottoDAO)
                    .findById(prodotto.getIdProdotto());

            verify(dettaglio)
                    .setProdotto(prodotto);
        }

        verify(utenteDAO, times(dettagli.size()))
                .findById(20L);

        verify(ordine, times(dettagli.size()))
                .setUtente(utente);

        verify(ordine)
                .setDettagliOrdine(dettagli);

        verify(request).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneordini"
        );

        verify(request).setAttribute(
                "ordini",
                ordini
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                utenteLoggato
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void gestioneordini_erroreCreazioneSessionFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneordini(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }
    }


    @Property
    void gestioneordini_erroreDuranteCloseSessionFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(ordineDAO.findAll())
                .thenReturn(Collections.emptyList());

        doThrow(eccezione)
                .when(sessionFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    AdminManagement.gestioneordini(
                            request,
                            response
                    )
            );
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(daoFactory)
                .beginTransaction();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(ordineDAO)
                .findAll();

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void gestioneordini_erroreDuranteRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezionePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(ordineDAO.findAll())
                .thenThrow(eccezionePrincipale);

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneordini(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezionePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory, never())
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificaordini_adminConCoupon(
            @ForAll("prezzoUnitario") double prezzo,
            @ForAll("percentualeSconto") int sconto) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);
        Utente utente = mock(Utente.class);

        Ordine ordine = mock(Ordine.class);
        DettagliOrdine dettaglio = mock(DettagliOrdine.class);

        Coupon coupon = mock(Coupon.class);
        Prodotto prodotto = mock(Prodotto.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(request.getParameter("stato"))
                .thenReturn("SPEDITO");

        when(request.getParameter("id"))
                .thenReturn("100");

        when(ordineDAO.findAll())
                .thenReturn(Collections.singletonList(ordine));

        when(ordine.getIdOrdine())
                .thenReturn(100L);

        when(ordine.getUtente())
                .thenReturn(utente);

        when(utente.getIdNome())
                .thenReturn(20L);

        List<DettagliOrdine> dettagli =
                Collections.singletonList(dettaglio);

        when(dettagliOrdineDAO.findByIdOrdine(100L))
                .thenReturn(dettagli);

        doAnswer(invocation -> {
            when(ordine.getDettagliOrdine())
                    .thenReturn(invocation.getArgument(0));
            return null;
        }).when(ordine).setDettagliOrdine(anyList());

        when(dettaglio.getCoupon())
                .thenReturn(coupon);

        when(coupon.getCodice())
                .thenReturn("SCONTO");

        when(couponDAO.findByCode("SCONTO"))
                .thenReturn(coupon);

        when(coupon.getSconto())
                .thenReturn(sconto);

        when(dettaglio.getPrezzoUnitario())
                .thenReturn(prezzo);

        when(dettaglio.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(1L);

        when(prodottoDAO.findById(1L))
                .thenReturn(prodotto);

        when(utenteDAO.findById(20L))
                .thenReturn(utente);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaordini(request, response);
        }


        double prezzoAtteso =
                prezzo - (prezzo * ((double) sconto / 100));

        verify(ordineDAO)
                .updateordine(100L, "SPEDITO");

        verify(ordineDAO)
                .findAll();

        verify(dettagliOrdineDAO)
                .findByIdOrdine(100L);

        verify(couponDAO)
                .findByCode("SCONTO");

        verify(dettaglio)
                .setPrezzoUnitario(eq(prezzoAtteso));

        verify(prodottoDAO)
                .findById(1L);

        verify(dettaglio)
                .setProdotto(prodotto);

        verify(utenteDAO)
                .findById(20L);

        verify(ordine)
                .setUtente(utente);

        verify(ordine)
                .setDettagliOrdine(dettagli);

        verify(request, times(2)).setAttribute(
                eq("viewUrl"),
                eq("AdminManagement/gestioneordini")
        );

        verify(request).setAttribute(
                eq("ordini"),
                eq(Collections.singletonList(ordine))
        );

        verify(request).setAttribute(
                eq("utenteList"),
                anyList()
        );

        verify(request, times(2)).setAttribute(
                eq("applicationMessage"),
                isNull()
        );

        verify(request).setAttribute(
                eq("loggedOn"),
                eq(true)
        );

        verify(request).setAttribute(
                eq("loggedUser"),
                eq(admin)
        );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificaordini_adminSenzaCoupon(
            @ForAll("codiceCoupon") String codiceCoupon) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);
        Utente utente = mock(Utente.class);

        Ordine ordine = mock(Ordine.class);
        DettagliOrdine dettaglio = mock(DettagliOrdine.class);

        Coupon coupon = mock(Coupon.class);
        Prodotto prodotto = mock(Prodotto.class);

        when(request.getParameter("stato"))
                .thenReturn("CONSEGNATO");
        when(request.getParameter("id"))
                .thenReturn("200");

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);
        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);
        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);
        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);
        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(ordineDAO.findAll())
                .thenReturn(Collections.singletonList(ordine));

        when(ordine.getIdOrdine())
                .thenReturn(200L);
        when(ordine.getUtente())
                .thenReturn(utente);
        when(utente.getIdNome())
                .thenReturn(30L);

        List<DettagliOrdine> dettagli =
                Collections.singletonList(dettaglio);

        when(dettagliOrdineDAO.findByIdOrdine(200L))
                .thenReturn(dettagli);

        doAnswer(invocation -> {
            when(ordine.getDettagliOrdine())
                    .thenReturn(invocation.getArgument(0));
            return null;
        }).when(ordine).setDettagliOrdine(anyList());

        when(dettaglio.getCoupon())
                .thenReturn(coupon);

        when(coupon.getCodice())
                .thenReturn(codiceCoupon);


        when(couponDAO.findByCode(codiceCoupon))
                .thenReturn(null);

        when(dettaglio.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(2L);

        when(prodottoDAO.findById(2L))
                .thenReturn(prodotto);

        when(utenteDAO.findById(30L))
                .thenReturn(utente);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaordini(request, response);
        }


        verify(ordineDAO)
                .updateordine(200L, "CONSEGNATO");

        verify(ordineDAO)
                .findAll();

        verify(dettagliOrdineDAO)
                .findByIdOrdine(200L);

        verify(couponDAO)
                .findByCode(codiceCoupon);


        verify(dettaglio, never())
                .setPrezzoUnitario(anyDouble());

        verify(prodottoDAO)
                .findById(2L);

        verify(dettaglio)
                .setProdotto(prodotto);


        verify(utenteDAO)
                .findById(30L);

        verify(ordine)
                .setUtente(utente);

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();

        verify(request, times(2)).setAttribute(
                eq("viewUrl"),
                eq("AdminManagement/gestioneordini")
        );

        verify(request).setAttribute(
                eq("ordini"),
                eq(Collections.singletonList(ordine))
        );

        verify(request).setAttribute(
                eq("utenteList"),
                anyList()
        );

        verify(request).setAttribute(
                eq("loggedOn"),
                eq(true)
        );

        verify(request).setAttribute(
                eq("loggedUser"),
                eq(admin)
        );

        verify(request, times(2)).setAttribute(
                eq("applicationMessage"),
                isNull()
        );
    }



    @Property
    void modificaordini_adminOrdiniVuoti(
            @ForAll("idOrdine") long idOrdine) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente admin = mock(Utente.class);

        when(request.getParameter("stato"))
                .thenReturn("SPEDITO");

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idOrdine));

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);
        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);
        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);
        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);
        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);


        when(ordineDAO.findAll())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaordini(request, response);
        }


        verify(ordineDAO)
                .updateordine(idOrdine, "SPEDITO");

        verify(ordineDAO)
                .findAll();

        verifyNoInteractions(dettagliOrdineDAO);
        verifyNoInteractions(couponDAO);
        verifyNoInteractions(prodottoDAO);
        verifyNoInteractions(utenteDAO);


        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificaordini_utenteNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);


        when(user.getRuolo())
                .thenReturn(false);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);
        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);
        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);
        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);
        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaordini(request, response);
        }

        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute("Prodotti", prodotti);

        verify(request)
                .setAttribute("marchi", marchi);

        verify(request)
                .setAttribute("promo", true);

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "HomeManagement/home"
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute("loggedOn", true);

        verify(request)
                .setAttribute("loggedUser", user);

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();

        verifyNoInteractions(ordineDAO);
        verifyNoInteractions(dettagliOrdineDAO);
        verifyNoInteractions(couponDAO);
        verifyNoInteractions(utenteDAO);
    }


    @Property
    void modificaordini_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);


        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);
        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);
        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);
        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);
        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaordini(request, response);
        }


        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute("Prodotti", prodotti);

        verify(request)
                .setAttribute("marchi", marchi);

        verify(request)
                .setAttribute("promo", true);

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "HomeManagement/home"
                );


        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute("loggedOn", false);

        verify(request)
                .setAttribute("loggedUser", null);

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();

        verifyNoInteractions(ordineDAO);
        verifyNoInteractions(dettagliOrdineDAO);
        verifyNoInteractions(couponDAO);
    }



    @Property
    void modificaordini_eccezioneRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenThrow(eccezioneOriginale);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaordini(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .closeTransaction();

        verifyNoInteractions(daoFactory);
    }



    @Property
    void modificaordini_eccezioneDopoCreazioneDaoFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaordini(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(daoFactory)
                .beginTransaction();

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }




    @Property
    void modificaordini_rollbackGeneraEccezione(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenThrow(eccezioneOriginale);

        doThrow(eccezioneRollback)
                .when(sessionFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaordini(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .closeTransaction();

        verifyNoInteractions(daoFactory);
    }



    @Property
    void modificaordini_closeGeneraEccezione(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(Collections.emptyList());

        when(marchioDAO.findAll())
                .thenReturn(Collections.emptyList());

        doThrow(eccezione)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(
                    () -> AdminManagement.modificaordini(
                            request,
                            response
                    )
            );
        }

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory, never())
                .closeTransaction();
    }



    @Property
    void modificaordini_erroreCreazioneSessionDAOFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaordini(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }
    }



    @Property
    void modificacoupon_adminCodiceEsistenteUguale(
            @ForAll("codiceCoupon") String codice) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);
        Coupon coupon = mock(Coupon.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenReturn("10");

        when(request.getParameter("codice"))
                .thenReturn(codice);

        when(request.getParameter("vecchiocodice"))
                .thenReturn(codice);


        when(couponDAO.findByCode(codice))
                .thenReturn(coupon);

        when(couponDAO.findAll())
                .thenReturn(Collections.singletonList(coupon));

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificacoupon(request, response);
        }

        verify(couponDAO)
                .findByCode(codice);


        verify(couponDAO)
                .ModificaCoupon(codice, codice, 10);

        verify(couponDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("coupon"),
                        eq(Collections.singletonList(coupon))
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("AdminManagement/gestionecoupon")
                );

        verify(request)
                .setAttribute(eq("loggedOn"), eq(true));

        verify(request)
                .setAttribute(eq("loggedUser"), eq(admin));

        verify(request)
                .setAttribute(eq("applicationMessage"), isNull());

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificacoupon_adminCodiceEsistenteDiverso(
            @ForAll("codiceCoupon") String nuovoCodice,
            @ForAll("codiceCoupon") String vecchioCodice) {

        Assume.that(!nuovoCodice.equals(vecchioCodice));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);
        Coupon coupon = mock(Coupon.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenReturn("20");

        when(request.getParameter("codice"))
                .thenReturn(nuovoCodice);

        when(request.getParameter("vecchiocodice"))
                .thenReturn(vecchioCodice);


        when(couponDAO.findByCode(nuovoCodice))
                .thenReturn(coupon);

        when(couponDAO.findAll())
                .thenReturn(Collections.singletonList(coupon));

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificacoupon(request, response);
        }


        verify(couponDAO, never())
                .ModificaCoupon(anyString(), anyString(), anyInt());

        verify(request, atLeastOnce())
                .setAttribute(
                        eq("applicationMessage"),
                        eq("Codice già in uso ad altro coupon!")
                );

        verify(couponDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("AdminManagement/gestionecoupon")
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificacoupon_adminCodiceNonEsistente(
            @ForAll("codiceCoupon") String nuovoCodice) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);

        String vecchioCodice = "VECCHIO10";

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenReturn("30");

        when(request.getParameter("codice"))
                .thenReturn(nuovoCodice);

        when(request.getParameter("vecchiocodice"))
                .thenReturn(vecchioCodice);


        when(couponDAO.findByCode(nuovoCodice))
                .thenReturn(null);

        when(couponDAO.findAll())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificacoupon(request, response);
        }


        verify(couponDAO)
                .findByCode(nuovoCodice);

        verify(couponDAO)
                .ModificaCoupon(
                        eq(vecchioCodice),
                        eq(nuovoCodice),
                        eq(30)
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        eq("Coupon " + vecchioCodice
                                + " modificato in " + nuovoCodice)
                );

        verify(couponDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("AdminManagement/gestionecoupon")
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }




    @Property
    void modificacoupon_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);


        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificacoupon(request, response);
        }


        verify(daoFactory)
                .getCouponDAO();


        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("Prodotti"),
                        eq(prodotti)
                );

        verify(request)
                .setAttribute(
                        eq("marchi"),
                        eq(marchi)
                );

        verify(request)
                .setAttribute(
                        eq("promo"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("HomeManagement/home")
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(false)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        isNull()
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        isNull()
                );

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificacoupon_utenteNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);

        when(user.getRuolo())
                .thenReturn(false);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificacoupon(request, response);
        }


        verify(user)
                .getRuolo();

        verify(daoFactory)
                .getCouponDAO();


        verify(couponDAO, never())
                .findByCode(anyString());

        verify(couponDAO, never())
                .ModificaCoupon(
                        anyString(),
                        anyString(),
                        anyInt()
                );

        verify(couponDAO, never())
                .findAll();


        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("Prodotti"),
                        eq(prodotti)
                );

        verify(request)
                .setAttribute(
                        eq("marchi"),
                        eq(marchi)
                );

        verify(request)
                .setAttribute(
                        eq("promo"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("HomeManagement/home")
                );

        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        eq(user)
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        isNull()
                );


        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }


    @Property
    void modificacoupon_eccezioneConRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente admin = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(request.getParameter("sconto"))
                .thenThrow(eccezione);

        doNothing()
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificacoupon(request, response)
            );

            assertSame(eccezione, exception.getCause());
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }


    @Property
    void modificacoupon_erroreCreazioneSessionDAOFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificacoupon(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }
    }


    @Property
    void modificacoupon_rollbackSessionFallisce(
            @ForAll("scontiNonNumerici") String scontoNonNumerico,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente admin = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(request.getParameter("sconto"))
                .thenReturn(scontoNonNumerico);

        doNothing()
                .when(daoFactory)
                .rollbackTransaction();

        doThrow(eccezioneRollback)
                .when(sessionFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificacoupon(request, response)
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }

    @Property
    void modificacoupon_closeGeneraEccezione(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        doThrow(eccezione)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(
                    () -> AdminManagement.modificacoupon(request, response)
            );
        }

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();
    }


    @Property
    void addCoupon_adminCodiceEsistente(
            @ForAll("codiceCoupon") String codice) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);
        Coupon couponEsistente = mock(Coupon.class);

        List<Coupon> couponList =
                Collections.singletonList(couponEsistente);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenReturn("10");

        when(request.getParameter("codice"))
                .thenReturn(codice);


        when(couponDAO.findByCode(codice))
                .thenReturn(couponEsistente);

        when(couponDAO.findAll())
                .thenReturn(couponList);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.addCoupon(request, response);
        }

        verify(admin).getRuolo();

        verify(request).getParameter("sconto");
        verify(request).getParameter("codice");


        verify(couponDAO)
                .findByCode(codice);

        verify(couponDAO, never())
                .creaCoupon(anyString(), anyInt());

        verify(couponDAO)
                .findAll();

        verify(request)
                .setAttribute(eq("coupon"), eq(couponList));

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("AdminManagement/gestionecoupon")
                );

        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        eq(admin)
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        eq("Codice già in uso ad altro coupon!")
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }




    @Property
    void addCoupon_adminCodiceNuovo(
            @ForAll("codiceCoupon") String codice,
            @ForAll("percentualeSconto") int sconto) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);

        List<Coupon> couponList =
                Collections.emptyList();

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenReturn(String.valueOf(sconto));

        when(request.getParameter("codice"))
                .thenReturn(codice);


        when(couponDAO.findByCode(codice))
                .thenReturn(null);

        when(couponDAO.findAll())
                .thenReturn(couponList);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.addCoupon(request, response);
        }

        verify(admin).getRuolo();

        verify(couponDAO)
                .findByCode(codice);


        verify(couponDAO)
                .creaCoupon(codice, sconto);

        verify(couponDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("coupon"),
                        eq(couponList)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("AdminManagement/gestionecoupon")
                );

        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        eq(admin)
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        eq("Nuovo coupon creato con successo!")
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void addCoupon_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);


        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenReturn("10");

        when(request.getParameter("codice"))
                .thenReturn("SCONTO10");

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.addCoupon(request, response);
        }


        verify(daoFactory)
                .getCouponDAO();

        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();


        verify(couponDAO, never())
                .findByCode(anyString());

        verify(couponDAO, never())
                .creaCoupon(anyString(), anyInt());


        verify(request)
                .setAttribute(
                        eq("Prodotti"),
                        eq(prodotti)
                );

        verify(request)
                .setAttribute(
                        eq("marchi"),
                        eq(marchi)
                );

        verify(request)
                .setAttribute(
                        eq("promo"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("HomeManagement/home")
                );

        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(false)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        isNull()
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        isNull()
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }


    @Property
    void addCoupon_utenteNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);

        when(user.getRuolo())
                .thenReturn(false);


        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenReturn("15");

        when(request.getParameter("codice"))
                .thenReturn("SCONTO15");

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.addCoupon(request, response);
        }


        verify(user)
                .getRuolo();


        verify(daoFactory)
                .getCouponDAO();


        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("Prodotti"),
                        eq(prodotti)
                );

        verify(request)
                .setAttribute(
                        eq("marchi"),
                        eq(marchi)
                );

        verify(request)
                .setAttribute(
                        eq("promo"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("HomeManagement/home")
                );

        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        eq(user)
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        isNull()
                );


        verify(couponDAO, never())
                .findByCode(anyString());

        verify(couponDAO, never())
                .creaCoupon(anyString(), anyInt());


        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }

    @Property
    void addCoupon_eccezionePrimaDiDaoFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        doThrow(eccezioneOriginale)
                .when(sessionFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            RuntimeException exception =
                    assertThrows(
                            RuntimeException.class,
                            () -> AdminManagement.addCoupon(request, response)
                    );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionFactory)
                .rollbackTransaction();
    }



    @Property
    void addCoupon_erroreCreazioneSessionDAOFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.addCoupon(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }
    }

    @Property
    void addCoupon_erroreDuranteCloseTransaction(
            @ForAll("eccezioniRuntime") RuntimeException eccezionePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneClose) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente admin = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(request.getParameter("sconto"))
                .thenThrow(eccezionePrincipale);

        doThrow(eccezioneClose)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.addCoupon(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezionePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory, never())
                .closeTransaction();
    }

    @Property
    void addCoupon_erroreDuranteRollbackSessionFactory(
            @ForAll("eccezioniRuntime") RuntimeException errorePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException erroreRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(errorePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(erroreRollback)
                .when(sessionFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.addCoupon(
                            request,
                            response
                    )
            );

            assertSame(
                    errorePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }

    @Property
    void gestioneprodotti_admin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("categorie") List<Categoria> categorie) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente admin = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        when(categoriaDAO.findAll())
                .thenReturn(categorie);

        when(prodottoDAO.cercaadmin(null, null, null))
                .thenReturn(prodotti);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestioneprodotti(request, response);
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(admin)
                .getRuolo();

        verify(daoFactory)
                .beginTransaction();

        verify(daoFactory)
                .getUtenteDAO();

        verify(daoFactory)
                .getOrdineDAO();

        verify(daoFactory)
                .getCategoriaDAO();

        verify(daoFactory)
                .getProdottoDAO();

        verify(daoFactory)
                .getMarchioDAO();

        verify(marchioDAO)
                .findAll();

        verify(categoriaDAO)
                .findAll();

        verify(prodottoDAO)
                .cercaadmin(null, null, null);

        verify(request)
                .setAttribute(
                        eq("prodotti"),
                        eq(prodotti)
                );

        verify(request)
                .setAttribute(
                        eq("marchi"),
                        eq(marchi)
                );

        verify(request)
                .setAttribute(
                        eq("categorie"),
                        eq(categorie)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("AdminManagement/gestioneprodotti")
                );


        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        eq(admin)
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        isNull()
                );

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void gestioneprodotti_utenteNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);

        when(user.getRuolo())
                .thenReturn(false);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestioneprodotti(request, response);
        }


        verify(user)
                .getRuolo();

        verify(daoFactory)
                .getProdottoDAO();

        verify(daoFactory)
                .getMarchioDAO();

        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("Prodotti"),
                        eq(prodotti)
                );

        verify(request)
                .setAttribute(
                        eq("marchi"),
                        eq(marchi)
                );

        verify(request)
                .setAttribute(
                        eq("promo"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("HomeManagement/home")
                );


        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        eq(user)
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        isNull()
                );


        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }


    @Property
    void gestioneprodotti_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestioneprodotti(request, response);
        }


        verify(sessionUserDAO)
                .findLoggedUser();


        verify(daoFactory)
                .getProdottoDAO();

        verify(daoFactory)
                .getMarchioDAO();

        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute(
                        eq("Prodotti"),
                        eq(prodotti)
                );

        verify(request)
                .setAttribute(
                        eq("marchi"),
                        eq(marchi)
                );

        verify(request)
                .setAttribute(
                        eq("promo"),
                        eq(true)
                );

        verify(request)
                .setAttribute(
                        eq("viewUrl"),
                        eq("HomeManagement/home")
                );


        verify(request)
                .setAttribute(
                        eq("loggedOn"),
                        eq(false)
                );

        verify(request)
                .setAttribute(
                        eq("loggedUser"),
                        isNull()
                );

        verify(request)
                .setAttribute(
                        eq("applicationMessage"),
                        isNull()
                );


        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }

    @Test
    void gestioneprodotti_eccezionePrimaDaoFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        doThrow(new RuntimeException("errore"))
                .when(sessionDAOFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneprodotti(
                            request,
                            response
                    )
            );
        }

        verify(sessionDAOFactory)
                .beginTransaction();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }


    @Property
    void gestioneprodotti_erroreRollbackSession(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .getUtenteDAO();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneprodotti(
                            request,
                            response
                    )
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .closeTransaction();

        verify(daoFactory)
                .closeTransaction();
    }

    @Property
    void gestioneprodotti_finallyCatchThrowable(
            @ForAll("eccezioniRuntime") RuntimeException eccezione) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);

        when(user.getRuolo())
                .thenReturn(false);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(Collections.emptyList());

        when(marchioDAO.findAll())
                .thenReturn(Collections.emptyList());

        doThrow(eccezione)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(
                    () -> AdminManagement.gestioneprodotti(
                            request,
                            response
                    )
            );
        }

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory, never())
                .closeTransaction();
    }

    @Property
    void gestioneprodotti_erroreDuranteRollback(
            @ForAll("eccezioniRuntime") RuntimeException errorePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException erroreRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(errorePrincipale)
                .when(daoFactory)
                .getUtenteDAO();

        doThrow(erroreRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneprodotti(
                            request,
                            response
                    )
            );

            assertSame(
                    errorePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }

    @Test
    void gestioneprodotti_sessionDAOFactoryNull() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(null);

            assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestioneprodotti(
                            request,
                            response
                    )
            );
        }
    }

    @Property
    void modificaprodotto_admin(
            @ForAll("nomeProdotto") String nomeProdotto,
            @ForAll("prezzoUnitario") double prezzo,
            @ForAll("quantitaProdotto") int quantita,
            @ForAll("prezzoScontato") double prezzoScontato,
            @ForAll("idProdotto") long idProdotto,
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("categorie") List<Categoria> categorie) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);

        Utente admin = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(request.getParameter("nomeprodotto"))
                .thenReturn(nomeProdotto);

        when(request.getParameter("prezzo"))
                .thenReturn(String.valueOf(prezzo));

        when(request.getParameter("quantita"))
                .thenReturn(String.valueOf(quantita));

        when(request.getParameter("promozione"))
                .thenReturn("true");

        when(request.getParameter("prezzo_scontato"))
                .thenReturn(String.valueOf(prezzoScontato));

        when(request.getParameter("stato"))
                .thenReturn("true");

        when(request.getParameter("idprodotto"))
                .thenReturn(String.valueOf(idProdotto));

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        when(categoriaDAO.findAll())
                .thenReturn(categorie);

        when(prodottoDAO.cercaadmin(null, null, null))
                .thenReturn(prodotti);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaprodotto(request, response);
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(admin)
                .getRuolo();

        verify(daoFactory)
                .beginTransaction();

        verify(daoFactory)
                .getProdottoDAO();


        ArgumentCaptor<Prodotto> captor =
                ArgumentCaptor.forClass(Prodotto.class);

        verify(prodottoDAO)
                .modifica(captor.capture());

        Prodotto prodotto = captor.getValue();

        assertEquals(
                nomeProdotto,
                prodotto.getNomeProdotto()
        );

        assertEquals(
                prezzo,
                prodotto.getPrezzo()
        );

        assertEquals(
                quantita,
                prodotto.getQuantitaDispo()
        );

        assertTrue(
                prodotto.getInPromo()
        );

        assertEquals(
                prezzoScontato,
                prodotto.getPrezzoSconto()
        );

        assertTrue(
                prodotto.isStatoprodotto()
        );

        assertEquals(
                idProdotto,
                prodotto.getIdProdotto()
        );

        verify(marchioDAO)
                .findAll();

        verify(categoriaDAO)
                .findAll();

        verify(prodottoDAO)
                .cercaadmin(null, null, null);

        verify(request)
                .setAttribute(
                        "prodotti",
                        prodotti
                );

        verify(request)
                .setAttribute(
                        "marchi",
                        marchi
                );

        verify(request)
                .setAttribute(
                        "categorie",
                        categorie
                );

        verify(request, times(2))
                .setAttribute(
                        "applicationMessage",
                        "Prodotto " + idProdotto
                                + " modificato con successo!"
                );

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "AdminManagement/gestioneprodotti"
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute(
                        "loggedOn",
                        true
                );

        verify(request)
                .setAttribute(
                        "loggedUser",
                        admin
                );

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificaprodotto_utenteNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);


        when(user.getRuolo())
                .thenReturn(false);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaprodotto(request, response);
        }


        verify(user).getRuolo();


        verify(daoFactory).getProdottoDAO();
        verify(daoFactory).getMarchioDAO();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(prodottoDAO, never())
                .modifica(any(Prodotto.class));


        verify(request).setAttribute(
                "Prodotti", prodotti);

        verify(request).setAttribute(
                "marchi", marchi);

        verify(request).setAttribute(
                "promo", true);

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home");


        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute(
                "loggedOn", true);

        verify(request).setAttribute(
                "loggedUser", user);

        verify(request).setAttribute(
                "applicationMessage", null);


        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificaprodotto_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.modificaprodotto(request, response);
        }

        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).getProdottoDAO();
        verify(daoFactory).getMarchioDAO();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(prodottoDAO, never())
                .modifica(any(Prodotto.class));

        verify(request).setAttribute(
                "Prodotti",
                prodotti);

        verify(request).setAttribute(
                "marchi",
                marchi);

        verify(request).setAttribute(
                "promo", true);

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home");

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute(
                "loggedOn", false);

        verify(request).setAttribute(
                "loggedUser", null);

        verify(request).setAttribute(
                "applicationMessage", null);

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Property
    void modificaprodotto_eccezionePrimaDaoFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        doThrow(eccezioneOriginale)
                .when(sessionFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaprodotto(
                            request,
                            response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }


    @Property
    void modificaprodotto_erroreConFactoryCreati(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .getProdottoDAO();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaprodotto(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionFactory, never())
                .commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void modificaprodotto_erroreDuranteRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezionePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .getProdottoDAO();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaprodotto(
                            request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .getProdottoDAO();

        verify(daoFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void modificaprodotto_erroreCreazioneSessionDAOFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.modificaprodotto(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }
    }



    @Property
    void modificaprodotto_erroreDuranteCloseSessionFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneClose) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(prodottoDAO.findPromo())
                .thenReturn(Collections.emptyList());

        when(marchioDAO.findAll())
                .thenReturn(Collections.emptyList());

        doThrow(eccezioneClose)
                .when(sessionFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    AdminManagement.modificaprodotto(
                            request, response
                    )
            );
        }

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void aggiungiprodotti_admin(
            @ForAll("nomeProdotto") String nomeProdotto,
            @ForAll("nomeProdotto") String descrizione,
            @ForAll("prezzoUnitario") double prezzo,
            @ForAll("quantitaProdotto") int quantita,
            @ForAll("nomeProdotto") String immagine,
            @ForAll("idProdotto") long idCategoria,
            @ForAll("prezzoScontato") double prezzoScontato,
            @ForAll("idProdotto") long idMarchio,
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("categorie") List<Categoria> categorie) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);

        Utente admin = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(admin);

        when(admin.getRuolo())
                .thenReturn(true);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(request.getParameter("nomeprodotto"))
                .thenReturn(nomeProdotto);

        when(request.getParameter("descrizione"))
                .thenReturn(descrizione);

        when(request.getParameter("prezzo"))
                .thenReturn(String.valueOf(prezzo));

        when(request.getParameter("quantita"))
                .thenReturn(String.valueOf(quantita));

        when(request.getParameter("immagine"))
                .thenReturn(immagine);

        when(request.getParameter("categoria"))
                .thenReturn(String.valueOf(idCategoria));

        when(request.getParameter("promozione"))
                .thenReturn("true");

        when(request.getParameter("prezzo_scontato"))
                .thenReturn(String.valueOf(prezzoScontato));

        when(request.getParameter("stato"))
                .thenReturn("true");

        when(request.getParameter("marchio"))
                .thenReturn(String.valueOf(idMarchio));

        when(prodottoDAO.cercaadmin(null, null, null))
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        when(categoriaDAO.findAll())
                .thenReturn(categorie);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.aggiungiprodotti(request, response);
        }

        ArgumentCaptor<Prodotto> captor =
                ArgumentCaptor.forClass(Prodotto.class);

        verify(prodottoDAO).create(captor.capture());

        Prodotto prodotto = captor.getValue();

        assertEquals(nomeProdotto, prodotto.getNomeProdotto());
        assertEquals(descrizione, prodotto.getDescrizione());
        assertEquals(prezzo, prodotto.getPrezzo());
        assertEquals(quantita, prodotto.getQuantitaDispo());
        assertEquals(immagine, prodotto.getImmagine());

        assertEquals(
                idCategoria,
                prodotto.getCategoria().getIdCategoria()
        );

        assertTrue(prodotto.getInPromo());

        assertEquals(
                prezzoScontato,
                prodotto.getPrezzoSconto()
        );

        assertTrue(prodotto.isStatoprodotto());

        assertEquals(
                idMarchio,
                prodotto.getMarchio().getIdMarchio()
        );

        verify(prodottoDAO).cercaadmin(null, null, null);
        verify(marchioDAO).findAll();
        verify(categoriaDAO).findAll();

        verify(request).setAttribute(
                "prodotti",
                prodotti
        );

        verify(request).setAttribute(
                "marchi",
                marchi
        );

        verify(request).setAttribute(
                "categorie",
                categorie
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Prodotto aggiunto con successo!"
        );

        verify(request).setAttribute(
                "viewUrl",
                "AdminManagement/gestioneprodotti"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                admin
        );

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Property
    void aggiungiprodotti_utenteNonAdmin(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);

        when(user.getRuolo())
                .thenReturn(false);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        // Il metodo fa il parsing prima del controllo del ruolo
        when(request.getParameter("nomeprodotto"))
                .thenReturn("Crema viso");
        when(request.getParameter("descrizione"))
                .thenReturn("Crema idratante");
        when(request.getParameter("prezzo"))
                .thenReturn("25.50");
        when(request.getParameter("quantita"))
                .thenReturn("10");
        when(request.getParameter("immagine"))
                .thenReturn("crema.jpg");
        when(request.getParameter("categoria"))
                .thenReturn("3");
        when(request.getParameter("promozione"))
                .thenReturn("true");
        when(request.getParameter("prezzo_scontato"))
                .thenReturn("20.00");
        when(request.getParameter("stato"))
                .thenReturn("true");
        when(request.getParameter("marchio"))
                .thenReturn("5");

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.aggiungiprodotti(request, response);
        }

        verify(prodottoDAO, never())
                .create(any(Prodotto.class));

        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute("Prodotti", prodotti);

        verify(request)
                .setAttribute("marchi", marchi);

        verify(request)
                .setAttribute("promo", true);

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "HomeManagement/home"
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute("loggedOn", true);

        verify(request)
                .setAttribute("loggedUser", user);

        verify(request)
                .setAttribute("applicationMessage", null);

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }


    @Property
    void aggiungiprodotti_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(request.getParameter("nomeprodotto"))
                .thenReturn("Crema viso");
        when(request.getParameter("descrizione"))
                .thenReturn("Crema idratante");
        when(request.getParameter("prezzo"))
                .thenReturn("25.50");
        when(request.getParameter("quantita"))
                .thenReturn("10");
        when(request.getParameter("immagine"))
                .thenReturn("crema.jpg");
        when(request.getParameter("categoria"))
                .thenReturn("3");
        when(request.getParameter("promozione"))
                .thenReturn("true");
        when(request.getParameter("prezzo_scontato"))
                .thenReturn("20.00");
        when(request.getParameter("stato"))
                .thenReturn("true");
        when(request.getParameter("marchio"))
                .thenReturn("5");

        when(prodottoDAO.findPromo())
                .thenReturn(prodotti);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.aggiungiprodotti(request, response);
        }

        verify(prodottoDAO, never())
                .create(any(Prodotto.class));

        verify(prodottoDAO)
                .findPromo();

        verify(marchioDAO)
                .findAll();

        verify(request)
                .setAttribute("Prodotti", prodotti);

        verify(request)
                .setAttribute("marchi", marchi);

        verify(request)
                .setAttribute("promo", true);

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "HomeManagement/home"
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute("loggedOn", false);

        verify(request)
                .setAttribute("loggedUser", null);

        verify(request)
                .setAttribute("applicationMessage", null);

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void aggiungiprodotti_erroreDuranteClose(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneClose) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);

        when(user.getRuolo())
                .thenReturn(false);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(request.getParameter("nomeprodotto"))
                .thenReturn("Crema viso");
        when(request.getParameter("descrizione"))
                .thenReturn("Crema idratante");
        when(request.getParameter("prezzo"))
                .thenReturn("25.50");
        when(request.getParameter("quantita"))
                .thenReturn("10");
        when(request.getParameter("immagine"))
                .thenReturn("crema.jpg");
        when(request.getParameter("categoria"))
                .thenReturn("3");
        when(request.getParameter("promozione"))
                .thenReturn("true");
        when(request.getParameter("prezzo_scontato"))
                .thenReturn("20.00");
        when(request.getParameter("stato"))
                .thenReturn("true");
        when(request.getParameter("marchio"))
                .thenReturn("5");

        when(prodottoDAO.findPromo())
                .thenReturn(Collections.emptyList());

        when(marchioDAO.findAll())
                .thenReturn(Collections.emptyList());

        doThrow(eccezioneClose)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(
                    () -> AdminManagement.aggiungiprodotti(
                            request, response)
            );
        }

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory, never())
                .closeTransaction();
    }



    @Property
    void aggiungiprodotti_eccezioneRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .getProdottoDAO();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.aggiungiprodotti(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .rollbackTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionFactory, never())
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }


    @Test
    void aggiungiprodotti_sessionDAOFactoryNull() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(null);

            assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.aggiungiprodotti(
                            request, response)
            );
        }
    }



    @Property
    void aggiungiprodotti_erroreDuranteRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezionePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .getProdottoDAO();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.aggiungiprodotti(
                            request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory)
                .getProdottoDAO();

        verify(daoFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void gestionecoupon_utenteLoggato(
            @ForAll("coupons") List<Coupon> coupons) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente user = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(user);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(couponDAO.findAll())
                .thenReturn(coupons);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestionecoupon(request, response);
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionFactory)
                .getUtenteDAO();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(daoFactory)
                .beginTransaction();

        verify(daoFactory)
                .getCouponDAO();

        verify(couponDAO)
                .findAll();

        verify(request)
                .setAttribute("coupon", coupons);

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "AdminManagement/gestionecoupon"
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute("loggedOn", true);

        verify(request)
                .setAttribute("loggedUser", user);

        verify(request)
                .setAttribute(
                        "applicationMessage",
                        null
                );

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Test
    void gestionecoupon_utenteNonLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            AdminManagement.gestionecoupon(request, response);
        }

        verify(sessionFactory)
                .beginTransaction();

        verify(sessionFactory)
                .getUtenteDAO();

        verify(sessionUserDAO)
                .findLoggedUser();


        verify(daoFactory, never())
                .getCouponDAO();

        verify(daoFactory)
                .beginTransaction();

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "HomeManagement/login"
                );

        verify(daoFactory)
                .commitTransaction();

        verify(sessionFactory)
                .commitTransaction();

        verify(request)
                .setAttribute(
                        "loggedOn",
                        false
                );

        verify(request)
                .setAttribute(
                        "loggedUser",
                        null
                );

        verify(request)
                .setAttribute(
                        "applicationMessage",
                        null
                );

        verify(daoFactory)
                .closeTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }



    @Property
    void gestionecoupon_eccezionePrimaDaoFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        doThrow(eccezioneOriginale)
                .when(sessionFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestionecoupon(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(sessionFactory).rollbackTransaction();
        verify(sessionFactory).closeTransaction();
    }

    @Property
    void gestionecoupon_erroreConFactoryCreati(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestionecoupon(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(daoFactory).beginTransaction();

        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionFactory, never())
                .commitTransaction();
    }

    @Property
    void gestionecoupon_erroreDuranteRollback(
            @ForAll("eccezioniRuntime") RuntimeException eccezionePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestionecoupon(
                            request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    exception.getCause()
            );
        }

        verify(daoFactory).beginTransaction();
        verify(daoFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }

    @Property
    void gestionecoupon_erroreCreazioneSessionDAOFactory(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> AdminManagement.gestionecoupon(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }
    }

    @Property
    void gestionecoupon_erroreDuranteClose(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneClose) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(eccezioneClose)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(
                    () -> AdminManagement.gestionecoupon(
                            request, response)
            );
        }

        verify(daoFactory).closeTransaction();

        verify(sessionFactory, never())
                .closeTransaction();
    }

    @Test
    void adminManagement_costruttore() {
        AdminManagement adminManagement = new AdminManagement();

        assertNotNull(adminManagement);
    }


}