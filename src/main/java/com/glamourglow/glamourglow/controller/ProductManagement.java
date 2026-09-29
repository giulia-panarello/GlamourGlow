package com.glamourglow.glamourglow.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.glamourglow.glamourglow.model.dao.CategoriaDAO;
import com.glamourglow.glamourglow.model.dao.DAOFactory;
import com.glamourglow.glamourglow.model.dao.ProdottoDAO;
import com.glamourglow.glamourglow.model.dao.UtenteDAO;
import com.glamourglow.glamourglow.model.mo.Prodotto;
import com.glamourglow.glamourglow.model.mo.Utente;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import com.glamourglow.glamourglow.services.config.Configuration;
import com.glamourglow.glamourglow.services.logservice.LogService;




public class ProductManagement {

    private ProductManagement() {

    }

    public static void viewprodotto(HttpServletRequest request, HttpServletResponse response) {

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

           int id = Integer.parseInt(request.getParameter("id"));

            Prodotto prodotto = prodottoDAO.findById(id);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "ProductManagement/descrizione");

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
