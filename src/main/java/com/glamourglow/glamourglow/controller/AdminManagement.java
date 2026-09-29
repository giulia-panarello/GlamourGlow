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

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser = null;
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

            List<Utente> utenteList = new ArrayList<>();
            List<List<Ordine>> ordini = new ArrayList<>();

            if(loggedUser != null && loggedUser.getRuolo()){
                utenteList =  utenteDAO.findSearch();
                request.setAttribute("viewUrl", "AdminManagement/gestioneutente");

                for(int i=0; i<utenteList.size(); i++){
                    ordini.add(ordineDAO.findByUser(utenteList.get(i).getIdNome()));
                }

                request.setAttribute("ordini", ordini);
                request.setAttribute("utenteList", utenteList);

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


    public static void modificautente(HttpServletRequest request, HttpServletResponse response) {


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

            List<Utente> utenteList = new ArrayList<>();
            List<List<Ordine>> ordini = new ArrayList<>();


            if(loggedUser != null && loggedUser.getRuolo()){
                request.setAttribute("viewUrl", "AdminManagement/gestioneutente");


              String email = request.getParameter("email");
               boolean ruolo = Boolean.parseBoolean(request.getParameter("ruolo"));
                String stato = request.getParameter("stato");
                long id = Long.parseLong(request.getParameter("id"));

                Utente utente = new Utente();
                utente.setStatoAccount(stato);
                utente.setEmail(email);
                utente.setRuolo(ruolo);
                utente.setIdNome(id);


                boolean emailAlreadyInUse = false;
                Utente existingUser = utenteDAO.findEmail(email);
                if(existingUser!=null && existingUser.getIdNome()!=id){

                    emailAlreadyInUse = true;
                    applicationMessage = "Impossibile modificare email";
                }


                if (!emailAlreadyInUse){

                    if(utente.getIdNome() == (loggedUser.getIdNome())){

                        utente.setNome(loggedUser.getNome());
                        utente.setCognome(loggedUser.getCognome());
                        utente.setPassword(loggedUser.getPassword());
                        utente.setWallet(loggedUser.getWallet());
                        loggedUser = utente;
                        sessionUserDAO.create(utente.getIdNome(), utente.getPassword(), utente.getNome(), utente.getCognome(), utente.getEmail(), null, utente.getWallet(), utente.getStatoAccount(), utente.getRuolo());

                    }
                    utenteDAO.update(utente);

                }

                utenteList =  utenteDAO.findSearch();

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


                for(int i=0; i<utenteList.size(); i++){
                    ordini.add(ordineDAO.findByUser(utenteList.get(i).getIdNome()));
                }

                request.setAttribute("ordini", ordini);
                request.setAttribute("utenteList", utenteList);
                request.setAttribute("applicationMessage", applicationMessage);
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


            if(loggedUser != null){

                List<Ordine> ordini = new ArrayList<>();
                OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
                DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
                CouponDAO couponDAO = daoFactory.getCouponDAO();
                Coupon coupon;
                ordini = ordineDAO.findAll();

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
                        ordini.get(i).setUtente(utenteDAO.findById(ordini.get(i).getUtente().getIdNome()));
                    }


                }

                request.setAttribute("viewUrl", "AdminManagement/gestioneordini");
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


    public static void modificaordini(HttpServletRequest request, HttpServletResponse response) {


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
            CouponDAO couponDAO = daoFactory.getCouponDAO();
            Coupon coupon ;

            List<Utente> utenteList = new ArrayList<>();

            if(loggedUser != null && loggedUser.getRuolo()){
                request.setAttribute("viewUrl", "AdminManagement/gestioneordini");
                String stato = request.getParameter("stato");
                long id = Long.parseLong(request.getParameter("id"));
                ordineDAO.updateordine(id, stato);

                List<Ordine> ordini = new ArrayList<>();
                DettagliOrdineDAO dettagliOrdineDAO = daoFactory.getDettagliOrdineDAO();
                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                ordini = ordineDAO.findAll();
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
                        ordini.get(i).setUtente(utenteDAO.findById(ordini.get(i).getUtente().getIdNome()));
                    }


                }

                request.setAttribute("viewUrl", "AdminManagement/gestioneordini");
                request.setAttribute("ordini", ordini);
                request.setAttribute("utenteList", utenteList);
                request.setAttribute("applicationMessage", applicationMessage);
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

            if(loggedUser != null){

                CouponDAO couponDAO = daoFactory.getCouponDAO();
                List<Coupon> coupon = new ArrayList<>();
                coupon = couponDAO.findAll();
                request.setAttribute("coupon", coupon);
                request.setAttribute("viewUrl", "AdminManagement/gestionecoupon");
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

            if(loggedUser != null && loggedUser.getRuolo()){

            int sconto = Integer.parseInt(request.getParameter("sconto"));
            String codice = request.getParameter("codice");
            String vecchiocodice = request.getParameter("vecchiocodice");

            if(couponDAO.findByCode(codice) != null){

                if(codice.equals(vecchiocodice)){
                    couponDAO.ModificaCoupon(vecchiocodice, codice, sconto);
                }

                else{
                    applicationMessage = "Codice già in uso ad altro coupon!";
                    request.setAttribute("applicationMessage", applicationMessage);
                }
            }

            else{
                applicationMessage = "Coupon " + vecchiocodice + " modificato in " + codice;
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

            CouponDAO couponDAO = daoFactory.getCouponDAO();
            int sconto = Integer.parseInt(request.getParameter("sconto"));
            String codice = request.getParameter("codice");

            if(loggedUser != null && loggedUser.getRuolo())
            {

                if(couponDAO.findByCode(codice) != null){
                   applicationMessage = "Codice già in uso ad altro coupon!";

                }
                else{
                    applicationMessage = "Nuovo coupon creato con successo!";
                    couponDAO.creaCoupon(codice, sconto);
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

    public static void gestioneprodotti(HttpServletRequest request, HttpServletResponse response) {


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

            List<Prodotto> prodotti = new ArrayList<>();
            List<Marchio> marchi = new ArrayList<>();
            List<Categoria> categorie = new ArrayList<>();

            if(loggedUser != null && loggedUser.getRuolo()){

                ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                marchi = marchioDAO.findAll();
                categorie = categoriaDAO.findAll();
                prodotti = prodottoDAO.cercaadmin(null, null, null);

                request.setAttribute("prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("categorie", categorie);
                request.setAttribute("viewUrl", "AdminManagement/gestioneprodotti");


            }

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

            if(loggedUser != null && loggedUser.getRuolo()){
                request.setAttribute("viewUrl", "AdminManagement/gestioneutente");

                String nomeprodotto = request.getParameter("nomeprodotto");
               double prezzo = Double.parseDouble(request.getParameter("prezzo"));
               int quantita = Integer.parseInt(request.getParameter("quantita"));
               boolean promozione = Boolean.parseBoolean(request.getParameter("promozione"));
               double prezzo_scontato = Double.parseDouble(request.getParameter("prezzo_scontato"));
               boolean stato = Boolean.parseBoolean(request.getParameter("stato"));
               long idprodotto = Long.parseLong(request.getParameter("idprodotto"));


               Prodotto prodotto = new Prodotto();

                prodotto.setNomeProdotto(nomeprodotto);
                prodotto.setPrezzo(prezzo);
                prodotto.setQuantitaDispo(quantita);
                prodotto.setInPromo(promozione);
                prodotto.setPrezzoSconto(prezzo_scontato);
                prodotto.setStatoprodotto(stato);
                prodotto.setIdProdotto(idprodotto);

                prodottoDAO.modifica(prodotto);

                List<Prodotto> prodotti = new ArrayList<>();
                List<Marchio> marchi = new ArrayList<>();
                List<Categoria> categorie = new ArrayList<>();
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                marchi = marchioDAO.findAll();
                CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();
                categorie = categoriaDAO.findAll();
                prodotti = prodottoDAO.cercaadmin(null, null, null);


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


            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

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

            Prodotto prodotto = new Prodotto();
            Marchio marc = new Marchio();
            marc.setIdMarchio(marchio);
            Categoria categ = new Categoria();
            categ.setIdCategoria(categoria);

            if(loggedUser != null && loggedUser.getRuolo())
            {

                List<Prodotto> prodotti = new ArrayList<>();
                List<Marchio> marchi = new ArrayList<>();
                List<Categoria> categorie = new ArrayList<>();


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


                prodottoDAO.create(prodotto);
                prodotti = prodottoDAO.cercaadmin(null, null, null);
                MarchioDAO marchioDAO = daoFactory.getMarchioDAO();
                marchi = marchioDAO.findAll();
                CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();
                categorie = categoriaDAO.findAll();

                request.setAttribute("prodotti", prodotti);
                request.setAttribute("marchi", marchi);
                request.setAttribute("categorie", categorie);
                applicationMessage = "Prodotto aggiunto con successo!";
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

}
