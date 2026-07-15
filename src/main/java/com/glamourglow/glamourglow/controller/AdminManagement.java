package com.glamourglow.glamourglow.controller;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.*;
import com.glamourglow.glamourglow.services.config.Configuration;
import com.glamourglow.glamourglow.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminManagement {

    public static void gestioneutente(HttpServletRequest request, HttpServletResponse response) {

        //System.out.println("SONO NEL CONTROLLER");

        DAOFactory sessionDAOFactory= null; // sessionDAOFactory e daoFactory sono istanze di DAOFactory,
        // che si occupano di creare i vari DAO necessari per interagire con il database.
        // sessionDAOFactory è utilizzato per la gestione della sessione dell'utente,
        // daoFactory è per operazioni generali sul database
        DAOFactory daoFactory = null;
        Utente loggedUser =null; // loggedUser: è un oggetto Utente che rappresenta l'utente attualmente loggato.
        String applicationMessage = null; // applicationMessage: una variabile che può
        // contenere un messaggio da visualizzare nell'applicazione

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();


            // Si ottiene il DAO per l'entità Utente tramite sessionDAOFactory.getUtenteDAO().
            //loggedUser = sessionUserDAO.findLoggedUser(); recupera l'utente che è attualmente loggato, se presente.
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);



            daoFactory.beginTransaction();

            // utenteDAO e ordineDAO sono i DAO per interagire con le entità Utente e Ordine nel database.
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();

            List<Utente> utenteList = new ArrayList<>(); // utenteList: una lista vuota che verrà popolata con gli utenti trovati.
            List<List<Ordine>> ordini = new ArrayList<>(); // ordini: una lista di liste, che conterrà gli ordini associati a ciascun utente.

            // Qui si verifica se l'utente è loggato (loggedUser != null) e se ha il ruolo di amministratore
            // Se l'utente è amministratore, viene eseguita la logica per recuperare gli utenti e i loro ordini.
            if(loggedUser != null && loggedUser.getRuolo()){
                utenteList =  utenteDAO.findSearch(); // utenteDAO.findSearch() recupera la lista degli utenti dal database.
                request.setAttribute("viewUrl", "AdminManagement/gestioneutente"); // richiesta in cui rimandare la pagina

                // il for scorre la lista degli utenti e per ciascun utente recupera i suoi ordini
                // tramite ordineDAO.findByUser()
                for(int i=0; i<utenteList.size(); i++){
                    ordini.add(ordineDAO.findByUser(utenteList.get(i).getIdNome()));
                }
                // Gli ordini e gli utenti vengono aggiunti agli attributi della richiesta,
                // così che possano essere visualizzati nella jsp (pagina)
                request.setAttribute("ordini", ordini);
                request.setAttribute("utenteList", utenteList);

            }
            else {
                // Se l'utente non è loggato o non ha il ruolo di amministratore, vengono recuperati i prodotti in promozione
                // (prodottoDAO.findPromo()) e i marchi (marchioDAO.findAll()).
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                // Questi dati vengono aggiunti agli attributi della richiesta.
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                // Viene anche impostato l'attributo viewUrl
                // per visualizzare la pagina principale dell'applicazione
                request.setAttribute("viewUrl", "HomeManagement/home");
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

    // Il metodo modificautente gestisce la modifica di un utente,
    // controllando che l'operazione sia eseguita solo da un amministratore.
    public static void modificautente(HttpServletRequest request, HttpServletResponse response) {

        //System.out.println("SONO NEL CONTROLLER");
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

            // Recupera i DAO specifici per Utente e Ordine.
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();

            // Inizializza le liste:
            //utenteList conterrà la lista di utenti.
            //ordini conterrà gli ordini per ogni utente.
            List<Utente> utenteList = new ArrayList<>();
            List<List<Ordine>> ordini = new ArrayList<>();

            // Controlla se l'utente è loggato e se è un amministratore
            //Solo gli amministratori possono modificare gli utenti.

            if(loggedUser != null && loggedUser.getRuolo()){
                request.setAttribute("viewUrl", "AdminManagement/gestioneutente");

                //Recupera i parametri dalla richiesta:
                //email: nuova email dell'utente.
                //ruolo: nuovo ruolo (true/false).
                //stato: nuovo stato dell'account (es. attivo/inattivo).
                //id: ID dell'utente da modificare.
              String email = request.getParameter("email");
               boolean ruolo = Boolean.parseBoolean(request.getParameter("ruolo"));
                String stato = request.getParameter("stato");
                long id = Long.parseLong(request.getParameter("id"));

                //Crea un nuovo oggetto Utente e imposta i valori ricevuti dalla richiesta
                // (stato, email, ruolo e ID).
                Utente utente = new Utente();
                utente.setStatoAccount(stato);
                utente.setEmail(email);
                utente.setRuolo(ruolo);
                utente.setIdNome(id);


                // Validazione dell'email:
                //Controlla se esiste già un utente con la stessa email tramite findEmail.
                //Se l'email è già in uso da un altro utente (con ID diverso),
                // imposta un flag per indicare un errore e aggiorna applicationMessage con un messaggio di errore.
                int flag = 0;
                Utente tmp = utenteDAO.findEmail(email);
                if(tmp!=null && tmp.getIdNome()!=id){
                    flag = 1;
                    applicationMessage = "Impossibile modificare email";
                }

                // Aggiornamento dell'utente:
                //Se il flag è 0, aggiorna l'utente
                if(flag == 0){
                    System.out.println("LOGGEDUSER:"+loggedUser);
                    System.out.println("UTENTE.GETEMAIL:"+utente.getEmail());
                    System.out.println("LOGGEDUSER.GETEMAIL:"+loggedUser.getEmail());
                    if(utente.getIdNome() == (loggedUser.getIdNome())){

                        utente.setNome(loggedUser.getNome());
                        utente.setCognome(loggedUser.getCognome());
                        utente.setPassword(loggedUser.getPassword());
                        utente.setWallet(loggedUser.getWallet());
                        loggedUser = utente;
                        sessionUserDAO.create(utente.getIdNome(), utente.getPassword(), utente.getNome(), utente.getCognome(), utente.getEmail(), null, utente.getWallet(), utente.getStatoAccount(), utente.getRuolo());
                        System.out.println("SONO NELL'IF");
                    }
                    utenteDAO.update(utente);

                }

                utenteList =  utenteDAO.findSearch(); // Recupera nuovamente la lista aggiornata degli utenti
                // indica la pagina da visualizzare
                if(loggedUser.getRuolo() && !loggedUser.getStatoAccount().equals("bloccato")){
                    request.setAttribute("viewUrl", "AdminManagement/gestioneutente");
                }
                else{
                    ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                    List<Prodotto> prodotti = prodottoDAO.findPromo();
                    MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                    List<Marchio> marchi = marchioDAO.findAll();
                    request.setAttribute("Prodotti", prodotti);
                    request.setAttribute("marchi", marchi);
                    request.setAttribute("promo", true);
                    if(loggedUser.getStatoAccount().equals("bloccato")){
                       sessionUserDAO.delete(loggedUser);
                       loggedUser = null;

                    }

                    request.setAttribute("viewUrl", "HomeManagement/home");
                }



                // Recupera gli ordini per ogni utente e li aggiunge alla lista ordini.
                for(int i=0; i<utenteList.size(); i++){
                    ordini.add(ordineDAO.findByUser(utenteList.get(i).getIdNome()));
                }

                // Imposta gli attributi della richiesta per passare i dati alla pagina/jsp
                // (utenti, ordini e messaggio).
                request.setAttribute("ordini", ordini);
                request.setAttribute("utenteList", utenteList);
                request.setAttribute("applicationMessage", applicationMessage);
            }
            else {

                // Se l'utente non è loggato o non è amministratore, mostra la homepage con i prodotti in promozione e i marchi disponibili.
                //Recupera i prodotti promozionali e i marchi e li passa come attributi alla jsp.
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                request.setAttribute("viewUrl", "HomeManagement/home");
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


    // Il metodo gestioneordini si occupa di:

    //Recuperare e processare gli ordini associati agli utenti loggati.
    //Gestire la visualizzazione degli ordini per gli amministratori.
    //Reindirizzare gli utenti non loggati alla pagina di login.
    public static void gestioneordini(HttpServletRequest request, HttpServletResponse response) {


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


            // Controlla se l'utente è loggato.
            if(loggedUser != null){

                // Inizializza le strutture per gestire gli ordini:
                //Recupera i DAO per ordini, dettagli ordine, prodotti e utenti.

                List<Ordine> ordini = new ArrayList<>();
                OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
                DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                CouponDAO couponDAO = daoFactory.getCouponDAO();
                Coupon coupon;
                ordini = ordineDAO.findAll();   //Recupera tutti gli ordini

                // Per ogni ordine, recupera i dettagli associati tramite dettagliOrdineDAO.findByIdOrdine.
                for(int i = 0; i<ordini.size(); i++){
                    ordini.get(i).setDettagliOrdine(dettagliOrdineDAO.findByIdOrdine(ordini.get(i).getIdOrdine()));
                    System.out.println("nome utente" + ordini.get(i).getUtente().getIdNome());


                    for(int j=0; j<ordini.get(i).getDettagliOrdine().size(); j++){
                        coupon = couponDAO.findByCode(ordini.get(i).getDettagliOrdine().get(j).getCoupon().getCodice());
                        if(coupon != null){
                            System.out.println("SONO NELL'IF DEGLI ORDINI");
                            double prezzo;
                            System.out.println("sconto coupon:"+coupon.getSconto());
                            System.out.println("prezzo unitario:"+ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario());
                            System.out.println("guarda parte scontata:"+(ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario() * ((double)coupon.getSconto()/100)));
                            prezzo = (ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario()) - (ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario() * ((double)coupon.getSconto()/100));
                            ordini.get(i).getDettagliOrdine().get(j).setPrezzoUnitario(prezzo);

                        }
                        // Recupera il prodotto per ogni dettaglio tramite prodottoDAO.findById.
                        ordini.get(i).getDettagliOrdine().get(j).setProdotto(prodottoDAO.findById(ordini.get(i).getDettagliOrdine().get(j).getProdotto().getIdProdotto()));
                        // Aggiorna l'utente associato all'ordine tramite utenteDAO.findById.
                        ordini.get(i).setUtente(utenteDAO.findById(ordini.get(i).getUtente().getIdNome()));
                    }

                    System.out.println("ordine" + ordini.get(i).getIdOrdine());

                }

                // pagina jsp da visualizzare gestioneordini.jsp
                request.setAttribute("viewUrl", "AdminManagement/gestioneordini");
                // richiesta per poter passare e utilizzare gli attributi nella jsp
                request.setAttribute("ordini", ordini);
            }

            else{
                // Se l'utente non è loggato, reindirizza alla pagina di login.

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


    // Il metodo progettato per modificare gli ordini.
    // Viene utilizzato principalmente in un contesto di gestione ordini, accessibile solo agli amministratori.
    public static void modificaordini(HttpServletRequest request, HttpServletResponse response) {

        //System.out.println("SONO NEL CONTROLLER");
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

            // recupera l'utente loggato tramite il DAO della sessione.
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);

            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            CouponDAO couponDAO = daoFactory.getCouponDAO();
            // Recuperiamo gli ordini per l'utente loggato
            Coupon coupon ;

            List<Utente> utenteList = new ArrayList<>();

            // La logica verifica che l'utente sia loggato e abbia il ruolo di amministratore
            // prima di consentire l'accesso alla gestione degli ordini.
            if(loggedUser != null && loggedUser.getRuolo()){
                request.setAttribute("viewUrl", "AdminManagement/gestioneordini");
                // Viene aggiornato lo stato di un ordine specifico in base ai parametri forniti nella richiesta.
                String stato = request.getParameter("stato");
                long id = Long.parseLong(request.getParameter("id"));
                ordineDAO.updateordine(id, stato);


                //Qui vengono recuperati tutti gli ordini e popolati con i relativi dettagli,
                // inclusi i prodotti e gli utenti associati.
                List<Ordine> ordini = new ArrayList<>();
                DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                ordini = ordineDAO.findAll();
                for(int i = 0; i<ordini.size(); i++){

                    // Recupera i dettagli dell'ordine
                    // La funzione findByIdOrdine() riceve l'ID dell'ordine e
                    // restituisce una lista di dettagli associati a quell'ordine
                    // dettagli associati all'ordine corrente tramite il metodo setDettagliOrdine().
                    ordini.get(i).setDettagliOrdine(dettagliOrdineDAO.findByIdOrdine(ordini.get(i).getIdOrdine()));
                    System.out.println("nome utente" + ordini.get(i).getUtente().getIdNome());

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
                        // Per ogni dettaglio dell'ordine, viene recuperato il prodotto associato
                        // findById() cerca un prodotto nel database utilizzando l'ID del prodotto
                        // Il prodotto recuperato viene poi associato al dettaglio dell'ordine tramite il metodo setProdotto()
                        ordini.get(i).getDettagliOrdine().get(j).setProdotto(prodottoDAO.findById(ordini.get(i).getDettagliOrdine().get(j).getProdotto().getIdProdotto()));

                        // L'utente associato all'ordine viene recuperato dal database con utenteDAO
                        // La funzione findById() utilizza l'ID del nome dell'utente per recuperare l'utente
                        // Una volta trovato, l'utente viene associato all'ordine corrente tramite setUtente()
                        ordini.get(i).setUtente(utenteDAO.findById(ordini.get(i).getUtente().getIdNome()));
                    }

                    System.out.println("ordine" + ordini.get(i).getIdOrdine());

                }

                // Imposta i dati elaborati come attributi della richiesta
                request.setAttribute("viewUrl", "AdminManagement/gestioneordini"); // pagina gestione ordini

                request.setAttribute("ordini", ordini);
                request.setAttribute("utenteList", utenteList);
                request.setAttribute("applicationMessage", applicationMessage);
            }

            // Se l'utente non è amministratore,
            // viene impostata una pagina alternativa ovvero la home
            else {

                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                request.setAttribute("viewUrl", "HomeManagement/home");
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

    public static void gestionecoupon(HttpServletRequest request, HttpServletResponse response) {


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

            // Se l'utente è loggato, viene recuperata la lista di coupon tramite
            // il DAO CouponDAO e passata come attributo alla richiesta
            if(loggedUser != null){

                CouponDAO couponDAO = daoFactory.getCouponDAO();
                List<Coupon> coupon = new ArrayList<>();
                coupon = couponDAO.findAll(); // con il metodo findAll cerco tutti gli attributi dei coupon
                request.setAttribute("coupon", coupon);
                // vengo riportata nella pagine della gestione dei coupon
                request.setAttribute("viewUrl", "AdminManagement/gestionecoupon");
            }

            // Se l'utente non è loggato,
            // il percorso della vista è reindirizzato alla pagina di login.
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


    public static void modificacoupon(HttpServletRequest request, HttpServletResponse response) {


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


            // Verifica se l'utente è loggato ed è un amministratore
            if(loggedUser != null && loggedUser.getRuolo()){

                // recupero i parametri
            int sconto = Integer.parseInt(request.getParameter("sconto"));
            String codice = request.getParameter("codice"); // codice modificato
            String vecchiocodice = request.getParameter("vecchiocodice"); // codice iniziale

                // verifica se il nuovo codice è già associato a un altro coupon esistente.
            if(couponDAO.findByCode(codice) != null){

                // verifico se il nuovo codice proposto è lo stesso del vecchio codice del coupon che si sta modificando
                //Se sono uguali il codice non viene realmente cambiato, quindi non c'è conflitto con altri coupon.
                if(codice.equals(vecchiocodice)){
                    //  aggiorna il coupon con il nuovo sconto
                    couponDAO.ModificaCoupon(vecchiocodice, codice, sconto);
                }

                // Se il nuovo codice è già in uso da un altro coupon, la modifica non è permessa
                else{
                    applicationMessage = "Codice già in uso ad altro coupon!";
                    request.setAttribute("applicationMessage", applicationMessage);
                }
            }

            // Se il codice non esiste nel database,
            // il nuovo codice può essere usato
            else{
                applicationMessage = "Coupon " + vecchiocodice + " modificato in " + codice;
                // Cambia il codice del coupon da vecchiocodice a codice
                couponDAO.ModificaCoupon(vecchiocodice, codice, sconto);
            }

                List<Coupon> coupon = new ArrayList<>();
                coupon = couponDAO.findAll();
                request.setAttribute("coupon", coupon);
                request.setAttribute("viewUrl", "AdminManagement/gestionecoupon");
            }
            else {
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                request.setAttribute("viewUrl", "HomeManagement/home");
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

    public static void addCoupon(HttpServletRequest request, HttpServletResponse response) {


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

            // recupero i parametri necessari
            CouponDAO couponDAO = daoFactory.getCouponDAO();
            int sconto = Integer.parseInt(request.getParameter("sconto"));
            String codice = request.getParameter("codice");

            // controllo se l'utente è loggato e se il ruolo è amministratore
            if(loggedUser != null && loggedUser.getRuolo())
            {

                // Verificare se il nuovo codice da aggiungere è già in uso
                if(couponDAO.findByCode(codice) != null){
                   applicationMessage = "Codice già in uso ad altro coupon!";


                }
                else{
                    applicationMessage = "Nuovo coupon creato con successo!";
                    couponDAO.creaCoupon(codice, sconto);
                }

                // Recuperare tutti i coupon aggiornati e passarli alla jsp per gestirli
                List<Coupon> coupon = new ArrayList<>();
                coupon = couponDAO.findAll();
                request.setAttribute("coupon", coupon);
                request.setAttribute("viewUrl", "AdminManagement/gestionecoupon");
            }
            else {
                // Se l'utente non è autorizzato, viene reindirizzato alla homepage
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                request.setAttribute("viewUrl", "HomeManagement/home");
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

    public static void gestioneprodotti(HttpServletRequest request, HttpServletResponse response) {

        //System.out.println("SONO NEL CONTROLLER");
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
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();

            // Inizializza le liste che verranno utilizzate per gestire i dati dei prodotti, marchi e categorie
            List<Prodotto> prodotti = new ArrayList<>();
            List<Marchio> marchi = new ArrayList<>();
            List<Categoria> categorie = new ArrayList<>();

            // Verifica se l'utente è loggato e ha il ruolo di amministratore.
            if(loggedUser != null && loggedUser.getRuolo()){

                // Crea istanze di ProdottoDAO e MarchioDAO per interagire
                // con i dati relativi a prodotti e marchi nel database
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                marchi = marchioDAO.findAll(); // Recupera tutti i marchi disponibili
                categorie = categoriaDAO.findAll(); // Recupera tutte le categorie di prodotti
                prodotti = prodottoDAO.cercaadmin(null, null, null); // Recupera tutti i prodotti

                // Memorizza i dati recuperati come attributi della richiesta
                // per renderli accessibili nella jsp gestione dei prodotti
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("categorie", categorie);
                request.setAttribute("viewUrl", "AdminManagement/gestioneprodotti");


            }

            // se l'utente non è amministratore torno alla homepage
            else {
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                request.setAttribute("viewUrl", "HomeManagement/home");
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

    public static void modificaprodotto(HttpServletRequest request, HttpServletResponse response) {


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



            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();


            // Verifica che l'utente sia loggato e abbia il ruolo di amministratore.
            if(loggedUser != null && loggedUser.getRuolo()){
                // imposto l'attributo viewUrl della richiesta sul percorso, per indirizzarmi a quella jsp
                request.setAttribute("viewUrl", "AdminManagement/gestioneutente");

                // recupero i parametri

                String nomeprodotto = request.getParameter("nomeprodotto");
               double prezzo = Double.parseDouble(request.getParameter("prezzo"));
               int quantita = Integer.parseInt(request.getParameter("quantita"));
               boolean promozione = Boolean.parseBoolean(request.getParameter("promozione"));
               double prezzo_scontato = Double.parseDouble(request.getParameter("prezzo_scontato"));
               boolean stato = Boolean.parseBoolean(request.getParameter("stato"));
               long idprodotto = Long.parseLong(request.getParameter("idprodotto"));

                System.out.println("ID: " + idprodotto);
                System.out.println("Nome: " + nomeprodotto);
                System.out.println("Prezzo: " + prezzo);
                System.out.println("Quantità: " + quantita);
                System.out.println("Promo: " + promozione);
                System.out.println("Sconto: " + prezzo_scontato);
                System.out.println("Stato: " + stato);





                // Crea un nuovo oggetto Prodotto.
                // Popola il prodotto con i dati precedentemente estratti dalla richiesta.
                // Ogni metodo set associa il valore corretto ai rispettivi campi dell'oggetto Prodotto.
               Prodotto prodotto = new Prodotto();

                prodotto.setNomeProdotto(nomeprodotto);
                prodotto.setPrezzo(prezzo);
                prodotto.setQuantitaDispo(quantita);
                prodotto.setInPromo(promozione);
                prodotto.setPrezzoSconto(prezzo_scontato);
                prodotto.setStatoprodotto(stato);
                prodotto.setIdProdotto(idprodotto);


                //  Il metodo modifica applica le modifiche effettuate sul prodotto tramite prodottoDAO
                prodottoDAO.modifica(prodotto);

                // Recupera la lista aggiornata dei prodotti, marchi e categorie dal database
                // per vedere la pagina con i prodotti modificati
                List<Prodotto> prodotti = new ArrayList<>();
                List<Marchio> marchi = new ArrayList<>();
                List<Categoria> categorie = new ArrayList<>();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                marchi = marchioDAO.findAll();
                CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();
                categorie = categoriaDAO.findAll();
                prodotti = prodottoDAO.cercaadmin(null, null, null);

                // imposta gli attributi aggiornati della richiesta da passare alla jsp
                // attributi sono utilizzati nella JSP per visualizzare i dati aggiornati
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("categorie", categorie);
                applicationMessage = "Prodotto " + prodotto.getIdProdotto() +" modificato con successo!";
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "AdminManagement/gestioneprodotti");

            }
            else {


                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                request.setAttribute("viewUrl", "HomeManagement/home");
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

    public static void aggiungiprodotti(HttpServletRequest request, HttpServletResponse response) {


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

            // creo l'oggetto prodottoDAO per recuperare i dati dal db
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

            // recupero i parametri che rappresentano i dettagli del prodotto
            // che l'utente sta cercando di aggiungere
            String nomeprodotto = request.getParameter("nomeprodotto");
            String descrizione = request.getParameter("descrizione");
            Double prezzo = Double.parseDouble(request.getParameter("prezzo"));
            int quantita = Integer.parseInt(request.getParameter("quantita"));
            String immagine = request.getParameter("immagine");
            long categoria = Long.parseLong(request.getParameter("categoria"));
            boolean promozione = Boolean.parseBoolean(request.getParameter("promozione"));
            Double prezzo_scontato = Double.parseDouble(request.getParameter("prezzo_scontato"));
            boolean stato = Boolean.parseBoolean(request.getParameter("stato"));
            long marchio = Long.parseLong(request.getParameter("marchio"));

            // Vengono creati nuovi oggetti Prodotto, Marchio e Categoria.
            // Questi oggetti saranno utilizzati per assegnare i valori recuperati dalla richiesta
            // e successivamente per salvare il nuovo prodotto nel database
            Prodotto prodotto = new Prodotto();
            Marchio marc = new Marchio();
            marc.setIdMarchio(marchio);
            Categoria categ = new Categoria();
            categ.setIdCategoria(categoria);

            System.out.println("promo:"+promozione);

            // verifico se l'utente è loggato e se è un amministratore
            if(loggedUser != null && loggedUser.getRuolo())
            {

                List<Prodotto> prodotti = new ArrayList<>();
                List<Marchio> marchi = new ArrayList<>();
                List<Categoria> categorie = new ArrayList<>();

                // imposto i valori dei vari attributi del prodotto che l'amministratore vuole aggiungere
                prodotto.setNomeProdotto(nomeprodotto);
                prodotto.setDescrizione(descrizione);
                prodotto.setPrezzo(prezzo);
                prodotto.setQuantitaDispo(quantita);
                prodotto.setImmagine(immagine);
                prodotto.setCategoria(categ);
                prodotto.setInPromo(promozione);
                prodotto.setPrezzoSconto(prezzo_scontato);
                prodotto.setStatoprodotto(stato);
                prodotto.setMarchio(marc);


                prodottoDAO.create(prodotto); // il prodotto viene creato nel database

                // Recupero della lista aggiornata di prodotti, marchi e categorie
                prodotti = prodottoDAO.cercaadmin(null, null, null);
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                marchi = marchioDAO.findAll();
                CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();
                categorie = categoriaDAO.findAll();

                // Si impostano degli attributi per la richiesta,
                // in modo che possano essere utilizzati nella pagina di risposta (la jsp gestione prodotti)
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("categorie", categorie);
                applicationMessage = "Prodotto aggiunto con successo!";
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "AdminManagement/gestioneprodotti");
            }

            // Se l'utente non ha il ruolo di amministratore si va alla homepage
            else {

                List<Prodotto> prodotti = prodottoDAO.findPromo();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                List<Marchio> marchi = marchioDAO.findAll();
                request.setAttribute("Prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("promo", true);
                request.setAttribute("viewUrl", "HomeManagement/home");
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
