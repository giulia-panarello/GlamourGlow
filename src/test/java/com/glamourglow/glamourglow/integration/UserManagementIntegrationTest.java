package com.glamourglow.glamourglow.integration;

import com.glamourglow.glamourglow.controller.UserManagement;
import com.glamourglow.glamourglow.services.config.Configuration;
import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.Utente;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserManagementIntegrationTest {

    private long testUserId;
    private long testProductId;

    private static final String USER_EMAIL =
            "integration.user.management@glamourglow.test";

    private static final String PRODUCT_NAME =
            "Integration User Management Product";

    private static final long TEST_CATEGORY_ID = 2007L;
    private static final long TEST_BRAND_ID = 3001L;

    private static final String COUPON_VALID = "INTUSER01";
    private static final String COUPON_DUPLICATE = "INTUSER02";
    private static final String COUPON_USED = "INTUSER03";

    private final List<Cookie> responseCookies = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {

        cleanupDatabase();

        DAOFactory daoFactory =
                DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

        daoFactory.beginTransaction();

        try {
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

            utenteDAO.create(
                    0,
                    "test",
                    "Integration",
                    "User",
                    USER_EMAIL,
                    "3333333333",
                    100.0,
                    "attivo",
                    false
            );

            Utente createdUser = utenteDAO.findEmail(USER_EMAIL);

            assertNotNull(createdUser);

            testUserId = createdUser.getIdNome();

            testProductId = insertProduct();

            insertCoupon(COUPON_VALID, 10);
            insertCoupon(COUPON_DUPLICATE, 20);
            insertCoupon(COUPON_USED, 30);

            daoFactory.commitTransaction();

        } catch (Exception e) {

            daoFactory.rollbackTransaction();
            throw e;

        } finally {

            daoFactory.closeTransaction();
        }
    }



    @Test
    void viewcarrello_utenteLoggato_visualizzaCarrello() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(2)
                );

        HttpServletResponse response = mockResponse();

        UserManagement.viewcarrello(request, response);

        assertEquals(
                "UserManagement/carrello",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                true,
                request.getAttribute("loggedOn")
        );

        assertNotNull(
                request.getAttribute("carrello")
        );
    }

    @Test
    void viewcarrello_utenteNonLoggato_rimandaHome() {

        HttpServletRequest request =
                mockRequestWithCookies();

        HttpServletResponse response = mockResponse();

        UserManagement.viewcarrello(request, response);

        assertEquals(
                "HomeManagement/home",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                "Fai il login per visualizzare il carrello",
                request.getAttribute("applicationMessage")
        );

        assertEquals(
                false,
                request.getAttribute("loggedOn")
        );
    }

    @Test
    void aggiungicarrello_utenteLoggato_aggiungeProdotto() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0)
                );

        when(request.getParameter("quantita"))
                .thenReturn("2");

        when(request.getParameter("idprod"))
                .thenReturn(String.valueOf(testProductId));

        HttpServletResponse response = mockResponse();

        UserManagement.aggiungicarrello(request, response);

        for (Cookie cookie : responseCookies) {
            System.out.println(
                    "COOKIE RESPONSE: "
                            + cookie.getName()
                            + " = "
                            + cookie.getValue()
            );
        }

        assertEquals(
                "ProductManagement/descrizione",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                "Integration User Management Product x2 aggiunto al carrello",
                request.getAttribute("applicationMessage")
        );

        assertNotNull(
                request.getAttribute("prodotto")
        );
    }

    @Test
    void aggiungicarrello_quantitaZero_rimuoveProdotto() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(2)
                );

        when(request.getParameter("quantita"))
                .thenReturn("0");

        when(request.getParameter("idprod"))
                .thenReturn(String.valueOf(testProductId));

        HttpServletResponse response = mockResponse();

        UserManagement.aggiungicarrello(request, response);
        for (Cookie cookie : responseCookies) {
            System.out.println(
                    "COOKIE RESPONSE: "
                            + cookie.getName()
                            + " = "
                            + cookie.getValue()
                            + " maxAge="
                            + cookie.getMaxAge()
            );
        }

        assertEquals(
                "UserManagement/carrello",
                request.getAttribute("viewUrl")
        );

        assertNotNull(
                request.getAttribute("carrello")
        );
    }

    @Test
    void aggiungicarrello_parametriMancanti_visualizzaCarrello() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        when(request.getParameter("quantita"))
                .thenReturn(null);

        when(request.getParameter("idprod"))
                .thenReturn(null);

        HttpServletResponse response = mockResponse();

        UserManagement.aggiungicarrello(request, response);

        assertEquals(
                "UserManagement/carrello",
                request.getAttribute("viewUrl")
        );

        assertNotNull(
                request.getAttribute("carrello")
        );
    }

    @Test
    void aggiungicarrello_utenteNonLoggato_nonAggiunge() {

        HttpServletRequest request =
                mockRequestWithCookies();

        when(request.getParameter("quantita"))
                .thenReturn("2");

        when(request.getParameter("idprod"))
                .thenReturn(String.valueOf(testProductId));

        HttpServletResponse response = mockResponse();

        UserManagement.aggiungicarrello(request, response);

        assertEquals(
                false,
                request.getAttribute("loggedOn")
        );

        assertNull(
                request.getAttribute("loggedUser")
        );
    }

    @Test
    void viewcheckout_utenteLoggato_visualizzaCheckout() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        HttpServletResponse response = mockResponse();

        UserManagement.viewcheckout(request, response);

        assertEquals(
                "UserManagement/checkout",
                request.getAttribute("viewUrl")
        );

        assertNotNull(
                request.getAttribute("carrello")
        );
    }

    @Test
    void viewcheckout_quantitaNonDisponibile_tornaCarrello() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(100)
                );

        HttpServletResponse response = mockResponse();

        UserManagement.viewcheckout(request, response);

        assertEquals(
                "UserManagement/carrello",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                "Non sono disponibili 100 quantità!",
                request.getAttribute("applicationMessage")
        );
    }

    @Test
    void viewcheckout_prodottoBloccato_tornaCarrello()
            throws Exception {

        setProductBlocked(true);

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        HttpServletResponse response = mockResponse();

        UserManagement.viewcheckout(request, response);

        assertEquals(
                "UserManagement/carrello",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                "Impossibile procedere al checkout: il prodotto "
                        + PRODUCT_NAME
                        + " è stato temporaneamente bloccato!",
                request.getAttribute("applicationMessage")
        );
    }

    @Test
    void viewcheckout_utenteNonLoggato_rimandaHome() {

        HttpServletRequest request =
                mockRequestWithCookies();

        HttpServletResponse response = mockResponse();

        UserManagement.viewcheckout(request, response);

        assertEquals(
                "HomeManagement/home",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                "Fai il login per visualizzare il carrello",
                request.getAttribute("applicationMessage")
        );
    }

    @Test
    void verificacoupon_couponValido_vieneAccettato() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{COUPON_VALID});

        HttpServletResponse response = mockResponse();

        UserManagement.verificacoupon(request, response);

        String[] coupon =
                (String[]) request.getAttribute("coupon");

        String[] sconto =
                (String[]) request.getAttribute("sconto");

        assertNotNull(coupon);

        assertEquals(
                COUPON_VALID,
                coupon[0]
        );

        assertNotNull(sconto);

        assertEquals(
                "10",
                sconto[0]
        );
    }

    @Test
    void verificacoupon_couponInesistente_vieneRifiutato() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        when(request.getParameterValues("coupon"))
                .thenReturn(
                        new String[]{"COUPONINESISTENTE"}
                );

        HttpServletResponse response = mockResponse();

        UserManagement.verificacoupon(request, response);

        String[] coupon =
                (String[]) request.getAttribute("coupon");

        assertNotNull(coupon);

        assertNull(coupon[0]);
    }

    @Test
    void verificacoupon_couponDuplicato_vieneRifiutato() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        when(request.getParameterValues("coupon"))
                .thenReturn(
                        new String[]{
                                COUPON_VALID,
                                COUPON_VALID
                        }
                );

        HttpServletResponse response = mockResponse();

        UserManagement.verificacoupon(request, response);

        String[] coupon =
                (String[]) request.getAttribute("coupon");

        assertNotNull(coupon);

        assertEquals(
                COUPON_VALID,
                coupon[0]
        );

        assertNull(
                coupon[1]
        );
    }

    @Test
    void verificacoupon_couponGiaUtilizzato_vieneRifiutato()
            throws Exception {

        createUsedCouponOrder();

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        when(request.getParameterValues("coupon"))
                .thenReturn(
                        new String[]{COUPON_USED}
                );

        HttpServletResponse response = mockResponse();

        UserManagement.verificacoupon(request, response);

        String[] coupon =
                (String[]) request.getAttribute("coupon");

        assertNotNull(coupon);

        assertNull(coupon[0]);
    }

    @Test
    void ricaricasaldo_utenteLoggato_apreWallet() {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0)
                );

        HttpServletResponse response = mockResponse();

        UserManagement.ricaricasaldo(request, response);

        assertEquals(
                "UserManagement/wallet",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                true,
                request.getAttribute("loggedOn")
        );
    }

    @Test
    void ricaricasaldo_utenteNonLoggato_rimandaLogin() {

        HttpServletRequest request =
                mockRequestWithCookies();

        HttpServletResponse response = mockResponse();

        UserManagement.ricaricasaldo(request, response);

        assertEquals(
                "UserManagement/login",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                false,
                request.getAttribute("loggedOn")
        );
    }

    @Test
    void ricarica_utenteLoggato_aggiornaWalletNelDatabase()
            throws Exception {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0)
                );

        when(request.getParameter("saldo"))
                .thenReturn("50.0");

        HttpServletResponse response = mockResponse();

        UserManagement.ricarica(request, response);

        assertEquals(
                "UserManagement/wallet",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                "Saldo ricaricato",
                request.getAttribute("applicationMessage")
        );

        assertEquals(
                150.0,
                getWallet(testUserId),
                0.001
        );

        boolean updatedCookie =
                responseCookies.stream()
                        .anyMatch(cookie ->
                                "loggedUser".equals(
                                        cookie.getName()
                                )
                                        && cookie.getValue()
                                        .contains("#150.0#")
                        );

        assertTrue(updatedCookie);
    }

    @Test
    void ricarica_utenteNonLoggato_rimandaLogin() {

        HttpServletRequest request =
                mockRequestWithCookies();

        when(request.getParameter("saldo"))
                .thenReturn("50.0");

        HttpServletResponse response = mockResponse();

        UserManagement.ricarica(request, response);

        assertEquals(
                "UserManagement/login",
                request.getAttribute("viewUrl")
        );
    }

    @Test
    void acquisto_utenteLoggato_acquistoCompletato()
            throws Exception {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(100.0),
                        cartCookie(1)
                );

        when(request.getParameter("Indirizzo"))
                .thenReturn("Via Test 1");

        when(request.getParameter("Stato"))
                .thenReturn("Italia");

        when(request.getParameter("Citta"))
                .thenReturn("Milano");

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{null});

        HttpServletResponse response = mockResponse();

        UserManagement.acquisto(request, response);

        assertEquals(
                "Acquisto effettuato con successo",
                request.getAttribute("Message")
        );

        assertEquals(
                "HomeManagement/home",
                request.getAttribute("viewUrl")
        );

        assertEquals(
                50.0,
                getWallet(testUserId),
                0.001
        );

        assertEquals(
                1,
                countOrdersForUser(testUserId)
        );

        assertEquals(
                1,
                countDetailsForUserOrders(testUserId)
        );

        boolean cartDeleted =
                responseCookies.stream()
                        .anyMatch(cookie ->
                                cookie.getName().equals(
                                        testUserId
                                                + "%"
                                                + testProductId
                                )
                                        && cookie.getMaxAge() == 0
                        );

        assertTrue(cartDeleted);
    }

    @Test
    void acquisto_creditoInsufficiente_nonCompletaOrdine()
            throws Exception {

        HttpServletRequest request =
                mockRequestWithCookies(
                        loggedUserCookie(10.0),
                        cartCookie(1)
                );

        when(request.getParameter("Indirizzo"))
                .thenReturn("Via Test 1");

        when(request.getParameter("Stato"))
                .thenReturn("Italia");

        when(request.getParameter("Citta"))
                .thenReturn("Milano");

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{null});

        setWallet(testUserId, 10.0);

        HttpServletResponse response = mockResponse();

        UserManagement.acquisto(request, response);

        assertEquals(
                0,
                countOrdersForUser(testUserId)
        );

        assertEquals(
                10.0,
                getWallet(testUserId),
                0.001
        );
    }

    @Test
    void acquisto_utenteNonLoggato_rimandaLogin() {

        HttpServletRequest request =
                mockRequestWithCookies();

        when(request.getParameter("Indirizzo"))
                .thenReturn("Via Test 1");

        when(request.getParameter("Stato"))
                .thenReturn("Italia");

        when(request.getParameter("Citta"))
                .thenReturn("Milano");

        when(request.getParameterValues("coupon"))
                .thenReturn(new String[]{null});

        HttpServletResponse response = mockResponse();

        UserManagement.acquisto(request, response);

        assertEquals(
                "UserManagement/login",
                request.getAttribute("viewUrl")
        );
    }

    private HttpServletResponse mockResponse() {

        HttpServletResponse response =
                Mockito.mock(HttpServletResponse.class);

        doAnswer(invocation -> {

            Cookie cookie =
                    invocation.getArgument(0);

            responseCookies.add(cookie);

            return null;

        }).when(response).addCookie(
                any(Cookie.class)
        );

        return response;
    }

    private HttpServletRequest mockRequestWithCookies(
            Cookie... cookies) {

        HttpServletRequest request =
                Mockito.mock(HttpServletRequest.class);

        Map<String, Object> attributes =
                new HashMap<>();

        when(request.getCookies())
                .thenReturn(cookies);

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

    private Cookie loggedUserCookie(
            double wallet) {

        return new Cookie(
                "loggedUser",
                testUserId
                        + "#test"
                        + "#Integration"
                        + "#User"
                        + "#false"
                        + "#"
                        + wallet
                        + "#attivo"
        );
    }

    private Cookie cartCookie(
            int quantity) {

        return new Cookie(
                testUserId
                        + "%"
                        + testProductId,
                String.valueOf(quantity)
        );
    }

    private long insertProduct()
            throws Exception {

        String sql =
                "INSERT INTO public.prodotto " +
                        "(nome_prodotto, descrizione, prezzo, " +
                        "quantita_disponibile, id_categoria, id_marchio, " +
                        "in_promozione, immagine, prezzo_scontato, " +
                        "stato_prodotto) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "RETURNING id_prodotto";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    PRODUCT_NAME
            );

            statement.setString(
                    2,
                    "Prodotto creato per integration test"
            );

            statement.setDouble(
                    3,
                    50.0
            );

            statement.setInt(
                    4,
                    10
            );

            statement.setLong(
                    5,
                    TEST_CATEGORY_ID
            );

            statement.setLong(
                    6,
                    TEST_BRAND_ID
            );

            statement.setBoolean(
                    7,
                    false
            );

            statement.setString(
                    8,
                    "integration-test.jpg"
            );

            statement.setDouble(
                    9,
                    50.0
            );

            statement.setBoolean(
                    10,
                    false
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                assertTrue(
                        resultSet.next()
                );

                return resultSet.getLong(
                        "id_prodotto"
                );
            }
        }
    }

    private void insertCoupon(
            String codice,
            int sconto)
            throws Exception {

        String sql =
                "INSERT INTO public.coupon " +
                        "(codice, sconto) VALUES (?, ?)";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    codice
            );

            statement.setInt(
                    2,
                    sconto
            );

            statement.executeUpdate();
        }
    }

    private double getWallet(
            long userId)
            throws Exception {

        String sql =
                "SELECT wallet " +
                        "FROM public.utente " +
                        "WHERE id_nome = ?";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                assertTrue(
                        resultSet.next()
                );

                return resultSet.getDouble(
                        "wallet"
                );
            }
        }
    }

    private void setWallet(
            long userId,
            double wallet)
            throws Exception {

        String sql =
                "UPDATE public.utente " +
                        "SET wallet = ? " +
                        "WHERE id_nome = ?";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDouble(
                    1,
                    wallet
            );

            statement.setLong(
                    2,
                    userId
            );

            statement.executeUpdate();
        }
    }

    private void setProductBlocked(
            boolean blocked)
            throws Exception {

        String sql =
                "UPDATE public.prodotto " +
                        "SET stato_prodotto = ? " +
                        "WHERE id_prodotto = ?";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setBoolean(
                    1,
                    blocked
            );

            statement.setLong(
                    2,
                    testProductId
            );

            statement.executeUpdate();
        }
    }

    private int countOrdersForUser(
            long userId)
            throws Exception {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM public.ordine " +
                        "WHERE id_utente = ?";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                assertTrue(
                        resultSet.next()
                );

                return resultSet.getInt(1);
            }
        }
    }

    private int countDetailsForUserOrders(
            long userId)
            throws Exception {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM public.dettagli_ordine d " +
                        "JOIN public.ordine o " +
                        "ON d.id_ordine = o.id_ordine " +
                        "WHERE o.id_utente = ?";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                assertTrue(
                        resultSet.next()
                );

                return resultSet.getInt(1);
            }
        }
    }

    private void createUsedCouponOrder()
            throws Exception {

        long orderId;

        String insertOrder =
                "INSERT INTO public.ordine " +
                        "(id_utente, data_ordine, stato_ordine, " +
                        "totale_ordine, indirizzo_consegna, citta, stato) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "RETURNING id_ordine";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(
                             insertOrder
                     )) {

            statement.setLong(
                    1,
                    testUserId
            );

            statement.setTimestamp(
                    2,
                    new Timestamp(
                            System.currentTimeMillis()
                    )
            );

            statement.setString(
                    3,
                    "nuovo"
            );

            statement.setDouble(
                    4,
                    50.0
            );

            statement.setString(
                    5,
                    "Via Test 1"
            );

            statement.setString(
                    6,
                    "Milano"
            );

            statement.setString(
                    7,
                    "Italia"
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                assertTrue(
                        resultSet.next()
                );

                orderId =
                        resultSet.getLong(
                                "id_ordine"
                        );
            }
        }

        String insertDetail =
                "INSERT INTO public.dettagli_ordine " +
                        "(id_ordine, id_prodotto, quantita, " +
                        "prezzo_unitario, coupon) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     );

             PreparedStatement statement =
                     connection.prepareStatement(
                             insertDetail
                     )) {

            statement.setLong(
                    1,
                    orderId
            );

            statement.setLong(
                    2,
                    testProductId
            );

            statement.setInt(
                    3,
                    1
            );

            statement.setDouble(
                    4,
                    50.0
            );

            statement.setString(
                    5,
                    COUPON_USED
            );

            statement.executeUpdate();
        }
    }

    private void cleanupDatabase()
            throws Exception {

        try (Connection connection =
                     DriverManager.getConnection(
                             Configuration.DATABASE_URL,
                             "postgres",
                             "postgres"
                     )) {

            String deleteDetails =
                    "DELETE FROM public.dettagli_ordine " +
                            "WHERE id_ordine IN (" +
                            "SELECT id_ordine " +
                            "FROM public.ordine " +
                            "WHERE id_utente IN (" +
                            "SELECT id_nome " +
                            "FROM public.utente " +
                            "WHERE email = ?" +
                            ")" +
                            ")";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 deleteDetails
                         )) {

                statement.setString(
                        1,
                        USER_EMAIL
                );

                statement.executeUpdate();
            }

            String deleteOrders =
                    "DELETE FROM public.ordine " +
                            "WHERE id_utente IN (" +
                            "SELECT id_nome " +
                            "FROM public.utente " +
                            "WHERE email = ?" +
                            ")";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 deleteOrders
                         )) {

                statement.setString(
                        1,
                        USER_EMAIL
                );

                statement.executeUpdate();
            }

            String deleteProduct =
                    "DELETE FROM public.prodotto " +
                            "WHERE nome_prodotto = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 deleteProduct
                         )) {

                statement.setString(
                        1,
                        PRODUCT_NAME
                );

                statement.executeUpdate();
            }

            String deleteCoupons =
                    "DELETE FROM public.coupon " +
                            "WHERE codice IN (?, ?, ?)";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 deleteCoupons
                         )) {

                statement.setString(
                        1,
                        COUPON_VALID
                );

                statement.setString(
                        2,
                        COUPON_DUPLICATE
                );

                statement.setString(
                        3,
                        COUPON_USED
                );

                statement.executeUpdate();
            }

            String deleteUser =
                    "DELETE FROM public.utente " +
                            "WHERE email = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 deleteUser
                         )) {

                statement.setString(
                        1,
                        USER_EMAIL
                );

                statement.executeUpdate();
            }
        }

        responseCookies.clear();
    }
}