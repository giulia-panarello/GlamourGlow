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


// La classe CarrelloDAOCookieImpl implementa l'interfaccia CarrelloDAO,
// e gestisce le operazioni di creazione, eliminazione e lettura del carrello tramite i cookie.
public class CarrelloDAOCookieImpl implements CarrelloDAO{

    // request e response sono utilizzate per leggere i cookie inviati dal client e per inviarne di nuovi al client
    private HttpServletRequest request;
    private HttpServletResponse response;

    // Dichiara che il costruttore serve a inizializzare l'oggetto per interagire con i cookie del carrello dell'utente.
    public CarrelloDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }



    @Override
    public void delete(long id_ut, long id_prod) {

        System.out.println("cancello cookie "+id_ut+"%"+id_prod);

        // Crea un nuovo cookie con un nome che combina l'ID utente e l'ID prodotto, separati dal simbolo '%'.
        Cookie cookie = new Cookie(id_ut + "%" + id_prod, "");

        // Imposta il cookie per scadere immediatamente
        cookie.setMaxAge(0);
        // Imposta il percorso del cookie, il cookie sarà accessibile in tutto il sito
        cookie.setPath("/");

        //invia un cookie al browser dell'utente.
        // per "cancellare" un prodotto dal carrello dell'utente.
        response.addCookie(cookie);
    }


    //  il cookie è utilizzato per memorizzare la quantità di un prodotto associato a un utente nel carrello,
    //  oppure per eliminarlo (impostandolo con un valore vuoto e scadenza immediata).
    @Override
    public void create( long id_ut, long id_prod, int qta) {

        // Recupera tutti i carrelli (prodotti e quantità) associati agli utenti dai cookie
        List<Carrello> cookiecarrello = findAll();

        // Flag per verificare se il prodotto esiste già nel carrello
        boolean trovato = false;

        // Itera su tutti gli oggetti carrello per verificare se il prodotto esiste già nel carrello dell'utente
        for(int i=0; i<cookiecarrello.size(); i++){

            // se L'ID dell'utente corrisponde all'ID dell'utente nel carrello
            // se  L'ID del prodotto corrisponde all'ID del prodotto nel carrello
            // Controlla se il prodotto specificato è già presente nel carrello per l'utente specifico
           if(id_ut == cookiecarrello.get(i).getUtente().getIdNome() && id_prod == cookiecarrello.get(i).getProdotto().getIdProdotto()){

               // Imposta la variabile "trovato" a true, indicando che il prodotto è già presente nel carrello
               trovato = true;

               // Controlla se la quantità aggiornata (quantità attuale + quantità da aggiungere) è minore o uguale a 0,
               // oppure se la quantità da aggiungere è esattamente 0
               if(qta + cookiecarrello.get(i).getQta() <= 0 || qta == 0){
                   // Se la quantità aggiornata è zero o inferiore, elimina il prodotto dal carrello
                   delete(id_ut,id_prod);
               }
               else{
                   // Altrimenti, aggiorna la quantità del prodotto nel carrello.
                   // Crea un nuovo cookie con il nuovo valore della quantità.
                   Cookie cookie = new Cookie(id_ut + "%" + id_prod, ""+(cookiecarrello.get(i).getQta() + qta));
                   // Rendi il cookie accessibile in tutto il sito.
                   cookie.setPath("/");

                   // Aggiunge il cookie alla risposta, aggiornando il valore sul client.
                   response.addCookie(cookie); // il server invia un cookie al browser del client.
               }


           }

        }


        // Se il prodotto non è già nel carrello, viene creato un nuovo cookie
        // per memorizzare la quantità di quel prodotto associata all'utente.

        if(!trovato){

            Cookie cookie = new Cookie(id_ut + "%" + id_prod, "" + qta);
            cookie.setPath("/"); // il cookie sarà accessibile in tutta l'applicazione web
            response.addCookie(cookie);
        }




    }

    // Metodo per recuperare tutti gli oggetti "Carrello" dai cookie.
    @Override
    public List<Carrello> findAll() {


        Cookie[] cookies = request.getCookies(); // Recupera tutti i cookie inviati dal client (browser)
        List<Carrello> lista = new ArrayList<>(); // Crea una lista vuota per memorizzare i carrelli.


        // Controlla se ci sono cookie nella richiesta.
        if (cookies != null) {
            // Itera attraverso tutti i cookie.
            for (int i = 0; i < cookies.length; i++) {
                String[] NomeCookie;
                // Divide il nome del cookie in base al simbolo "%"
                NomeCookie = cookies[i].getName().split("%");

                // Verifica che il cookie sia nel formato corretto (es. "id_utente%id_prodotto")
                if (NomeCookie.length == 2) {

                    // Crea un nuovo oggetto Carrello
                    Carrello carrello = new Carrello();

                    // prende il valore del cookie, che rappresenta la quantità di un prodotto nel carrello, lo converte in un intero
                    // e lo associa all'oggetto carrello
                    carrello.setQta(Integer.parseInt(cookies[i].getValue()));

                    // Crea un oggetto Utente.
                    Utente utente = new Utente();
                    // Imposta l'ID dell'utente, ottenendolo dalla prima parte del nome del cookie.
                    utente.setIdNome((Long.parseLong(NomeCookie[0])));
                    // Associa l'utente al carrello.
                    carrello.setUtente(utente);


                    // Crea un oggetto Prodotto.
                    Prodotto prodotto = new Prodotto();
                    // Imposta l'ID del prodotto, ottenendolo dalla seconda parte del nome del cookie.
                    prodotto.setIdProdotto((Long.parseLong(NomeCookie[1])));
                    carrello.setProdotto(prodotto);  // Associa il prodotto al carrello.
                   lista.add(carrello);  // Aggiunge il carrello completo alla lista.
                }
            }
        }

        return lista; // Restituisce la lista di carrelli.

    }


    // Cerca tutti i prodotti nel carrello (salvati come cookie)
    // appartenenti a un determinato utente
    @Override
    public List<Carrello> findById(long id_utente) {

        // Ottiene tutti i cookie dalla richiesta
        Cookie[] cookies = request.getCookies();

        // Crea una lista vuota che conterrà gli oggetti Carrello corrispondenti all'utente.
        List<Carrello> lista = new ArrayList<>();


        // Verifica che ci siano cookie nella richiesta.
        if (cookies != null) {

            // Itera attraverso tutti i cookie presenti.
            for (int i = 0; i < cookies.length; i++) {
                String[] NomeCookie;
                // Divide il nome del cookie usando il carattere "%" come separatore
                // Si aspetta che il formato del nome del cookie sia "id_utente%id_prodotto".
                NomeCookie = cookies[i].getName().split("%");

                System.out.println("guarda nome cookie:" + cookies[i].getName());


                // Controlla se il cookie è nel formato corretto (nome diviso in due parti) e
                // se il primo elemento (id_utente) corrisponde all'ID dell'utente specificato.
                if (NomeCookie.length == 2 && Integer.parseInt(NomeCookie[0]) == id_utente) {
                    // Crea un nuovo oggetto Carrello.
                    Carrello carrello = new Carrello();

                    // Imposta la quantità dell'oggetto Carrello leggendo il valore del cookie
                    // e convertendolo da stringa a intero.
                    carrello.setQta(Integer.parseInt(cookies[i].getValue()));


                    // Crea un oggetto Utente e imposta l'ID utente.
                    Utente utente = new Utente();
                    utente.setIdNome((Long.parseLong(NomeCookie[0])));
                    carrello.setUtente(utente);


                    // Crea un oggetto Prodotto e imposta l'ID prodotto.
                    Prodotto prodotto = new Prodotto();
                    prodotto.setIdProdotto((Long.parseLong(NomeCookie[1])));
                    carrello.setProdotto(prodotto);

                    // Aggiunge l'oggetto Carrello alla lista.
                    lista.add(carrello);
                }
            }
        }

        // Ritorna la lista di oggetti Carrello trovati per l'utente specificato.
        return lista;

    }



}
