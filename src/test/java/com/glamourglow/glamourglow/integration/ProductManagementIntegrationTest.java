package com.glamourglow.glamourglow.integration;
import com.glamourglow.glamourglow.controller.ProductManagement;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;
import com.glamourglow.glamourglow.services.config.Configuration;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductManagementIntegrationTest {

    private DAOFactory daoFactory;

    private long testUserId;
    private long testProductId;

    private static final String USER_EMAIL =
            "integration.product.user@glamourglow.test";

    private static final String PRODUCT_NAME =
            "Integration Product Management Test";

    private static final long TEST_CATEGORY_ID = 2007L;
    private static final long TEST_BRAND_ID = 3001L;


    @BeforeEach
    void setUp() {

        cleanupDatabase();
        DAOFactory factory = null;

        try {

            factory = DAOFactory.getDAOFactory(
                    Configuration.DAO_IMPL,
                    null
            );

            factory.beginTransaction();

            UtenteDAO utenteDAO = factory.getUtenteDAO();

            utenteDAO.create(
                    0,
                    "test",
                    "Product",
                    "Integration",
                    USER_EMAIL,
                    "3333333333",
                    100.0,
                    "attivo",
                    false
            );

            factory.commitTransaction();

        } catch (Exception e) {

            if (factory != null) {
                try {
                    factory.rollbackTransaction();
                } catch (Throwable ignored) {
                }
            }

            throw new RuntimeException(
                    "Errore nella creazione dell'utente di test",
                    e
            );

        } finally {

            if (factory != null) {
                try {
                    factory.closeTransaction();
                } catch (Throwable ignored) {
                }
            }
        }

        factory = null;

        try {

            factory = DAOFactory.getDAOFactory(
                    Configuration.DAO_IMPL,
                    null
            );

            factory.beginTransaction();

            UtenteDAO utenteDAO = factory.getUtenteDAO();

            Utente testUser = utenteDAO.findEmail(USER_EMAIL);

            factory.commitTransaction();

            assertNotNull(
                    testUser,
                    "L'utente di test deve essere presente nel database"
            );

            testUserId = testUser.getIdNome();

        } catch (Exception e) {

            if (factory != null) {
                try {
                    factory.rollbackTransaction();
                } catch (Throwable ignored) {
                }
            }

            throw new RuntimeException(
                    "Errore nel recupero dell'utente di test",
                    e
            );

        } finally {

            if (factory != null) {
                try {
                    factory.closeTransaction();
                } catch (Throwable ignored) {
                }
            }
        }

        factory = null;

        try {

            factory = DAOFactory.getDAOFactory(
                    Configuration.DAO_IMPL,
                    null
            );

            factory.beginTransaction();

            ProdottoDAO prodottoDAO = factory.getProdottoDAO();

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(TEST_CATEGORY_ID);

            Marchio marchio = new Marchio();
            marchio.setIdMarchio(TEST_BRAND_ID);

            Prodotto prodotto = new Prodotto();

            prodotto.setNomeProdotto(PRODUCT_NAME);
            prodotto.setDescrizione(
                    "Prodotto creato per integration test ProductManagement"
            );
            prodotto.setPrezzo(50.0);
            prodotto.setQuantitaDispo(10);
            prodotto.setCategoria(categoria);
            prodotto.setMarchio(marchio);
            prodotto.setInPromo(false);
            prodotto.setImmagine("");
            prodotto.setPrezzoSconto(50.0);
            prodotto.setStatoprodotto(false);

            prodottoDAO.create(prodotto);

            factory.commitTransaction();

        } catch (Exception e) {

            if (factory != null) {
                try {
                    factory.rollbackTransaction();
                } catch (Throwable ignored) {
                }
            }

            throw new RuntimeException(
                    "Errore nella creazione del prodotto di test",
                    e
            );

        } finally {

            if (factory != null) {
                try {
                    factory.closeTransaction();
                } catch (Throwable ignored) {
                }
            }
        }

        factory = null;

        try {

            factory = DAOFactory.getDAOFactory(
                    Configuration.DAO_IMPL,
                    null
            );

            factory.beginTransaction();

            ProdottoDAO prodottoDAO = factory.getProdottoDAO();

            java.util.List<Prodotto> prodotti =
                    prodottoDAO.cercaadmin(
                            PRODUCT_NAME,
                            "",
                            ""
                    );

            factory.commitTransaction();

            assertNotNull(
                    prodotti,
                    "La lista dei prodotti non deve essere null"
            );

            assertFalse(
                    prodotti.isEmpty(),
                    "Il prodotto di test deve essere presente nel database"
            );

            Prodotto prodottoTrovato = null;

            for (Prodotto prodotto : prodotti) {
                if (PRODUCT_NAME.equals(prodotto.getNomeProdotto())) {
                    prodottoTrovato = prodotto;
                    break;
                }
            }

            assertNotNull(
                    prodottoTrovato,
                    "Il prodotto di test deve essere recuperabile"
            );

            testProductId = prodottoTrovato.getIdProdotto();

        } catch (Exception e) {

            if (factory != null) {
                try {
                    factory.rollbackTransaction();
                } catch (Throwable ignored) {
                }
            }

            throw new RuntimeException(
                    "Errore nel recupero del prodotto di test",
                    e
            );

        } finally {

            if (factory != null) {
                try {
                    factory.closeTransaction();
                } catch (Throwable ignored) {
                }
            }
        }
    }



    private void cleanupDatabase() {


        try {

            Class.forName(Configuration.DATABASE_DRIVER);

            try (Connection conn =
                         DriverManager.getConnection(
                                 Configuration.DATABASE_URL
                         )) {

                String sql =
                        "DELETE FROM prodotto WHERE nome_prodotto = ?";

                try (PreparedStatement ps =
                             conn.prepareStatement(sql)) {

                    ps.setString(1, PRODUCT_NAME);
                    ps.executeUpdate();
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nella cancellazione del prodotto di test",
                    e
            );
        }

        try {

            Class.forName(Configuration.DATABASE_DRIVER);

            try (Connection conn =
                         DriverManager.getConnection(
                                 Configuration.DATABASE_URL
                         )) {

                String sql =
                        "DELETE FROM utente WHERE email = ?";

                try (PreparedStatement ps =
                             conn.prepareStatement(sql)) {

                    ps.setString(1, USER_EMAIL);
                    ps.executeUpdate();
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nella cancellazione dell'utente di test",
                    e
            );
        }


        daoFactory = null;
        testUserId = 0;
        testProductId = 0;
    }

    private HttpServletRequest createRequest(Cookie... cookies) {

        HttpServletRequest request =
                Mockito.mock(HttpServletRequest.class);

        Map<String, Object> attributes = new HashMap<>();

        Mockito.when(request.getCookies())
                .thenReturn(cookies);

        Mockito.when(request.getAttribute(Mockito.anyString()))
                .thenAnswer(invocation ->
                        attributes.get(invocation.getArgument(0)));

        Mockito.doAnswer(invocation -> {

            String name = invocation.getArgument(0);
            Object value = invocation.getArgument(1);

            attributes.put(name, value);

            return null;

        }).when(request).setAttribute(
                Mockito.anyString(),
                Mockito.any()
        );

        return request;
    }


    private Cookie userCookie() {

        return new Cookie(
                "loggedUser",
                testUserId
                        + "#test"
                        + "#Product"
                        + "#Integration"
                        + "#false"
                        + "#100.0"
                        + "#attivo"
        );
    }


    @Test
    void viewprodotto_utenteLoggato_prodottoEsistente() {

        HttpServletRequest request =
                createRequest(userCookie());

        HttpServletResponse response =
                Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("id"))
                .thenReturn(String.valueOf(testProductId));


        ProductManagement.viewprodotto(
                request,
                response
        );


        Boolean loggedOn =
                (Boolean) request.getAttribute("loggedOn");

        assertNotNull(
                loggedOn,
                "loggedOn deve essere valorizzato"
        );

        assertTrue(
                loggedOn,
                "L'utente con cookie valido deve risultare autenticato"
        );

        Utente loggedUser =
                (Utente) request.getAttribute("loggedUser");

        assertNotNull(
                loggedUser,
                "loggedUser deve essere valorizzato"
        );

        assertEquals(
                testUserId,
                loggedUser.getIdNome()
        );


        Prodotto prodotto =
                (Prodotto) request.getAttribute("prodotto");

        assertNotNull(
                prodotto,
                "Il prodotto deve essere recuperato dal database"
        );

        assertEquals(
                testProductId,
                prodotto.getIdProdotto()
        );

        assertEquals(
                PRODUCT_NAME,
                prodotto.getNomeProdotto()
        );

        assertEquals(
                50.0,
                prodotto.getPrezzo(),
                0.001
        );

        assertEquals(
                "ProductManagement/descrizione",
                request.getAttribute("viewUrl")
        );

        assertNull(
                request.getAttribute("applicationMessage")
        );
    }


    @Test
    void viewprodotto_utenteNonAutenticato_prodottoEsistente() {

        HttpServletRequest request =
                createRequest();

        HttpServletResponse response =
                Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("id"))
                .thenReturn(String.valueOf(testProductId));

        ProductManagement.viewprodotto(
                request,
                response
        );

        Boolean loggedOn =
                (Boolean) request.getAttribute("loggedOn");

        assertNotNull(
                loggedOn,
                "loggedOn deve essere valorizzato"
        );

        assertFalse(
                loggedOn,
                "Senza cookie l'utente non deve risultare autenticato"
        );

        assertNull(
                request.getAttribute("loggedUser")
        );

        Prodotto prodotto =
                (Prodotto) request.getAttribute("prodotto");

        assertNotNull(
                prodotto,
                "Il prodotto deve essere recuperato anche senza autenticazione"
        );

        assertEquals(
                testProductId,
                prodotto.getIdProdotto()
        );

        assertEquals(
                PRODUCT_NAME,
                prodotto.getNomeProdotto()
        );

        assertEquals(
                "ProductManagement/descrizione",
                request.getAttribute("viewUrl")
        );
    }

    @Test
    void viewprodotto_idInesistente_restituisceProdottoNull() {

        long idInesistente = 999999999L;

        HttpServletRequest request =
                createRequest();

        HttpServletResponse response =
                Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("id"))
                .thenReturn(String.valueOf(idInesistente));

        ProductManagement.viewprodotto(
                request,
                response
        );

        Prodotto prodotto =
                (Prodotto) request.getAttribute("prodotto");

        assertNull(
                prodotto,
                "Per un ID inesistente findById deve restituire null"
        );

        assertEquals(
                "ProductManagement/descrizione",
                request.getAttribute("viewUrl")
        );

        Boolean loggedOn =
                (Boolean) request.getAttribute("loggedOn");

        assertNotNull(loggedOn);

        assertFalse(
                loggedOn,
                "Senza cookie l'utente deve risultare non autenticato"
        );

        assertNull(
                request.getAttribute("loggedUser")
        );
    }


    @Test
    void viewprodotto_idNonNumerico_lanciaRuntimeException() {

        HttpServletRequest request =
                createRequest();

        HttpServletResponse response =
                Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("id"))
                .thenReturn("abc");

        assertThrows(
                RuntimeException.class,
                () -> ProductManagement.viewprodotto(
                        request,
                        response
                ),
                "Un ID non numerico deve generare una RuntimeException"
        );
    }
}

