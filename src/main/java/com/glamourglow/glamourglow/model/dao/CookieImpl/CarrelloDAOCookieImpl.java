package com.glamourglow.glamourglow.model.dao.CookieImpl;

import com.glamourglow.glamourglow.model.mo.Carrello;
import com.glamourglow.glamourglow.model.mo.Prodotto;
import com.glamourglow.glamourglow.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.glamourglow.glamourglow.model.dao.CarrelloDAO;

import java.util.ArrayList;
import java.util.List;


public class CarrelloDAOCookieImpl implements CarrelloDAO{

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CarrelloDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public void delete(long id_ut, long id_prod) {

        Cookie cookie = new Cookie(id_ut + "%" + id_prod, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    @Override
    public void create( long id_ut, long id_prod, int qta) {


        List<Carrello> cookiecarrello = findAll();
        boolean trovato = false;
        for(int i=0; i<cookiecarrello.size(); i++){

           if(id_ut == cookiecarrello.get(i).getUtente().getIdNome() && id_prod == cookiecarrello.get(i).getProdotto().getIdProdotto()){

               trovato = true;

               if(qta + cookiecarrello.get(i).getQta() <= 0 || qta == 0){
                   delete(id_ut,id_prod);
               }
               else{

                   Cookie cookie = new Cookie(id_ut + "%" + id_prod, ""+(cookiecarrello.get(i).getQta() + qta));
                   cookie.setPath("/");
                   response.addCookie(cookie);
               }


           }

        }



        if(!trovato){

            Cookie cookie = new Cookie(id_ut + "%" + id_prod, "" + qta);
            cookie.setPath("/");
            response.addCookie(cookie);
        }



    }

    @Override
    public List<Carrello> findAll() {


        Cookie[] cookies = request.getCookies();
        List<Carrello> lista = new ArrayList<>();


        if (cookies != null) {

            for (int i = 0; i < cookies.length; i++) {
                String[] NomeCookie;
                NomeCookie = cookies[i].getName().split("%");

                if (NomeCookie.length == 2) {

                    Carrello carrello = new Carrello();
                    carrello.setQta(Integer.parseInt(cookies[i].getValue()));
                    Utente utente = new Utente();
                    utente.setIdNome((Long.parseLong(NomeCookie[0])));
                    carrello.setUtente(utente);

                    Prodotto prodotto = new Prodotto();
                    prodotto.setIdProdotto((Long.parseLong(NomeCookie[1])));
                    carrello.setProdotto(prodotto);
                   lista.add(carrello);
                }
            }
        }

        return lista;

    }

    @Override
    public List<Carrello> findById(long id_utente) {

        Cookie[] cookies = request.getCookies();
        List<Carrello> lista = new ArrayList<>();


        if (cookies != null) {

            for (int i = 0; i < cookies.length; i++) {
                String[] NomeCookie;
                NomeCookie = cookies[i].getName().split("%");


                if (NomeCookie.length == 2 && Integer.parseInt(NomeCookie[0]) == id_utente) {

                    Carrello carrello = new Carrello();
                    carrello.setQta(Integer.parseInt(cookies[i].getValue()));


                    Utente utente = new Utente();
                    utente.setIdNome((Long.parseLong(NomeCookie[0])));
                    carrello.setUtente(utente);

                    Prodotto prodotto = new Prodotto();
                    prodotto.setIdProdotto((Long.parseLong(NomeCookie[1])));
                    carrello.setProdotto(prodotto);

                    lista.add(carrello);
                }
            }
        }

        return lista;

    }



}
