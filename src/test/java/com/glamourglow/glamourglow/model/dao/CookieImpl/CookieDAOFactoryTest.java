package com.glamourglow.glamourglow.model.dao.CookieImpl;

import com.glamourglow.glamourglow.model.dao.*;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CookieDAOFactoryTest {

    @Test
    void beginTransaction_parametriValidi() {

        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = org.mockito.Mockito.mock(HttpServletResponse.class);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("request", request);
        parameters.put("response", response);

        CookieDAOFactory factory = new CookieDAOFactory(parameters);

        assertDoesNotThrow(factory::beginTransaction);

        UtenteDAO utenteDAO = factory.getUtenteDAO();
        assertNotNull(utenteDAO);
        assertTrue(utenteDAO instanceof UtenteDAOCookieImpl);

        CarrelloDAO carrelloDAO = factory.getCarrelloDAO();
        assertNotNull(carrelloDAO);
        assertTrue(carrelloDAO instanceof CarrelloDAOCookieImpl);
    }


    @Test
    void beginTransaction_parametriNull_generaRuntimeException() {

        CookieDAOFactory factory = new CookieDAOFactory(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                factory::beginTransaction
        );

        assertNotNull(exception.getCause());
    }


    @Test
    void commitTransaction_nonLanciaEccezioni() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        assertDoesNotThrow(factory::commitTransaction);
    }


    @Test
    void rollbackTransaction_nonLanciaEccezioni() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        assertDoesNotThrow(factory::rollbackTransaction);
    }


    @Test
    void closeTransaction_nonLanciaEccezioni() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        assertDoesNotThrow(factory::closeTransaction);
    }


    @Test
    void getUtenteDAO_restituisceDAOcorretto() {

        Map<String, Object> parameters = new HashMap<>();

        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = org.mockito.Mockito.mock(HttpServletResponse.class);

        parameters.put("request", request);
        parameters.put("response", response);

        CookieDAOFactory factory = new CookieDAOFactory(parameters);

        factory.beginTransaction();

        UtenteDAO result = factory.getUtenteDAO();

        assertNotNull(result);
        assertInstanceOf(UtenteDAOCookieImpl.class, result);
    }


    @Test
    void getCarrelloDAO_restituisceDAOcorretto() {

        Map<String, Object> parameters = new HashMap<>();

        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = org.mockito.Mockito.mock(HttpServletResponse.class);

        parameters.put("request", request);
        parameters.put("response", response);

        CookieDAOFactory factory = new CookieDAOFactory(parameters);

        factory.beginTransaction();

        CarrelloDAO result = factory.getCarrelloDAO();

        assertNotNull(result);
        assertInstanceOf(CarrelloDAOCookieImpl.class, result);
    }


    @Test
    void getOrdineDAO_nonSupportato() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                factory::getOrdineDAO
        );

        assertEquals("Not supported yet.", exception.getMessage());
    }


    @Test
    void getDettagliOrdineDAO_nonSupportato() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                factory::getDettagliOrdineDAO
        );

        assertEquals("Not supported yet.", exception.getMessage());
    }


    @Test
    void getMarchioDAO_nonSupportato() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                factory::getMarchioDAO
        );

        assertEquals("Not supported yet.", exception.getMessage());
    }


    @Test
    void getCategoriaDAO_nonSupportato() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                factory::getCategoriaDAO
        );

        assertEquals("Not supported yet.", exception.getMessage());
    }


    @Test
    void getProdottoDAO_nonSupportato() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                factory::getProdottoDAO
        );

        assertEquals("Not supported yet.", exception.getMessage());
    }


    @Test
    void getCouponDAO_restituisceNull() {

        CookieDAOFactory factory = new CookieDAOFactory(new HashMap<>());

        CouponDAO result = factory.getCouponDAO();

        assertNull(result);
    }
}