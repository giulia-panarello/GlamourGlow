package com.glamourglow.glamourglow.services.config;
import com.glamourglow.glamourglow.model.dao.DAOFactory;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationTest {

    @Test
    void configurazioneDatabase() {

        assertEquals(
                DAOFactory.POSTGRESQLJDBCIMPL,
                Configuration.DAO_IMPL
        );

        assertEquals(
                "org.postgresql.Driver",
                Configuration.DATABASE_DRIVER
        );

        assertEquals(
                "jdbc:postgresql://localhost:5432/ecommerce",
                Configuration.DATABASE_URL
        );
    }

    @Test
    void configurazioneSessione() {

        assertEquals(
                DAOFactory.COOKIEIMPL,
                Configuration.COOKIE_IMPL
        );
    }

    @Test
    void configurazioneServerTimezone() {

        assertEquals(
                Calendar.getInstance()
                        .getTimeZone()
                        .getID(),
                Configuration.SERVER_TIMEZONE
        );
    }

    @Test
    void configurazioneLogger() {

        assertEquals(
                "rubrica",
                Configuration.GLOBAL_LOGGER_NAME
        );

        assertEquals(
                "/Users/giuliapanarello/Desktop/ProgettoSistemiWeb/ecommerce_log.%g.%u.txt",
                Configuration.GLOBAL_LOGGER_FILE
        );

        assertEquals(
                Level.ALL,
                Configuration.GLOBAL_LOGGER_LEVEL
        );
    }

    @Test
    void Configuration_costruttore() {
        Configuration configuration = new Configuration();

        assertNotNull(configuration);
    }
}