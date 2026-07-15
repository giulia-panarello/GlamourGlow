package com.glamourglow.glamourglow.model.dao.CookieImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.glamourglow.glamourglow.model.dao.UtenteDAO;
import com.glamourglow.glamourglow.model.mo.Utente;

import java.util.ArrayList;
import java.util.List;

public class UtenteDAOCookieImpl implements UtenteDAO {

    // Classe che implementa l'interfaccia UtenteDAO usando i cookie per gestire i dati dell'utente.
    private HttpServletRequest request;
    private HttpServletResponse response;


    // Costruttore per inizializzare richiesta e risposta HTTP.
    public UtenteDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    // Aggiorna i dati dell'utente memorizzati nel cookie.
    @Override
    public void update(Utente utente) {
        // Elimina l'eventuale cookie esistente.
        delete(utente);
        // Crea un nuovo cookie con i dati codificati dell'utente.
        Cookie cookie = new Cookie("loggedUser", encode(utente));
        // Rende il cookie disponibile per tutto il sito
        cookie.setPath("/");
        response.addCookie(cookie); // aggiunge il cookie (che contiene le informazioni dell'utente)
        // alla risposta HTTP inviata dal server al client (solitamente un browser).
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


    // Elimina il cookie associato all'utente.
    @Override
    public void delete(Utente utente) {
        // Crea un cookie con valore vuoto.
        Cookie cookie = new Cookie("loggedUser", "");
        // Imposta il cookie come scaduto, ha durata istantanea, scade subito
        cookie.setMaxAge(0);
        // cookie valido per tutto il sito
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    // Questo codice cerca un cookie specifico chiamato "loggedUser", che contiene le informazioni dell'utente loggato
    // Se il cookie è presente, il suo valore viene decodificato in un oggetto Utente, che viene restituito.
    // Se il cookie non è presente, la funzione restituisce null
    @Override
    public Utente findLoggedUser() {

        Cookie[] cookies = request.getCookies(); // Recupera tutti i cookie dal browser
        Utente loggedUser = null; // Inizializza la variabile per l'utente loggato.

        // Controlla se ci sono cookie nel browser
        if (cookies != null) {
            // Scorre tutti i cookie per trovare quello con nome "loggedUser".
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {

                // Ogni cookie ha un nome (come "loggedUser") e un valore
               // si verifica se il nome del cookie corrente è "loggedUser"
                // Se è così, significa che questo cookie contiene le informazioni dell'utente loggato.
                if (cookies[i].getName().equals("loggedUser")) {

                    // prende il valore del cookie e lo trasforma in un oggetto Utente
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


    // codificare le informazioni di un oggetto Utente in una stringa, in modo che possano essere facilmente salvate in un cookie
    private String encode(Utente utente) {
        // Codifica l'utente in una stringa
        return utente.getIdNome() + "#" + utente.getPassword() + "#" +
                utente.getNome() + "#" + utente.getCognome()  + "#" + utente.getRuolo() + "#" + utente.getWallet() + "#" + utente.getStatoAccount();
    }


    // Serve a decodificare una stringa codificata (che contiene i dati di un utente) per ricostruire un oggetto di tipo Utente
    // es. 12345#mypassword#John#Doe#true#150.50#attivo corrisponde ad un determinato utente con i vari attributi
    private Utente decode(String value) {
        // Decodifica la stringa in un oggetto Utente
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


    // Il metodo create consente di creare un nuovo oggetto Utente,
    // memorizzarlo come un cookie nel browser dell'utente e restituire l'oggetto Utente creato.
    @Override
    public Utente create( long id_nome, String password, String nome, String cognome, String email, String telefono, Double wallet, String statoaccount, boolean ruolo) {
       // Creazione dell'oggetto Utente
        Utente utente = new Utente();
        utente.setPassword(password);
        utente.setNome(nome);
        cognome = cognome.replace(" ", "");
        utente.setCognome(cognome);
        utente.setIdNome(id_nome);
        utente.setRuolo(ruolo);
        utente.setStatoAccount(statoaccount);
        utente.setWallet(wallet);
        System.out.println("pw:" + password + "\nnome:"+nome+"\ncognome:"+cognome+"\ntelefono"+telefono );


        // Salva l'utente in un cookie
        Cookie cookie = new Cookie("loggedUser", encode(utente));
        cookie.setPath("/");
        response.addCookie(cookie);

        return utente;
    }
}
