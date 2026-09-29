
package com.glamourglow.glamourglow.controller;

import com.glamourglow.glamourglow.model.dao.DAOFactory;
import com.glamourglow.glamourglow.model.dao.ProdottoDAO;
import com.glamourglow.glamourglow.model.dao.UtenteDAO;
import com.glamourglow.glamourglow.model.mo.Prodotto;
import com.glamourglow.glamourglow.model.mo.Utente;
import com.glamourglow.glamourglow.services.config.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Provide;


public class ProductManagementTest {

    @Provide
    Arbitrary<Long> idProdotto() {
        return Arbitraries.longs()
                .between(1L, 1000L);
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
    void viewprodotto_utenteLoggato_prodottoPresente(
            @ForAll("idProdotto") long idProdotto) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);
        Prodotto prodotto = new Prodotto();

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idProdotto));

        when(prodottoDAO.findById((int) idProdotto))
                .thenReturn(prodotto);

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
                    ProductManagement.viewprodotto(request, response)
            );
        }

        verify(sessionDAOFactory).beginTransaction();
        verify(sessionDAOFactory).getUtenteDAO();
        verify(sessionUserDAO).findLoggedUser();

        verify(daoFactory).beginTransaction();
        verify(daoFactory).getProdottoDAO();

        verify(request).getParameter("id");

        verify(prodottoDAO).findById((int) idProdotto);

        verify(daoFactory).commitTransaction();
        verify(sessionDAOFactory).commitTransaction();

        verify(request).setAttribute("loggedOn", true);
        verify(request).setAttribute("loggedUser", loggedUser);
        verify(request).setAttribute("applicationMessage", null);
        verify(request).setAttribute("prodotto", prodotto);
        verify(request).setAttribute(
                "viewUrl",
                "ProductManagement/descrizione"
        );

        verify(daoFactory).closeTransaction();
        verify(sessionDAOFactory).closeTransaction();
    }



    @Property
    void viewprodotto_utenteNonLoggato_prodottoPresente(
            @ForAll("idProdotto") long idProdotto) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Prodotto prodotto = new Prodotto();

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idProdotto));

        when(prodottoDAO.findById((int) idProdotto))
                .thenReturn(prodotto);

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
                    ProductManagement.viewprodotto(request, response)
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

        verify(request)
                .getParameter("id");

        verify(prodottoDAO)
                .findById((int) idProdotto);

        verify(daoFactory)
                .commitTransaction();

        verify(sessionDAOFactory)
                .commitTransaction();

        verify(request)
                .setAttribute("loggedOn", false);

        verify(request)
                .setAttribute("loggedUser", null);

        verify(request)
                .setAttribute("applicationMessage", null);

        verify(request)
                .setAttribute("prodotto", prodotto);

        verify(request)
                .setAttribute(
                        "viewUrl",
                        "ProductManagement/descrizione"
                );

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }



    @Property
    void viewprodotto_erroreDuranteFindById(
            @ForAll("idProdotto") long idProdotto,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idProdotto));

        when(prodottoDAO.findById((int) idProdotto))
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

            RuntimeException thrown = assertThrows(
                    RuntimeException.class,
                    () -> ProductManagement.viewprodotto(
                            request, response)
            );

            assertSame(eccezioneOriginale, thrown.getCause());
        }

        verify(sessionDAOFactory)
                .beginTransaction();

        verify(sessionDAOFactory)
                .getUtenteDAO();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(daoFactory)
                .beginTransaction();

        verify(prodottoDAO)
                .findById((int) idProdotto);

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
    void viewprodotto_erroreDuranteRollback(
            @ForAll("idProdotto") long idProdotto,
            @ForAll("eccezioniRuntime") RuntimeException eccezionePrincipale,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneRollback) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        Utente loggedUser = mock(Utente.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(loggedUser);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idProdotto));

        when(prodottoDAO.findById((int) idProdotto))
                .thenThrow(eccezionePrincipale);

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

            RuntimeException thrown = assertThrows(
                    RuntimeException.class,
                    () -> ProductManagement.viewprodotto(
                            request, response)
            );

            assertSame(
                    eccezionePrincipale,
                    thrown.getCause()
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

        verify(prodottoDAO)
                .findById((int) idProdotto);

        verify(daoFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory, never())
                .rollbackTransaction();

        verify(daoFactory, never())
                .commitTransaction();

        verify(sessionDAOFactory, never())
                .commitTransaction();

        verify(daoFactory)
                .closeTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }


    @Property
    void viewprodotto_erroreDuranteCloseTransaction(
            @ForAll("idProdotto") long idProdotto,
            @ForAll("eccezioniRuntime") RuntimeException eccezioneClose) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);
        DAOFactory daoFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);
        ProdottoDAO prodottoDAO = mock(ProdottoDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenReturn(null);

        when(daoFactory.getProdottoDAO())
                .thenReturn(prodottoDAO);

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(idProdotto));

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
                    ProductManagement.viewprodotto(
                            request, response
                    )
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

        verify(prodottoDAO)
                .findById((int) idProdotto);

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
    void viewprodotto_erroreDuranteFindLoggedUser(
            @ForAll("eccezioniRuntime") RuntimeException eccezioneOriginale) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory sessionDAOFactory = mock(DAOFactory.class);

        UtenteDAO sessionUserDAO = mock(UtenteDAO.class);

        when(sessionDAOFactory.getUtenteDAO())
                .thenReturn(sessionUserDAO);

        when(sessionUserDAO.findLoggedUser())
                .thenThrow(eccezioneOriginale);

        try (MockedStatic<DAOFactory> mockedDAOFactory =
                     Mockito.mockStatic(DAOFactory.class)) {

            mockedDAOFactory.when(() ->
                    DAOFactory.getDAOFactory(
                            eq(Configuration.COOKIE_IMPL),
                            anyMap()
                    )
            ).thenReturn(sessionDAOFactory);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> ProductManagement.viewprodotto(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    exception.getCause()
            );
        }

        verify(sessionDAOFactory)
                .beginTransaction();

        verify(sessionDAOFactory)
                .getUtenteDAO();

        verify(sessionUserDAO)
                .findLoggedUser();

        verify(sessionDAOFactory)
                .rollbackTransaction();

        verify(sessionDAOFactory)
                .closeTransaction();
    }

    @Property
    void viewprodotto_erroreCreazioneSessionDAOFactory(
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

            RuntimeException thrown = assertThrows(
                    RuntimeException.class,
                    () -> ProductManagement.viewprodotto(
                            request, response)
            );

            assertSame(
                    eccezioneOriginale,
                    thrown.getCause()
            );
        }
    }

}
