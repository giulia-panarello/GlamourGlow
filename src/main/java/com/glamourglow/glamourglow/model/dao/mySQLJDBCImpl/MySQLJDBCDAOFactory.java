package com.glamourglow.glamourglow.model.dao.mySQLJDBCImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


import com.glamourglow.glamourglow.services.config.Configuration;
import com.glamourglow.glamourglow.model.dao.*;


public class MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }


    @Override
    public void beginTransaction() {

        try {
            // 1. Carica il driver definito nella Configuration (org.postgresql.Driver)
            Class.forName(Configuration.DATABASE_DRIVER);

            try {
                Class.forName("org.postgresql.Driver");
                System.out.println("DRIVER CARICATO");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // 2. MODIFICA FONDAMENTALE: Passa URL, USER e PASSWORD separatamente
            this.connection = DriverManager.getConnection(
                    Configuration.DATABASE_URL

            );

            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver non trovato: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Errore di connessione: " + e.getMessage(), e);
        }

    }
   /* @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    */

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ProdottoDAO getProdottoDAO() {
        return new ProdottoDAOMySQLJDBCImpl(connection);
    }

    @Override
    public OrdineDAO getOrdineDAO() {
        return new OrdineDAOMySQLJDBCImpl(connection);
    }

    @Override
    public MarchioDAO getMarchioDAO() {
        return new MarchioDAOMySQLJDBCImpl(connection);
    }

    @Override
    public DettagliOrdineDAO getDettagliOrdineDAO() {
        return new DettagliOrdineDAOMySQLJDBCImpl(connection);
    }

    @Override
    public CategoriaDAO getCategoriaDAO() {
        return new CategoriaDAOMySQLJDBCImpl(connection);
    }
    @Override
    public CouponDAO getCouponDAO(){
        return new CouponDAOMySQLJDBCImpl(connection);
    }


    @Override
    public CarrelloDAO getCarrelloDAO() {
        return null;
    }


}