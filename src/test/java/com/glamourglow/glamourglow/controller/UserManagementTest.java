package com.glamourglow.glamourglow.controller;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;
import com.glamourglow.glamourglow.services.config.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserManagementTest {

    @Test
    void viewcarrello_utenteNonLoggato() {

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
                    UserManagement.viewcarrello(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
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
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(request).setAttribute(
                "applicationMessage",
                "Fai il login per visualizzare il carrello"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }

    @Test
    void viewcarrello_utenteLoggatoCarrelloVuoto() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);

        List<Carrello> carrello = Collections.emptyList();

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

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
                    UserManagement.viewcarrello(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(loggedUser).getIdNome();

        verify(sessionCarrelloDAO).findById(10L);

        // Il for non deve eseguire nessuna iterazione
        verify(utenteDAO, never()).findById(anyLong());
        verify(prodottoDAO, never()).findById(anyLong());

        verify(request).setAttribute(
                "carrello",
                carrello
        );

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
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

    @Test
    void viewcarrello_utenteLoggatoConUnElemento() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteCarrello = mock(Utente.class);
        Utente utenteCompleto = mock(Utente.class);

        Prodotto prodottoCarrello = mock(Prodotto.class);
        Prodotto prodottoCompleto = mock(Prodotto.class);

        Carrello carrelloItem = mock(Carrello.class);

        List<Carrello> carrello =
                new ArrayList<>();

        carrello.add(carrelloItem);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

        // Utente presente nel carrello
        when(carrelloItem.getUtente())
                .thenReturn(utenteCarrello);

        when(utenteCarrello.getIdNome())
                .thenReturn(20L);

        when(utenteDAO.findById(20L))
                .thenReturn(utenteCompleto);

        // Prodotto presente nel carrello
        when(carrelloItem.getProdotto())
                .thenReturn(prodottoCarrello);

        when(prodottoCarrello.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodottoCompleto);

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
                    UserManagement.viewcarrello(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(loggedUser).getIdNome();

        verify(sessionCarrelloDAO).findById(10L);

        verify(carrelloItem).getUtente();
        verify(utenteCarrello).getIdNome();

        verify(utenteDAO).findById(20L);
        verify(carrelloItem).setUtente(utenteCompleto);

        verify(carrelloItem).getProdotto();
        verify(prodottoCarrello).getIdProdotto();

        verify(prodottoDAO).findById(50L);
        verify(carrelloItem).setProdotto(prodottoCompleto);

        verify(request).setAttribute(
                "carrello",
                carrello
        );

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
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


    @Test
    void viewcarrello_eccezioneDuranteFindCarrello() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(mock(UtenteDAO.class));

        when(daoFactory.getProdottoDAO())
                .thenReturn(mock(ProdottoDAO.class));

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        RuntimeException errore =
                new RuntimeException("Errore database");

        when(sessionCarrelloDAO.findById(10L))
                .thenThrow(errore);

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
                    () -> UserManagement.viewcarrello(request, response)
            );

            assertSame(
                    errore,
                    exception.getCause()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionFactory, never()).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcarrello_erroreDuranteClose() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);


        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);


        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(Collections.emptyList());


        doThrow(new RuntimeException("Errore close DAO"))
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


            assertDoesNotThrow(() ->
                    UserManagement.viewcarrello(request, response)
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


        verify(daoFactory, never())
                .rollbackTransaction();

        verify(sessionFactory, never())
                .rollbackTransaction();
    }

    @Test
    void viewcarrello_errorePrimaCreazioneDaoFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        doThrow(new RuntimeException("Errore begin session"))
                .when(sessionFactory)
                .beginTransaction();

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
                    () -> UserManagement.viewcarrello(request, response)
            );

            assertNotNull(exception.getCause());
        }

        verify(sessionFactory).beginTransaction();
        verify(sessionFactory).rollbackTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcarrello_erroreDuranteRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);


        RuntimeException erroreOriginale =
                new RuntimeException("Errore durante il recupero del carrello");

        when(sessionCarrelloDAO.findById(10L))
                .thenThrow(erroreOriginale);


        doThrow(new RuntimeException("Errore durante rollback DAO"))
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
                    () -> UserManagement.viewcarrello(request, response)
            );


            assertSame(
                    erroreOriginale,
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
    void viewcarrello_errorePrimaCreazioneSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactory = mock(DAOFactory.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(
                    new RuntimeException("Errore creazione session factory")
            );

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewcarrello(request, response)
            );

            assertNotNull(exception.getCause());
        }


    }


    @Test
    void aggiungicarrello_utenteNonLoggato() {

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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", false);
        verify(request).setAttribute("loggedUser", null);
        verify(request).setAttribute("applicationMessage", null);

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void aggiungicarrello_aggiuntaProdotto_quantitaMaggioreDiZero() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(request.getParameter("quantita"))
                .thenReturn("3");

        when(request.getParameter("idprod"))
                .thenReturn("50");

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);

        when(prodotto.getNomeProdotto())
                .thenReturn("Rossetto");

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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }

        verify(sessionCarrelloDAO).create(10L, 50L, 3);

        verify(prodottoDAO).findById(50L);

        verify(request).setAttribute(
                "prodotto",
                prodotto
        );

        verify(request).setAttribute(
                "viewUrl",
                "ProductManagement/descrizione"
        );

        verify(request).setAttribute(
                "applicationMessage",
                "Rossetto x3 aggiunto al carrello"
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void aggiungicarrello_rimozioneProdotto_carrelloVuoto() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);

        List<Carrello> carrello = new ArrayList<>();

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(request.getParameter("quantita"))
                .thenReturn("0");

        when(request.getParameter("idprod"))
                .thenReturn("50");

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }

        verify(sessionCarrelloDAO).create(10L, 50L, 0);

        verify(prodottoDAO).findById(50L);

        verify(sessionCarrelloDAO).findById(10L);

        verify(request).setAttribute(
                "prodotto",
                prodotto
        );

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
        );

        verify(request).setAttribute(
                "carrello",
                carrello
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


    @Test
    void aggiungicarrello_rimozioneProdotto_presenteNelCarrello() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteCarrello = mock(Utente.class);

        Prodotto prodotto = mock(Prodotto.class);

        Carrello item = mock(Carrello.class);

        List<Carrello> carrello = new ArrayList<>();
        carrello.add(item);


        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);


        when(request.getParameter("quantita"))
                .thenReturn("0");

        when(request.getParameter("idprod"))
                .thenReturn("50");


        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);


        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);


        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(50L);


        when(item.getUtente())
                .thenReturn(utenteCarrello);

        when(utenteCarrello.getIdNome())
                .thenReturn(10L);

        when(utenteDAO.findById(10L))
                .thenReturn(utenteCarrello);


        when(item.getProdotto())
                .thenReturn(prodotto);


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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }


        verify(sessionFactory)
                .beginTransaction();

        verify(daoFactory)
                .beginTransaction();


        verify(sessionUserDAO)
                .findLoggedUser();


        verify(loggedUser, times(2))
                .getIdNome();


        verify(sessionCarrelloDAO)
                .create(10L, 50L, 0);


        verify(sessionCarrelloDAO)
                .findById(10L);


        verify(prodottoDAO, times(2))
                .findById(50L);

        verify(request)
                .setAttribute(
                        "prodotto",
                        prodotto
                );


        verify(item)
                .getUtente();

        verify(utenteCarrello)
                .getIdNome();

        verify(utenteDAO)
                .findById(10L);

        verify(item)
                .setUtente(utenteCarrello);

        verify(item, times(2))
                .getProdotto();


        verify(prodotto, times(2))
                .getIdProdotto();


        verify(item)
                .setProdotto(prodotto);


        assertTrue(carrello.isEmpty());


        verify(request)
                .setAttribute(
                        "viewUrl",
                        "UserManagement/carrello"
                );

        verify(request)
                .setAttribute(
                        "carrello",
                        carrello
                );

        verify(request)
                .setAttribute(
                        "prodotto",
                        prodotto
                );

        verify(request)
                .setAttribute(
                        "loggedOn",
                        true
                );

        verify(request)
                .setAttribute(
                        "loggedUser",
                        loggedUser
                );

        verify(request)
                .setAttribute(
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


        verify(daoFactory, never())
                .rollbackTransaction();

        verify(sessionFactory, never())
                .rollbackTransaction();
    }


    @Test
    void aggiungicarrello_parametriMancanti() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);

        List<Carrello> carrello = Collections.emptyList();

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        // Mancano entrambi i parametri
        when(request.getParameter("quantita"))
                .thenReturn(null);

        when(request.getParameter("idprod"))
                .thenReturn(null);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }

        verify(sessionCarrelloDAO).findById(10L);

        verify(utenteDAO, never()).findById(anyLong());
        verify(prodottoDAO, never()).findById(anyLong());

        verify(request).setAttribute(
                "carrello",
                carrello
        );

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
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


    @Test
    void aggiungicarrello_eccezioneDuranteCreate() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(request.getParameter("quantita"))
                .thenReturn("2");

        when(request.getParameter("idprod"))
                .thenReturn("50");

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        RuntimeException errore =
                new RuntimeException("Errore create");

        doThrow(errore)
                .when(sessionCarrelloDAO)
                .create(10L, 50L, 2);

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
                    () -> UserManagement.aggiungicarrello(request, response)
            );

            assertSame(
                    errore,
                    exception.getCause()
            );
        }

        verify(sessionCarrelloDAO)
                .create(10L, 50L, 2);

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
    void aggiungicarrello_eccezioneDuranteRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(request.getParameter("quantita"))
                .thenReturn("0");

        when(request.getParameter("idprod"))
                .thenReturn("50");

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);


        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);


        RuntimeException erroreOriginale =
                new RuntimeException("Errore originale");

        when(sessionCarrelloDAO.findById(10L))
                .thenThrow(erroreOriginale);

        doThrow(new RuntimeException("Errore rollback"))
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
                    () -> UserManagement.aggiungicarrello(request, response)
            );


            assertSame(
                    erroreOriginale,
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


        verify(sessionCarrelloDAO)
                .create(10L, 50L, 0);

        verify(prodottoDAO)
                .findById(50L);

        verify(request)
                .setAttribute(
                        "prodotto",
                        prodotto
                );
    }


    @Test
    void aggiungicarrello_eccezioneDuranteClose() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(request.getParameter("quantita"))
                .thenReturn("1");

        when(request.getParameter("idprod"))
                .thenReturn("50");

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);


        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        Prodotto prodotto = mock(Prodotto.class);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);

        when(prodotto.getNomeProdotto())
                .thenReturn("Rossetto");


        doThrow(new RuntimeException("Errore close"))
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


            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
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


        verify(daoFactory, never())
                .rollbackTransaction();

        verify(sessionFactory, never())
                .rollbackTransaction();


        verify(sessionCarrelloDAO)
                .create(10L, 50L, 1);

        verify(prodottoDAO)
                .findById(50L);

        verify(request)
                .setAttribute(
                        "prodotto",
                        prodotto
                );

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "ProductManagement/descrizione"
                );

        verify(request)
                .setAttribute(
                        "applicationMessage",
                        "Rossetto x1 aggiunto al carrello"
                );

        verify(request)
                .setAttribute(
                        "loggedOn",
                        true
                );

        verify(request)
                .setAttribute(
                        "loggedUser",
                        loggedUser
                );

        verify(request)
                .setAttribute(
                        "applicationMessage",
                        "Rossetto x1 aggiunto al carrello"
                );
    }


    @Test
    void aggiungicarrello_rimozioneProdotto_nonPresenteNelCarrello() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteCarrello = mock(Utente.class);

        Prodotto prodottoRichiesto = mock(Prodotto.class);
        Prodotto prodottoNelCarrello = mock(Prodotto.class);

        Carrello item = mock(Carrello.class);

        List<Carrello> carrello = new ArrayList<>();
        carrello.add(item);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(request.getParameter("quantita"))
                .thenReturn("0");

        when(request.getParameter("idprod"))
                .thenReturn("50");

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodottoRichiesto);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);


        when(item.getUtente())
                .thenReturn(utenteCarrello);

        when(utenteCarrello.getIdNome())
                .thenReturn(10L);

        when(utenteDAO.findById(10L))
                .thenReturn(utenteCarrello);


        when(item.getProdotto())
                .thenReturn(prodottoNelCarrello);

        when(prodottoNelCarrello.getIdProdotto())
                .thenReturn(99L);

        when(prodottoDAO.findById(99L))
                .thenReturn(prodottoNelCarrello);

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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }


        verify(sessionCarrelloDAO)
                .create(10L, 50L, 0);


        verify(prodottoDAO)
                .findById(50L);


        verify(prodottoDAO)
                .findById(99L);


        verify(item)
                .setUtente(utenteCarrello);

        verify(item)
                .setProdotto(prodottoNelCarrello);


        assertFalse(carrello.isEmpty());
        assertEquals(1, carrello.size());

        verify(request)
                .setAttribute(
                        "prodotto",
                        prodottoRichiesto
                );

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "UserManagement/carrello"
                );

        verify(request)
                .setAttribute(
                        "carrello",
                        carrello
                );

        verify(request)
                .setAttribute(
                        "loggedOn",
                        true
                );

        verify(request)
                .setAttribute(
                        "loggedUser",
                        loggedUser
                );

        verify(request)
                .setAttribute(
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


    @Test
    void aggiungicarrello_carrelloConPiuElementi() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);

        Utente utente1 = mock(Utente.class);
        Utente utente2 = mock(Utente.class);

        Prodotto prodotto1 = mock(Prodotto.class);
        Prodotto prodotto2 = mock(Prodotto.class);

        Carrello item1 = mock(Carrello.class);
        Carrello item2 = mock(Carrello.class);

        List<Carrello> carrello = new ArrayList<>();
        carrello.add(item1);
        carrello.add(item2);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);


        when(request.getParameter("quantita"))
                .thenReturn(null);

        when(request.getParameter("idprod"))
                .thenReturn(null);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);


        when(item1.getUtente())
                .thenReturn(utente1);

        when(utente1.getIdNome())
                .thenReturn(10L);

        when(utenteDAO.findById(10L))
                .thenReturn(utente1);

        when(item1.getProdotto())
                .thenReturn(prodotto1);

        when(prodotto1.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto1);


        when(item2.getUtente())
                .thenReturn(utente2);

        when(utente2.getIdNome())
                .thenReturn(20L);

        when(utenteDAO.findById(20L))
                .thenReturn(utente2);

        when(item2.getProdotto())
                .thenReturn(prodotto2);

        when(prodotto2.getIdProdotto())
                .thenReturn(60L);

        when(prodottoDAO.findById(60L))
                .thenReturn(prodotto2);

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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }


        verify(sessionCarrelloDAO)
                .findById(10L);

        verify(utenteDAO)
                .findById(10L);

        verify(item1)
                .setUtente(utente1);

        verify(prodottoDAO)
                .findById(50L);

        verify(item1)
                .setProdotto(prodotto1);

        verify(utenteDAO)
                .findById(20L);

        verify(item2)
                .setUtente(utente2);

        verify(prodottoDAO)
                .findById(60L);

        verify(item2)
                .setProdotto(prodotto2);

        verify(request)
                .setAttribute(
                        "carrello",
                        carrello
                );

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "UserManagement/carrello"
                );

        verify(request)
                .setAttribute(
                        "loggedOn",
                        true
                );

        verify(request)
                .setAttribute(
                        "loggedUser",
                        loggedUser
                );

        verify(request)
                .setAttribute(
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


    @Test
    void aggiungicarrello_quantitaPresenteIdProdMancante() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);

        List<Carrello> carrello = Collections.emptyList();

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(request.getParameter("quantita"))
                .thenReturn("2");

        when(request.getParameter("idprod"))
                .thenReturn(null);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

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

            assertDoesNotThrow(() ->
                    UserManagement.aggiungicarrello(request, response)
            );
        }

        // Deve entrare nell'else dell'if sui parametri
        verify(sessionCarrelloDAO)
                .findById(10L);

        verify(request)
                .setAttribute("carrello", carrello);

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "UserManagement/carrello"
                );

        verify(request)
                .setAttribute("loggedOn", true);

        verify(request)
                .setAttribute("loggedUser", loggedUser);

        verify(request)
                .setAttribute("applicationMessage", null);

        verify(utenteDAO, never())
                .findById(anyLong());

        verify(prodottoDAO, never())
                .findById(anyLong());

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
    void aggiungicarrello_eccezionePrimaCreazioneDaoFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(request.getParameter("quantita"))
                .thenReturn("2");

        when(request.getParameter("idprod"))
                .thenReturn("50");

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        RuntimeException errore =
                new RuntimeException("Errore creazione DAO");

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
            ).thenThrow(errore);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.aggiungicarrello(request, response)
            );

            assertSame(errore, exception.getCause());
        }


        verify(sessionFactory)
                .rollbackTransaction();

        verify(sessionFactory)
                .closeTransaction();
    }

    @Test
    void aggiungicarrello_eccezionePrimaCreazioneSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException errore =
                new RuntimeException("Errore creazione session factory");

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(errore);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.aggiungicarrello(request, response)
            );

            assertSame(errore, exception.getCause());
        }


    }


    @Test
    void userManagement_costruttore() {
        UserManagement userManagement = new UserManagement();

        assertNotNull(userManagement);
    }

    @Test
    void viewcheckout_utenteNonLoggato() {

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

            assertDoesNotThrow(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(prodottoDAO).findPromo();
        verify(marchioDAO).findAll();

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
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
                "loggedOn",
                false
        );

        verify(request).setAttribute(
                "loggedUser",
                null
        );

        verify(request).setAttribute(
                "applicationMessage",
                "Fai il login per visualizzare il carrello"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcheckout_utenteLoggatoCarrelloVuoto() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);

        List<Carrello> carrello = Collections.emptyList();

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

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

            assertDoesNotThrow(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(loggedUser).getIdNome();
        verify(sessionCarrelloDAO).findById(10L);

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/checkout"
        );

        verify(request).setAttribute(
                "carrello",
                carrello
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


    @Test
    void viewcheckout_prodottoDisponibileENonBloccato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteCarrello = mock(Utente.class);

        Prodotto prodotto = mock(Prodotto.class);
        Carrello item = mock(Carrello.class);

        List<Carrello> carrello = new ArrayList<>();
        carrello.add(item);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

        when(item.getUtente())
                .thenReturn(utenteCarrello);

        when(utenteCarrello.getIdNome())
                .thenReturn(10L);

        when(utenteDAO.findById(10L))
                .thenReturn(utenteCarrello);

        when(item.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);

        when(item.getQta())
                .thenReturn(2);

        when(prodotto.getQuantitaDispo())
                .thenReturn(5);

        when(prodotto.isStatoprodotto())
                .thenReturn(false);

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

            assertDoesNotThrow(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();
        verify(loggedUser).getIdNome();

        verify(sessionCarrelloDAO).findById(10L);

        verify(item).getUtente();
        verify(utenteCarrello).getIdNome();
        verify(utenteDAO).findById(10L);
        verify(item).setUtente(utenteCarrello);

        verify(item, times(3)).getProdotto();
        verify(prodotto).getIdProdotto();
        verify(prodottoDAO).findById(50L);
        verify(item).setProdotto(prodotto);

        verify(item).getQta();
        verify(prodotto).getQuantitaDispo();
        verify(prodotto).isStatoprodotto();

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/checkout"
        );

        verify(request).setAttribute(
                "carrello",
                carrello
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

        verify(daoFactory, never()).rollbackTransaction();
        verify(sessionFactory, never()).rollbackTransaction();
    }


    @Test
    void viewcheckout_quantitaSuperioreDisponibilita() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteCarrello = mock(Utente.class);

        Prodotto prodotto = mock(Prodotto.class);
        Carrello item = mock(Carrello.class);

        List<Carrello> carrello = new ArrayList<>();
        carrello.add(item);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

        when(item.getUtente())
                .thenReturn(utenteCarrello);

        when(utenteCarrello.getIdNome())
                .thenReturn(10L);

        when(utenteDAO.findById(10L))
                .thenReturn(utenteCarrello);

        when(item.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);


        when(item.getQta())
                .thenReturn(7);

        when(prodotto.getQuantitaDispo())
                .thenReturn(3);


        when(prodotto.isStatoprodotto())
                .thenReturn(false);

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

            assertDoesNotThrow(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(item, times(2)).getQta();
        verify(prodotto).getQuantitaDispo();

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Non sono disponibili 7 quantità!"
        );
        verify(request).setAttribute(
                "carrello",
                carrello
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcheckout_prodottoBloccato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteCarrello = mock(Utente.class);

        Prodotto prodotto = mock(Prodotto.class);
        Carrello item = mock(Carrello.class);

        List<Carrello> carrello = new ArrayList<>();
        carrello.add(item);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

        when(item.getUtente())
                .thenReturn(utenteCarrello);

        when(utenteCarrello.getIdNome())
                .thenReturn(10L);

        when(utenteDAO.findById(10L))
                .thenReturn(utenteCarrello);

        when(item.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);

        when(item.getQta())
                .thenReturn(2);

        when(prodotto.getQuantitaDispo())
                .thenReturn(5);

        when(prodotto.isStatoprodotto())
                .thenReturn(true);

        when(prodotto.getNomeProdotto())
                .thenReturn("Rossetto");

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

            assertDoesNotThrow(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(prodotto).isStatoprodotto();
        verify(prodotto).getNomeProdotto();

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Impossibile procedere al checkout: il prodotto Rossetto è stato temporaneamente bloccato!"
        );

        verify(request).setAttribute(
                "carrello",
                carrello
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcheckout_quantitaSuperioreEProdottoBloccato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Utente utenteCarrello = mock(Utente.class);

        Prodotto prodotto = mock(Prodotto.class);
        Carrello item = mock(Carrello.class);

        List<Carrello> carrello = new ArrayList<>();
        carrello.add(item);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(carrello);

        when(item.getUtente())
                .thenReturn(utenteCarrello);

        when(utenteCarrello.getIdNome())
                .thenReturn(10L);

        when(utenteDAO.findById(10L))
                .thenReturn(utenteCarrello);

        when(item.getProdotto())
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(50L);

        when(prodottoDAO.findById(50L))
                .thenReturn(prodotto);

        when(item.getQta())
                .thenReturn(10);

        when(prodotto.getQuantitaDispo())
                .thenReturn(3);

        when(prodotto.isStatoprodotto())
                .thenReturn(true);

        when(prodotto.getNomeProdotto())
                .thenReturn("Fondotinta");

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

            assertDoesNotThrow(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(item, times(2)).getQta();
        verify(prodotto).getQuantitaDispo();

        verify(prodotto).isStatoprodotto();
        verify(prodotto).getNomeProdotto();

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Impossibile procedere al checkout: il prodotto Fondotinta è stato temporaneamente bloccato!"
        );

        verify(request, times(2)).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
        );

        verify(request).setAttribute(
                "carrello",
                carrello
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcheckout_eccezioneDuranteFindCarrello() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        RuntimeException errore =
                new RuntimeException("Errore database");

        when(sessionCarrelloDAO.findById(10L))
                .thenThrow(errore);

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
                    () -> UserManagement.viewcheckout(request, response)
            );

            assertSame(
                    errore,
                    exception.getCause()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();

        verify(daoFactory, never()).commitTransaction();
        verify(sessionFactory, never()).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcheckout_eccezionePrimaCreazioneDaoFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        RuntimeException errore =
                new RuntimeException("Errore creazione DAO");

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
            ).thenThrow(errore);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewcheckout(request, response)
            );

            assertSame(
                    errore,
                    exception.getCause()
            );
        }

        verify(sessionFactory).rollbackTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcheckout_eccezionePrimaCreazioneSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        RuntimeException errore =
                new RuntimeException("Errore creazione session factory");

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(errore);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewcheckout(request, response)
            );

            assertSame(
                    errore,
                    exception.getCause()
            );
        }


    }


    @Test
    void viewcheckout_eccezioneDuranteRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        RuntimeException erroreOriginale =
                new RuntimeException("Errore originale");

        when(sessionCarrelloDAO.findById(10L))
                .thenThrow(erroreOriginale);

        doThrow(new RuntimeException("Errore rollback"))
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
                    () -> UserManagement.viewcheckout(request, response)
            );

            assertSame(
                    erroreOriginale,
                    exception.getCause()
            );
        }

        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory, never())
                .rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewcheckout_eccezioneDuranteClose() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(Collections.emptyList());

        doThrow(new RuntimeException("Errore close DAO"))
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

            assertDoesNotThrow(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();


        verify(sessionFactory, never())
                .closeTransaction();

        verify(daoFactory, never())
                .rollbackTransaction();

        verify(sessionFactory, never())
                .rollbackTransaction();
    }


    @Test
    void verificacoupon_utenteLoggato_couponValido() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);

        Utente loggedUser = mock(Utente.class);
        Coupon coupon = mock(Coupon.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);

        when(loggedUser.getIdNome()).thenReturn(10L);

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{"SCONTO10"});

        when(couponDAO.findByCode("SCONTO10"))
                .thenReturn(coupon);

        when(dettagliOrdineDAO.usoCoupon(10L, "SCONTO10"))
                .thenReturn(false);

        when(coupon.getSconto())
                .thenReturn(10);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class);
             MockedStatic<UserManagement> userManagementMock =
                     Mockito.mockStatic(UserManagement.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            userManagementMock.when(() ->
                    UserManagement.verificacoupon(request, response)
            ).thenCallRealMethod();

            userManagementMock.when(() ->
                    UserManagement.viewcheckout(request, response)
            ).thenAnswer(invocation -> null);

            assertDoesNotThrow(() ->
                    UserManagement.verificacoupon(request, response)
            );

            userManagementMock.verify(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(request).getParameterValues("coupon");

        verify(couponDAO).findByCode("SCONTO10");

        verify(loggedUser).getIdNome();

        verify(dettagliOrdineDAO)
                .usoCoupon(10L, "SCONTO10");

        verify(coupon).getSconto();

        verify(request).setAttribute(
                eq("coupon"),
                aryEq(new String[]{"SCONTO10"})
        );

        verify(request).setAttribute(
                eq("sconto"),
                aryEq(new String[]{"10"})
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

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
                "Coupon inserito con successo!"
        );

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void verificacoupon_utenteLoggato_couponInesistente() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);

        when(loggedUser.getIdNome()).thenReturn(10L);

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{"NONESISTE"});

        when(couponDAO.findByCode("NONESISTE"))
                .thenReturn(null);

        when(dettagliOrdineDAO.usoCoupon(10L, "NONESISTE"))
                .thenReturn(false);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class);
             MockedStatic<UserManagement> userManagementMock =
                     Mockito.mockStatic(UserManagement.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            userManagementMock.when(() ->
                    UserManagement.verificacoupon(request, response)
            ).thenCallRealMethod();

            userManagementMock.when(() ->
                    UserManagement.viewcheckout(request, response)
            ).thenAnswer(invocation -> null);

            assertDoesNotThrow(() ->
                    UserManagement.verificacoupon(request, response)
            );

            userManagementMock.verify(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(couponDAO).findByCode("NONESISTE");

        verify(dettagliOrdineDAO)
                .usoCoupon(10L, "NONESISTE");

        verify(request).setAttribute(
                eq("coupon"),
                aryEq(new String[]{null})
        );

        verify(request).setAttribute(
                eq("sconto"),
                aryEq(new String[]{null})
        );

        verify(request).setAttribute(
                "applicationMessage",
                "Coupon non valido, poichè non esiste!"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void verificacoupon_utenteLoggato_couponGiaUtilizzato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);

        Utente loggedUser = mock(Utente.class);
        Coupon coupon = mock(Coupon.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);

        when(loggedUser.getIdNome()).thenReturn(10L);

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{"SCONTO10"});

        when(couponDAO.findByCode("SCONTO10"))
                .thenReturn(coupon);

        when(dettagliOrdineDAO.usoCoupon(10L, "SCONTO10"))
                .thenReturn(true);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class);
             MockedStatic<UserManagement> userManagementMock =
                     Mockito.mockStatic(UserManagement.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            userManagementMock.when(() ->
                    UserManagement.verificacoupon(request, response)
            ).thenCallRealMethod();

            userManagementMock.when(() ->
                    UserManagement.viewcheckout(request, response)
            ).thenAnswer(invocation -> null);

            assertDoesNotThrow(() ->
                    UserManagement.verificacoupon(request, response)
            );
        }

        verify(dettagliOrdineDAO)
                .usoCoupon(10L, "SCONTO10");

        verify(request).setAttribute(
                eq("coupon"),
                aryEq(new String[]{null})
        );

        verify(request).setAttribute(
                eq("sconto"),
                aryEq(new String[]{null})
        );

        verify(request).setAttribute(
                "applicationMessage",
                "Coupon non valido, poichè è già stato usato!"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void verificacoupon_utenteLoggato_couponVuoto() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);

        when(loggedUser.getIdNome()).thenReturn(10L);

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{""});

        when(couponDAO.findByCode(""))
                .thenReturn(null);

        when(dettagliOrdineDAO.usoCoupon(10L, ""))
                .thenReturn(false);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class);
             MockedStatic<UserManagement> userManagementMock =
                     Mockito.mockStatic(UserManagement.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            userManagementMock.when(() ->
                    UserManagement.verificacoupon(request, response)
            ).thenCallRealMethod();

            userManagementMock.when(() ->
                    UserManagement.viewcheckout(request, response)
            ).thenAnswer(invocation -> null);

            assertDoesNotThrow(() ->
                    UserManagement.verificacoupon(request, response)
            );
        }

        verify(couponDAO).findByCode("");

        verify(dettagliOrdineDAO)
                .usoCoupon(10L, "");

        verify(request).setAttribute(
                eq("coupon"),
                aryEq(new String[]{null})
        );

        verify(request).setAttribute(
                eq("sconto"),
                aryEq(new String[]{null})
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


    @Test
    void verificacoupon_dueCouponValidiDistinti() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);

        Utente loggedUser = mock(Utente.class);

        Coupon coupon10 = mock(Coupon.class);
        Coupon coupon20 = mock(Coupon.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);

        when(loggedUser.getIdNome()).thenReturn(10L);

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{
                        "SCONTO10",
                        "SCONTO20"
                });

        when(couponDAO.findByCode("SCONTO10"))
                .thenReturn(coupon10);

        when(couponDAO.findByCode("SCONTO20"))
                .thenReturn(coupon20);

        when(dettagliOrdineDAO.usoCoupon(10L, "SCONTO10"))
                .thenReturn(false);

        when(dettagliOrdineDAO.usoCoupon(10L, "SCONTO20"))
                .thenReturn(false);

        when(coupon10.getSconto())
                .thenReturn(10);

        when(coupon20.getSconto())
                .thenReturn(20);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class);
             MockedStatic<UserManagement> userManagementMock =
                     Mockito.mockStatic(UserManagement.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            userManagementMock.when(() ->
                    UserManagement.verificacoupon(request, response)
            ).thenCallRealMethod();

            userManagementMock.when(() ->
                    UserManagement.viewcheckout(request, response)
            ).thenAnswer(invocation -> null);

            assertDoesNotThrow(() ->
                    UserManagement.verificacoupon(request, response)
            );

            userManagementMock.verify(() ->
                    UserManagement.viewcheckout(request, response)
            );
        }

        verify(couponDAO).findByCode("SCONTO10");
        verify(couponDAO).findByCode("SCONTO20");

        verify(dettagliOrdineDAO)
                .usoCoupon(10L, "SCONTO10");

        verify(dettagliOrdineDAO)
                .usoCoupon(10L, "SCONTO20");

        verify(coupon10).getSconto();
        verify(coupon20).getSconto();

        verify(request).setAttribute(
                eq("coupon"),
                aryEq(new String[]{
                        "SCONTO10",
                        "SCONTO20"
                })
        );

        verify(request).setAttribute(
                eq("sconto"),
                aryEq(new String[]{
                        "10",
                        "20"
                })
        );


        verify(request).setAttribute(
                "applicationMessage",
                "Coupon inserito con successo!"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }

    @Test
    void verificacoupon_couponDuplicato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);

        Utente loggedUser = mock(Utente.class);
        Coupon coupon = mock(Coupon.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);

        when(loggedUser.getIdNome()).thenReturn(10L);

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{
                        "SCONTO10",
                        "SCONTO10"
                });

        when(couponDAO.findByCode("SCONTO10"))
                .thenReturn(coupon);

        when(dettagliOrdineDAO.usoCoupon(10L, "SCONTO10"))
                .thenReturn(false);

        when(coupon.getSconto())
                .thenReturn(10);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class);
             MockedStatic<UserManagement> userManagementMock =
                     Mockito.mockStatic(UserManagement.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            userManagementMock.when(() ->
                    UserManagement.verificacoupon(request, response)
            ).thenCallRealMethod();

            userManagementMock.when(() ->
                    UserManagement.viewcheckout(request, response)
            ).thenAnswer(invocation -> null);

            assertDoesNotThrow(() ->
                    UserManagement.verificacoupon(request, response)
            );
        }

        verify(couponDAO, times(2))
                .findByCode("SCONTO10");

        verify(dettagliOrdineDAO, times(2))
                .usoCoupon(10L, "SCONTO10");

        verify(request).setAttribute(
                eq("coupon"),
                aryEq(new String[]{
                        "SCONTO10",
                        null
                })
        );

        verify(request).setAttribute(
                eq("sconto"),
                aryEq(new String[]{
                        "10",
                        null
                })
        );

        verify(request).setAttribute(
                "applicationMessage",
                "Coupon non valido, poichè già inserito!"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void verificacoupon_utenteNonLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class);
             MockedStatic<UserManagement> userManagementMock =
                     Mockito.mockStatic(UserManagement.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            userManagementMock.when(() ->
                    UserManagement.verificacoupon(request, response)
            ).thenCallRealMethod();

            assertDoesNotThrow(() ->
                    UserManagement.verificacoupon(request, response)
            );

            userManagementMock.verify(
                    () -> UserManagement.viewcheckout(request, response),
                    never()
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(request, never())
                .getParameterValues("coupon");

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


    @Test
    void verificacoupon_eccezioneRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        doThrow(new RuntimeException("Errore DB"))
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

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.verificacoupon(request, response)
            );

            assertEquals(
                    "java.lang.RuntimeException: Errore DB",
                    exception.getMessage()
            );
        }

        verify(sessionFactory).beginTransaction();

        verify(sessionFactory).rollbackTransaction();

        verify(sessionFactory).closeTransaction();
    }


    @Test
    void verificacoupon_erroreDopoCreazioneDaoFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);


        doThrow(new RuntimeException("Errore DB"))
                .when(daoFactory)
                .beginTransaction();

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
                    () -> UserManagement.verificacoupon(request, response)
            );
        }


        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void verificacoupon_errorePrimaCreazioneSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {


            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(new RuntimeException("Errore creazione factory"));

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.verificacoupon(request, response)
            );
        }
    }


    @Test
    void verificacoupon_erroreDuranteRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);


        doThrow(new RuntimeException("Errore begin"))
                .when(sessionFactory)
                .beginTransaction();


        doThrow(new RuntimeException("Errore rollback"))
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

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.verificacoupon(request, response)
            );
        }


        verify(sessionFactory).rollbackTransaction();


        verify(sessionFactory).closeTransaction();
    }


    @Test
    void verificacoupon_erroreDuranteCloseTransaction() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);


        doThrow(new RuntimeException("Errore DB"))
                .when(sessionFactory)
                .beginTransaction();

        doThrow(new RuntimeException("Errore close"))
                .when(sessionFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.verificacoupon(request, response)
            );
        }


        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricaricasaldo_utenteLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    UserManagement.ricaricasaldo(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(sessionFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();
        verify(daoFactory).beginTransaction();

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/wallet"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

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

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricaricasaldo_utenteNonLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    UserManagement.ricaricasaldo(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(sessionFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/login"
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


    @Test
    void ricaricasaldo_erroreDopoCreazioneDaoFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);


        doThrow(new RuntimeException("Errore DB"))
                .when(daoFactory)
                .beginTransaction();

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricaricasaldo(request, response)
            );

            assertNotNull(exception.getCause());
            assertEquals(
                    "Errore DB",
                    exception.getCause().getMessage()
            );
        }


        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();


        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricaricasaldo_erroreCreazioneSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {


            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(
                    new RuntimeException("Errore creazione factory")
            );

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricaricasaldo(request, response)
            );

            assertNotNull(exception.getCause());

            assertEquals(
                    "Errore creazione factory",
                    exception.getCause().getMessage()
            );
        }


    }


    @Test
    void ricaricasaldo_erroreDuranteRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);


        doThrow(new RuntimeException("Errore begin"))
                .when(daoFactory)
                .beginTransaction();


        doThrow(new RuntimeException("Errore rollback"))
                .when(daoFactory)
                .rollbackTransaction();

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricaricasaldo(request, response)
            );
        }


        verify(daoFactory).rollbackTransaction();


        verify(sessionFactory, never())
                .rollbackTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricaricasaldo_erroreDuranteCloseTransaction() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);


        doThrow(new RuntimeException("Errore begin"))
                .when(sessionFactory)
                .beginTransaction();


        doThrow(new RuntimeException("Errore close"))
                .when(sessionFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricaricasaldo(request, response)
            );


            assertNotNull(exception.getCause());

            assertEquals(
                    "Errore begin",
                    exception.getCause().getMessage()
            );
        }


        verify(sessionFactory).rollbackTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricarica_utenteLoggato_ricaricaValida() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(loggedUser.getWallet())
                .thenReturn(100.0);

        when(loggedUser.getPassword())
                .thenReturn("password");

        when(loggedUser.getNome())
                .thenReturn("Mario");

        when(loggedUser.getCognome())
                .thenReturn("Rossi");

        when(loggedUser.getEmail())
                .thenReturn("mario@email.it");

        when(loggedUser.getTelefono())
                .thenReturn("3331234567");

        when(loggedUser.getStatoAccount())
                .thenReturn("attivo");

        when(loggedUser.getRuolo())
                .thenReturn(true);

        when(request.getParameter("saldo"))
                .thenReturn("50.0");

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    UserManagement.ricarica(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(request).getParameter("saldo");

        verify(utenteDAO).updatewallet(
                10L,
                150.0
        );

        verify(sessionUserDAO).create(
                10L,
                "password",
                "Mario",
                "Rossi",
                "mario@email.it",
                "3331234567",
                150.0,
                "attivo",
                true
        );

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/wallet"
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Saldo ricaricato"
        );

        verify(request).setAttribute(
                "loggedOn",
                true
        );

        verify(request).setAttribute(
                "loggedUser",
                loggedUser
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricarica_utenteNonLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.DAO_IMPL),
                            isNull()
                    )
            ).thenReturn(daoFactory);

            assertDoesNotThrow(() ->
                    UserManagement.ricarica(request, response)
            );
        }


        verify(sessionUserDAO).findLoggedUser();


        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/login"
        );


        verify(request, never())
                .getParameter("saldo");

        verify(utenteDAO, never())
                .updatewallet(anyLong(), anyDouble());

        verify(sessionUserDAO, never())
                .create(
                        anyLong(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyDouble(),
                        anyString(),
                        anyBoolean()
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


    @Test
    void ricarica_eccezioneBeginSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        doThrow(new RuntimeException("Errore DB"))
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

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricarica(request, response)
            );

            assertNotNull(exception.getCause());
            assertEquals(
                    "Errore DB",
                    exception.getCause().getMessage()
            );
        }

        verify(sessionFactory).beginTransaction();

        verify(sessionFactory).rollbackTransaction();

        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricarica_eccezioneSecondaFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(new RuntimeException("Errore seconda factory"))
                .when(daoFactory)
                .beginTransaction();

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
                    () -> UserManagement.ricarica(request, response)
            );

            assertNotNull(exception.getCause());
            assertEquals(
                    "Errore seconda factory",
                    exception.getCause().getMessage()
            );
        }

        verify(sessionFactory).beginTransaction();

        verify(daoFactory).beginTransaction();

        verify(daoFactory).rollbackTransaction();

        verify(sessionFactory).rollbackTransaction();

        verify(daoFactory).closeTransaction();

        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricarica_eccezioneDuranteRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        doThrow(new RuntimeException("Errore begin"))
                .when(sessionFactory)
                .beginTransaction();

        doThrow(new RuntimeException("Errore rollback"))
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

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricarica(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();

        verify(sessionFactory).rollbackTransaction();

        verify(sessionFactory).closeTransaction();
    }


    @Test
    void ricarica_eccezioneDuranteCloseTransaction() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        when(sessionFactory.getUtenteDAO())
                .thenThrow(new RuntimeException("Errore DB"));

        doThrow(new RuntimeException("Errore close"))
                .when(sessionFactory)
                .closeTransaction();

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionFactory);

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricarica(request, response)
            );
        }

        verify(sessionFactory).closeTransaction();
    }

    @Test
    void ricarica_erroreCreazioneSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> daoFactoryMock =
                     Mockito.mockStatic(DAOFactory.class)) {

            daoFactoryMock.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(new RuntimeException("Errore creazione factory"));

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.ricarica(request, response)
            );
        }
    }

    @Test
    void acquisto_utenteNonLoggato() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionFactory.getCarrelloDAO()).thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);
        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);

        when(sessionUserDAO.findLoggedUser()).thenReturn(null);

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

            assertDoesNotThrow(() ->
                    UserManagement.acquisto(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(daoFactory).beginTransaction();

        verify(sessionUserDAO).findLoggedUser();

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/login"
        );

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", false);
        verify(request).setAttribute("loggedUser", null);
        verify(request).setAttribute("applicationMessage", null);

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();

        verifyNoInteractions(
                utenteDAO,
                prodottoDAO,
                ordineDAO,
                dettagliOrdineDAO,
                couponDAO,
                sessionCarrelloDAO
        );
    }


    @Test
    void acquisto_utenteLoggato_prodottoNormale_senzaCoupon() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente loggedUser = mock(Utente.class);
        Carrello carrello = mock(Carrello.class);
        Utente carrelloUtente = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);
        Ordine ordine = mock(Ordine.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionFactory.getCarrelloDAO()).thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);
        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);

        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(loggedUser.getIdNome()).thenReturn(10L);
        when(loggedUser.getWallet()).thenReturn(100.0);
        when(loggedUser.getPassword()).thenReturn("password");
        when(loggedUser.getNome()).thenReturn("Mario");
        when(loggedUser.getCognome()).thenReturn("Rossi");
        when(loggedUser.getEmail()).thenReturn("mario@email.it");
        when(loggedUser.getTelefono()).thenReturn("3331234567");
        when(loggedUser.getStatoAccount()).thenReturn("attivo");
        when(loggedUser.getRuolo()).thenReturn(false);

        when(request.getParameter("Indirizzo"))
                .thenReturn("Via Roma 10");
        when(request.getParameter("Stato"))
                .thenReturn("Italia");
        when(request.getParameter("Citta"))
                .thenReturn("Catania");

        when(request.getParameterValues("coupon"))
                .thenReturn(null);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(List.of(carrello));

        when(carrello.getUtente())
                .thenReturn(carrelloUtente);
        when(carrelloUtente.getIdNome())
                .thenReturn(10L);

        when(carrello.getProdotto())
                .thenReturn(prodotto);
        when(carrello.getQta())
                .thenReturn(2);

        when(utenteDAO.findById(10L))
                .thenReturn(loggedUser);

        when(prodottoDAO.findById(20L))
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(20L);
        when(prodotto.getInPromo())
                .thenReturn(false);
        when(prodotto.getPrezzo())
                .thenReturn(50.0);
        when(prodotto.getQuantitaDispo())
                .thenReturn(10);

        when(prodottoDAO.findPromo())
                .thenReturn(new ArrayList<>());

        MarchioDAO marchioDAO = mock(MarchioDAO.class);
        when(daoFactory.getMarchioDAO()).thenReturn(marchioDAO);
        when(marchioDAO.findAll())
                .thenReturn(new ArrayList<>());

        when(ordineDAO.create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(100.0),
                eq("Via Roma 10"),
                eq("Catania"),
                eq("Italia")
        )).thenReturn(ordine);

        when(ordine.getIdOrdine())
                .thenReturn(100L);

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

            assertDoesNotThrow(() ->
                    UserManagement.acquisto(request, response)
            );
        }

        verify(ordineDAO).create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(100.0),
                eq("Via Roma 10"),
                eq("Catania"),
                eq("Italia")
        );

        verify(dettagliOrdineDAO).create(
                isNull(),
                eq(100L),
                eq(20L),
                eq(2),
                eq(50.0),
                isNull()
        );

        verify(sessionCarrelloDAO)
                .delete(10L, 20L);

        verify(utenteDAO)
                .updatewallet(10L, 0.0);

        verify(sessionUserDAO).create(
                eq(10L),
                eq("password"),
                eq("Mario"),
                eq("Rossi"),
                eq("mario@email.it"),
                eq("3331234567"),
                eq(0.0),
                eq("attivo"),
                eq(false)
        );

        verify(request).setAttribute(
                "Message",
                "Acquisto effettuato con successo"
        );

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/home"
        );

        verify(request).setAttribute("promo", true);

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void acquisto_utenteLoggato_couponValido_prodottoInPromo() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente loggedUser = mock(Utente.class);
        Carrello carrello = mock(Carrello.class);
        Utente carrelloUtente = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);
        Coupon couponDB = mock(Coupon.class);
        Ordine ordine = mock(Ordine.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionFactory.getCarrelloDAO()).thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);
        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getMarchioDAO()).thenReturn(marchioDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);
        when(loggedUser.getWallet())
                .thenReturn(100.0);
        when(loggedUser.getPassword())
                .thenReturn("password");
        when(loggedUser.getNome())
                .thenReturn("Mario");
        when(loggedUser.getCognome())
                .thenReturn("Rossi");
        when(loggedUser.getEmail())
                .thenReturn("mario@email.it");
        when(loggedUser.getTelefono())
                .thenReturn("3331234567");
        when(loggedUser.getStatoAccount())
                .thenReturn("attivo");
        when(loggedUser.getRuolo())
                .thenReturn(false);

        when(request.getParameter("Indirizzo"))
                .thenReturn("Via Roma 10");
        when(request.getParameter("Stato"))
                .thenReturn("Italia");
        when(request.getParameter("Citta"))
                .thenReturn("Catania");

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{"SCONTO10"});

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(List.of(carrello));

        when(carrello.getUtente())
                .thenReturn(carrelloUtente);
        when(carrelloUtente.getIdNome())
                .thenReturn(10L);

        when(carrello.getProdotto())
                .thenReturn(prodotto);
        when(carrello.getQta())
                .thenReturn(2);

        when(utenteDAO.findById(10L))
                .thenReturn(loggedUser);

        when(prodottoDAO.findById(20L))
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(20L);
        when(prodotto.getInPromo())
                .thenReturn(true);
        when(prodotto.getPrezzoSconto())
                .thenReturn(40.0);
        when(prodotto.getPrezzo())
                .thenReturn(50.0);
        when(prodotto.getQuantitaDispo())
                .thenReturn(10);

        when(couponDAO.findByCode("SCONTO10"))
                .thenReturn(couponDB);

        when(couponDB.getSconto())
                .thenReturn(10);

        when(prodottoDAO.findPromo())
                .thenReturn(new ArrayList<>());

        when(marchioDAO.findAll())
                .thenReturn(new ArrayList<>());

        when(ordineDAO.create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(72.0),
                eq("Via Roma 10"),
                eq("Catania"),
                eq("Italia")
        )).thenReturn(ordine);

        when(ordine.getIdOrdine())
                .thenReturn(100L);

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

            assertDoesNotThrow(() ->
                    UserManagement.acquisto(request, response)
            );
        }

        verify(sessionUserDAO).findLoggedUser();
        verify(sessionCarrelloDAO).findById(10L);

        verify(utenteDAO).findById(10L);
        verify(prodottoDAO).findById(20L);

        verify(couponDAO).findByCode("SCONTO10");

        verify(ordineDAO).create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(72.0),
                eq("Via Roma 10"),
                eq("Catania"),
                eq("Italia")
        );

        verify(dettagliOrdineDAO).create(
                isNull(),
                eq(100L),
                eq(20L),
                eq(2),
                eq(40.0),
                eq("SCONTO10")
        );

        verify(sessionCarrelloDAO)
                .delete(10L, 20L);

        verify(utenteDAO)
                .updatewallet(10L, 28.0);

        verify(sessionUserDAO).create(
                eq(10L),
                eq("password"),
                eq("Mario"),
                eq("Rossi"),
                eq("mario@email.it"),
                eq("3331234567"),
                eq(28.0),
                eq("attivo"),
                eq(false)
        );

        verify(request).setAttribute(
                "Message",
                "Acquisto effettuato con successo"
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


    @Test
    void acquisto_utenteLoggato_couponStringaNull_prodottoNormale() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente loggedUser = mock(Utente.class);
        Carrello carrello = mock(Carrello.class);
        Utente carrelloUtente = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);
        Ordine ordine = mock(Ordine.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionFactory.getCarrelloDAO()).thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);
        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);
        when(daoFactory.getMarchioDAO()).thenReturn(marchioDAO);

        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(loggedUser.getIdNome()).thenReturn(10L);
        when(loggedUser.getWallet()).thenReturn(100.0);
        when(loggedUser.getPassword()).thenReturn("password");
        when(loggedUser.getNome()).thenReturn("Mario");
        when(loggedUser.getCognome()).thenReturn("Rossi");
        when(loggedUser.getEmail()).thenReturn("mario@email.it");
        when(loggedUser.getTelefono()).thenReturn("3331234567");
        when(loggedUser.getStatoAccount()).thenReturn("attivo");
        when(loggedUser.getRuolo()).thenReturn(false);

        when(request.getParameter("Indirizzo")).thenReturn("Via Roma");
        when(request.getParameter("Stato")).thenReturn("Italia");
        when(request.getParameter("Citta")).thenReturn("Catania");

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{"null"});

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(List.of(carrello));

        when(carrello.getUtente()).thenReturn(carrelloUtente);
        when(carrelloUtente.getIdNome()).thenReturn(10L);

        when(carrello.getProdotto()).thenReturn(prodotto);
        when(carrello.getQta()).thenReturn(1);

        when(utenteDAO.findById(10L)).thenReturn(loggedUser);
        when(prodottoDAO.findById(20L)).thenReturn(prodotto);

        when(prodotto.getIdProdotto()).thenReturn(20L);
        when(prodotto.getInPromo()).thenReturn(false);
        when(prodotto.getPrezzo()).thenReturn(50.0);
        when(prodotto.getQuantitaDispo()).thenReturn(10);

        when(prodottoDAO.findPromo()).thenReturn(new ArrayList<>());
        when(marchioDAO.findAll()).thenReturn(new ArrayList<>());

        when(ordineDAO.create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(50.0),
                eq("Via Roma"),
                eq("Catania"),
                eq("Italia")
        )).thenReturn(ordine);

        when(ordine.getIdOrdine()).thenReturn(100L);

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

            assertDoesNotThrow(() ->
                    UserManagement.acquisto(request, response)
            );
        }

        verify(ordineDAO).create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(50.0),
                eq("Via Roma"),
                eq("Catania"),
                eq("Italia")
        );

        verify(dettagliOrdineDAO).create(
                isNull(),
                eq(100L),
                eq(20L),
                eq(1),
                eq(50.0),
                isNull()
        );

        verify(couponDAO, never()).findByCode(anyString());

        verify(sessionCarrelloDAO).delete(10L, 20L);

        verify(utenteDAO).updatewallet(10L, 50.0);

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void acquisto_quantitaNonDisponibile() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente loggedUser = mock(Utente.class);
        Carrello carrello = mock(Carrello.class);
        Utente carrelloUtente = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);

        when(sessionFactory.getUtenteDAO()).thenReturn(sessionUserDAO);
        when(sessionFactory.getCarrelloDAO()).thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO()).thenReturn(utenteDAO);
        when(daoFactory.getProdottoDAO()).thenReturn(prodottoDAO);
        when(daoFactory.getOrdineDAO()).thenReturn(ordineDAO);
        when(daoFactory.getDettagliOrdineDAO()).thenReturn(dettagliOrdineDAO);
        when(daoFactory.getCouponDAO()).thenReturn(couponDAO);

        when(sessionUserDAO.findLoggedUser()).thenReturn(loggedUser);

        when(loggedUser.getIdNome()).thenReturn(10L);
        when(loggedUser.getWallet()).thenReturn(1000.0);

        when(request.getParameterValues("coupon")).thenReturn(null);

        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(List.of(carrello));

        when(carrello.getUtente()).thenReturn(carrelloUtente);
        when(carrelloUtente.getIdNome()).thenReturn(10L);

        when(carrello.getProdotto()).thenReturn(prodotto);
        when(carrello.getQta()).thenReturn(5);

        when(utenteDAO.findById(10L)).thenReturn(loggedUser);
        when(prodottoDAO.findById(20L)).thenReturn(prodotto);

        when(prodotto.getIdProdotto()).thenReturn(20L);
        when(prodotto.getInPromo()).thenReturn(false);
        when(prodotto.getPrezzo()).thenReturn(20.0);

        when(prodotto.getQuantitaDispo()).thenReturn(2);

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

            assertDoesNotThrow(() ->
                    UserManagement.acquisto(request, response)
            );
        }

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/carrello"
        );

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Le quantità selezionate non sono più disponibili"
        );

        verify(ordineDAO, never()).create(
                anyLong(),
                anyLong(),
                anyString(),
                any(),
                anyDouble(),
                anyString(),
                anyString(),
                anyString()
        );

        verify(utenteDAO, never())
                .updatewallet(anyLong(), anyDouble());

        verify(sessionCarrelloDAO, never())
                .delete(anyLong(), anyLong());

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }



    @Test
    void acquisto_creditoInsufficiente() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente loggedUser = mock(Utente.class);
        Carrello carrello = mock(Carrello.class);
        Utente carrelloUtente = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);


        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);


        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);


        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);


        when(loggedUser.getWallet())
                .thenReturn(30.0);


        when(request.getParameterValues("coupon"))
                .thenReturn(null);


        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(List.of(carrello));

        when(carrello.getUtente())
                .thenReturn(carrelloUtente);

        when(carrelloUtente.getIdNome())
                .thenReturn(10L);

        when(carrello.getProdotto())
                .thenReturn(prodotto);

        when(carrello.getQta())
                .thenReturn(2);


        when(prodotto.getIdProdotto())
                .thenReturn(20L);

        when(prodotto.getInPromo())
                .thenReturn(false);

        when(prodotto.getPrezzo())
                .thenReturn(50.0);

        when(prodotto.getQuantitaDispo())
                .thenReturn(10);

        // DAO restituiscono gli stessi mock
        when(utenteDAO.findById(10L))
                .thenReturn(loggedUser);

        when(prodottoDAO.findById(20L))
                .thenReturn(prodotto);



        try (
                MockedStatic<DAOFactory> mockedDAOFactory =
                        Mockito.mockStatic(DAOFactory.class);

                MockedStatic<UserManagement> mockedUserManagement =
                        Mockito.mockStatic(
                                UserManagement.class,
                                Mockito.CALLS_REAL_METHODS
                        )
        ) {


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
                    UserManagement.acquisto(request, response)
            );
        }

        verify(request, times(2)).setAttribute(
                "applicationMessage",
                "Impossibile procedere con il checkout, credito non disponibile!"
        );



        verify(ordineDAO, never()).create(
                anyLong(),
                anyLong(),
                anyString(),
                any(),
                anyDouble(),
                anyString(),
                anyString(),
                anyString()
        );



        verify(dettagliOrdineDAO, never()).create(
                any(),
                any(),
                any(),
                anyInt(),
                anyDouble(),
                anyString()
        );



        verify(utenteDAO, never())
                .updatewallet(anyLong(), anyDouble());


        verify(sessionCarrelloDAO, never())
                .delete(anyLong(), anyLong());


        verify(daoFactory, times(2))
                .commitTransaction();


        verify(daoFactory, times(2)).commitTransaction();


        verify(daoFactory, times(2)).closeTransaction();

        verify(sessionFactory,times(2))
                .closeTransaction();
    }



    @Test
    void acquisto_eccezione_rollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenThrow(new RuntimeException("Errore di test"));

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

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
                    () -> UserManagement.acquisto(request, response)
            );
        }
        verify(sessionFactory).beginTransaction();
        verify(sessionUserDAO).findLoggedUser();
        verify(daoFactory).beginTransaction();
        verify(daoFactory).getUtenteDAO();
        verify(daoFactory).rollbackTransaction();
        verify(sessionFactory).rollbackTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
        verify(daoFactory, never()).commitTransaction();
        verify(sessionFactory, never()).commitTransaction();
    }


    @Test
    void acquisto_eccezione_prima_creazione_factory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(new RuntimeException("Errore creazione factory"));

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.acquisto(request, response)
            );
        }

        verify(sessionFactory, never()).rollbackTransaction();
        verify(sessionFactory, never()).closeTransaction();
    }


    @Test
    void acquisto_eccezione_durante_rollback() {

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

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(daoFactory.getUtenteDAO())
                .thenThrow(new RuntimeException("Errore principale"));

        doThrow(new RuntimeException("Errore durante rollback"))
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

            assertThrows(RuntimeException.class, () ->
                    UserManagement.acquisto(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(daoFactory).beginTransaction();
        verify(daoFactory).rollbackTransaction();
        verify(sessionDAOFactory, never()).rollbackTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }



    @Test
    void acquisto_eccezione_durante_close() {

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

        when(loggedUser.getIdNome())
                .thenReturn(10L);


        when(daoFactory.getUtenteDAO())
                .thenThrow(new RuntimeException("Errore principale"));


        doThrow(new RuntimeException("Errore durante close"))
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

            assertThrows(RuntimeException.class, () ->
                    UserManagement.acquisto(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(daoFactory).beginTransaction();
        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory, never()).closeTransaction();
    }


    @Test
    void acquisto_utenteLoggato_couponNull_prodottoNormale() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        UtenteDAO utenteDAO = mock(UtenteDAO.class);
        CarrelloDAO sessionCarrelloDAO = mock(CarrelloDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);
        MarchioDAO marchioDAO = mock(MarchioDAO.class);

        Utente loggedUser = mock(Utente.class);
        Carrello carrello = mock(Carrello.class);
        Utente carrelloUtente = mock(Utente.class);
        Prodotto prodotto = mock(Prodotto.class);
        Ordine ordine = mock(Ordine.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionFactory.getCarrelloDAO())
                .thenReturn(sessionCarrelloDAO);

        when(daoFactory.getUtenteDAO())
                .thenReturn(utenteDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(daoFactory.getMarchioDAO())
                .thenReturn(marchioDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(loggedUser.getWallet())
                .thenReturn(100.0);

        when(loggedUser.getPassword())
                .thenReturn("password");

        when(loggedUser.getNome())
                .thenReturn("Mario");

        when(loggedUser.getCognome())
                .thenReturn("Rossi");

        when(loggedUser.getEmail())
                .thenReturn("mario@email.it");

        when(loggedUser.getTelefono())
                .thenReturn("3331234567");

        when(loggedUser.getStatoAccount())
                .thenReturn("attivo");

        when(loggedUser.getRuolo())
                .thenReturn(false);

        when(request.getParameter("Indirizzo"))
                .thenReturn("Via Roma");

        when(request.getParameter("Stato"))
                .thenReturn("Italia");

        when(request.getParameter("Citta"))
                .thenReturn("Catania");


        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{null});


        when(sessionCarrelloDAO.findById(10L))
                .thenReturn(List.of(carrello));

        when(carrello.getUtente())
                .thenReturn(carrelloUtente);

        when(carrelloUtente.getIdNome())
                .thenReturn(10L);

        when(carrello.getProdotto())
                .thenReturn(prodotto);

        when(carrello.getQta())
                .thenReturn(1);


        when(utenteDAO.findById(10L))
                .thenReturn(loggedUser);

        when(prodottoDAO.findById(20L))
                .thenReturn(prodotto);

        when(prodotto.getIdProdotto())
                .thenReturn(20L);

        when(prodotto.getInPromo())
                .thenReturn(false);

        when(prodotto.getPrezzo())
                .thenReturn(50.0);

        when(prodotto.getQuantitaDispo())
                .thenReturn(10);

        when(prodottoDAO.findPromo())
                .thenReturn(new ArrayList<>());

        when(marchioDAO.findAll())
                .thenReturn(new ArrayList<>());


        when(ordineDAO.create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(50.0),
                eq("Via Roma"),
                eq("Catania"),
                eq("Italia")
        )).thenReturn(ordine);

        when(ordine.getIdOrdine())
                .thenReturn(100L);

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

            assertDoesNotThrow(() ->
                    UserManagement.acquisto(request, response)
            );
        }


        verify(couponDAO, never())
                .findByCode(anyString());


        verify(ordineDAO).create(
                anyLong(),
                eq(10L),
                anyString(),
                isNull(),
                eq(50.0),
                eq("Via Roma"),
                eq("Catania"),
                eq("Italia")
        );


        verify(dettagliOrdineDAO).create(
                isNull(),
                eq(100L),
                eq(20L),
                eq(1),
                eq(50.0),
                isNull()
        );


        verify(sessionCarrelloDAO)
                .delete(10L, 20L);


        verify(utenteDAO)
                .updatewallet(10L, 50.0);


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
    void viewordini_utenteNonLoggato() {

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

            assertDoesNotThrow(() ->
                    UserManagement.viewordini(request, response)
            );
        }

        verify(sessionFactory).beginTransaction();
        verify(sessionUserDAO).findLoggedUser();

        verify(request).setAttribute(
                "viewUrl",
                "HomeManagement/login"
        );

        verify(daoFactory).beginTransaction();

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", false);
        verify(request).setAttribute("loggedUser", null);
        verify(request).setAttribute("applicationMessage", null);

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewordini_utenteLoggato_nessunOrdine() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(loggedUser.getIdNome())
                .thenReturn(10L);

        when(ordineDAO.findByUser(10L))
                .thenReturn(new ArrayList<>());

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

            assertDoesNotThrow(() ->
                    UserManagement.viewordini(request, response)
            );
        }

        verify(sessionUserDAO).findLoggedUser();

        verify(ordineDAO).findByUser(10L);

        verify(request).setAttribute(
                "viewUrl",
                "UserManagement/ordini"
        );

        verify(request).setAttribute(
                "ordini",
                new ArrayList<>()
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

        verify(dettagliOrdineDAO, never())
                .findByIdOrdine(anyLong());

        verify(couponDAO, never())
                .findByCode(anyString());

        verify(prodottoDAO, never())
                .findById(anyLong());

        verify(daoFactory).commitTransaction();
        verify(sessionFactory).commitTransaction();

        verify(daoFactory).closeTransaction();
        verify(sessionFactory).closeTransaction();
    }


    @Test
    void viewordini_couponPresente() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);


        Utente user = mock(Utente.class);

        when(user.getIdNome()).thenReturn(1L);
        when(sessionUserDAO.findLoggedUser()).thenReturn(user);

        Coupon couponAssociato = new Coupon();

        couponAssociato.setCodice("SCONTO10");


        Coupon couponTrovato = new Coupon();

        couponTrovato.setSconto(10);

        when(couponDAO.findByCode("SCONTO10"))
                .thenReturn(couponTrovato);


        Prodotto prodotto = new Prodotto();

        prodotto.setIdProdotto(1L);

        when(prodottoDAO.findById(1L))
                .thenReturn(prodotto);


        DettagliOrdine dettaglio = new DettagliOrdine();

        dettaglio.setCoupon(couponAssociato);
        dettaglio.setProdotto(prodotto);
        dettaglio.setPrezzoUnitario(100.0);

        List<DettagliOrdine> dettagli = new ArrayList<>();
        dettagli.add(dettaglio);


        Ordine ordine = new Ordine();

        ordine.setIdOrdine(1L);

        List<Ordine> ordini = new ArrayList<>();
        ordini.add(ordine);


        when(ordineDAO.findByUser(1L))
                .thenReturn(ordini);

        when(dettagliOrdineDAO.findByIdOrdine(1L))
                .thenReturn(dettagli);


        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

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


            UserManagement.viewordini(request, response);


            verify(sessionDAOFactory)
                    .beginTransaction();

            verify(daoFactory)
                    .beginTransaction();


            verify(sessionUserDAO)
                    .findLoggedUser();

            verify(ordineDAO)
                    .findByUser(1L);

            verify(dettagliOrdineDAO)
                    .findByIdOrdine(1L);


            verify(couponDAO)
                    .findByCode("SCONTO10");


            assertEquals(
                    90.0,
                    dettaglio.getPrezzoUnitario()
            );


            verify(prodottoDAO)
                    .findById(1L);


            verify(request).setAttribute(
                    "viewUrl",
                    "UserManagement/ordini"
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
                    user
            );

            verify(request).setAttribute(
                    "applicationMessage",
                    null
            );


            verify(daoFactory)
                    .commitTransaction();

            verify(sessionDAOFactory)
                    .commitTransaction();


            verify(daoFactory)
                    .closeTransaction();

            verify(sessionDAOFactory)
                    .closeTransaction();
        }
    }


    @Test
    void viewordini_couponNonPresente() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        OrdineDAO ordineDAO = mock(OrdineDAO.class);
        DettagliOrdineDAO dettagliOrdineDAO = mock(DettagliOrdineDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);
        CouponDAO couponDAO = mock(CouponDAO.class);


        Utente user = mock(Utente.class);

        when(user.getIdNome()).thenReturn(1L);
        when(sessionUserDAO.findLoggedUser()).thenReturn(user);


        Coupon couponAssociato = new Coupon();
        couponAssociato.setCodice("SCONTO10");


        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(1L);


        DettagliOrdine dettaglio = new DettagliOrdine();

        dettaglio.setCoupon(couponAssociato);
        dettaglio.setProdotto(prodotto);
        dettaglio.setPrezzoUnitario(100.0);

        List<DettagliOrdine> dettagli = new ArrayList<>();
        dettagli.add(dettaglio);


        Ordine ordine = new Ordine();
        ordine.setIdOrdine(1L);

        List<Ordine> ordini = new ArrayList<>();
        ordini.add(ordine);


        when(ordineDAO.findByUser(1L))
                .thenReturn(ordini);

        when(dettagliOrdineDAO.findByIdOrdine(1L))
                .thenReturn(dettagli);

        when(couponDAO.findByCode("SCONTO10"))
                .thenReturn(null);

        when(prodottoDAO.findById(1L))
                .thenReturn(prodotto);


        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(daoFactory.getOrdineDAO())
                .thenReturn(ordineDAO);

        when(daoFactory.getDettagliOrdineDAO())
                .thenReturn(dettagliOrdineDAO);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(daoFactory.getCouponDAO())
                .thenReturn(couponDAO);

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

            UserManagement.viewordini(request, response);

            verify(couponDAO)
                    .findByCode("SCONTO10");

            assertEquals(
                    100.0,
                    dettaglio.getPrezzoUnitario()
            );

            verify(prodottoDAO)
                    .findById(1L);


            verify(sessionDAOFactory)
                    .beginTransaction();

            verify(daoFactory)
                    .beginTransaction();

            verify(daoFactory)
                    .commitTransaction();

            verify(sessionDAOFactory)
                    .commitTransaction();

            verify(daoFactory)
                    .closeTransaction();

            verify(sessionDAOFactory)
                    .closeTransaction();


            verify(request).setAttribute(
                    "viewUrl",
                    "UserManagement/ordini"
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
                    user
            );

            verify(request).setAttribute(
                    "applicationMessage",
                    null
            );
        }
    }


    @Test
    void viewordini_eccezione_rollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenThrow(new RuntimeException("errore test"));

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

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewordini(request, response)
            );
        }


        verify(sessionDAOFactory)
                .beginTransaction();


        verify(daoFactory, never())
                .beginTransaction();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(daoFactory, never())
                .rollbackTransaction();


        verify(sessionDAOFactory)
                .closeTransaction();

        verify(daoFactory, never())
                .closeTransaction();
    }


    @Test
    void viewordini_eccezione_durante_closeTransaction() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        doThrow(new RuntimeException("errore close"))
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
                    UserManagement.viewordini(request, response)
            );
        }

        verify(sessionDAOFactory)
                .beginTransaction();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(daoFactory)
                .beginTransaction();

        verify(daoFactory)
                .commitTransaction();

        verify(sessionDAOFactory)
                .commitTransaction();


        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory, never())
                .closeTransaction();
    }


    @Test
    void viewordini_eccezione_daoFactoryRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(mock(Utente.class));

        when(daoFactory.getOrdineDAO())
                .thenThrow(new RuntimeException("errore test"));

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

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewordini(request, response)
            );
        }


        verify(sessionDAOFactory)
                .beginTransaction();

        verify(daoFactory)
                .beginTransaction();


        verify(daoFactory)
                .rollbackTransaction();


        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }


    @Test
    void viewordini_sessionFactoryCloseNelCatch() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);


        when(sessionUserDAO.findLoggedUser())
                .thenThrow(new RuntimeException("errore test"));

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);


            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewordini(request, response)
            );
        }

        verify(sessionDAOFactory)
                .beginTransaction();

        verify(sessionUserDAO)
                .findLoggedUser();


        verify(sessionDAOFactory)
                .rollbackTransaction();


        verify(sessionDAOFactory)
                .closeTransaction();
    }


    @Test
    void viewordini_eccezioneDuranteRollback() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(mock(Utente.class));


        when(daoFactory.getOrdineDAO())
                .thenThrow(new RuntimeException("errore principale"));


        doThrow(new RuntimeException("errore rollback"))
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

            assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewordini(request, response)
            );
        }

        verify(sessionDAOFactory)
                .beginTransaction();

        verify(daoFactory)
                .beginTransaction();


        verify(daoFactory)
                .rollbackTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }

    @Test
    void viewordini_eccezioneCreazioneSessionFactory() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {


            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenThrow(
                    new RuntimeException("errore creazione session factory")
            );

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> UserManagement.viewordini(request, response)
            );

            assertEquals(
                    "errore creazione session factory",
                    ex.getCause().getMessage()
            );
        }


    }


}