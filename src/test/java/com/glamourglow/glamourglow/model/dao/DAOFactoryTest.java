
package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.dao.CookieImpl.CookieDAOFactory;
import com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl.PostgreSQLJDBCDAOFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class DAOFactoryTest {

    @Test
    void getDAOFactory_PostgreSQLJDBC_restituiscePostgreSQLFactory() {

        DAOFactory factory = DAOFactory.getDAOFactory(
                DAOFactory.POSTGRESQLJDBCIMPL,
                new HashMap<>()
        );

        assertInstanceOf(
                PostgreSQLJDBCDAOFactory.class,
                factory
        );
    }

    @Test
    void getDAOFactory_Cookie_restituisceCookieFactory() {

        DAOFactory factory = DAOFactory.getDAOFactory(
                DAOFactory.COOKIEIMPL,
                new HashMap<>()
        );

        assertInstanceOf(
                CookieDAOFactory.class,
                factory
        );
    }

    @Test
    void getDAOFactory_tipoNonSupportato_restituisceNull() {

        DAOFactory factory = DAOFactory.getDAOFactory(
                "TIPO_NON_SUPPORTATO",
                new HashMap<>()
        );

        assertNull(factory);
    }

}

