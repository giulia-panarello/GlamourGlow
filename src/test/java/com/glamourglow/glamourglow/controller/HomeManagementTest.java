package com.glamourglow.glamourglow.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.glamourglow.glamourglow.services.config.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.times;
import java.util.ArrayList;
import java.util.List;


public class HomeManagementTest {


    @Provide
    Arbitrary<Boolean> statoLogin() {
        return Arbitraries.of(true, false);
    }

    @Provide
    Arbitrary<String> messaggiErrore() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(50);
    }

    @Provide
    Arbitrary<String> password() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> email() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> nome + "@test.it");
    }

    @Provide
    Arbitrary<String> nomeUtente() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> cognomeUtente() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<Long> idUtente() {
        return Arbitraries.longs().between(1L, 1000L);
    }


    @Provide
    Arbitrary<Double> wallet() {
        return Arbitraries.doubles()
                .between(0.0, 10000.0);
    }

    @Provide
    Arbitrary<String> telefono() {
        return Arbitraries.strings()
                .numeric()
                .ofMinLength(6)
                .ofMaxLength(15);
    }

    @Provide
    Arbitrary<String> nomeProdotto() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> nomeMarchio() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> nomeCategoria() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<Boolean> promo() {
        return Arbitraries.of(true, false);
    }

    @Provide
    Arbitrary<Long> idCategoria() {
        return Arbitraries.longs()
                .between(1L, 1000L);
    }

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

    @Property
    void viewlogin_statoLogin_impostaAttributiCorretti(
            @ForAll("statoLogin") boolean utenteLoggato) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        Utente loggedUser = utenteLoggato
                ? mock(Utente.class)
                : null;

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewlogin(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionDAOFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", utenteLoggato);
        verify(request).setAttribute("loggedUser", loggedUser);
        verify(request).setAttribute("applicationMessage", null);
        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/login"
        );

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }
    @Property
    void viewlogin_eccezione_rollbackEntrambeLeFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

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
            ).thenReturn(sessionDAOFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewlogin(request, response)
            );

            assertSame(eccezioneOriginale, ex.getCause());
            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionDAOFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionDAOFactory, never()).commitTransaction();
    }

    @Property
    void viewlogin_eccezione_findLoggedUser_rollbackSessione(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        when(sessionUserDAO.findLoggedUser())
                .thenThrow(eccezioneOriginale);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewlogin(request, response)
            );

            assertSame(eccezioneOriginale, ex.getCause());
            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionUserDAO).findLoggedUser();
        verify(sessionDAOFactory).rollbackTransaction();

        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void viewlogin_eccezione_creazioneSessionDAOFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewlogin(request, response)
            );

            assertSame(eccezioneOriginale, ex.getCause());
            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

    }

    @Property
    void viewlogin_eccezione_durante_rollback(
            @ForAll("messaggiErrore") String messaggioErrore,
            @ForAll("messaggiErrore") String messaggioRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrore);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioRollback);

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
            ).thenReturn(sessionDAOFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewlogin(request, response)
            );


            assertSame(
                    eccezionePrincipale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();

        verify(
                sessionDAOFactory,
                never()
        ).rollbackTransaction();


        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();


        verify(daoFactory, never()).commitTransaction();
        verify(sessionDAOFactory, never()).commitTransaction();
    }

    @Property
    void viewlogin_eccezione_durante_close(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneClose =
                new RuntimeException(messaggioErrore);

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
            ).thenReturn(sessionDAOFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewlogin(request, response)
            );
        }

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();

        verify(sessionDAOFactory, never())
                .closeTransaction();
    }

    @Property
    void login_utenteGiaLoggato(
            @ForAll("email") String email,
            @ForAll("password") String password) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Password"))
                .thenReturn(password);

        try (MockedStatic<DAOFactory> mocked =
                     Mockito.mockStatic(DAOFactory.class)) {

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mocked.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.login(request, response)
            );
        }

        verify(sessionUserDAO).findLoggedUser();

        verify(utenteDAO, never()).findEmail(any());

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
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
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }


    @Property
    void login_passwordErrata(
            @ForAll("email") String email,
            @ForAll("password") String passwordInserita,
            @ForAll("password") String passwordCorretta) {

        Assume.that(!passwordInserita.equals(passwordCorretta));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente utente = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Password"))
                .thenReturn(passwordInserita);

        when(utenteDAO.findEmail(email))
                .thenReturn(utente);

        when(utente.getEmail())
                .thenReturn(email);

        when(utente.getPassword())
                .thenReturn(passwordCorretta);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.login(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionDAOFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();
        verify(daoFactory).getUtenteDAO();

        verify(utenteDAO).findEmail(email);

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/login"
        );

        verify(request).setAttribute(
                "applicationMessage",
                "Email o password errati!"
        );

        verify(request).setAttribute(
                "email",
                email
        );

        verify(request).setAttribute(
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }



    @Property
    void login_utenteValido_accountAttivo(
            @ForAll("email") String email,
            @ForAll("password") String password,
            @ForAll("idUtente") long idUtente,
            @ForAll("wallet") double wallet,
            @ForAll("nomeUtente") String nome,
            @ForAll("cognomeUtente") String cognome) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente utente = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Password"))
                .thenReturn(password);

        when(utenteDAO.findEmail(email))
                .thenReturn(utente);

        when(utente.getEmail())
                .thenReturn(email);

        when(utente.getPassword())
                .thenReturn(password);

        when(utente.getStatoAccount())
                .thenReturn("attivo");

        when(utente.getIdNome())
                .thenReturn(idUtente);

        when(utente.getNome())
                .thenReturn(nome);

        when(utente.getCognome())
                .thenReturn(cognome);

        when(utente.getWallet())
                .thenReturn(wallet);

        when(utente.getRuolo())
                .thenReturn(false);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            mockedHomeManagement.when(() ->
                    HomeManagement.login(request, response)
            ).thenCallRealMethod();

            assertDoesNotThrow(() ->
                    HomeManagement.login(request, response)
            );

            mockedHomeManagement.verify(() ->
                    HomeManagement.viewhome(request, response)
            );
        }

        verify(utenteDAO).findEmail(email);

        verify(sessionUserDAO).create(
                idUtente,
                password,
                nome,
                cognome,
                email,
                null,
                wallet,
                "attivo",
                false
        );

        verify(sessionDAOFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                utente
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void login_accountBloccato(
            @ForAll("email") String email,
            @ForAll("password") String password) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente utente = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Password"))
                .thenReturn(password);

        when(utenteDAO.findEmail(email))
                .thenReturn(utente);

        when(utente.getEmail())
                .thenReturn(email);

        when(utente.getPassword())
                .thenReturn(password);

        when(utente.getStatoAccount())
                .thenReturn("bloccato");

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedHomeManagement.when(() ->
                    HomeManagement.login(request, response)
            ).thenCallRealMethod();

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.login(request, response)
            );
        }

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Impossibile accedere: l'account è stato bloccato!"
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/login"
        );

        verify(request).setAttribute(
                "email",
                email
        );

        verify(request).setAttribute(
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(sessionUserDAO, never()).create(
                anyLong(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                isNull(),
                anyDouble(),
                anyString(),
                anyBoolean()
        );

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }


    @Property
    void login_utenteNonEsistente(
            @ForAll("email") String email,
            @ForAll("password") String password) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Password"))
                .thenReturn(password);

        when(utenteDAO.findEmail(email))
                .thenReturn(null);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedHomeManagement.when(() ->
                    HomeManagement.login(request, response)
            ).thenCallRealMethod();

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.login(request, response)
            );
        }

        verify(request).setAttribute(
                "applicationMessage",
                "Email o password errati!"
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/login"
        );

        verify(request).setAttribute(
                "email",
                email
        );

        verify(request).setAttribute(
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void login_emailDiversa(
            @ForAll("email") String emailInserita,
            @ForAll("email") String emailUtente) {

        Assume.that(!emailInserita.equals(emailUtente));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente utente = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(request.getParameter("Email"))
                .thenReturn(emailInserita);

        when(request.getParameter("Password"))
                .thenReturn("password");

        when(utenteDAO.findEmail(emailInserita))
                .thenReturn(utente);

        when(utente.getEmail())
                .thenReturn(emailUtente);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedHomeManagement.when(() ->
                    HomeManagement.login(request, response)
            ).thenCallRealMethod();

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.login(request, response)
            );
        }

        verify(request).setAttribute(
                "applicationMessage",
                "Email o password errati!"
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/login"
        );

        verify(request).setAttribute(
                "email",
                emailInserita
        );

        verify(request).setAttribute(
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(sessionUserDAO, never()).create(
                anyLong(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                isNull(),
                anyDouble(),
                anyString(),
                anyBoolean()
        );

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void login_eccezioneDuranteBeginTransaction(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(sessionDAOFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedHomeManagement.when(() ->
                    HomeManagement.login(request, response)
            ).thenCallRealMethod();

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.login(request, response)
            );

            assertSame(eccezioneOriginale, exception.getCause());
            assertEquals(
                    messaggioErrore,
                    exception.getCause().getMessage()
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionDAOFactory).rollbackTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void login_eccezione_daoFactoryRollback(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.login(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    exception.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionDAOFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionDAOFactory, never()).commitTransaction();
    }

    @Property
    void login_eccezioneDuranteRollback(
            @ForAll("messaggiErrore") String messaggioErrorePrincipale,
            @ForAll("messaggiErrore") String messaggioErroreRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrorePrincipale);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioErroreRollback);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.login(request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    exception.getCause()
            );

            assertEquals(
                    messaggioErrorePrincipale,
                    exception.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();


        verify(sessionDAOFactory, never())
                .rollbackTransaction();


        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void login_eccezioneDuranteClose(
            @ForAll("email") String email,
            @ForAll("password") String password) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Password"))
                .thenReturn(password);

        when(utenteDAO.findEmail(email))
                .thenReturn(null);

        RuntimeException eccezioneClose =
                new RuntimeException("errore close");

        doThrow(eccezioneClose)
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedHomeManagement.when(() ->
                    HomeManagement.login(request, response)
            ).thenCallRealMethod();

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.login(request, response)
            );
        }

        verify(sessionUserDAO).findLoggedUser();

        verify(utenteDAO).findEmail(email);

        verify(request).setAttribute(
                "applicationMessage",
                "Email o password errati!"
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/login"
        );

        verify(request).setAttribute(
                "email",
                email
        );

        verify(request).setAttribute(
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();

        verify(sessionDAOFactory, never())
                .closeTransaction();
    }

    @Property
    void login_eccezione_creazioneSessionDAOFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedHomeManagement.when(() ->
                    HomeManagement.login(request, response)
            ).thenCallRealMethod();

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.login(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }
    }

    @Property
    void viewregistration_statoLogin(
            @ForAll("statoLogin") boolean utenteLoggato) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        Utente loggedUser = utenteLoggato
                ? mock(Utente.class)
                : null;

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewregistration(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionDAOFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(request).setAttribute(
                "loggedOn",
                utenteLoggato
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/registration"
        );

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void viewregistration_eccezioneCreazioneSessionDAOFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewregistration(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }
    }

    @Property
    void viewregistration_eccezioneBeginSessionFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(sessionDAOFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewregistration(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
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
    void viewregistration_eccezioneDaoFactoryBegin(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewregistration(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionDAOFactory, never())
                .commitTransaction();
    }

    @Property
    void viewregistration_eccezioneDuranteRollback(
            @ForAll("messaggiErrore") String messaggioErrorePrincipale,
            @ForAll("messaggiErrore") String messaggioErroreRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrorePrincipale);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioErroreRollback);


        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewregistration(request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrorePrincipale,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory, never())
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }

    @Property
    void viewregistration_eccezioneDuranteClose(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneClose =
                new RuntimeException(messaggioErrore);

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
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewregistration(request, response)
            );
        }

        verify(daoFactory).beginTransaction();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();

        verify(sessionDAOFactory, never())
                .closeTransaction();
    }


    @Test
    void registration_utenteGiaLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.registration(request, response)
            );
        }

        verify(sessionUserDAO).findLoggedUser();

        verify(utenteDAO, never()).findEmail(anyString());

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
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
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }



    @Property
    void registration_nuovoUtente(
            @ForAll("nomeUtente") String nome,
            @ForAll("cognomeUtente") String cognome,
            @ForAll("email") String email,
            @ForAll("telefono") String telefono,
            @ForAll("password") String password) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente nuovoUtente = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        // Input reali della registration()
        when(request.getParameter("Nome"))
                .thenReturn(nome);

        when(request.getParameter("Cognome"))
                .thenReturn(cognome);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Telefono"))
                .thenReturn(telefono);

        when(request.getParameter("Password"))
                .thenReturn(password);

        when(utenteDAO.findEmail(email))
                .thenReturn(null);

        when(utenteDAO.create(
                0,
                password,
                nome,
                cognome,
                email,
                telefono,
                null,
                null,
                false
        )).thenReturn(nuovoUtente);

        when(nuovoUtente.getIdNome())
                .thenReturn(1L);

        when(nuovoUtente.getPassword())
                .thenReturn(password);

        when(nuovoUtente.getNome())
                .thenReturn(nome);

        when(nuovoUtente.getCognome())
                .thenReturn(cognome);

        when(nuovoUtente.getEmail())
                .thenReturn(email);

        when(nuovoUtente.getTelefono())
                .thenReturn(telefono);

        when(nuovoUtente.getWallet())
                .thenReturn(null);

        when(nuovoUtente.getStatoAccount())
                .thenReturn(null);

        when(nuovoUtente.getRuolo())
                .thenReturn(false);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class);

             MockedStatic<HomeManagement> mockedHomeManagement =
                     Mockito.mockStatic(HomeManagement.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            mockedHomeManagement.when(() ->
                    HomeManagement.viewhome(request, response)
            ).thenAnswer(invocation -> null);

            mockedHomeManagement.when(() ->
                    HomeManagement.registration(request, response)
            ).thenCallRealMethod();

            assertDoesNotThrow(() ->
                    HomeManagement.registration(request, response)
            );

            mockedHomeManagement.verify(() ->
                    HomeManagement.viewhome(request, response)
            );
        }

        verify(utenteDAO).findEmail(email);

        verify(utenteDAO).create(
                0,
                password,
                nome,
                cognome,
                email,
                telefono,
                null,
                null,
                false
        );

        verify(sessionUserDAO).create(
                1L,
                password,
                nome,
                cognome,
                email,
                telefono,
                null,
                null,
                false
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                nuovoUtente
        );

        verify(request).setAttribute(
                "applicationMessage",
                null
        );

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void registration_emailGiaEsistente(
            @ForAll("nomeUtente") String nome,
            @ForAll("cognomeUtente") String cognome,
            @ForAll("email") String email,
            @ForAll("telefono") String telefono,
            @ForAll("password") String password) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente utenteEsistente = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Nome"))
                .thenReturn(nome);

        when(request.getParameter("Cognome"))
                .thenReturn(cognome);

        when(request.getParameter("Email"))
                .thenReturn(email);

        when(request.getParameter("Telefono"))
                .thenReturn(telefono);

        when(request.getParameter("Password"))
                .thenReturn(password);

        when(utenteDAO.findEmail(email))
                .thenReturn(utenteEsistente);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.registration(request, response)
            );
        }

        verify(sessionUserDAO).findLoggedUser();

        verify(utenteDAO).findEmail(email);

        verify(utenteDAO, never()).create(
                anyLong(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                any(),
                any(),
                any(),
                anyBoolean()
        );

        verify(sessionUserDAO, never()).create(
                anyLong(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                any(),
                any(),
                any(),
                anyBoolean()
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/registration"
        );

        verify(request).setAttribute(
                "nome",
                nome
        );

        verify(request).setAttribute(
                "cognome",
                cognome
        );

        verify(request).setAttribute(
                "telefono",
                telefono
        );

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
                "Utente già esistente!"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void registration_eccezioneCreazioneSessionDAOFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.registration(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }
    }

    @Property
    void registration_eccezioneBeginSessionFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(sessionDAOFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.registration(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
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
    void registration_eccezioneBeginDaoFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.registration(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionDAOFactory, never())
                .commitTransaction();
    }

    @Property
    void registration_eccezioneDuranteFindEmail(
            @ForAll("email") String email,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Email"))
                .thenReturn(email);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        when(utenteDAO.findEmail(email))
                .thenThrow(eccezioneOriginale);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.registration(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(utenteDAO).findEmail(email);

        verify(daoFactory).rollbackTransaction();
        verify(sessionDAOFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionDAOFactory, never()).commitTransaction();
    }


    @Property
    void registration_eccezioneDuranteRollbackDaoFactory(
            @ForAll("messaggiErrore") String messaggioErrore,
            @ForAll("messaggiErrore") String messaggioRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrore);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioRollback);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.registration(request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory, never())
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }


    @Property
    void registration_eccezioneDuranteRollbackSessionFactory(
            @ForAll("messaggiErrore") String messaggioErrore,
            @ForAll("messaggiErrore") String messaggioRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrore);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioRollback);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(sessionDAOFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.registration(request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }

    @Property
    void registration_eccezioneDuranteCloseDaoFactory(
            @ForAll("nomeUtente") String nome,
            @ForAll("cognomeUtente") String cognome,
            @ForAll("email") String email,
            @ForAll("telefono") String telefono,
            @ForAll("password") String password,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Nome"))
                .thenReturn(nome);
        when(request.getParameter("Cognome"))
                .thenReturn(cognome);
        when(request.getParameter("Email"))
                .thenReturn(email);
        when(request.getParameter("Telefono"))
                .thenReturn(telefono);
        when(request.getParameter("Password"))
                .thenReturn(password);

        // Evita il ramo create()
        when(utenteDAO.findEmail(email))
                .thenReturn(mock(Utente.class));

        RuntimeException eccezioneClose =
                new RuntimeException(messaggioErrore);

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
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.registration(request, response)
            );
        }

        verify(daoFactory).beginTransaction();
        verify(sessionDAOFactory).beginTransaction();
        verify(utenteDAO).findEmail(email);
        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory, never())
                .closeTransaction();
    }


    @Property
    void registration_eccezioneDuranteCloseSessionFactory(
            @ForAll("nomeUtente") String nome,
            @ForAll("cognomeUtente") String cognome,
            @ForAll("email") String email,
            @ForAll("telefono") String telefono,
            @ForAll("password") String password,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(request.getParameter("Nome"))
                .thenReturn(nome);
        when(request.getParameter("Cognome"))
                .thenReturn(cognome);
        when(request.getParameter("Email"))
                .thenReturn(email);
        when(request.getParameter("Telefono"))
                .thenReturn(telefono);
        when(request.getParameter("Password"))
                .thenReturn(password);

        when(utenteDAO.findEmail(email))
                .thenReturn(mock(Utente.class));

        RuntimeException eccezioneClose =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneClose)
                .when(sessionDAOFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.registration(request, response)
            );
        }

        verify(daoFactory).beginTransaction();
        verify(sessionDAOFactory).beginTransaction();

        verify(utenteDAO).findEmail(email);

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }


    @Property
    void cerca_utenteLoggato_prodottiPresenti(
            @ForAll("nomeProdotto") String nomeProdotto,
            @ForAll("nomeMarchio") String nomeMarchio,
            @ForAll("nomeCategoria") String nomeCategoria,
            @ForAll("promo") boolean promo,
            @ForAll("idCategoria") long idCategoria) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = mock(Utente.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Prodotto prodotto = mock(Prodotto.class);
        Categoria categoriaProdotto = mock(Categoria.class);
        Categoria categoriaCompleta = mock(Categoria.class);
        Marchio marchio = mock(Marchio.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(request.getParameter("Nomeprodotto"))
                .thenReturn(nomeProdotto);

        when(request.getParameter("Marchioprodotto"))
                .thenReturn(nomeMarchio);

        when(request.getParameter("Categoria"))
                .thenReturn(nomeCategoria);

        when(request.getParameter("Promo"))
                .thenReturn(String.valueOf(promo));

        List<Prodotto> prodotti = new ArrayList<>();
        prodotti.add(prodotto);

        when(prodottoDAO.cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        )).thenReturn(prodotti);

        when(prodotto.getCategoria())
                .thenReturn(categoriaProdotto);

        when(categoriaProdotto.getIdCategoria())
                .thenReturn(idCategoria);

        when(categoriaDAO.findById(idCategoria))
                .thenReturn(categoriaCompleta);

        List<Marchio> marchi = new ArrayList<>();
        marchi.add(marchio);

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.cerca(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionDAOFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();
        verify(daoFactory).getProdottoDAO();
        verify(daoFactory).getCategoriaDAO();
        verify(daoFactory).getMarchioDAO();

        verify(prodottoDAO).cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        );

        verify(prodotto).getCategoria();

        verify(categoriaProdotto).getIdCategoria();

        verify(categoriaDAO).findById(idCategoria);

        verify(prodotto).setCategoria(categoriaCompleta);

        verify(marchioDAO).findAll();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

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

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(request).setAttribute(
                "Prodotti",
                prodotti
        );

        verify(request).setAttribute(
                "Categoria",
                nomeCategoria
        );

        verify(request).setAttribute(
                "marchi",
                marchi
        );

        verify(request).setAttribute(
                "Marchio",
                nomeMarchio
        );

        verify(request).setAttribute(
                "promo",
                promo
        );

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void cerca_utenteNonLoggato_listaProdottiVuota(
            @ForAll("nomeProdotto") String nomeProdotto,
            @ForAll("nomeMarchio") String nomeMarchio,
            @ForAll("nomeCategoria") String nomeCategoria,
            @ForAll("promo") boolean promo) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(request.getParameter("Nomeprodotto"))
                .thenReturn(nomeProdotto);

        when(request.getParameter("Marchioprodotto"))
                .thenReturn(nomeMarchio);

        when(request.getParameter("Categoria"))
                .thenReturn(nomeCategoria);

        when(request.getParameter("Promo"))
                .thenReturn(String.valueOf(promo));

        List<Prodotto> prodotti = new ArrayList<>();

        when(prodottoDAO.cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        )).thenReturn(prodotti);

        List<Marchio> marchi = new ArrayList<>();

        when(marchioDAO.findAll())
                .thenReturn(marchi);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.cerca(request, response)
            );
        }


        verify(sessionUserDAO).findLoggedUser();

        verify(prodottoDAO).cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        );

        verify(categoriaDAO, never()).findById(
                org.mockito.ArgumentMatchers.anyLong()
        );

        verify(marchioDAO).findAll();

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

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(request).setAttribute(
                "Prodotti",
                prodotti
        );

        verify(request).setAttribute(
                "Categoria",
                nomeCategoria
        );

        verify(request).setAttribute(
                "marchi",
                marchi
        );

        verify(request).setAttribute(
                "Marchio",
                nomeMarchio
        );

        verify(request).setAttribute(
                "promo",
                promo
        );


        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void cerca_eccezioneCreazioneSessionFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.cerca(request, response)
            );


            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }
    }


    @Property
    void cerca_eccezioneBeginSessionFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(sessionDAOFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.cerca(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }


    @Property
    void cerca_eccezioneDaoFactoryBegin(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.cerca(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionDAOFactory, never())
                .commitTransaction();
    }


    @Property
    void cerca_eccezioneDuranteRicercaProdotti(
            @ForAll("nomeProdotto") String nomeProdotto,
            @ForAll("nomeMarchio") String nomeMarchio,
            @ForAll("nomeCategoria") String nomeCategoria,
            @ForAll("promo") boolean promo,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(request.getParameter("Nomeprodotto"))
                .thenReturn(nomeProdotto);

        when(request.getParameter("Marchioprodotto"))
                .thenReturn(nomeMarchio);

        when(request.getParameter("Categoria"))
                .thenReturn(nomeCategoria);

        when(request.getParameter("Promo"))
                .thenReturn(String.valueOf(promo));

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        when(prodottoDAO.cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        )).thenThrow(eccezioneOriginale);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.cerca(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(prodottoDAO).cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        );

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionDAOFactory, never())
                .commitTransaction();
    }


    @Property
    void cerca_eccezioneDuranteRollback(
            @ForAll("messaggiErrore") String messaggioErrore,
            @ForAll("messaggiErrore") String messaggioRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrore);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioRollback);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.cerca(request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory, never())
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }

    @Property
    void cerca_eccezioneDuranteCloseDaoFactory(
            @ForAll("nomeProdotto") String nomeProdotto,
            @ForAll("nomeMarchio") String nomeMarchio,
            @ForAll("nomeCategoria") String nomeCategoria,
            @ForAll("promo") boolean promo,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(request.getParameter("Nomeprodotto"))
                .thenReturn(nomeProdotto);

        when(request.getParameter("Marchioprodotto"))
                .thenReturn(nomeMarchio);

        when(request.getParameter("Categoria"))
                .thenReturn(nomeCategoria);

        when(request.getParameter("Promo"))
                .thenReturn(String.valueOf(promo));

        when(prodottoDAO.cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        )).thenReturn(new ArrayList<>());

        when(marchioDAO.findAll())
                .thenReturn(new ArrayList<>());

        doThrow(
                new RuntimeException(messaggioErrore)
        ).when(daoFactory).closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);


            assertDoesNotThrow(() ->
                    HomeManagement.cerca(request, response)
            );
        }

        verify(daoFactory)
                .commitTransaction();

        verify(sessionDAOFactory)
                .commitTransaction();


        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory, never())
                .closeTransaction();
    }

    @Property
    void cerca_eccezioneDuranteCloseSessionFactory(
            @ForAll("nomeProdotto") String nomeProdotto,
            @ForAll("nomeMarchio") String nomeMarchio,
            @ForAll("nomeCategoria") String nomeCategoria,
            @ForAll("promo") boolean promo,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CategoriaDAO categoriaDAO = mock(CategoriaDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCategoriaDAO())
                .thenReturn(categoriaDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(request.getParameter("Nomeprodotto"))
                .thenReturn(nomeProdotto);

        when(request.getParameter("Marchioprodotto"))
                .thenReturn(nomeMarchio);

        when(request.getParameter("Categoria"))
                .thenReturn(nomeCategoria);

        when(request.getParameter("Promo"))
                .thenReturn(String.valueOf(promo));


        when(prodottoDAO.cerca(
                nomeProdotto,
                nomeMarchio,
                nomeCategoria,
                promo
        )).thenReturn(new ArrayList<>());

        when(marchioDAO.findAll())
                .thenReturn(new ArrayList<>());


        doThrow(
                new RuntimeException(messaggioErrore)
        ).when(sessionDAOFactory).closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.cerca(request, response)
            );
        }

        verify(daoFactory)
                .commitTransaction();

        verify(sessionDAOFactory)
                .commitTransaction();


        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }
    @Property
    void viewhome_utenteLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = mock(Utente.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
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
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewhome(request, response)
            );
        }


        verify(sessionDAOFactory)
                .beginTransaction();

        verify(sessionDAOFactory)
                .getUtenteDAO();

        verify(sessionUserDAO)
                .findLoggedUser();


        verify(daoFactory)
                .beginTransaction();

        verify(daoFactory)
                .getProdottoDAO();

        verify(prodottoDAO)
                .findPromo();

        verify(daoFactory)
                .getMarchioDAO();

        verify(marchioDAO)
                .findAll();


        verify(daoFactory)
                .commitTransaction();

        verify(sessionDAOFactory)
                .commitTransaction();

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


        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }

    @Property
    void viewhome_utenteNonLoggato(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
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
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewhome(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();
        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", false);
        verify(request).setAttribute("loggedUser", null);
        verify(request).setAttribute("applicationMessage", null);
        verify(request).setAttribute("Prodotti", prodotti);
        verify(request).setAttribute("marchi", marchi);
        verify(request).setAttribute("promo", true);
        verify(request).setAttribute("viewUrl", "HomeManagement/home");

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void viewhome_eccezioneCreazioneSessionDAOFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewhome(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }
    }

    @Property
    void viewhome_eccezioneBeginSessionFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(sessionDAOFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewhome(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(sessionDAOFactory).rollbackTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void viewhome_eccezioneDaoFactoryBegin(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewhome(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionDAOFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionDAOFactory, never()).commitTransaction();
    }

    @Property
    void viewhome_eccezioneDuranteRollback(
            @ForAll("messaggiErrore") String messaggioErrore,
            @ForAll("messaggiErrore") String messaggioRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrore);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioRollback);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.viewhome(request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();


        verify(
                sessionDAOFactory,
                never()
        ).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void viewhome_eccezioneDuranteCloseDaoFactory(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
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

        doThrow(new RuntimeException(messaggioErrore))
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewhome(request, response)
            );
        }

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();

        verify(
                sessionDAOFactory,
                never()
        ).closeTransaction();
    }

    @Property
    void viewhome_eccezioneDuranteCloseSessionFactory(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
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

        doThrow(new RuntimeException(messaggioErrore))
                .when(sessionDAOFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.viewhome(request, response)
            );
        }

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

@Property
    void logout_statoLogin(
                    @ForAll("statoLogin") boolean utenteLoggato,
                    @ForAll("prodotti") List<Prodotto> prodotti,
                    @ForAll("marchi") List<Marchio> marchi) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = utenteLoggato ? mock(Utente.class) : null;

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
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
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.logout(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionDAOFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();
        verify(daoFactory).getProdottoDAO();
        verify(prodottoDAO).findPromo();
        verify(daoFactory).getMarchioDAO();
        verify(marchioDAO).findAll();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

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

        if (utenteLoggato) {
            verify(sessionUserDAO).delete(loggedUser);
        } else {
            verify(sessionUserDAO, never()).delete(any(Utente.class));
        }

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }


    @Property
    void logout_eccezioneCreazioneSessionDAOFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(eccezioneOriginale);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.logout(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }
    }
    @Property
    void logout_eccezioneBeginSessionFactory(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(sessionDAOFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.logout(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(sessionDAOFactory).rollbackTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

    @Property
    void logout_eccezioneDaoFactoryBegin(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.logout(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionDAOFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionDAOFactory, never()).commitTransaction();
    }

    @Property
    void logout_eccezioneDuranteDelete(
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        RuntimeException eccezioneOriginale =
                new RuntimeException(messaggioErrore);

        doThrow(eccezioneOriginale)
                .when(sessionUserDAO)
                .delete(loggedUser);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.logout(request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(sessionUserDAO).delete(loggedUser);

        verify(daoFactory).rollbackTransaction();
        verify(sessionDAOFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionDAOFactory, never()).commitTransaction();
    }


    @Property
    void logout_eccezioneDuranteRollback(
            @ForAll("messaggiErrore") String messaggioErrore,
            @ForAll("messaggiErrore") String messaggioRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        RuntimeException eccezionePrincipale =
                new RuntimeException(messaggioErrore);

        RuntimeException eccezioneRollback =
                new RuntimeException(messaggioRollback);

        doThrow(eccezionePrincipale)
                .when(daoFactory)
                .beginTransaction();

        doThrow(eccezioneRollback)
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> HomeManagement.logout(request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    ex.getCause()
            );

            assertEquals(
                    messaggioErrore,
                    ex.getCause().getMessage()
            );
        }

        verify(daoFactory).rollbackTransaction();


        verify(
                sessionDAOFactory,
                never()
        ).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }


    @Property
    void logout_eccezioneDuranteCloseDaoFactory(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
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

        doThrow(new RuntimeException(messaggioErrore))
                .when(daoFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.logout(request, response)
            );
        }

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();

        verify(
                sessionDAOFactory,
                never()
        ).closeTransaction();
    }


    @Property
    void logout_eccezioneDuranteCloseSessionFactory(
            @ForAll("prodotti") List<Prodotto> prodotti,
            @ForAll("marchi") List<Marchio> marchi,
            @ForAll("messaggiErrore") String messaggioErrore) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
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

        doThrow(new RuntimeException(messaggioErrore))
                .when(sessionDAOFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    HomeManagement.logout(request, response)
            );
        }

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }

}




