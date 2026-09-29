package com.glamourglow.glamourglow.integration;

import com.glamourglow.glamourglow.controller.AdminManagement;
import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;
import com.glamourglow.glamourglow.services.config.Configuration;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AdminManagementIntegrationTest {

    private DAOFactory daoFactory;
    private UtenteDAO utenteDAO;
    private ProdottoDAO prodottoDAO;
    private OrdineDAO ordineDAO;
    private MarchioDAO marchioDAO;
    private DettagliOrdineDAO dettagliOrdineDAO;
    private CategoriaDAO categoriaDAO;
    private CouponDAO couponDAO;
    private long testAdminId;
    private long testUserId;
    private long testProductId;
    private long testOrderId;

    private static final long TEST_DETAIL_ID = 900001L;
    private static final long TEST_CATEGORY_ID = 2007L;
    private static final long TEST_BRAND_ID = 3001L;

    private static final String ADMIN_EMAIL =
            "integration.admin@glamourglow.test";

    private static final String USER_EMAIL =
            "integration.user@glamourglow.test";

    private static final String PRODUCT_NAME =
            "Integration Test Product";

    private static final String COUPON_CODE_1 =
            "ITCOUPON01";

    private static final String COUPON_CODE_2 =
            "ITCOUPON02";

    @BeforeEach
    void setUp() {

        cleanupDatabase();

        Utente createdAdmin;
        Utente createdUser;

        daoFactory = DAOFactory.getDAOFactory(
                DAOFactory.POSTGRESQLJDBCIMPL,
                new HashMap<>()
        );

        daoFactory.beginTransaction();

        try {

            utenteDAO = daoFactory.getUtenteDAO();

            utenteDAO.create(
                    0,
                    "admin",
                    "Admin",
                    "Test",
                    ADMIN_EMAIL,
                    "0000000000",
                    100.0,
                    "attivo",
                    true
            );

            utenteDAO.create(
                    0,
                    "user",
                    "Integration",
                    "User",
                    USER_EMAIL,
                    "0000000000",
                    100.0,
                    "attivo",
                    false
            );

            createdAdmin = utenteDAO.findEmail(ADMIN_EMAIL);
            createdUser = utenteDAO.findEmail(USER_EMAIL);

            daoFactory.commitTransaction();

        } catch (RuntimeException e) {

            daoFactory.rollbackTransaction();
            throw e;

        } finally {

            daoFactory.closeTransaction();
        }

        assertNotNull(createdAdmin,
                "L'amministratore di test deve essere creato");

        assertNotNull(createdUser,
                "L'utente di test deve essere creato");

        testAdminId = createdAdmin.getIdNome();
        testUserId = createdUser.getIdNome();


        daoFactory = DAOFactory.getDAOFactory(
                DAOFactory.POSTGRESQLJDBCIMPL,
                new HashMap<>()
        );

        daoFactory.beginTransaction();

        try {

            couponDAO = daoFactory.getCouponDAO();

            couponDAO.creaCoupon(
                    COUPON_CODE_1,
                    10
            );

            daoFactory.commitTransaction();

        } catch (RuntimeException e) {

            daoFactory.rollbackTransaction();
            throw e;

        } finally {

            daoFactory.closeTransaction();
        }


        Prodotto createdProduct;

        daoFactory = DAOFactory.getDAOFactory(
                DAOFactory.POSTGRESQLJDBCIMPL,
                new HashMap<>()
        );

        daoFactory.beginTransaction();

        try {

            prodottoDAO = daoFactory.getProdottoDAO();
            categoriaDAO = daoFactory.getCategoriaDAO();
            marchioDAO = daoFactory.getMarchioDAO();

            Categoria categoria =
                    categoriaDAO.findById(TEST_CATEGORY_ID);

            assertNotNull(categoria,
                    "La categoria di test deve esistere nel database");

            Marchio marchio = null;

            List<Marchio> marchi = marchioDAO.findAll();

            if (marchi != null) {

                for (Marchio m : marchi) {

                    if (m.getIdMarchio() != null &&
                            m.getIdMarchio() == TEST_BRAND_ID) {

                        marchio = m;
                        break;
                    }
                }
            }

            assertNotNull(marchio,
                    "Il marchio di test deve esistere nel database");

            Prodotto prodotto = new Prodotto();

            prodotto.setNomeProdotto(PRODUCT_NAME);
            prodotto.setDescrizione("Integration test product");
            prodotto.setPrezzo(10.0);
            prodotto.setQuantitaDispo(10);
            prodotto.setCategoria(categoria);
            prodotto.setMarchio(marchio);
            prodotto.setInPromo(false);
            prodotto.setImmagine("");
            prodotto.setPrezzoSconto(10.0);
            prodotto.setStatoprodotto(true);

            prodottoDAO.create(prodotto);

            List<Prodotto> prodotti =
                    prodottoDAO.cercaadmin(
                            PRODUCT_NAME,
                            "",
                            ""
                    );

            assertNotNull(prodotti);
            assertEquals(1, prodotti.size());

            createdProduct = prodotti.get(0);

            daoFactory.commitTransaction();

        } catch (RuntimeException e) {

            daoFactory.rollbackTransaction();
            throw e;

        } finally {

            daoFactory.closeTransaction();
        }

        assertNotNull(createdProduct,
                "Il prodotto di test deve essere creato");

        testProductId = createdProduct.getIdProdotto();

        Ordine createdOrder;
        DettagliOrdine createdDetail;

        daoFactory = DAOFactory.getDAOFactory(
                DAOFactory.POSTGRESQLJDBCIMPL,
                new HashMap<>()
        );

        daoFactory.beginTransaction();

        try {

            ordineDAO = daoFactory.getOrdineDAO();
            dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();


            ordineDAO.create(
                    0,
                    testUserId,
                    "2026-09-08 14:30:00",
                    "nuovo",
                    10.0,
                    "Via Test 1",
                    "Milano",
                    "Italia"
            );

            List<Ordine> ordini =
                    ordineDAO.findByUser(testUserId);

            assertNotNull(ordini);
            assertFalse(ordini.isEmpty());

            createdOrder =
                    ordini.get(ordini.size() - 1);

            assertNotNull(createdOrder);
            assertNotNull(createdOrder.getIdOrdine());

            createdDetail =
                    dettagliOrdineDAO.create(
                            TEST_DETAIL_ID,
                            createdOrder.getIdOrdine(),
                            testProductId,
                            1,
                            10.0,
                            COUPON_CODE_1
                    );

            assertNotNull(createdDetail);

            daoFactory.commitTransaction();

        } catch (RuntimeException e) {

            daoFactory.rollbackTransaction();
            throw e;

        } finally {

            daoFactory.closeTransaction();
        }

        assertNotNull(createdOrder,
                "L'ordine di test deve essere creato");

        assertNotNull(createdDetail,
                "Il dettaglio dell'ordine deve essere creato");

        testOrderId = createdOrder.getIdOrdine();
        daoFactory = null;
    }




    private void cleanupDatabase() {

        try {

            Class.forName(Configuration.DATABASE_DRIVER);

            try (Connection connection =
                         DriverManager.getConnection(
                                 Configuration.DATABASE_URL
                         )) {

                connection.setAutoCommit(false);

                try {

                    String deleteDetails =
                            "DELETE FROM public.dettagli_ordine " +
                                    "WHERE id_ordine IN (" +
                                    " SELECT id_ordine " +
                                    " FROM public.ordine " +
                                    " WHERE id_utente IN (" +
                                    "  SELECT id_nome " +
                                    "  FROM public.utente " +
                                    "  WHERE email = ? OR email = ?" +
                                    " )" +
                                    ")";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(
                                         deleteDetails
                                 )) {

                        ps.setString(1, ADMIN_EMAIL);
                        ps.setString(2, USER_EMAIL);

                        ps.executeUpdate();
                    }

                    String deleteOrders =
                            "DELETE FROM public.ordine " +
                                    "WHERE id_utente IN (" +
                                    " SELECT id_nome " +
                                    " FROM public.utente " +
                                    " WHERE email = ? OR email = ?" +
                                    ")";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(
                                         deleteOrders
                                 )) {

                        ps.setString(1, ADMIN_EMAIL);
                        ps.setString(2, USER_EMAIL);

                        ps.executeUpdate();
                    }

                    String deleteProduct =
                            "DELETE FROM public.prodotto " +
                                    "WHERE nome_prodotto = ?";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(
                                         deleteProduct
                                 )) {

                        ps.setString(1, PRODUCT_NAME);

                        ps.executeUpdate();
                    }

                    String deleteUsers =
                            "DELETE FROM public.utente " +
                                    "WHERE email = ? OR email = ?";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(
                                         deleteUsers
                                 )) {

                        ps.setString(1, ADMIN_EMAIL);
                        ps.setString(2, USER_EMAIL);

                        ps.executeUpdate();
                    }

                    String deleteCoupons =
                            "DELETE FROM public.coupon " +
                                    "WHERE codice = ? OR codice = ?";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(
                                         deleteCoupons
                                 )) {

                        ps.setString(1, COUPON_CODE_1);
                        ps.setString(2, COUPON_CODE_2);

                        ps.executeUpdate();
                    }

                    connection.commit();

                } catch (Exception e) {

                    connection.rollback();
                    throw e;
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore durante il cleanup del database",
                    e
            );
        }
    }


    private HttpServletRequest createRequest(Cookie cookie) {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        Map<String, Object> attributes =
                new HashMap<>();

        when(request.getAttribute(anyString()))
                .thenAnswer(invocation ->
                        attributes.get(
                                invocation.getArgument(0)
                        )
                );

        doAnswer(invocation -> {

            String name =
                    invocation.getArgument(0);

            Object value =
                    invocation.getArgument(1);

            attributes.put(name, value);

            return null;

        }).when(request)
                .setAttribute(
                        anyString(),
                        any()
                );

        if (cookie == null) {

            when(request.getCookies())
                    .thenReturn(null);

        } else {

            when(request.getCookies())
                    .thenReturn(
                            new Cookie[]{cookie}
                    );
        }

        return request;
    }


    private Cookie adminCookie() {

        return new Cookie(
                "loggedUser",
                testAdminId +
                        "#admin#Admin#Test#true#100.0#attivo"
        );
    }


    private Cookie userCookie() {

        return new Cookie(
                "loggedUser",
                testUserId +
                        "#user#Integration#User#false#100.0#attivo"
        );
    }

    @Test
    void gestioneordini_utenteLoggato_caricaOrdiniDettagliUtentiECoupon() {

        HttpServletRequest request =
                createRequest(adminCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        AdminManagement.gestioneordini(
                request,
                response
        );

        assertEquals(
                "AdminManagement/gestioneordini",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNotNull(
                request.getAttribute("loggedUser")
        );

        List<Ordine> ordini =
                (List<Ordine>)
                        request.getAttribute("ordini");

        assertNotNull(ordini);
        assertFalse(ordini.isEmpty());

        Ordine ordineTest = null;

        for (Ordine ordine : ordini) {

            if (ordine != null &&
                    ordine.getIdOrdine() != null &&
                    ordine.getIdOrdine() == testOrderId) {

                ordineTest = ordine;
                break;
            }
        }

        assertNotNull(
                ordineTest,
                "L'ordine creato dal test deve essere presente"
        );

        assertNotNull(
                ordineTest.getUtente()
        );

        assertEquals(
                testUserId,
                ordineTest.getUtente().getIdNome()
        );

        assertNotNull(
                ordineTest.getDettagliOrdine()
        );

        assertFalse(
                ordineTest.getDettagliOrdine().isEmpty()
        );

        DettagliOrdine dettaglio =
                ordineTest
                        .getDettagliOrdine()
                        .get(0);

        assertNotNull(dettaglio);

        assertNotNull(
                dettaglio.getProdotto()
        );

        assertEquals(
                testProductId,
                dettaglio
                        .getProdotto()
                        .getIdProdotto()
        );

        assertNotNull(
                dettaglio.getCoupon()
        );

        assertEquals(
                COUPON_CODE_1,
                dettaglio
                        .getCoupon()
                        .getCodice()
        );

        assertEquals(
                9.0,
                dettaglio.getPrezzoUnitario(),
                0.001
        );
    }


    @Test
    void gestioneordini_utenteNonAutenticato_impostaLogin() {

        HttpServletRequest request =
                createRequest(null);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        AdminManagement.gestioneordini(
                request,
                response
        );

        assertEquals(
                "HomeManagement/login",
                request.getAttribute("viewUrl")
        );

        assertFalse(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNull(
                request.getAttribute("loggedUser")
        );
    }


    @Test
    void gestioneutente_admin_caricaUtentiEOrdini() {

        HttpServletRequest request =
                createRequest(adminCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        AdminManagement.gestioneutente(
                request,
                response
        );

        assertEquals(
                "AdminManagement/gestioneutente",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );

        List<Utente> utenti =
                (List<Utente>)
                        request.getAttribute("utenteList");

        assertNotNull(utenti);
        assertFalse(utenti.isEmpty());

        boolean adminTrovato = false;
        boolean userTrovato = false;

        for (Utente utente : utenti) {

            if (utente.getIdNome() == testAdminId) {
                adminTrovato = true;
            }

            if (utente.getIdNome() == testUserId) {
                userTrovato = true;
            }
        }

        assertTrue(
                adminTrovato,
                "L'amministratore di test deve essere presente"
        );

        assertTrue(
                userTrovato,
                "L'utente di test deve essere presente"
        );

        List<List<Ordine>> ordini =
                (List<List<Ordine>>)
                        request.getAttribute("ordini");

        assertNotNull(ordini);

        assertEquals(
                utenti.size(),
                ordini.size()
        );

        boolean ordineTrovato = false;

        for (List<Ordine> ordiniUtente : ordini) {

            if (ordiniUtente == null) {
                continue;
            }

            for (Ordine ordine : ordiniUtente) {

                if (ordine != null &&
                        ordine.getIdOrdine() != null &&
                        ordine.getIdOrdine() == testOrderId) {

                    ordineTrovato = true;
                    break;
                }
            }

            if (ordineTrovato) {
                break;
            }
        }

        assertTrue(
                ordineTrovato,
                "L'ordine di test deve essere presente"
        );
    }


    @Test
    void gestioneutente_utenteNonAutenticato_impostaHome() {

        HttpServletRequest request =
                createRequest(null);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        AdminManagement.gestioneutente(
                request,
                response
        );

        assertEquals(
                "HomeManagement/home",
                request.getAttribute("viewUrl")
        );

        assertFalse(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNull(
                request.getAttribute("loggedUser")
        );

        assertNotNull(
                request.getAttribute("Prodotti")
        );

        assertNotNull(
                request.getAttribute("marchi")
        );

        assertEquals(
                true,
                request.getAttribute("promo")
        );
    }


    @Test
    void modificaordini_admin_modificaStatoOrdine() {

        HttpServletRequest request =
                createRequest(adminCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(testOrderId));

        when(request.getParameter("stato"))
                .thenReturn("spedito");

        AdminManagement.modificaordini(
                request,
                response
        );

        assertEquals(
                "AdminManagement/gestioneordini",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );


        Ordine ordineVerificato;

        DAOFactory verificationFactory =
                DAOFactory.getDAOFactory(
                        DAOFactory.POSTGRESQLJDBCIMPL,
                        new HashMap<>()
                );

        verificationFactory.beginTransaction();

        try {

            OrdineDAO verificationDAO =
                    verificationFactory.getOrdineDAO();

            ordineVerificato =
                    verificationDAO.findById(
                            testOrderId
                    );

            verificationFactory.rollbackTransaction();

        } catch (RuntimeException e) {

            verificationFactory.rollbackTransaction();
            throw e;

        } finally {

            verificationFactory.closeTransaction();
        }

        assertNotNull(ordineVerificato);

        assertEquals(
                "spedito",
                ordineVerificato.getStatoOrdine()
        );
    }


    @Test
    void modificaordini_utenteNonAutenticato_impostaHome() {

        HttpServletRequest request =
                createRequest(null);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("id"))
                .thenReturn(String.valueOf(testOrderId));

        when(request.getParameter("stato"))
                .thenReturn("spedito");

        AdminManagement.modificaordini(
                request,
                response
        );

        assertEquals(
                "HomeManagement/home",
                request.getAttribute("viewUrl")
        );

        assertFalse(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNull(
                request.getAttribute("loggedUser")
        );

        assertNotNull(
                request.getAttribute("Prodotti")
        );

        assertNotNull(
                request.getAttribute("marchi")
        );
    }


    @Test
    void gestionecoupon_utenteLoggato_caricaCoupon() {

        HttpServletRequest request =
                createRequest(adminCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        AdminManagement.gestionecoupon(
                request,
                response
        );

        assertEquals(
                "AdminManagement/gestionecoupon",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );

        List<Coupon> coupons =
                (List<Coupon>)
                        request.getAttribute("coupon");

        assertNotNull(coupons);
        assertFalse(coupons.isEmpty());

        boolean couponTrovato = false;

        for (Coupon coupon : coupons) {

            if (coupon != null &&
                    COUPON_CODE_1.equals(
                            coupon.getCodice()
                    )) {

                couponTrovato = true;
                break;
            }
        }

        assertTrue(
                couponTrovato,
                "Il coupon di test deve essere presente"
        );
    }


    @Test
    void gestionecoupon_utenteNonAutenticato_impostaLogin() {

        HttpServletRequest request =
                createRequest(null);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        AdminManagement.gestionecoupon(
                request,
                response
        );

        assertEquals(
                "HomeManagement/login",
                request.getAttribute("viewUrl")
        );

        assertFalse(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNull(
                request.getAttribute("loggedUser")
        );

        assertNull(
                request.getAttribute("coupon")
        );
    }


    @Test
    void addCoupon_admin_creaNuovoCoupon() {

        HttpServletRequest request =
                createRequest(adminCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("codice"))
                .thenReturn(COUPON_CODE_2);

        when(request.getParameter("sconto"))
                .thenReturn("20");

        AdminManagement.addCoupon(
                request,
                response
        );

        assertEquals(
                "AdminManagement/gestionecoupon",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNotNull(
                request.getAttribute("coupon")
        );

        Coupon couponVerificato;

        DAOFactory verificationFactory =
                DAOFactory.getDAOFactory(
                        DAOFactory.POSTGRESQLJDBCIMPL,
                        new HashMap<>()
                );

        verificationFactory.beginTransaction();

        try {

            CouponDAO verificationDAO =
                    verificationFactory.getCouponDAO();

            couponVerificato =
                    verificationDAO.findByCode(
                            COUPON_CODE_2
                    );

            verificationFactory.rollbackTransaction();

        } catch (RuntimeException e) {

            verificationFactory.rollbackTransaction();
            throw e;

        } finally {

            verificationFactory.closeTransaction();
        }

        assertNotNull(
                couponVerificato,
                "Il nuovo coupon deve essere presente"
        );

        assertEquals(
                20,
                couponVerificato.getSconto()
        );
    }


    @Test
    void addCoupon_admin_couponDuplicato_nonSovrascrive() {

        HttpServletRequest request =
                createRequest(adminCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("codice"))
                .thenReturn(COUPON_CODE_1);

        when(request.getParameter("sconto"))
                .thenReturn("50");

        AdminManagement.addCoupon(
                request,
                response
        );

        assertEquals(
                "AdminManagement/gestionecoupon",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );

        Coupon couponVerificato;

        DAOFactory verificationFactory =
                DAOFactory.getDAOFactory(
                        DAOFactory.POSTGRESQLJDBCIMPL,
                        new HashMap<>()
                );

        verificationFactory.beginTransaction();

        try {

            CouponDAO verificationDAO =
                    verificationFactory.getCouponDAO();

            couponVerificato =
                    verificationDAO.findByCode(
                            COUPON_CODE_1
                    );

            verificationFactory.rollbackTransaction();

        } catch (RuntimeException e) {

            verificationFactory.rollbackTransaction();
            throw e;

        } finally {

            verificationFactory.closeTransaction();
        }

        assertNotNull(couponVerificato);
        assertEquals(
                10,
                couponVerificato.getSconto()
        );
    }


    @Test
    void addCoupon_utenteNonAdmin_impostaHomeENonCrea() {

        HttpServletRequest request =
                createRequest(userCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("codice"))
                .thenReturn(COUPON_CODE_2);

        when(request.getParameter("sconto"))
                .thenReturn("20");

        AdminManagement.addCoupon(
                request,
                response
        );

        assertEquals(
                "HomeManagement/home",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNotNull(
                request.getAttribute("loggedUser")
        );

        Utente loggedUser =
                (Utente)
                        request.getAttribute("loggedUser");

        assertEquals(
                testUserId,
                loggedUser.getIdNome()
        );

        assertFalse(
                loggedUser.getRuolo()
        );

        Coupon couponVerificato;

        DAOFactory verificationFactory =
                DAOFactory.getDAOFactory(
                        DAOFactory.POSTGRESQLJDBCIMPL,
                        new HashMap<>()
                );

        verificationFactory.beginTransaction();

        try {

            CouponDAO verificationDAO =
                    verificationFactory.getCouponDAO();

            couponVerificato =
                    verificationDAO.findByCode(
                            COUPON_CODE_2
                    );

            verificationFactory.rollbackTransaction();

        } catch (RuntimeException e) {

            verificationFactory.rollbackTransaction();
            throw e;

        } finally {

            verificationFactory.closeTransaction();
        }

        assertNull(
                couponVerificato,
                "Un utente non amministratore non deve poter creare coupon"
        );
    }


    @Test
    void modificacoupon_admin_modificaCodiceESconto() {

        HttpServletRequest request =
                createRequest(adminCookie());

        HttpServletResponse response =
                mock(HttpServletResponse.class);


        when(request.getParameter("sconto"))
                .thenReturn("25");

        when(request.getParameter("codice"))
                .thenReturn(COUPON_CODE_2);

        when(request.getParameter("vecchiocodice"))
                .thenReturn(COUPON_CODE_1);

        AdminManagement.modificacoupon(
                request,
                response
        );

        assertEquals(
                "AdminManagement/gestionecoupon",
                request.getAttribute("viewUrl")
        );

        assertTrue(
                (Boolean) request.getAttribute("loggedOn")
        );

        assertNotNull(
                request.getAttribute("coupon")
        );

        Coupon oldCoupon;
        Coupon newCoupon;

        DAOFactory verificationFactory =
                DAOFactory.getDAOFactory(
                        DAOFactory.POSTGRESQLJDBCIMPL,
                        new HashMap<>()
                );

        verificationFactory.beginTransaction();

        try {

            CouponDAO verificationDAO =
                    verificationFactory.getCouponDAO();

            oldCoupon =
                    verificationDAO.findByCode(
                            COUPON_CODE_1
                    );

            newCoupon =
                    verificationDAO.findByCode(
                            COUPON_CODE_2
                    );

            verificationFactory.rollbackTransaction();

        } catch (RuntimeException e) {

            verificationFactory.rollbackTransaction();
            throw e;

        } finally {

            verificationFactory.closeTransaction();
        }

        assertNull(
                oldCoupon,
                "Il vecchio codice deve essere rimosso"
        );

        assertNotNull(
                newCoupon,
                "Il nuovo codice deve essere presente"
        );

        assertEquals(
                25,
                newCoupon.getSconto()
        );
    }
}