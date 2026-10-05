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
    void resolveDatabaseUrl_conEnvUrlValida() {
        String customUrl = "jdbc:postgresql://customhost:5432/customdb";
        assertEquals(customUrl, Configuration.resolveDatabaseUrl(customUrl));
    }

    @Test
    void resolveDatabaseUrl_conNull_restituisceDefault() {
        assertEquals("jdbc:postgresql://localhost:5432/ecommerce", Configuration.resolveDatabaseUrl(null));
    }

    @Test
    void resolveDatabaseUrl_conStringaVuota_restituisceDefault() {
        assertEquals("jdbc:postgresql://localhost:5432/ecommerce", Configuration.resolveDatabaseUrl("   "));
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

        assertEquals("ecommerce_log.%g.%u.txt", Configuration.GLOBAL_LOGGER_FILE);

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