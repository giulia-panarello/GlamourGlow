
package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.mo.Coupon;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;

public class CouponDAOPostgreSQLJDBCImplTest {

    @Provide
    Arbitrary<String> codiceCoupon() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }
    @Provide
    Arbitrary<Integer> scontoCoupon() {
        return Arbitraries.integers().between(1, 100);
    }


    @Property
    void findByCode_couponPresente_restituisceCoupon(
            @ForAll("codiceCoupon") String codice) throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("codice")).thenReturn(codice);
        when(rs.getInt("sconto")).thenReturn(10);

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        Coupon coupon = dao.findByCode(codice);

        assertNotNull(coupon);
        assertEquals(codice, coupon.getCodice());
        assertEquals(10, coupon.getSconto());

        verify(ps).setString(1, codice);
        verify(ps).executeQuery();
    }


    @Property
    void findByCode_couponNonPresente_restituisceNull(
            @ForAll("codiceCoupon") String codice) throws Exception {


        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        Coupon coupon = dao.findByCode(codice);

        assertNull(coupon);

        verify(ps).setString(1, codice);
        verify(ps).executeQuery();

    }


    @Test
    void findAll_couponPresenti_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getString("codice"))
                .thenReturn("SCONTO10", "SCONTO20");

        when(rs.getInt("sconto"))
                .thenReturn(10, 20);

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        List<Coupon> coupon = dao.findAll();

        assertNotNull(coupon);
        assertEquals(2, coupon.size());

        assertEquals("SCONTO10", coupon.get(0).getCodice());
        assertEquals(10, coupon.get(0).getSconto());

        assertEquals("SCONTO20", coupon.get(1).getCodice());
        assertEquals(20, coupon.get(1).getSconto());

        verify(ps).executeQuery();


    }


    @Test
    void findAll_nessunCoupon_restituisceListaVuota() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        // Nessun coupon trovato
        when(rs.next()).thenReturn(false);

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        List<Coupon> coupon = dao.findAll();

        assertNotNull(coupon);
        assertTrue(coupon.isEmpty());

        verify(ps).executeQuery();
    }

    @Property
    void creaCoupon_inserisceCodiceESconto(
            @ForAll("codiceCoupon") String codice,
            @ForAll("scontoCoupon") int sconto) throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        dao.creaCoupon(codice, sconto);

        verify(conn).prepareStatement(
                "INSERT INTO coupon (codice, sconto) VALUES (?, ?)"
        );

        verify(ps).setString(1, codice);
        verify(ps).setInt(2, sconto);
        verify(ps).executeUpdate();

    }


    @Test
    void modificaCoupon_aggiornaCodiceESconto() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        dao.ModificaCoupon("SCONTO10", "SCONTO25", 25);

        verify(conn).prepareStatement(
                "UPDATE coupon SET codice = ?, sconto = ? WHERE codice = ?"
        );

        verify(ps).setString(1, "SCONTO25");
        verify(ps).setInt(2, 25);
        verify(ps).setString(3, "SCONTO10");
        verify(ps).executeUpdate();
    }



    @Test
    void findByCode_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore database"));

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findByCode("SCONTO10")
        );

        assertTrue(
                exception.getMessage().contains("Errore findByCode Coupon")
        );
        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }


    @Test
    void findAll_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore database"));

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findAll()
        );

        assertTrue(exception.getMessage().contains("Errore findAll Coupon"));
        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }

    @Test
    void creaCoupon_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore database"));

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.creaCoupon("SCONTO30", 30)
        );

        assertTrue(exception.getMessage().contains("Errore creazione Coupon"));
        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }

    @Test
    void modificaCoupon_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore database"));

        CouponDAOPostgreSQLJDBCImpl dao =
                new CouponDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.ModificaCoupon("SCONTO10", "SCONTO25", 25)
        );

        assertTrue(exception.getMessage().contains("Errore modifica Coupon"));
        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }



}

