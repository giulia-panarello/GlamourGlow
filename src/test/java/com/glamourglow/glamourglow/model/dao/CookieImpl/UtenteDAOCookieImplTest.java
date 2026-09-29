package com.glamourglow.glamourglow.model.dao.CookieImpl;

import com.glamourglow.glamourglow.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtenteDAOCookieImplTest {

    @Provide
    Arbitrary<Long> idUtenteCookie() {
        return Arbitraries.longs().between(1L, 1000L);
    }

    @Provide
    Arbitrary<String> passwordCookie() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> nomeUtenteCookie() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> cognomeUtenteCookie() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<Double> walletUtenteCookie() {
        return Arbitraries.doubles()
                .between(0.0, 10000.0);
    }

    @Provide
    Arbitrary<String> cognomeConSpazio() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(10)
                .map(parte -> parte + " " + parte);
    }

    @Provide
    Arbitrary<Boolean> ruoloUtenteCookie() {
        return Arbitraries.of(true, false);
    }

    @Provide
    Arbitrary<String> statoAccountCookie() {
        return Arbitraries.of("attivo", "bloccato");
    }

    @Provide
    Arbitrary<String> emailCookie() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> nome + "@test.it");
    }

    @Property
    void delete_creaCookiePerEliminazione(
            @ForAll("idUtenteCookie") long idUtente) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente utente = new Utente();
        utente.setIdNome(idUtente);

        dao.delete(utente);

        ArgumentCaptor<Cookie> captor =
                ArgumentCaptor.forClass(Cookie.class);

        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals("loggedUser", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
    }

    @Property
    void create_creaUtenteECookieCorrettamente(
            @ForAll("idUtenteCookie") long idUtente,
            @ForAll("passwordCookie") String password,
            @ForAll("nomeUtenteCookie") String nome,
            @ForAll("cognomeUtenteCookie") String cognome,
            @ForAll("walletUtenteCookie") double wallet) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.create(
                idUtente,
                password,
                nome,
                cognome,
                "mario@email.it",
                "3331234567",
                wallet,
                "attivo",
                true
        );

        assertNotNull(risultato);
        assertEquals(idUtente, risultato.getIdNome());
        assertEquals(password, risultato.getPassword());
        assertEquals(nome, risultato.getNome());
        assertEquals(cognome, risultato.getCognome());
        assertTrue(risultato.getRuolo());
        assertEquals(wallet, risultato.getWallet());
        assertEquals("attivo", risultato.getStatoAccount());

        ArgumentCaptor<Cookie> captor =
                ArgumentCaptor.forClass(Cookie.class);

        verify(response).addCookie(captor.capture());

        Cookie cookie = captor.getValue();

        assertEquals("loggedUser", cookie.getName());
        assertEquals("/", cookie.getPath());

        assertEquals(
                idUtente + "#" + password + "#" + nome + "#" + cognome
                        + "#true#" + wallet + "#attivo",
                cookie.getValue()
        );
    }

    @Property
    void create_rimuoveGliSpaziDalCognome(
            @ForAll("idUtenteCookie") long idUtente,
            @ForAll("cognomeConSpazio") String cognome) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.create(
                idUtente,
                "pwd",
                "Anna",
                cognome,
                "anna@email.it",
                "3330000000",
                100.0,
                "attivo",
                false
        );

        String cognomeSenzaSpazi = cognome.replace(" ", "");

        assertEquals(cognomeSenzaSpazi, risultato.getCognome());

        ArgumentCaptor<Cookie> captor =
                ArgumentCaptor.forClass(Cookie.class);

        verify(response).addCookie(captor.capture());

        assertTrue(
                captor.getValue()
                        .getValue()
                        .contains("#" + cognomeSenzaSpazi + "#")
        );
    }



    @Property
    void update_eliminaVecchioCookieECreaQuelloNuovo(
            @ForAll("idUtenteCookie") long idUtente,
            @ForAll("passwordCookie") String password,
            @ForAll("nomeUtenteCookie") String nome,
            @ForAll("cognomeUtenteCookie") String cognome,
            @ForAll("walletUtenteCookie") double wallet) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente utente = new Utente();

        utente.setIdNome(idUtente);
        utente.setPassword(password);
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setRuolo(true);
        utente.setWallet(wallet);
        utente.setStatoAccount("attivo");

        dao.update(utente);

        ArgumentCaptor<Cookie> captor =
                ArgumentCaptor.forClass(Cookie.class);

        verify(response, times(2)).addCookie(captor.capture());

        List<Cookie> cookies = captor.getAllValues();

        Cookie cookieEliminazione = cookies.get(0);

        assertEquals("loggedUser", cookieEliminazione.getName());
        assertEquals("", cookieEliminazione.getValue());
        assertEquals(0, cookieEliminazione.getMaxAge());
        assertEquals("/", cookieEliminazione.getPath());

        Cookie cookieNuovo = cookies.get(1);

        assertEquals("loggedUser", cookieNuovo.getName());
        assertEquals("/", cookieNuovo.getPath());

        assertEquals(
                idUtente + "#" + password + "#" + nome + "#" + cognome
                        + "#true#" + wallet + "#attivo",
                cookieNuovo.getValue()
        );
    }


    @Test
    void findLoggedUser_cookieNull_restituisceNull() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getCookies()).thenReturn(null);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.findLoggedUser();

        assertNull(risultato);

        verify(request).getCookies();
    }


    @Test
    void findLoggedUser_cookiePresentMaUtenteNonPresente_restituisceNull() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie cookie1 =
                new Cookie("carrello", "10%20");

        Cookie cookie2 =
                new Cookie("altroCookie", "123");

        when(request.getCookies()).thenReturn(
                new Cookie[]{cookie1, cookie2}
        );

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.findLoggedUser();

        assertNull(risultato);
    }


    @Property
    void findLoggedUser_trovaLoggedUser_eLoDecodifica(
            @ForAll("idUtenteCookie") long idUtente,
            @ForAll("passwordCookie") String password,
            @ForAll("nomeUtenteCookie") String nome,
            @ForAll("cognomeUtenteCookie") String cognome,
            @ForAll("ruoloUtenteCookie") boolean ruolo,
            @ForAll("walletUtenteCookie") double wallet,
            @ForAll("statoAccountCookie") String statoAccount) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie cookie = new Cookie(
                "loggedUser",
                idUtente + "#" + password + "#" + nome + "#" + cognome
                        + "#" + ruolo + "#" + wallet + "#" + statoAccount
        );

        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.findLoggedUser();

        assertNotNull(risultato);

        assertEquals(idUtente, risultato.getIdNome());
        assertEquals(password, risultato.getPassword());
        assertEquals(nome, risultato.getNome());
        assertEquals(cognome, risultato.getCognome());
        assertEquals(ruolo, risultato.getRuolo());
        assertEquals(wallet, risultato.getWallet());
        assertEquals(statoAccount, risultato.getStatoAccount());
    }

    @Property
    void findLoggedUser_ignoraCookiePrecedenteETrovaLoggedUser(
            @ForAll("idUtenteCookie") long idUtente,
            @ForAll("passwordCookie") String password,
            @ForAll("nomeUtenteCookie") String nome,
            @ForAll("cognomeUtenteCookie") String cognome,
            @ForAll("ruoloUtenteCookie") boolean ruolo,
            @ForAll("walletUtenteCookie") double wallet,
            @ForAll("statoAccountCookie") String statoAccount) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie altroCookie =
                new Cookie("altroCookie", "123");

        Cookie loggedUser = new Cookie(
                "loggedUser",
                idUtente + "#" + password + "#" + nome + "#" + cognome
                        + "#" + ruolo + "#" + wallet + "#" + statoAccount
        );

        when(request.getCookies()).thenReturn(
                new Cookie[]{altroCookie, loggedUser}
        );

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.findLoggedUser();

        assertNotNull(risultato);

        assertEquals(idUtente, risultato.getIdNome());
        assertEquals(password, risultato.getPassword());
        assertEquals(nome, risultato.getNome());
        assertEquals(cognome, risultato.getCognome());
        assertEquals(ruolo, risultato.getRuolo());
        assertEquals(wallet, risultato.getWallet());
        assertEquals(statoAccount, risultato.getStatoAccount());
    }


    @Test
    void findById_restituisceNull() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.findById(10L);

        assertNull(risultato);
    }


    @Test
    void updatewallet_nonModificaIlCookie() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        assertDoesNotThrow(() ->
                dao.updatewallet(10L, 100.0)
        );

        verifyNoInteractions(response);
    }


    @Test
    void findSearch_restituisceNull() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        List<Utente> risultato = dao.findSearch();

        assertNull(risultato);
    }




    @Property
    void findEmail_restituisceNull(
            @ForAll("emailCookie") String email) {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.findEmail(email);

        assertNull(risultato);
    }

    @Test
    void findLoggedUser_trovaIlPrimoCookieLoggedUser() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie loggedUser = new Cookie(
                "loggedUser",
                "10#password#Mario#Rossi#true#150.0#attivo"
        );

        Cookie altroCookie = new Cookie(
                "altroCookie",
                "123"
        );

        when(request.getCookies()).thenReturn(
                new Cookie[]{loggedUser, altroCookie}
        );

        UtenteDAOCookieImpl dao =
                new UtenteDAOCookieImpl(request, response);

        Utente risultato = dao.findLoggedUser();

        assertNotNull(risultato);
        assertEquals(10L, risultato.getIdNome());
        assertEquals("Mario", risultato.getNome());
    }
}