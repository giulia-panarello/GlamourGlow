package com.glamourglow.glamourglow.model.dao.CookieImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.glamourglow.glamourglow.model.dao.UtenteDAO;
import com.glamourglow.glamourglow.model.mo.Utente;

import java.util.ArrayList;
import java.util.List;

public class UtenteDAOCookieImpl implements UtenteDAO {


    private HttpServletRequest request;
    private HttpServletResponse response;


    public UtenteDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void update(Utente utente) {
        delete(utente);
        Cookie cookie = new Cookie("loggedUser", encode(utente));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public Utente findById(long id) {
        return null;
    }

    @Override
    public void updatewallet(long idNome, Double saldo) {

    }

    @Override
    public List<Utente> findSearch() {
        return null;
    }


    @Override
    public void delete(Utente utente) {
        Cookie cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    @Override
    public Utente findLoggedUser() {

        Cookie[] cookies = request.getCookies();
        Utente loggedUser = null;

        if (cookies != null) {

            for (int i = 0; i < cookies.length && loggedUser == null; i++) {

                if (cookies[i].getName().equals("loggedUser")) {

                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;

    }

    @Override
    public Utente findEmail(String email) {
        return null;
    }

    private String encode(Utente utente) {
        return utente.getIdNome() + "#" + utente.getPassword() + "#" +
                utente.getNome() + "#" + utente.getCognome()  + "#" + utente.getRuolo() + "#" + utente.getWallet() + "#" + utente.getStatoAccount();
    }


    private Utente decode(String value) {
        String[] parts = value.split("#");
        Utente utente = new Utente();
        utente.setIdNome(Long.parseLong(parts[0]));
        utente.setPassword(parts[1]);
        utente.setNome(parts[2]);
        utente.setCognome(parts[3]);
        utente.setRuolo(Boolean.parseBoolean(parts[4]));
        utente.setWallet(Double.parseDouble(parts[5]));
        utente.setStatoAccount(parts[6]);

        return utente;
    }


    @Override
    public Utente create( long id_nome, String password, String nome, String cognome, String email, String telefono, Double wallet, String statoaccount, boolean ruolo) {

        Utente utente = new Utente();
        utente.setPassword(password);
        utente.setNome(nome);
        cognome = cognome.replace(" ", "");
        utente.setCognome(cognome);
        utente.setIdNome(id_nome);
        utente.setRuolo(ruolo);
        utente.setStatoAccount(statoaccount);
        utente.setWallet(wallet);


        Cookie cookie = new Cookie("loggedUser", encode(utente));
        cookie.setPath("/");
        response.addCookie(cookie);

        return utente;
    }
}
