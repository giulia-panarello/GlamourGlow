package com.glamourglow.glamourglow.controller;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;
import com.glamourglow.glamourglow.services.config.Configuration;
import com.glamourglow.glamourglow.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManagement {

    public static void viewcarrello(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            System.out.println("guardalloggeduser in cart:"+loggedUser);
            // verifica se l'utente è loggato
            if(loggedUser != null){

                // recupero dei DAO per interagire con il database 
                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

                // cookiecarrello contiene gli articoli del carrello, che sono stati precedentemente memorizzati nei cookie
                // Questa chiamata recupera gli articoli del carrello memorizzati nei cookie per l'utente loggato
                // Il metodo findById cerca gli articoli del carrello associati all'ID dell'utente.
                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());
                System.out.println("cookietrovati:"+cookiecarrello.size());

                // Itera su ciascun elemento della lista cookiecarrello per arricchire ogni articolo con informazioni aggiuntive
                for(int i=0; i<cookiecarrello.size(); i++){

                    // Recupera l'utente associato a ciascun articolo del carrello usando l'ID dell'utente e lo imposta nell'oggetto Carrello
                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                    // Recupera le informazioni complete del prodotto associato a ciascun articolo nel carrello usando l'ID del prodotto e lo imposta nell'oggetto Carrello.
                    cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                }

                request.setAttribute("carrello", cookiecarrello);
                request.setAttribute("viewUrl", "UserManagement/carrello");

            }

            // se non è loggato
            else{
                request.setAttribute("viewUrl", "HomeManagement/home");
                applicationMessage= "Fai il login per visualizzare il carrello";
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);


            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);



        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }

    public static void aggiungicarrello(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();


            // Se l'utente è loggato si prosegue con l'aggiunta del prodotto al carrello
            if(loggedUser != null){

                // recupero i parametri dal form

            if(request.getParameter("quantita")!=null && request.getParameter("idprod")!=null)
            {
                int qta = Integer.parseInt(request.getParameter("quantita"));
                long idprod = Long.parseLong(request.getParameter("idprod"));
                // Recupera l'oggetto CarrelloDAO per gestire l'inserimento del prodotto nei cookie
                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                // create(): Aggiunge il prodotto con ID idprod e quantità qta al carrello dell'utente
                sessionCarrelloDAO.create(loggedUser.getIdNome(), idprod, qta);

                // Si recupera il prodotto che è stato aggiunto al carrello, utilizzando l'ID del prodotto
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                Prodotto prodotto = prodottoDAO.findById(idprod);
                request.setAttribute("prodotto", prodotto);

                // Se la quantità qta è 0, il prodotto viene rimosso dal carrello
                if(qta == 0){

                    UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                    request.setAttribute("viewUrl", "UserManagement/carrello");
                    List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());

                    // il ciclo itera attraverso la lista cookiecarrello, che contiene gli oggetti Carrello
                    for(int i=0; i<cookiecarrello.size(); i++){
                        // Recupera e imposta l'utente associato a ciascun carrello
                        cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                        // Recupera e imposta il prodotto associato a ciascun carrello
                        cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                    }
                    for(int i=0;i<cookiecarrello.size();i++)
                    {
                        //  il codice verifica se l'ID del prodotto associato a quel carrello
                        //  corrisponde all'ID del prodotto che si vuole rimuovere
                        // Se gli ID corrispondono, significa che il prodotto che l'utente vuole rimuovere è stato trovato
                        if(cookiecarrello.get(i).getProdotto().getIdProdotto()==idprod)
                        {
                            // Rimozione del prodotto dal carrello
                            cookiecarrello.remove(cookiecarrello.get(i));
                        }
                    }
                    request.setAttribute("carrello", cookiecarrello);
                }

                // Se la quantità è maggiore di 0, un messaggio conferma l'aggiunta al carrello
                else{
                    request.setAttribute("viewUrl", "ProductManagement/descrizione");
                    applicationMessage = prodotto.getNomeProdotto() + " x" + qta + " aggiunto al carrello";
                }

            }
            else
            {

                // recupero dei DAO per interagire con il database
                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

                // cookiecarrello contiene gli articoli del carrello, che sono stati precedentemente memorizzati nei cookie
                // Questa chiamata recupera gli articoli del carrello memorizzati nei cookie per l'utente loggato
                // Il metodo findById cerca gli articoli del carrello associati all'ID dell'utente.
                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());
                System.out.println("cookietrovati:"+cookiecarrello.size());

                // Itera su ciascun elemento della lista cookiecarrello per arricchire ogni articolo con informazioni aggiuntive
                for(int i=0; i<cookiecarrello.size(); i++){

                    // Recupera l'utente associato a ciascun articolo del carrello usando l'ID dell'utente e lo imposta nell'oggetto Carrello
                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                    // Recupera le informazioni complete del prodotto associato a ciascun articolo nel carrello usando l'ID del prodotto e lo imposta nell'oggetto Carrello.
                    cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                }

                request.setAttribute("carrello", cookiecarrello);
                request.setAttribute("viewUrl", "UserManagement/carrello");
            }
            }




            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);

            System.out.println(request.getAttribute("viewUrl"));


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }

    public static void viewcheckout(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            // se l'utente è loggato
            if(loggedUser != null){


                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());

                request.setAttribute("viewUrl", "UserManagement/checkout" );
                // itera attraverso tutti gli oggetti Carrello presenti nel carrello dell'utente
                for(int i=0; i<cookiecarrello.size(); i++){

                    // Per ogni carrello vengono recuperati i dettagli dell'utente e del prodotto associato
                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                    cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                    // Viene controllato se la quantità richiesta nel carrello > della quantità disponibile del prodotto
                    if(cookiecarrello.get(i).getQta() > cookiecarrello.get(i).getProdotto().getQuantitaDispo()){
                        request.setAttribute("viewUrl", "UserManagement/carrello");
                        applicationMessage = "Non sono disponibili " + cookiecarrello.get(i).getQta() + " quantità!";
                        request.setAttribute("applicationMessage", applicationMessage);
                    }

                    // Controllo se il prodotto è stato bloccato
                    if(cookiecarrello.get(i).getProdotto().isStatoprodotto()){
                        // Se il prodotto è bloccato, viene generato un messaggio che indica che il checkout non può procedere per quel prodotto e
                        // la vista viene di nuovo reindirizzata al carrello
                        request.setAttribute("viewUrl", "UserManagement/carrello");
                        applicationMessage = "Impossibile procedere al checkout: il prodotto " + cookiecarrello.get(i).getProdotto().getNomeProdotto() + " è stato temporaneamente bloccato!";
                        request.setAttribute("applicationMessage", applicationMessage);
                    }
                }

                request.setAttribute("carrello", cookiecarrello);


            }

            // utente non loggato
            else{
                request.setAttribute("viewUrl", "HomeManagement/home");
                applicationMessage= "Fai il login per visualizzare il carrello";
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);


            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);



        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }

    public static void verificacoupon(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            CouponDAO couponDAO = daoFactory.getCouponDAO();
            DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();

            // se l'utente è loggato
            if(loggedUser != null){

                // recupero parametri
                // definisco due array per memorizzare i coupon validi e gli sconti corrispondenti
                String[] coupon = request.getParameterValues("coupon");
                String[] couponvalidi = new String[coupon.length];
                String[] percentualisconto = new String[coupon.length];

                for(int i=0; i< coupon.length; i++){
                    Coupon coupondb = new Coupon();
                    int flag = 0;

                    System.out.println("coupon[i]:"+coupon[i]);
                    coupondb = couponDAO.findByCode(coupon[i]); // Si cerca il coupon nel database

                    // se il coupon è vuoto
                    // non mette nessun messaggio e va direttamente alla fine
                    if(coupon[i].isEmpty()){
                        flag = 1;
                    }
                    // Se il coupon non esiste
                    if(coupondb == null && !coupon[i].isEmpty()){
                        System.out.println("Non valido perchè non esiste");
                        applicationMessage = "Coupon non valido, poichè non esiste!";
                        flag = 1; // viene impostato un flag per segnalarne la non validità
                    }

                    // Si verifica che il coupon non sia già stato inserito in precedenza nel ciclo
                    // Se è stato già inserito, il coupon viene marcato come non valido

                    for(int j=0; j<i; j++){
                        if(coupon[i].equals(couponvalidi[j])){
                            System.out.println("Non valido perchè inserito prima");
                            applicationMessage = "Coupon non valido, poichè già inserito!";
                            flag = 1; // Non valido perchè inserito prima
                        }
                    }

                    // metodo usoCoupon che il coupon non sia già stato utilizzato dall'utente
                    if(dettagliOrdineDAO.usoCoupon(loggedUser.getIdNome(), coupon[i])){
                        System.out.println("Non valido perchè già usato");
                        applicationMessage = "Coupon non valido, poichè è già stato usato!";
                        flag = 1;  // Non valido perchè già usato
                    }

                    // Se il coupon è valido, lo aggiunge agli array couponvalidi e percentualisconto
                    if(flag == 0){
                        couponvalidi[i] = coupon[i];
                        applicationMessage = "Coupon inserito con successo!";
                        percentualisconto[i] = ""+coupondb.getSconto();
                    }
                }
                request.setAttribute("coupon", couponvalidi);
                request.setAttribute("sconto", percentualisconto);
                viewcheckout(request, response); // per continuare con il processo di checkout
                }
                else{
                // Gestione dell'utente non loggato
                }



            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);

            System.out.println(request.getAttribute("viewUrl"));


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }

    public static void ricaricasaldo(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();


            // se l'utente è loggato
            if(loggedUser != null){

                // vado sulla pagina del portafoglio virtuale
              request.setAttribute("viewUrl", "UserManagement/wallet");
            }
            else{
                request.setAttribute("viewUrl", "UserManagement/login");
            }



            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);

            System.out.println(request.getAttribute("viewUrl"));


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }
    public static void ricarica(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

            // se l'utente è loggato
            if(loggedUser != null){
                // viene reindirizzato alla pagina del wallet
                request.setAttribute("viewUrl", "UserManagement/wallet");
                // Messaggio per indicare il successo dell'operazione
                applicationMessage = "Saldo ricaricato";
                request.setAttribute("applicationMessage", applicationMessage);

                // Recupero del valore del saldo dal parametro della richiesta dal form
                Double saldo = Double.parseDouble(request.getParameter("saldo"));

                // Aggiornamento del saldo del portafoglio nel database
                // Identificatore dell'utente
                // Nuovo saldo aggiornato
                utenteDAO.updatewallet(loggedUser.getIdNome(), saldo + loggedUser.getWallet());
                // Aggiornamento della sessione dell'utente con il nuovo saldo
                sessionUserDAO.create(loggedUser.getIdNome(), loggedUser.getPassword(), loggedUser.getNome(), loggedUser.getCognome(), loggedUser.getEmail(), loggedUser.getTelefono(), loggedUser.getWallet() + saldo, loggedUser.getStatoAccount(), loggedUser.getRuolo());

            }
            // Se non è loggato, viene reindirizzato alla pagina di login
            else{
                request.setAttribute("viewUrl", "UserManagement/login");
            }



            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);

            System.out.println(request.getAttribute("viewUrl"));


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }



    public static void acquisto(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();
            CouponDAO couponDAO = daoFactory.getCouponDAO();

            // Controlla se l'utente è loggato
            if(loggedUser != null){
                // Recupera i parametri della richiesta dal form
                String indirizzo = (String) request.getParameter("Indirizzo");
                String stato = (String) request.getParameter("Stato");
                String citta = (String) request.getParameter("Citta");
                String[] coupon = request.getParameterValues("coupon");

                // Imposta la vista per il checkout
                request.setAttribute("viewUrl", "UserManagement/checkout");

                // Recupera gli articoli nel carrello associato all'utente loggato
                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());
                int flag = 0; // Flag per segnalare problemi durante l'acquisto
                double totalecarrello = 0; // Totale del carrello

                // Verifica se l'array "coupon" non è null
                if(coupon != null) {
                    // Itera su ciascun elemento dell'array "coupon"
                    for (int i = 0; i < coupon.length; i++) {
                        // Stampa sulla console il contenuto del coupon con il relativo indice
                        System.out.println("coupon["+ i +"]: " + coupon[i]);
                    }
                }
                System.out.println("cookiecarrello.size:" + cookiecarrello.size());

                // Itera su ogni elemento della lista "cookiecarrello"
                for(int i=0; i< cookiecarrello.size(); i++){
                    // Aggiorna l'oggetto "Utente" nel carrello recuperandolo dal database usando il suo ID
                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                    // Aggiorna l'oggetto "Prodotto" nel carrello recuperandolo dal database usando il suo ID
                    cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                    // Dichiarazione della variabile "prezzo" per calcolare il prezzo del prodotto
                    double prezzo;

                    // Controlla se il prodotto è in promozione
                    if(cookiecarrello.get(i).getProdotto().getInPromo()){


                        // Usa il prezzo scontato se il prodotto è in promozione
                        prezzo = cookiecarrello.get(i).getProdotto().getPrezzoSconto();
                        System.out.println("prezzo promo:"+prezzo);
                    }
                    else{
                        // Usa il prezzo normale se il prodotto non è in promozione
                        prezzo = cookiecarrello.get(i).getProdotto().getPrezzo();
                        System.out.println("prezzo originario:"+prezzo);
                    }

                    // Se esiste almeno un coupon applicabile
                    if(coupon != null){
                        System.out.println("guarda coupon["+i+"]:"+coupon[i]+"\n");

                        // Controlla se il coupon attuale non è nullo e non è "null" come stringa
                        if(coupon[i] !=null && !coupon[i].equals("null")){
                            // Recupera i dettagli del coupon dal database
                           Coupon coupondb = couponDAO.findByCode(coupon[i]);

                            // Calcola il totale del carrello con lo sconto applicato dal coupon
                            totalecarrello += (prezzo - (prezzo * coupondb.getSconto()/100)) * cookiecarrello.get(i).getQta();
                            System.out.println("totale carrello con coupon:"+ totalecarrello);
                        }
                        else{

                            // Se il coupon non è valido, calcola il totale senza sconto
                            totalecarrello += prezzo * cookiecarrello.get(i).getQta();
                            System.out.println("totale carrello senza coupon A:"+ totalecarrello);
                        }

                    }
                    else{

                        // Se non ci sono coupon, calcola il totale normalmente
                        totalecarrello += prezzo * cookiecarrello.get(i).getQta();
                        System.out.println("totale carrello senza coupon B:"+ totalecarrello);
                    }

                    // Controlla se la quantità richiesta supera la disponibilità del prodotto
                    if(cookiecarrello.get(i).getQta() > cookiecarrello.get(i).getProdotto().getQuantitaDispo()){

                        // Imposta la vista alla pagina del carrello in caso di errore
                        request.setAttribute("viewUrl", "UserManagement/carrello");
                        applicationMessage = "Le quantità selezionate non sono più disponibili";
                        request.setAttribute("applicationMessage", applicationMessage);

                        // Imposta un flag per indicare un errore
                        flag = 1;
                        System.out.println("quantità non dispo");
                    }
                }

                // se il valore totale del carrello supera il saldo del portafoglio dell'utente
               if(totalecarrello > loggedUser.getWallet()){
                   applicationMessage = "Impossibile procedere con il checkout, credito non disponibile!";
                   // Reindirizzamento alla vista di checkout
                   viewcheckout(request, response);
                   request.setAttribute("applicationMessage", applicationMessage);
                   // Il flag viene impostato a 1 per segnalare che si è verificato un problema (credito insufficiente).
                   flag = 1;


               }


               // Se il flag è 0, si procede con l'elaborazione dell'acquisto, no errori
               if(flag == 0){

                   // Recupero dei prodotti in promozione
                   List<Prodotto> prodotti = prodottoDAO.findPromo();

                   // elenco di tutti i marchi disponibili.
                   MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                   List<Marchio> marchi = marchioDAO.findAll();

                   String Message = "Acquisto effettuato con successo";
                   // Aggiunta di attributi alla richiesta
                   request.setAttribute("Message", Message);
                   request.setAttribute("Prodotti", prodotti);
                   request.setAttribute("marchi", marchi);
                   request.setAttribute("promo", true);

                   // Impostazione della vista successiva
                   request.setAttribute("viewUrl", "HomeManagement/home");

                   // Recupera la data e l'ora attuali e le formatta in una stringa leggibile,
                   // che sarà usata per registrare l'ordine.
                   LocalDateTime now = LocalDateTime.now();
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                   String formattedDateTime = now.format(formatter);

                   // Viene creato un nuovo ordine con le informazioni dell'utente, il totale del carrello, l'indirizzo e altri dettagli.
                   Ordine ordine = ordineDAO.create(0, loggedUser.getIdNome(), formattedDateTime, null, totalecarrello, indirizzo, citta, stato );

                   // Iterazione sui prodotti nel carrello:
                   for(int i = 0; i<cookiecarrello.size(); i++){
                       // Determinazione del prezzo:
                       double prezzo;

                       // Se il prodotto è in promozione, utilizza il prezzo scontato.
                       if(cookiecarrello.get(i).getProdotto().getInPromo()){
                           prezzo = cookiecarrello.get(i).getProdotto().getPrezzoSconto();
                       }
                       // Altrimenti, utilizza il prezzo normale.
                       else{
                           prezzo = cookiecarrello.get(i).getProdotto().getPrezzo();
                       }

                       // Verifica dell'esistenza dei coupon
                       if(coupon != null){
                           // Controlla se il coupon per il prodotto corrente (coupon[i]) non è nullo e non è una stringa "null"
                           // Questo evita di usare coupon non validi o vuoti.
                           if(coupon[i] != null && !coupon[i].equals("null")){
                               // Creazione del dettaglio ordine con coupon
                               // Registra i dettagli del prodotto acquistato,
                               dettagliOrdineDAO.create(null, ordine.getIdOrdine(), cookiecarrello.get(i).getProdotto().getIdProdotto(), cookiecarrello.get(i).getQta(), prezzo, coupon[i]);
                           }

                           // Gestione dei prodotti senza coupon valido
                           else{
                               // Registra i dettagli del prodotto acquistato senza specificare alcun codice coupon
                               dettagliOrdineDAO.create(null, ordine.getIdOrdine(), cookiecarrello.get(i).getProdotto().getIdProdotto(), cookiecarrello.get(i).getQta(), prezzo, null);
                           }

                       }

                       // Gestione dell'assenza di coupon
                       else{

                           // Registra i dettagli del prodotto acquistato senza specificare alcun codice coupon (null),
                           // dato che nessun coupon è stato applicato per tutti i prodotti
                           dettagliOrdineDAO.create(null, ordine.getIdOrdine(), cookiecarrello.get(i).getProdotto().getIdProdotto(), cookiecarrello.get(i).getQta(), prezzo, null);
                       }

                       // eliminare un prodotto dal carrello dell'utente loggato dopo che l'acquisto è stato confermato.
                       sessionCarrelloDAO.delete(loggedUser.getIdNome(), cookiecarrello.get(i).getProdotto().getIdProdotto());
                   }

                   // Aggiorna il portafoglio dell'utente sottraendo il totale dell'acquisto
                   utenteDAO.updatewallet(loggedUser.getIdNome(), loggedUser.getWallet() - totalecarrello);
                   // Aggiorna la sessione utente con il nuovo saldo del portafoglio e le altre informazioni dell'utente
                   sessionUserDAO.create(loggedUser.getIdNome(), loggedUser.getPassword(), loggedUser.getNome(), loggedUser.getCognome(), loggedUser.getEmail(), loggedUser.getTelefono(), loggedUser.getWallet() - totalecarrello , loggedUser.getStatoAccount(), loggedUser.getRuolo());

               }
            }

            // altrimenti login
            else{
                request.setAttribute("viewUrl", "UserManagement/login");
            }



            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);

            System.out.println(request.getAttribute("viewUrl"));


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }

    public static void viewordini(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser =null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();


            // Verifica se l'utente è loggato:
            if(loggedUser != null){

                // Creiamo una lista vuota di ordini
            List<Ordine> ordini = new ArrayList<>();

            //Otteniamo i vari DAO necessari per recuperare gli ordini, i dettagli degli ordini, e i prodotti associati agli ordini
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            CouponDAO couponDAO = daoFactory.getCouponDAO();
            // Recuperiamo gli ordini per l'utente loggato
            ordini = ordineDAO.findByUser(loggedUser.getIdNome());
            Coupon coupon ;


            // iterazione attraverso la lista di ordini
            for(int i = 0; i<ordini.size(); i++){

                //  Per ogni ordine, viene invocato il metodo findByIdOrdine del DAO DettagliOrdineDAO
                //  per ottenere i dettagli relativi a quell'ordine specifico.
                ordini.get(i).setDettagliOrdine(dettagliOrdineDAO.findByIdOrdine(ordini.get(i).getIdOrdine()));


                // per ogni ordine, si itera su ciascun dettaglio dell'ordine.
                for(int j=0; j<ordini.get(i).getDettagliOrdine().size(); j++){

                    // Recupera il coupon associato al dettaglio dell'ordine corrente
                    coupon = couponDAO.findByCode(ordini.get(i).getDettagliOrdine().get(j).getCoupon().getCodice());
                    if(coupon != null){  // Controlla se il coupon esiste

                        double prezzo; // Variabile per il calcolo del nuovo prezzo unitario

                        // Stampa il prezzo unitario originale del prodotto
                        System.out.println("prezzo unitario:"+ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario());
                        // Calcola e stampa la parte scontata del prezzo unitario
                        System.out.println("guarda parte scontata:"+(ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario() * ((double)coupon.getSconto()/100)));
                        // Calcola il prezzo scontato sottraendo la parte scontata dal prezzo originale
                        prezzo = (ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario()) - (ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario() * ((double)coupon.getSconto()/100));
                        // Aggiorna il prezzo unitario del dettaglio dell'ordine con il valore scontato
                        ordini.get(i).getDettagliOrdine().get(j).setPrezzoUnitario(prezzo);
                        // Stampa il prezzo finale da mostrare all'utente
                        System.out.println("prezzo da mostrare:"+prezzo);

                    }
                    // Per ogni dettaglio dell'ordine, viene recuperato il prodotto associato:
                    ordini.get(i).getDettagliOrdine().get(j).setProdotto(prodottoDAO.findById(ordini.get(i).getDettagliOrdine().get(j).getProdotto().getIdProdotto()));

                }


            }
            request.setAttribute("viewUrl", "UserManagement/ordini");

            //  la lista degli ordini, ora arricchita con i dettagli e i prodotti,
                //  è impostata come attributo della richiesta per essere utilizzata nella vista.
            request.setAttribute("ordini", ordini);
            }

            else{

            request.setAttribute("viewUrl", "HomeManagement/login");

            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);



        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }

    }

}
