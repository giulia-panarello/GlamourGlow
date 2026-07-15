package com.glamourglow.glamourglow.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.model.mo.Marchio;
import com.glamourglow.glamourglow.model.mo.Prodotto;
import com.glamourglow.glamourglow.model.mo.Utente;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import com.glamourglow.glamourglow.services.config.Configuration;
import com.glamourglow.glamourglow.services.logservice.LogService;



public class HomeManagement {

    private HomeManagement() {

    }


    public static void viewlogin(HttpServletRequest request, HttpServletResponse response) {

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





            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            //  imposto gli attributi che la jsp userà per visualizzare i dati
            // Verifica se l'utente è loggato, e imposta l'attributo 'loggedOn'
            request.setAttribute("loggedOn",loggedUser!=null);
            // Se l'utente è loggato, gli passiamo anche i suoi dati
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "HomeManagement/login"); // pagina che vedo

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

    public static void login(HttpServletRequest request, HttpServletResponse response) {

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

            // Recupero dei dati inseriti nel modulo di login
            String email = request.getParameter("Email");
            String password = request.getParameter("Password");
            // // Impostazione dell'URL di destinazione della vista
            request.setAttribute("viewUrl", "HomeManagement/home");

            // Se non c'è nessun utente loggato
            if(loggedUser == null){

                // Cerchiamo l'utente con l'email fornita nel database
                Utente utente = utenteDAO.findEmail(email);
                // Verifica che l'utente esista e che la password sia corretta
                if(utente != null && email.equals(utente.getEmail()) && password.equals(utente.getPassword())){
                    // Se l'account non è bloccato, eseguiamo il login
                    if(!utente.getStatoAccount().equals("bloccato")){
                        loggedUser = utente; // Assegniamo l'utente loggato
                        // Creiamo la sessione per l'utente loggato
                        sessionUserDAO.create(utente.getIdNome(), utente.getPassword(), utente.getNome(), utente.getCognome(), utente.getEmail(), null, utente.getWallet(), utente.getStatoAccount(), utente.getRuolo());
                        // Una volta loggato, reindirizziamo alla home page
                        viewhome(request, response);
                    }
                    else{
                        // Se l'account è bloccato, visualizziamo un messaggio
                        applicationMessage = "Impossibile accedere: l'account è stato bloccato!";
                        request.setAttribute("applicationMessage", applicationMessage);
                        request.setAttribute("viewUrl", "HomeManagement/login");
                        // impostare un attributo sulla richiesta, che verrà poi passato alla vista
                        request.setAttribute("email", email ); // Ricarichiamo l'email nel campo
                    }
                }
                else {
                    // Se email o password sono errati, visualizziamo un messaggio di errore
                    applicationMessage = "Email o password errati!";
                    request.setAttribute("viewUrl", "HomeManagement/login");
                    request.setAttribute("email", email );
                }
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


    public static void viewregistration(HttpServletRequest request, HttpServletResponse response) {

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




            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null); // attributo è usato per verificare se c'è un utente loggato
            // Questo attributo memorizza l'oggetto loggedUser e lo rende disponibile nella vista
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            // pagina di destinazione.
            request.setAttribute("viewUrl", "HomeManagement/registration");

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

    public static void registration(HttpServletRequest request, HttpServletResponse response) {

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

            // Recupera l'istanza del DAO per la gestione degli utenti
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

            // recupero i parametri
            String nome = request.getParameter("Nome");
            String cognome = request.getParameter("Cognome");
            String email = request.getParameter("Email");
            String telefono = request.getParameter("Telefono");
            String password = request.getParameter("Password");
            // Imposta la vista predefinita come la home page.
            request.setAttribute("viewUrl", "HomeManagement/home");

            // verifica se l'utente non è loggato
            if(loggedUser == null){
                // Verifica se l'email fornita è già associata a un utente esistente.
                Utente utente = utenteDAO.findEmail(email);

                // se nessun utente trovato con l'email fornita
                if(utente == null ){

                    // Crea un nuovo utente nel database con i dati forniti
                    utente = utenteDAO.create(0, password, nome, cognome, email, telefono, null, null, false);
                    // Salva l'utente nella sessione.
                    sessionUserDAO.create(utente.getIdNome(), utente.getPassword(), utente.getNome(), utente.getCognome(), utente.getEmail(), utente.getTelefono(), utente.getWallet(), utente.getStatoAccount(), utente.getRuolo());
                    // L'utente appena creato viene impostato come loggato.
                    viewhome(request, response); // Reindirizza l'utente alla home page
                    loggedUser = utente; // Aggiorna la variabile con il nuovo utente loggato.
                }

                // Caso in cui l'utente esiste già.
                else {
                    applicationMessage = "Utente già esistente!";
                    // Imposta la vista di registrazione.
                    request.setAttribute("viewUrl", "HomeManagement/registration");
                    // Mantiene i dati già inseriti nel form.
                    request.setAttribute("nome", nome );
                    request.setAttribute("cognome", cognome );
                    request.setAttribute("telefono", telefono );
                }
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

    public static void cerca(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("SONO NEL CONTROLLER");
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
            // Recupero delle DAO necessarie per i prodotti e le categorie
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();

            // recupero i parametri dal form
            String nomeprodotto = request.getParameter("Nomeprodotto");
            String marchioprodotto = request.getParameter("Marchioprodotto");
            String categoria = request.getParameter("Categoria");
            boolean promo = Boolean.parseBoolean(request.getParameter("Promo"));
            List<Prodotto> prodotti = new ArrayList<>();
            // ricerca prdotti
            prodotti = prodottoDAO.cerca(nomeprodotto, marchioprodotto, categoria, promo);

            System.out.println("guarda prodotti.size():"+prodotti.size());
            // scorro per l'intera lista di prodotti
            for(int i=0; i<prodotti.size(); i++){
                // trovo l'id della categoria associato per ogni prodotto
                // in modo tale da trovare tutte le altre caratteristiche associate per le categorie
                prodotti.get(i).setCategoria(categoriaDAO.findById(prodotti.get(i).getCategoria().getIdCategoria()));
            }


            // recupero tutte le informazioni sui marchi presenti nel database e le salvo in una lista
            MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
            List<Marchio> marchi = new ArrayList<>();
            marchi = marchioDAO.findAll();
            System.out.println("guarda marchi.size():"+marchi.size());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            // per passare i dati alla jsp
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "HomeManagement/home");
            System.out.println("guarda url controller:" + request.getAttribute("viewUrl"));
            request.setAttribute("Prodotti", prodotti);
            request.setAttribute("Categoria", categoria);
            request.setAttribute("marchi", marchi);
            request.setAttribute("Marchio", marchioprodotto);
            request.setAttribute("promo", promo);


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

    public static void viewhome(HttpServletRequest request, HttpServletResponse response) {

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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
           // I prodotti in promozione vengono recuperati dal ProdottoDAO
            List<Prodotto> prodotti = prodottoDAO.findPromo();
            MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
            // ottengo l'elenco completo dei marchi.
            List<Marchio> marchi = marchioDAO.findAll();



            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            // Gli attributi sono impostati per essere utilizzati nella pagina JSP

            // i primi 2 indicano se un utente è loggato e forniscono i suoi dettagli.
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("Prodotti", prodotti);
            request.setAttribute("marchi", marchi);
            request.setAttribute("promo", true);
            request.setAttribute("viewUrl", "HomeManagement/home");

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


    public static void logout(HttpServletRequest request, HttpServletResponse response) {

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


            // Se l'utente è loggato il suo account viene eliminato dalla sessione
            // con il metodo delete di UtenteDAO
            if(loggedUser != null){
                sessionUserDAO.delete(loggedUser);
                loggedUser = null;
            }

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            // Vengono recuperati i prodotti in promozione
            List<Prodotto> prodotti = prodottoDAO.findPromo();
            MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
            // Viene anche recuperato l'elenco completo dei marchi
            List<Marchio> marchi = marchioDAO.findAll();

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("Prodotti", prodotti);
            request.setAttribute("marchi", marchi);
            request.setAttribute("promo", true);
            request.setAttribute("viewUrl", "HomeManagement/home");


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
