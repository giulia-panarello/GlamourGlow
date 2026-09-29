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

            if(loggedUser != null){

                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());

                for(int i=0; i<cookiecarrello.size(); i++){

                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                    cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                }

                request.setAttribute("carrello", cookiecarrello);
                request.setAttribute("viewUrl", "UserManagement/carrello");

            }

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


            if(loggedUser != null){


            if(request.getParameter("quantita")!=null && request.getParameter("idprod")!=null)
            {
                int qta = Integer.parseInt(request.getParameter("quantita"));
                long idprod = Long.parseLong(request.getParameter("idprod"));
                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                sessionCarrelloDAO.create(loggedUser.getIdNome(), idprod, qta);

                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                Prodotto prodotto = prodottoDAO.findById(idprod);
                request.setAttribute("prodotto", prodotto);

                if(qta == 0){

                    UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                    request.setAttribute("viewUrl", "UserManagement/carrello");
                    List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());

                    for(int i=0; i<cookiecarrello.size(); i++){
                        cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                        cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                    }
                    for(int i=0;i<cookiecarrello.size();i++)
                    {
                        if(cookiecarrello.get(i).getProdotto().getIdProdotto()==idprod)
                        {
                            cookiecarrello.remove(cookiecarrello.get(i));
                        }
                    }
                    request.setAttribute("carrello", cookiecarrello);
                }

                else{
                    request.setAttribute("viewUrl", "ProductManagement/descrizione");
                    applicationMessage = prodotto.getNomeProdotto() + " x" + qta + " aggiunto al carrello";
                }

            }
            else
            {

                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());

                for(int i=0; i<cookiecarrello.size(); i++){

                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
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

            if(loggedUser != null){


                CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());

                request.setAttribute("viewUrl", "UserManagement/checkout" );
                for(int i=0; i<cookiecarrello.size(); i++){

                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                    cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));

                    if(cookiecarrello.get(i).getQta() > cookiecarrello.get(i).getProdotto().getQuantitaDispo()){
                        request.setAttribute("viewUrl", "UserManagement/carrello");
                        applicationMessage = "Non sono disponibili " + cookiecarrello.get(i).getQta() + " quantità!";
                        request.setAttribute("applicationMessage", applicationMessage);
                    }


                    if(cookiecarrello.get(i).getProdotto().isStatoprodotto()){

                        request.setAttribute("viewUrl", "UserManagement/carrello");
                        applicationMessage = "Impossibile procedere al checkout: il prodotto " + cookiecarrello.get(i).getProdotto().getNomeProdotto() + " è stato temporaneamente bloccato!";
                        request.setAttribute("applicationMessage", applicationMessage);
                    }
                }

                request.setAttribute("carrello", cookiecarrello);

            }

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

            if(loggedUser != null){

                String[] coupon = request.getParameterValues("coupon");
                String[] couponvalidi = new String[coupon.length];
                String[] percentualisconto = new String[coupon.length];

                for(int i=0; i< coupon.length; i++){
                    Coupon coupondb = new Coupon();
                    int flag = 0;

                    coupondb = couponDAO.findByCode(coupon[i]);
                    if(coupon[i].isEmpty()){
                        flag = 1;
                    }

                    if(coupondb == null && !coupon[i].isEmpty()){
                        applicationMessage = "Coupon non valido, poichè non esiste!";
                        flag = 1;
                    }


                    for(int j=0; j<i; j++){
                        if(coupon[i].equals(couponvalidi[j])){
                            applicationMessage = "Coupon non valido, poichè già inserito!";
                            flag = 1;
                        }
                    }


                    if(dettagliOrdineDAO.usoCoupon(loggedUser.getIdNome(), coupon[i])){
                        applicationMessage = "Coupon non valido, poichè è già stato usato!";
                        flag = 1;
                    }

                    if(flag == 0){
                        couponvalidi[i] = coupon[i];
                        applicationMessage = "Coupon inserito con successo!";
                        percentualisconto[i] = ""+coupondb.getSconto();
                    }
                }
                request.setAttribute("coupon", couponvalidi);
                request.setAttribute("sconto", percentualisconto);
                viewcheckout(request, response);
                }
                else{

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


            if(loggedUser != null){

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

            if(loggedUser != null){
                request.setAttribute("viewUrl", "UserManagement/wallet");
                applicationMessage = "Saldo ricaricato";
                request.setAttribute("applicationMessage", applicationMessage);
                Double saldo = Double.parseDouble(request.getParameter("saldo"));
                utenteDAO.updatewallet(loggedUser.getIdNome(), saldo + loggedUser.getWallet());
                sessionUserDAO.create(loggedUser.getIdNome(), loggedUser.getPassword(), loggedUser.getNome(), loggedUser.getCognome(), loggedUser.getEmail(), loggedUser.getTelefono(), loggedUser.getWallet() + saldo, loggedUser.getStatoAccount(), loggedUser.getRuolo());

            }
            else{
                request.setAttribute("viewUrl", "UserManagement/login");
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

            if(loggedUser != null){

                String indirizzo = (String) request.getParameter("Indirizzo");
                String stato = (String) request.getParameter("Stato");
                String citta = (String) request.getParameter("Citta");
                String[] coupon = request.getParameterValues("coupon");
                request.setAttribute("viewUrl", "UserManagement/checkout");
                List<Carrello> cookiecarrello = sessionCarrelloDAO.findById(loggedUser.getIdNome());
                int flag = 0;
                double totalecarrello = 0;

                if(coupon != null) {
                    for (int i = 0; i < coupon.length; i++) {
                        System.out.println("coupon["+ i +"]: " + coupon[i]);
                    }
                }
                System.out.println("cookiecarrello.size:" + cookiecarrello.size());

                for(int i=0; i< cookiecarrello.size(); i++){

                    cookiecarrello.get(i).setUtente(utenteDAO.findById(cookiecarrello.get(i).getUtente().getIdNome()));
                    cookiecarrello.get(i).setProdotto(prodottoDAO.findById(cookiecarrello.get(i).getProdotto().getIdProdotto()));
                    double prezzo;

                    if(cookiecarrello.get(i).getProdotto().getInPromo()){
                        prezzo = cookiecarrello.get(i).getProdotto().getPrezzoSconto();
                    }
                    else{
                        prezzo = cookiecarrello.get(i).getProdotto().getPrezzo();

                    }

                    if(coupon != null){


                        if(coupon[i] !=null && !coupon[i].equals("null")){
                           Coupon coupondb = couponDAO.findByCode(coupon[i]);
                            totalecarrello += (prezzo - (prezzo * coupondb.getSconto()/100)) * cookiecarrello.get(i).getQta();

                        }
                        else{
                            totalecarrello += prezzo * cookiecarrello.get(i).getQta();
                        }

                    }
                    else{
                        totalecarrello += prezzo * cookiecarrello.get(i).getQta();
                    }

                    if(cookiecarrello.get(i).getQta() > cookiecarrello.get(i).getProdotto().getQuantitaDispo()){


                        request.setAttribute("viewUrl", "UserManagement/carrello");
                        applicationMessage = "Le quantità selezionate non sono più disponibili";
                        request.setAttribute("applicationMessage", applicationMessage);
                        flag = 1;

                    }
                }

               if(totalecarrello > loggedUser.getWallet()){
                   applicationMessage = "Impossibile procedere con il checkout, credito non disponibile!";
                   viewcheckout(request, response);
                   request.setAttribute("applicationMessage", applicationMessage);
                   flag = 1;

               }


               if(flag == 0){

                   List<Prodotto> prodotti = prodottoDAO.findPromo();
                   MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                   List<Marchio> marchi = marchioDAO.findAll();

                   String Message = "Acquisto effettuato con successo";
                   request.setAttribute("Message", Message);
                   request.setAttribute("Prodotti", prodotti);
                   request.setAttribute("marchi", marchi);
                   request.setAttribute("promo", true);
                   request.setAttribute("viewUrl", "HomeManagement/home");

                   LocalDateTime now = LocalDateTime.now();
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                   String formattedDateTime = now.format(formatter);
                   Ordine ordine = ordineDAO.create(0, loggedUser.getIdNome(), formattedDateTime, null, totalecarrello, indirizzo, citta, stato );

                   for(int i = 0; i<cookiecarrello.size(); i++){
                       double prezzo;
                       if(cookiecarrello.get(i).getProdotto().getInPromo()){
                           prezzo = cookiecarrello.get(i).getProdotto().getPrezzoSconto();
                       }

                       else{
                           prezzo = cookiecarrello.get(i).getProdotto().getPrezzo();
                       }

                       if(coupon != null){
                           if(coupon[i] != null && !coupon[i].equals("null")){

                               dettagliOrdineDAO.create(null, ordine.getIdOrdine(), cookiecarrello.get(i).getProdotto().getIdProdotto(), cookiecarrello.get(i).getQta(), prezzo, coupon[i]);
                           }

                           else{
                               dettagliOrdineDAO.create(null, ordine.getIdOrdine(), cookiecarrello.get(i).getProdotto().getIdProdotto(), cookiecarrello.get(i).getQta(), prezzo, null);
                           }

                       }

                       else{

                           dettagliOrdineDAO.create(null, ordine.getIdOrdine(), cookiecarrello.get(i).getProdotto().getIdProdotto(), cookiecarrello.get(i).getQta(), prezzo, null);
                       }

                       sessionCarrelloDAO.delete(loggedUser.getIdNome(), cookiecarrello.get(i).getProdotto().getIdProdotto());
                   }

                   utenteDAO.updatewallet(loggedUser.getIdNome(), loggedUser.getWallet() - totalecarrello);
                   sessionUserDAO.create(loggedUser.getIdNome(), loggedUser.getPassword(), loggedUser.getNome(), loggedUser.getCognome(), loggedUser.getEmail(), loggedUser.getTelefono(), loggedUser.getWallet() - totalecarrello , loggedUser.getStatoAccount(), loggedUser.getRuolo());

               }
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

            if(loggedUser != null){

            List<Ordine> ordini = new ArrayList<>();
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            CouponDAO couponDAO = daoFactory.getCouponDAO();
            ordini = ordineDAO.findByUser(loggedUser.getIdNome());
            Coupon coupon ;


            for(int i = 0; i<ordini.size(); i++){

                ordini.get(i).setDettagliOrdine(dettagliOrdineDAO.findByIdOrdine(ordini.get(i).getIdOrdine()));

                for(int j=0; j<ordini.get(i).getDettagliOrdine().size(); j++){

                    coupon = couponDAO.findByCode(ordini.get(i).getDettagliOrdine().get(j).getCoupon().getCodice());
                    if(coupon != null){

                        double prezzo;
                        prezzo = (ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario()) - (ordini.get(i).getDettagliOrdine().get(j).getPrezzoUnitario() * ((double)coupon.getSconto()/100));
                        ordini.get(i).getDettagliOrdine().get(j).setPrezzoUnitario(prezzo);

                    }
                    ordini.get(i).getDettagliOrdine().get(j).setProdotto(prodottoDAO.findById(ordini.get(i).getDettagliOrdine().get(j).getProdotto().getIdProdotto()));
                }


            }
            request.setAttribute("viewUrl", "UserManagement/ordini");
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
