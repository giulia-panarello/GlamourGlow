package com.glamourglow.glamourglow.services.config;


import java.util.Calendar;
import java.util.logging.Level;

import com.glamourglow.glamourglow.model.dao.DAOFactory;

public class Configuration {

    /* Database Configuration */
    public static final String DAO_IMPL=DAOFactory.MYSQLJDBCIMPL;
    /*public static final String DATABASE_DRIVER="com.mysql.cj.jdbc.Driver"; */
    public static final String DATABASE_DRIVER = "org.postgresql.Driver";
    public static final String SERVER_TIMEZONE=Calendar.getInstance().getTimeZone().getID();

    public static final String
            DATABASE_URL = "jdbc:postgresql://localhost:5432/ecommerce";
    /* MYSQL: public static final String
            DATABASE_URL="jdbc:mysql://localhost:3306/ecommerce?user=root&password=90000Gmp&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+SERVER_TIMEZONE; */

    /* Session Configuration */
    public static final String COOKIE_IMPL=DAOFactory.COOKIEIMPL;

    /* Logger Configuration */
    public static final String GLOBAL_LOGGER_NAME="rubrica";
    public static final String GLOBAL_LOGGER_FILE="/Users/giuliapanarello/Desktop/ProgettoSistemiWeb/ecommerce_log.%g.%u.txt";
    public static final Level GLOBAL_LOGGER_LEVEL=Level.ALL;

}
