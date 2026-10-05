package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


import com.glamourglow.glamourglow.services.config.Configuration;
import com.glamourglow.glamourglow.model.dao.*;


public class PostgreSQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public PostgreSQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    protected void loadDriver() throws ClassNotFoundException {
        Class.forName(Configuration.DATABASE_DRIVER);
    }
    @Override
    public void beginTransaction() {
        try {
            loadDriver();

            String dbUrl = System.getenv("DB_URL");
            if (dbUrl != null && !dbUrl.isBlank()) {
                String dbUser = System.getenv("DB_USER");
                String dbPassword = System.getenv("DB_PASSWORD");
                if (dbUser != null && !dbUser.isBlank() && dbPassword != null && !dbPassword.isBlank()) {
                    this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                } else {
                    this.connection = DriverManager.getConnection(dbUrl);
                }
            } else {
                this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            }

            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver non trovato: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Errore di connessione: " + e.getMessage(), e);
        }
    }

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
        return new UtenteDAOPostgreSQLJDBCImpl(connection);
    }

    @Override
    public ProdottoDAO getProdottoDAO() {
        return new ProdottoDAOPostgreSQLJDBCImpl(connection);
    }

    @Override
    public OrdineDAO getOrdineDAO() {
        return new OrdineDAOPostgreSQLJDBCImpl(connection);
    }

    @Override
    public MarchioDAO getMarchioDAO() {
        return new MarchioDAOPostgreSQLJDBCImpl(connection);
    }

    @Override
    public DettagliOrdineDAO getDettagliOrdineDAO() {
        return new DettagliOrdineDAOPostgreSQLJDBCImpl(connection);
    }

    @Override
    public CategoriaDAO getCategoriaDAO() {
        return new CategoriaDAOPostgreSQLJDBCImpl(connection);
    }
    @Override
    public CouponDAO getCouponDAO(){
        return new CouponDAOPostgreSQLJDBCImpl(connection);
    }


    @Override
    public CarrelloDAO getCarrelloDAO() {
        return null;
    }


}