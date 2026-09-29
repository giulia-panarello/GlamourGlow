package com.glamourglow.glamourglow.model.dao.CookieImpl;

import com.glamourglow.glamourglow.model.mo.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CarrelloDAOCookieImplTest {

    @Provide
    Arbitrary<Long> idUtenteCarrello() {
        return Arbitraries.longs().between(1L, 1000L);
    }

    @Provide
    Arbitrary<Long> idProdottoCarrello() {
        return Arbitraries.longs().between(1L, 1000L);
    }

    @Provide
    Arbitrary<Integer> quantitaCarrelloPositiva() {
        return Arbitraries.integers().between(1, 1000);
    }

    @Property
    void delete_creaCookieDiCancellazione(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        dao.delete(idUtente, idProdotto);

        var captor = org.mockito.ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals(idUtente + "%" + idProdotto, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
    }



    @Test
    void findAll_nessunCookie() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getCookies()).thenReturn(null);

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        List<Carrello> risultato = dao.findAll();

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());

        verify(request).getCookies();
    }



    @Property
    void findAll_cookieValidi(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qta1,
            @ForAll("quantitaCarrelloPositiva") int qta2) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        long altroProdotto = idProdotto == 1000L
                ? 999L
                : idProdotto + 1;

        Cookie cookie1 =
                new Cookie(idUtente + "%" + idProdotto, String.valueOf(qta1));

        Cookie cookie2 =
                new Cookie(idUtente + "%" + altroProdotto, String.valueOf(qta2));

        when(request.getCookies()).thenReturn(
                new Cookie[]{cookie1, cookie2}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        List<Carrello> risultato = dao.findAll();

        assertNotNull(risultato);
        assertEquals(2, risultato.size());

        assertEquals(idUtente,
                risultato.get(0).getUtente().getIdNome());
        assertEquals(idProdotto,
                risultato.get(0).getProdotto().getIdProdotto());
        assertEquals(qta1,
                risultato.get(0).getQta());

        assertEquals(idUtente,
                risultato.get(1).getUtente().getIdNome());
        assertEquals(altroProdotto,
                risultato.get(1).getProdotto().getIdProdotto());
        assertEquals(qta2,
                risultato.get(1).getQta());
    }



    @Property
    void findAll_ignoraCookieConNomeNonValido(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qta) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie valido =
                new Cookie(idUtente + "%" + idProdotto, String.valueOf(qta));

        Cookie nonValido =
                new Cookie("cookieNonValido", "5");

        when(request.getCookies()).thenReturn(
                new Cookie[]{valido, nonValido}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        List<Carrello> risultato = dao.findAll();

        assertNotNull(risultato);
        assertEquals(1, risultato.size());

        assertEquals(idUtente,
                risultato.get(0).getUtente().getIdNome());

        assertEquals(idProdotto,
                risultato.get(0).getProdotto().getIdProdotto());

        assertEquals(qta,
                risultato.get(0).getQta());
    }


    @Test
    void findById_nessunCookie() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getCookies()).thenReturn(null);

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        List<Carrello> risultato = dao.findById(10L);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());

        verify(request).getCookies();
    }



    @Property
    void findById_trovaCookieDellUtente(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qta) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        long altroUtente = idUtente == 1000L
                ? 999L
                : idUtente + 1;

        Cookie cookieUtente =
                new Cookie(idUtente + "%" + idProdotto, String.valueOf(qta));

        Cookie cookieAltroUtente =
                new Cookie(altroUtente + "%1", "5");

        when(request.getCookies()).thenReturn(
                new Cookie[]{cookieUtente, cookieAltroUtente}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        List<Carrello> risultato = dao.findById(idUtente);

        assertNotNull(risultato);
        assertEquals(1, risultato.size());

        Carrello carrello = risultato.get(0);

        assertEquals(idUtente,
                carrello.getUtente().getIdNome());

        assertEquals(idProdotto,
                carrello.getProdotto().getIdProdotto());

        assertEquals(qta, carrello.getQta());
    }



    @Property
    void findById_ignoraCookieDiUnAltroUtente(
            @ForAll("idUtenteCarrello") long idUtente) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        long altroUtente = idUtente == 1000L
                ? 999L
                : idUtente + 1;

        Cookie cookie =
                new Cookie(altroUtente + "%20", "3");

        when(request.getCookies()).thenReturn(
                new Cookie[]{cookie}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        List<Carrello> risultato = dao.findById(idUtente);

        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
    }


    @Property
    void findById_ignoraCookieConFormatoNonValido(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qta) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie valido =
                new Cookie(idUtente + "%" + idProdotto, String.valueOf(qta));

        Cookie nonValido =
                new Cookie("cookieNonValido", "5");

        when(request.getCookies()).thenReturn(
                new Cookie[]{valido, nonValido}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        List<Carrello> risultato = dao.findById(idUtente);

        assertNotNull(risultato);
        assertEquals(1, risultato.size());

        assertEquals(idUtente,
                risultato.get(0).getUtente().getIdNome());

        assertEquals(idProdotto,
                risultato.get(0).getProdotto().getIdProdotto());

        assertEquals(qta,
                risultato.get(0).getQta());
    }


    @Property
    void create_prodottoNonPresente_creaCookie(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qta) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getCookies()).thenReturn(new Cookie[0]);

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        dao.create(idUtente, idProdotto, qta);

        var captor = org.mockito.ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals(idUtente + "%" + idProdotto, cookie.getName());
        assertEquals(String.valueOf(qta), cookie.getValue());
        assertEquals("/", cookie.getPath());
    }


    @Property
    void create_prodottoPresente_quantitaPositiva_aggiornaCookie(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qtaEsistente,
            @ForAll("quantitaCarrelloPositiva") int qtaDaAggiungere) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie esistente =
                new Cookie(
                        idUtente + "%" + idProdotto,
                        String.valueOf(qtaEsistente)
                );

        when(request.getCookies()).thenReturn(
                new Cookie[]{esistente}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        dao.create(idUtente, idProdotto, qtaDaAggiungere);

        var captor = org.mockito.ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals(
                idUtente + "%" + idProdotto,
                cookie.getName()
        );

        assertEquals(
                String.valueOf(qtaEsistente + qtaDaAggiungere),
                cookie.getValue()
        );

        assertEquals("/", cookie.getPath());
    }


    @Property
    void create_prodottoPresente_quantitaFinaleZero_eliminaCookie(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qtaEsistente) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie esistente =
                new Cookie(
                        idUtente + "%" + idProdotto,
                        String.valueOf(qtaEsistente)
                );

        when(request.getCookies()).thenReturn(
                new Cookie[]{esistente}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        dao.create(idUtente, idProdotto, -qtaEsistente);

        var captor = org.mockito.ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals(idUtente + "%" + idProdotto, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
    }

    @Property
    void create_prodottoPresente_quantitaZero_eliminaCookie(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qtaEsistente) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie esistente =
                new Cookie(
                        idUtente + "%" + idProdotto,
                        String.valueOf(qtaEsistente)
                );

        when(request.getCookies()).thenReturn(
                new Cookie[]{esistente}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        dao.create(idUtente, idProdotto, 0);

        var captor = org.mockito.ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals(idUtente + "%" + idProdotto, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
    }


    @Property
    void create_cookieDiAltroUtente_creaNuovoCookie(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qta) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        long altroUtente = idUtente == 1000L
                ? 999L
                : idUtente + 1;

        Cookie esistente =
                new Cookie(altroUtente + "%20", "3");

        when(request.getCookies()).thenReturn(
                new Cookie[]{esistente}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        dao.create(idUtente, idProdotto, qta);

        var captor = org.mockito.ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals(idUtente + "%" + idProdotto, cookie.getName());
        assertEquals(String.valueOf(qta), cookie.getValue());
        assertEquals("/", cookie.getPath());
    }


    @Property
    void create_stessoUtenteMaProdottoDiverso_creaNuovoCookie(
            @ForAll("idUtenteCarrello") long idUtente,
            @ForAll("idProdottoCarrello") long idProdotto,
            @ForAll("quantitaCarrelloPositiva") int qta) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        long altroProdotto = idProdotto == 1000L
                ? 999L
                : idProdotto + 1;

        Cookie esistente =
                new Cookie(idUtente + "%" + altroProdotto, "3");

        when(request.getCookies()).thenReturn(
                new Cookie[]{esistente}
        );

        CarrelloDAOCookieImpl dao =
                new CarrelloDAOCookieImpl(request, response);

        dao.create(idUtente, idProdotto, qta);

        var captor = org.mockito.ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals(idUtente + "%" + idProdotto, cookie.getName());
        assertEquals(String.valueOf(qta), cookie.getValue());
        assertEquals("/", cookie.getPath());
    }


}