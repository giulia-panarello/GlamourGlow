package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.*;
import com.glamourglow.glamourglow.services.config.Configuration;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PostgreSQLJDBCDAOFactoryTest {


    @Test
    void getUtenteDAO_restituisceDAO() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        UtenteDAO dao = factory.getUtenteDAO();

        assertNotNull(dao);
        assertInstanceOf(UtenteDAOPostgreSQLJDBCImpl.class, dao);
    }



    @Test
    void getProdottoDAO_restituisceDAO() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        ProdottoDAO dao = factory.getProdottoDAO();

        assertNotNull(dao);
        assertInstanceOf(ProdottoDAOPostgreSQLJDBCImpl.class, dao);
    }


    @Test
    void getOrdineDAO_restituisceDAO() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        OrdineDAO dao = factory.getOrdineDAO();

        assertNotNull(dao);
        assertInstanceOf(OrdineDAOPostgreSQLJDBCImpl.class, dao);
    }


    @Test
    void getMarchioDAO_restituisceDAO() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        MarchioDAO dao = factory.getMarchioDAO();

        assertNotNull(dao);
        assertInstanceOf(MarchioDAOPostgreSQLJDBCImpl.class, dao);
    }


    @Test
    void getDettagliOrdineDAO_restituisceDAO() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        DettagliOrdineDAO dao = factory.getDettagliOrdineDAO();

        assertNotNull(dao);
        assertInstanceOf(
                DettagliOrdineDAOPostgreSQLJDBCImpl.class,
                dao
        );
    }



    @Test
    void getCategoriaDAO_restituisceDAO() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        CategoriaDAO dao = factory.getCategoriaDAO();

        assertNotNull(dao);
        assertInstanceOf(CategoriaDAOPostgreSQLJDBCImpl.class, dao);
    }



    @Test
    void getCouponDAO_restituisceDAO() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        CouponDAO dao = factory.getCouponDAO();

        assertNotNull(dao);
        assertInstanceOf(CouponDAOPostgreSQLJDBCImpl.class, dao);
    }



    @Test
    void getCarrelloDAO_restituisceNull() {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        CarrelloDAO dao = factory.getCarrelloDAO();

        assertNull(dao);
    }


    @Test
    void commitTransaction_esegueCommit() throws Exception {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        Connection connection = mock(Connection.class);

        Field field =
                PostgreSQLJDBCDAOFactory.class.getDeclaredField("connection");

        field.setAccessible(true);
        field.set(factory, connection);

        factory.commitTransaction();

        verify(connection).commit();
    }



    @Test
    void rollbackTransaction_esegueRollback() throws Exception {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        Connection connection = mock(Connection.class);

        Field field =
                PostgreSQLJDBCDAOFactory.class.getDeclaredField("connection");

        field.setAccessible(true);
        field.set(factory, connection);

        factory.rollbackTransaction();

        verify(connection).rollback();
    }


    @Test
    void closeTransaction_esegueClose() throws Exception {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        Connection connection = mock(Connection.class);

        Field field =
                PostgreSQLJDBCDAOFactory.class.getDeclaredField("connection");

        field.setAccessible(true);
        field.set(factory, connection);

        factory.closeTransaction();

        verify(connection).close();
    }


    @Test
    void commitTransaction_erroreSQL_lanciaRuntimeException() throws Exception {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        Connection connection = mock(Connection.class);

        SQLException exception =
                new SQLException("Errore commit");

        doThrow(exception)
                .when(connection)
                .commit();

        Field field =
                PostgreSQLJDBCDAOFactory.class.getDeclaredField("connection");

        field.setAccessible(true);
        field.set(factory, connection);

        RuntimeException result =
                assertThrows(
                        RuntimeException.class,
                        factory::commitTransaction
                );

        assertSame(exception, result.getCause());
    }



    @Test
    void rollbackTransaction_erroreSQL_lanciaRuntimeException() throws Exception {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        Connection connection = mock(Connection.class);

        SQLException exception =
                new SQLException("Errore rollback");

        doThrow(exception)
                .when(connection)
                .rollback();

        Field field =
                PostgreSQLJDBCDAOFactory.class.getDeclaredField("connection");

        field.setAccessible(true);
        field.set(factory, connection);

        RuntimeException result =
                assertThrows(
                        RuntimeException.class,
                        factory::rollbackTransaction
                );

        assertSame(exception, result.getCause());
    }


    @Test
    void closeTransaction_erroreSQL_lanciaRuntimeException() throws Exception {

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        Connection connection = mock(Connection.class);

        SQLException exception =
                new SQLException("Errore close");

        doThrow(exception)
                .when(connection)
                .close();

        Field field =
                PostgreSQLJDBCDAOFactory.class.getDeclaredField("connection");

        field.setAccessible(true);
        field.set(factory, connection);

        RuntimeException result =
                assertThrows(
                        RuntimeException.class,
                        factory::closeTransaction
                );

        assertSame(exception, result.getCause());
    }



    @Test
    void beginTransaction_disabilitaAutoCommit() throws Exception {
        Connection connectionMock = mock(Connection.class);

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock
                    .when(() -> DriverManager.getConnection(Configuration.DATABASE_URL))
                    .thenReturn(connectionMock);

            PostgreSQLJDBCDAOFactory factory =
                    new PostgreSQLJDBCDAOFactory(new HashMap<>());

            factory.beginTransaction();

            verify(connectionMock).setAutoCommit(false);
        }
    }

    @Test
    void beginTransaction_erroreSQL_lanciaRuntimeException() {
        SQLException exceptionDaLanciare =
                new SQLException("Errore di connessione");

        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>());

        try (MockedStatic<DriverManager> driverManagerMock =
                     mockStatic(DriverManager.class)) {

            driverManagerMock
                    .when(() -> DriverManager.getConnection(Configuration.DATABASE_URL))
                    .thenThrow(exceptionDaLanciare);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    factory::beginTransaction
            );

            assertSame(
                    exceptionDaLanciare,
                    exception.getCause()
            );
        }
    }

    @Test
    void beginTransaction_driverNonTrovato_lanciaRuntimeException() {
        PostgreSQLJDBCDAOFactory factory =
                new PostgreSQLJDBCDAOFactory(new HashMap<>()) {

                    @Override
                    protected void loadDriver()
                            throws ClassNotFoundException {

                        throw new ClassNotFoundException(
                                "Driver non trovato"
                        );
                    }
                };

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                factory::beginTransaction
        );

        assertInstanceOf(
                ClassNotFoundException.class,
                exception.getCause()
        );
    }


}