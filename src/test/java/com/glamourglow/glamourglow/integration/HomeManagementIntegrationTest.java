
package com.glamourglow.glamourglow.integration;

import com.glamourglow.glamourglow.controller.HomeManagement;
import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;
import com.glamourglow.glamourglow.services.config.Configuration;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HomeManagementIntegrationTest {

    private static final String TEST_EMAIL =
            "integration.home@glamourglow.test";

    private static final String BLOCKED_EMAIL =
            "integration.home.blocked@glamourglow.test";

    private static final String PRODUCT_PROMO_NAME =
            "Integration Home Promo Product";

    private static final String PRODUCT_NORMAL_NAME =
            "Integration Home Normal Product";

    private static final String TEST_PASSWORD =
            "test_password";

    private Long testUserId;
    private Long blockedUserId;

    private Long promoProductId;
    private Long normalProductId;

    private Long categoryId;
    private Long brandId;

    private String registrationEmail;


    @BeforeEach
    void setUp() throws Exception {

        cleanupDatabase();

        DAOFactory daoFactory =
                DAOFactory.getDAOFactory(
                        Configuration.DAO_IMPL,
                        null
                );

        daoFactory.beginTransaction();

        try {

            CategoriaDAO categoriaDAO =
                    daoFactory.getCategoriaDAO();

            MarchioDAO marchioDAO =
                    daoFactory.getMarchioDAO();

            List<Categoria> categorie =
                    categoriaDAO.findAll();

            List<Marchio> marchi =
                    marchioDAO.findAll();

            assertNotNull(categorie);
            assertFalse(categorie.isEmpty());

            assertNotNull(marchi);
            assertFalse(marchi.isEmpty());

            categoryId =
                    categorie.get(0).getIdCategoria();

            brandId =
                    marchi.get(0).getIdMarchio();

            UtenteDAO utenteDAO =
                    daoFactory.getUtenteDAO();

            Utente testUser =
                    utenteDAO.create(
                            0,
                            TEST_PASSWORD,
                            "Home",
                            "Integration",
                            TEST_EMAIL,
                            "3331111111",
                            100.0,
                            "attivo",
                            false
                    );

            assertNotNull(testUser);

            testUserId =
                    testUser.getIdNome();


            Utente blockedUser =
                    utenteDAO.create(
                            0,
                            TEST_PASSWORD,
                            "Home",
                            "Blocked",
                            BLOCKED_EMAIL,
                            "3332222222",
                            100.0,
                            "bloccato",
                            false
                    );

            assertNotNull(blockedUser);

            blockedUserId =
                    blockedUser.getIdNome();

            Prodotto promoProduct =
                    new Prodotto();

            promoProduct.setNomeProdotto(
                    PRODUCT_PROMO_NAME
            );

            promoProduct.setDescrizione(
                    "Prodotto di test HomeManagement in promozione"
            );

            promoProduct.setPrezzo(50.0);

            promoProduct.setQuantitaDispo(10);

            Categoria categoriaPromo =
                    categoriaDAO.findById(categoryId);

            Marchio marchioPromo =
                    marchioDAO.findAll()
                            .stream()
                            .filter(m ->
                                    m.getIdMarchio()
                                            .equals(brandId))
                            .findFirst()
                            .orElse(null);

            assertNotNull(categoriaPromo);
            assertNotNull(marchioPromo);

            promoProduct.setCategoria(
                    categoriaPromo
            );

            promoProduct.setMarchio(
                    marchioPromo
            );

            promoProduct.setInPromo(true);

            promoProduct.setImmagine(
                    "integration-home-promo.jpg"
            );

            promoProduct.setPrezzoSconto(
                    40.0
            );

            promoProduct.setStatoprodotto(false);

            ProdottoDAO prodottoDAO =
                    daoFactory.getProdottoDAO();

            prodottoDAO.create(
                    promoProduct
            );

            Prodotto normalProduct =
                    new Prodotto();

            normalProduct.setNomeProdotto(
                    PRODUCT_NORMAL_NAME
            );

            normalProduct.setDescrizione(
                    "Prodotto di test HomeManagement normale"
            );

            normalProduct.setPrezzo(30.0);

            normalProduct.setQuantitaDispo(10);

            normalProduct.setCategoria(
                    categoriaPromo
            );

            normalProduct.setMarchio(
                    marchioPromo
            );

            normalProduct.setInPromo(false);

            normalProduct.setImmagine(
                    "integration-home-normal.jpg"
            );

            normalProduct.setPrezzoSconto(
                    30.0
            );

            normalProduct.setStatoprodotto(false);

            prodottoDAO.create(
                    normalProduct
            );


            daoFactory.commitTransaction();

        } catch (Exception e) {

            daoFactory.rollbackTransaction();

            throw e;

        } finally {

            daoFactory.closeTransaction();
        }

        DAOFactory verifyFactory =
                DAOFactory.getDAOFactory(
                        Configuration.DAO_IMPL,
                        null
                );

        verifyFactory.beginTransaction();

        try {

            ProdottoDAO prodottoDAO =
                    verifyFactory.getProdottoDAO();

            Prodotto promo =
                    prodottoDAO.cerca(
                                    PRODUCT_PROMO_NAME,
                                    "",
                                    "",
                                    true
                            )
                            .stream()
                            .filter(p ->
                                    PRODUCT_PROMO_NAME.equals(
                                            p.getNomeProdotto()))
                            .findFirst()
                            .orElse(null);

            Prodotto normal =
                    prodottoDAO.cerca(
                                    PRODUCT_NORMAL_NAME,
                                    "",
                                    "",
                                    false
                            )
                            .stream()
                            .filter(p ->
                                    PRODUCT_NORMAL_NAME.equals(
                                            p.getNomeProdotto()))
                            .findFirst()
                            .orElse(null);

            assertNotNull(promo);
            assertNotNull(normal);

            promoProductId =
                    promo.getIdProdotto();

            normalProductId =
                    normal.getIdProdotto();

            verifyFactory.commitTransaction();

        } catch (Exception e) {

            verifyFactory.rollbackTransaction();

            throw e;

        } finally {

            verifyFactory.closeTransaction();
        }
    }


    private void cleanupDatabase() throws Exception {

        Class.forName(
                Configuration.DATABASE_DRIVER
        );

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL
                     )) {

            connection.setAutoCommit(false);

            try {

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     "DELETE FROM public.dettagli_ordine " +
                                             "WHERE id_ordine IN (" +
                                             "    SELECT id_ordine " +
                                             "    FROM public.ordine " +
                                             "    WHERE id_utente IN (" +
                                             "        SELECT id_nome " +
                                             "        FROM public.utente " +
                                             "        WHERE email IN (?, ?)" +
                                             "    )" +
                                             ")"
                             )) {

                    ps.setString(1, TEST_EMAIL);
                    ps.setString(2, BLOCKED_EMAIL);

                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     "DELETE FROM public.ordine " +
                                             "WHERE id_utente IN (" +
                                             "    SELECT id_nome " +
                                             "    FROM public.utente " +
                                             "    WHERE email IN (?, ?)" +
                                             ")"
                             )) {

                    ps.setString(1, TEST_EMAIL);
                    ps.setString(2, BLOCKED_EMAIL);

                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     "DELETE FROM public.prodotto " +
                                             "WHERE nome_prodotto IN (?, ?)"
                             )) {

                    ps.setString(1, PRODUCT_PROMO_NAME);
                    ps.setString(2, PRODUCT_NORMAL_NAME);

                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     "DELETE FROM public.utente " +
                                             "WHERE email IN (?, ?)"
                             )) {

                    ps.setString(1, TEST_EMAIL);
                    ps.setString(2, BLOCKED_EMAIL);

                    ps.executeUpdate();
                }

                connection.commit();

            } catch (Exception e) {

                connection.rollback();
                throw e;
            }
        }
    }


    private HttpServletRequest mockRequest(
            Cookie... cookies) {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        Map<String, Object> attributes =
                new HashMap<>();

        when(request.getCookies())
                .thenReturn(
                        cookies == null || cookies.length == 0
                                ? null
                                : cookies
                );

        doAnswer(invocation -> {

            String name =
                    invocation.getArgument(0);

            Object value =
                    invocation.getArgument(1);

            attributes.put(
                    name,
                    value
            );

            return null;

        }).when(request).setAttribute(
                anyString(),
                any()
        );

        when(request.getAttribute(anyString()))
                .thenAnswer(invocation ->
                        attributes.get(
                                invocation.getArgument(0)
                        )
                );

        return request;
    }


    private HttpServletResponse mockResponse() {
        return mock(HttpServletResponse.class);
    }


    private Cookie loggedUserCookie() {

        return new Cookie(
                "loggedUser",
                testUserId
                        + "#"
                        + TEST_PASSWORD
                        + "#Home"
                        + "#Integration"
                        + "#false"
                        + "#100.0"
                        + "#attivo"
        );
    }


    private Cookie blockedUserCookie() {

        return new Cookie(
                "loggedUser",
                blockedUserId
                        + "#"
                        + TEST_PASSWORD
                        + "#Home"
                        + "#Blocked"
                        + "#false"
                        + "#100.0"
                        + "#bloccato"
        );
    }


    @Test
    void viewhome_caricaProdottiInPromozioneEMarchiDalDatabase() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        HomeManagement.viewhome(
                request,
                response
        );

        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti =
                (List<Prodotto>)
                        request.getAttribute("Prodotti");

        @SuppressWarnings("unchecked")
        List<Marchio> marchi =
                (List<Marchio>)
                        request.getAttribute("marchi");

        assertNotNull(prodotti);
        assertFalse(prodotti.isEmpty());

        assertNotNull(marchi);
        assertFalse(marchi.isEmpty());

        boolean prodottoPresente =
                prodotti.stream()
                        .anyMatch(p ->
                                PRODUCT_PROMO_NAME.equals(
                                        p.getNomeProdotto()));

        assertTrue(prodottoPresente);

        assertEquals(
                true,
                request.getAttribute("promo")
        );

        assertEquals(
                "HomeManagement/home",
                request.getAttribute("viewUrl")
        );
    }


    @Test
    void cerca_perNomeRestituisceProdottoDalDatabase() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        when(request.getParameter("Nomeprodotto"))
                .thenReturn(PRODUCT_PROMO_NAME);

        when(request.getParameter("Marchioprodotto"))
                .thenReturn("");

        when(request.getParameter("Categoria"))
                .thenReturn("");

        when(request.getParameter("Promo"))
                .thenReturn("true");

        HomeManagement.cerca(
                request,
                response
        );

        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti =
                (List<Prodotto>)
                        request.getAttribute("Prodotti");

        assertNotNull(prodotti);
        assertFalse(prodotti.isEmpty());

        boolean trovato =
                prodotti.stream()
                        .anyMatch(p ->
                                PRODUCT_PROMO_NAME.equals(
                                        p.getNomeProdotto()));

        assertTrue(trovato);
    }


    @Test
    void cerca_perCategoriaRestituisceProdottiDalDatabase() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        DAOFactory daoFactory =
                DAOFactory.getDAOFactory(
                        Configuration.DAO_IMPL,
                        null
                );

        daoFactory.beginTransaction();

        CategoriaDAO categoriaDAO =
                daoFactory.getCategoriaDAO();

        Categoria categoria =
                categoriaDAO.findById(categoryId);

        assertNotNull(categoria);

        daoFactory.commitTransaction();
        daoFactory.closeTransaction();

        when(request.getParameter("Nomeprodotto"))
                .thenReturn("");

        when(request.getParameter("Marchioprodotto"))
                .thenReturn("");

        when(request.getParameter("Categoria"))
                .thenReturn(
                        categoria.getNomeCategoria()
                );

        when(request.getParameter("Promo"))
                .thenReturn("false");

        HomeManagement.cerca(
                request,
                response
        );

        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti =
                (List<Prodotto>)
                        request.getAttribute("Prodotti");

        assertNotNull(prodotti);
        assertFalse(prodotti.isEmpty());

        boolean trovato =
                prodotti.stream()
                        .anyMatch(p ->
                                PRODUCT_NORMAL_NAME.equals(
                                        p.getNomeProdotto()));

        assertTrue(trovato);
    }


    @Test
    void cerca_perMarchioRestituisceProdottiDalDatabase() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        DAOFactory daoFactory =
                DAOFactory.getDAOFactory(
                        Configuration.DAO_IMPL,
                        null
                );

        daoFactory.beginTransaction();

        MarchioDAO marchioDAO =
                daoFactory.getMarchioDAO();

        Marchio marchio =
                marchioDAO.findAll()
                        .stream()
                        .filter(m ->
                                m.getIdMarchio()
                                        .equals(brandId))
                        .findFirst()
                        .orElse(null);

        assertNotNull(marchio);

        daoFactory.commitTransaction();
        daoFactory.closeTransaction();

        when(request.getParameter("Nomeprodotto"))
                .thenReturn("");

        when(request.getParameter("Marchioprodotto"))
                .thenReturn(
                        marchio.getNomeMarchio()
                );

        when(request.getParameter("Categoria"))
                .thenReturn("");

        when(request.getParameter("Promo"))
                .thenReturn("false");

        HomeManagement.cerca(
                request,
                response
        );

        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti =
                (List<Prodotto>)
                        request.getAttribute("Prodotti");

        assertNotNull(prodotti);
        assertFalse(prodotti.isEmpty());

        boolean trovato =
                prodotti.stream()
                        .anyMatch(p ->
                                PRODUCT_NORMAL_NAME.equals(
                                        p.getNomeProdotto()));

        assertTrue(trovato);
    }


    @Test
    void login_credenzialiCorretteEffettuaLogin() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        when(request.getParameter("Email"))
                .thenReturn(TEST_EMAIL);

        when(request.getParameter("Password"))
                .thenReturn(TEST_PASSWORD);

        HomeManagement.login(
                request,
                response
        );

        Utente loggedUser =
                (Utente)
                        request.getAttribute(
                                "loggedUser"
                        );

        assertNotNull(loggedUser);

        assertEquals(
                TEST_EMAIL,
                loggedUser.getEmail()
        );

        assertEquals(
                "Home",
                loggedUser.getNome()
        );

        assertEquals(
                true,
                request.getAttribute("loggedOn")
        );
    }


    @Test
    void login_passwordErrata_mostraMessaggioDiErrore() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        when(request.getParameter("Email"))
                .thenReturn(TEST_EMAIL);

        when(request.getParameter("Password"))
                .thenReturn("password_sbagliata");

        HomeManagement.login(
                request,
                response
        );

        assertEquals(
                "Email o password errati!",
                request.getAttribute(
                        "applicationMessage"
                )
        );

        assertEquals(
                "HomeManagement/login",
                request.getAttribute(
                        "viewUrl"
                )
        );
    }


    @Test
    void login_accountBloccato_mostraMessaggioDiErrore() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        when(request.getParameter("Email"))
                .thenReturn(BLOCKED_EMAIL);

        when(request.getParameter("Password"))
                .thenReturn(TEST_PASSWORD);

        HomeManagement.login(
                request,
                response
        );

        assertEquals(
                "Impossibile accedere: l'account è stato bloccato!",
                request.getAttribute(
                        "applicationMessage"
                )
        );

        assertEquals(
                "HomeManagement/login",
                request.getAttribute(
                        "viewUrl"
                )
        );
    }


    @Test
    void registration_creaNuovoUtenteEEffettuaLogin()
            throws Exception {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        registrationEmail =
                "test.integration."
                        + System.currentTimeMillis()
                        + "@email.com";

        when(request.getParameter("Nome"))
                .thenReturn("Test");

        when(request.getParameter("Cognome"))
                .thenReturn("Integration");

        when(request.getParameter("Email"))
                .thenReturn(registrationEmail);

        when(request.getParameter("Telefono"))
                .thenReturn("3331234567");

        when(request.getParameter("Password"))
                .thenReturn(TEST_PASSWORD);

        HomeManagement.registration(
                request,
                response
        );

        Utente loggedUser =
                (Utente)
                        request.getAttribute(
                                "loggedUser"
                        );

        assertNotNull(loggedUser);

        assertEquals(
                registrationEmail,
                loggedUser.getEmail()
        );

        assertEquals(
                "Test",
                loggedUser.getNome()
        );

        assertEquals(
                "Integration",
                loggedUser.getCognome()
        );

        DAOFactory daoFactory =
                DAOFactory.getDAOFactory(
                        Configuration.DAO_IMPL,
                        null
                );

        daoFactory.beginTransaction();

        UtenteDAO utenteDAO =
                daoFactory.getUtenteDAO();

        Utente dbUser =
                utenteDAO.findEmail(
                        registrationEmail
                );

        assertNotNull(dbUser);

        assertEquals(
                registrationEmail,
                dbUser.getEmail()
        );

        daoFactory.commitTransaction();
        daoFactory.closeTransaction();
    }


    @Test
    void registration_emailGiaEsistente_mostraMessaggioDiErrore() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        when(request.getParameter("Nome"))
                .thenReturn("Test");

        when(request.getParameter("Cognome"))
                .thenReturn("Integration");

        when(request.getParameter("Email"))
                .thenReturn(TEST_EMAIL);

        when(request.getParameter("Telefono"))
                .thenReturn("3331234567");

        when(request.getParameter("Password"))
                .thenReturn(TEST_PASSWORD);

        HomeManagement.registration(
                request,
                response
        );

        assertEquals(
                "Utente già esistente!",
                request.getAttribute(
                        "applicationMessage"
                )
        );

        assertEquals(
                "HomeManagement/registration",
                request.getAttribute(
                        "viewUrl"
                )
        );
    }


    @Test
    void logout_utenteLoggatoEliminaSessioneECariacaHome() {

        HttpServletRequest request =
                mockRequest(
                        loggedUserCookie()
                );

        HttpServletResponse response =
                mockResponse();

        HomeManagement.logout(
                request,
                response
        );

        assertFalse(
                (Boolean)
                        request.getAttribute(
                                "loggedOn"
                        )
        );

        assertNull(
                request.getAttribute(
                        "loggedUser"
                )
        );

        assertEquals(
                "HomeManagement/home",
                request.getAttribute(
                        "viewUrl"
                )
        );
    }


    @Test
    void logout_utenteNonAutenticatoMantieneLoggedOnFalse() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        HomeManagement.logout(
                request,
                response
        );

        assertFalse(
                (Boolean)
                        request.getAttribute(
                                "loggedOn"
                        )
        );

        assertNull(
                request.getAttribute(
                        "loggedUser"
                )
        );

        assertEquals(
                "HomeManagement/home",
                request.getAttribute(
                        "viewUrl"
                )
        );

        assertNotNull(
                request.getAttribute(
                        "Prodotti"
                )
        );

        assertNotNull(
                request.getAttribute(
                        "marchi"
                )
        );

        assertTrue(
                (Boolean)
                        request.getAttribute(
                                "promo"
                        )
        );
    }


    @Test
    void viewlogin_utenteNonLoggato_mostraLogin() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        HomeManagement.viewlogin(
                request,
                response
        );

        assertEquals(
                "HomeManagement/login",
                request.getAttribute(
                        "viewUrl"
                )
        );

        assertFalse(
                (Boolean)
                        request.getAttribute(
                                "loggedOn"
                        )
        );

        assertNull(
                request.getAttribute(
                        "loggedUser"
                )
        );
    }


    @Test
    void viewregistration_utenteNonLoggato_mostraRegistrazione() {

        HttpServletRequest request =
                mockRequest();

        HttpServletResponse response =
                mockResponse();

        HomeManagement.viewregistration(
                request,
                response
        );

        assertEquals(
                "HomeManagement/registration",
                request.getAttribute(
                        "viewUrl"
                )
        );

        assertFalse(
                (Boolean)
                        request.getAttribute(
                                "loggedOn"
                        )
        );

        assertNull(
                request.getAttribute(
                        "loggedUser"
                )
        );
    }
}

