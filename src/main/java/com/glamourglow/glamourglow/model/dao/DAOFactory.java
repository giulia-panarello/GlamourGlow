package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.dao.CookieImpl.CookieDAOFactory;
import com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl.PostgreSQLJDBCDAOFactory;

import java.util.Map;

public abstract class DAOFactory {

    public static final String POSTGRESQLJDBCIMPL = "PostgreSQLJDBCImpl";
    public static final String COOKIEIMPL= "CookieImpl";

    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    public abstract UtenteDAO getUtenteDAO();

    public abstract ProdottoDAO getProdottoDAO();
    public abstract OrdineDAO getOrdineDAO();
    public abstract MarchioDAO getMarchioDAO();
    public abstract DettagliOrdineDAO getDettagliOrdineDAO();

    public abstract CategoriaDAO getCategoriaDAO();
    public abstract  CarrelloDAO getCarrelloDAO();
    public abstract CouponDAO getCouponDAO();

    public static DAOFactory getDAOFactory(String whichFactory,Map factoryParameters) {

        if (whichFactory.equals(POSTGRESQLJDBCIMPL)) {
            return new PostgreSQLJDBCDAOFactory(factoryParameters);
        } else if (whichFactory.equals(COOKIEIMPL)) {
            return new CookieDAOFactory(factoryParameters);
        } else {
            return null;
        }
    }
}

