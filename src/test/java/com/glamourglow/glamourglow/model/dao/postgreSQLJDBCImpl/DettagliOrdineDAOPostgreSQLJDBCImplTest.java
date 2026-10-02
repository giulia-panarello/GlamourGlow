
package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.mo.DettagliOrdine;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;

public class DettagliOrdineDAOPostgreSQLJDBCImplTest {

    @Provide
    Arbitrary<Long> idDettaglio() {
        return Arbitraries.longs().between(1L, 1000L);
    }

    @Test
    void create_dettaglioValido_restituisceDettaglio() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), any(String[].class)))
                .thenReturn(ps);

        when(ps.getGeneratedKeys()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        when(rs.getLong(1)).thenReturn(20L);

        PreparedStatement psFind = mock(PreparedStatement.class);
        ResultSet rsFind = mock(ResultSet.class);

        when(conn.prepareStatement(
                "SELECT * FROM dettagli_ordine WHERE id_dettaglio = ?"
        )).thenReturn(psFind);

        when(psFind.executeQuery()).thenReturn(rsFind);
        when(rsFind.next()).thenReturn(true);

        when(rsFind.getLong("id_dettaglio")).thenReturn(20L);
        when(rsFind.getInt("quantita")).thenReturn(2);
        when(rsFind.getDouble("prezzo_unitario")).thenReturn(25.50);
        when(rsFind.getString("coupon")).thenReturn("SCONTO10");
        when(rsFind.getLong("id_ordine")).thenReturn(10L);
        when(rsFind.getLong("id_prodotto")).thenReturn(5L);

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        DettagliOrdine dettaglio = dao.create(
                0L,
                10L,
                5L,
                2,
                25.50,
                "SCONTO10"
        );

        assertNotNull(dettaglio);

        assertEquals(20L, dettaglio.getIdDettaglio());
        assertEquals(2, dettaglio.getQuantita());
        assertEquals(25.50, dettaglio.getPrezzoUnitario());

        assertNotNull(dettaglio.getCoupon());
        assertEquals("SCONTO10", dettaglio.getCoupon().getCodice());

        assertNotNull(dettaglio.getOrdine());
        assertEquals(10L, dettaglio.getOrdine().getIdOrdine());

        assertNotNull(dettaglio.getProdotto());
        assertEquals(5L, dettaglio.getProdotto().getIdProdotto());

        verify(ps).setLong(1, 10L);
        verify(ps).setLong(2, 5L);
        verify(ps).setInt(3, 2);
        verify(ps).setDouble(4, 25.50);
        verify(ps).setString(5, "SCONTO10");
        verify(ps).executeUpdate();
        verify(ps).getGeneratedKeys();
    }


    @Property
    void findById_dettaglioPresente_restituisceDettaglio(
            @ForAll("idDettaglio") long id) throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_dettaglio")).thenReturn(id);
        when(rs.getInt("quantita")).thenReturn(2);
        when(rs.getDouble("prezzo_unitario")).thenReturn(25.50);
        when(rs.getString("coupon")).thenReturn("SCONTO10");
        when(rs.getLong("id_ordine")).thenReturn(10L);
        when(rs.getLong("id_prodotto")).thenReturn(5L);

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        DettagliOrdine dettaglio = dao.findById(id);

        assertNotNull(dettaglio);

        assertEquals(id, dettaglio.getIdDettaglio());
        assertEquals(2, dettaglio.getQuantita());
        assertEquals(25.50, dettaglio.getPrezzoUnitario());

        assertNotNull(dettaglio.getCoupon());
        assertEquals("SCONTO10", dettaglio.getCoupon().getCodice());

        assertNotNull(dettaglio.getOrdine());
        assertEquals(10L, dettaglio.getOrdine().getIdOrdine());

        assertNotNull(dettaglio.getProdotto());
        assertEquals(5L, dettaglio.getProdotto().getIdProdotto());

        verify(ps).setLong(1, id);
        verify(ps).executeQuery();
    }



    @Test
    void findByIdOrdine_dettagliPresenti_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getLong("id_dettaglio")).thenReturn(20L, 21L);
        when(rs.getInt("quantita")).thenReturn(2, 1);
        when(rs.getDouble("prezzo_unitario")).thenReturn(25.50, 15.00);
        when(rs.getString("coupon")).thenReturn("SCONTO10", (String) null);
        when(rs.getLong("id_ordine")).thenReturn(10L);
        when(rs.getLong("id_prodotto")).thenReturn(5L, 6L);

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        List<DettagliOrdine> lista = dao.findByIdOrdine(10L);

        assertNotNull(lista);
        assertEquals(2, lista.size());

        assertEquals(20L, lista.get(0).getIdDettaglio());
        assertEquals(2, lista.get(0).getQuantita());
        assertEquals(25.50, lista.get(0).getPrezzoUnitario());
        assertEquals("SCONTO10", lista.get(0).getCoupon().getCodice());
        assertEquals(10L, lista.get(0).getOrdine().getIdOrdine());
        assertEquals(5L, lista.get(0).getProdotto().getIdProdotto());

        assertEquals(21L, lista.get(1).getIdDettaglio());
        assertEquals(1, lista.get(1).getQuantita());
        assertEquals(15.00, lista.get(1).getPrezzoUnitario());
        assertEquals(6L, lista.get(1).getProdotto().getIdProdotto());


        verify(ps).setLong(1, 10L);
        verify(ps).executeQuery();
    }



    @Test
    void usoCoupon_couponPresente_restituisceTrue() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        boolean risultato = dao.usoCoupon(1L, "SCONTO10");

        assertTrue(risultato);

        verify(conn).prepareStatement(
                "SELECT 1 FROM dettagli_ordine WHERE coupon = ? LIMIT 1"
        );
        verify(ps).setString(1, "SCONTO10");
        verify(ps).executeQuery();
    }



    @Test
    void usoCoupon_couponNonPresente_restituisceFalse() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        boolean risultato = dao.usoCoupon(1L, "SCONTO10");

        assertFalse(risultato);

        verify(conn).prepareStatement(
                "SELECT 1 FROM dettagli_ordine WHERE coupon = ? LIMIT 1"
        );
        verify(ps).setString(1, "SCONTO10");
        verify(ps).executeQuery();
    }



    @Test
    void findById_dettaglioNonPresente_restituisceNull() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        DettagliOrdine risultato = dao.findById(999L);

        assertNull(risultato);

        verify(conn).prepareStatement(anyString());
        verify(ps).setLong(1, 999L);
        verify(ps).executeQuery();
    }


    @Test
    void create_nessunaChiaveGenerata_restituisceNull() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), any(String[].class)))
                .thenReturn(ps);

        when(ps.getGeneratedKeys()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        PreparedStatement psFind = mock(PreparedStatement.class);
        ResultSet rsFind = mock(ResultSet.class);

        when(conn.prepareStatement(anyString()))
                .thenReturn(psFind);

        when(psFind.executeQuery()).thenReturn(rsFind);
        when(rsFind.next()).thenReturn(false);

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        DettagliOrdine risultato = dao.create(
                1L,
                10L,
                20L,
                2,
                15.0,
                "SCONTO10"
        );

        assertNull(risultato);

        verify(ps).setLong(1, 10L);
        verify(ps).setLong(2, 20L);
        verify(ps).setInt(3, 2);
        verify(ps).setDouble(4, 15.0);
        verify(ps).setString(5, "SCONTO10");

        verify(ps).executeUpdate();
        verify(rs).next();
    }


    @Test
    void usoCoupon_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore DB"));

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.usoCoupon(1L, "SCONTO10")
        );

        assertTrue(exception.getMessage()
                .contains("Errore verifica coupon"));

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }

    @Test
    void create_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString(), any(String[].class)))
                .thenThrow(new java.sql.SQLException("Errore DB"));

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.create(
                        1L,
                        10L,
                        20L,
                        2,
                        15.0,
                        "SCONTO10"
                )
        );

        assertTrue(exception.getMessage()
                .contains("Errore creazione dettaglio ordine"));

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }

    @Test
    void findById_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore DB"));

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findById(999L)
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }

    @Test
    void findByIdOrdine_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore DB"));

        DettagliOrdineDAOPostgreSQLJDBCImpl dao =
                new DettagliOrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findByIdOrdine(10L)
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }


}

