
package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.mo.Ordine;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;

public class OrdineDAOPostgreSQLJDBCImplTest {

    @Provide
    Arbitrary<Long> idOrdine() {
        return Arbitraries.longs().between(1L, 1000L);
    }
    @Test
    void create_ordineValido_restituisceOrdine() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), any(String[].class)))
                .thenReturn(ps);

        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong(1)).thenReturn(10L);

        PreparedStatement psFind = mock(PreparedStatement.class);
        ResultSet rsFind = mock(ResultSet.class);

        when(conn.prepareStatement(
                "SELECT * FROM ordine WHERE id_ordine = ?"
        )).thenReturn(psFind);

        when(psFind.executeQuery()).thenReturn(rsFind);
        when(rsFind.next()).thenReturn(true);

        when(rsFind.getLong("id_ordine")).thenReturn(10L);
        when(rsFind.getString("data_ordine"))
                .thenReturn("2026-08-19 10:30:00");
        when(rsFind.getString("stato_ordine"))
                .thenReturn("In elaborazione");
        when(rsFind.getDouble("totale_ordine"))
                .thenReturn(99.90);
        when(rsFind.getString("indirizzo_consegna"))
                .thenReturn("Via Roma 10");
        when(rsFind.getString("citta"))
                .thenReturn("Milano");
        when(rsFind.getString("stato"))
                .thenReturn("Italia");
        when(rsFind.getLong("id_utente"))
                .thenReturn(5L);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        Ordine ordine = dao.create(
                0L,
                5L,
                "2026-08-19 10:30:00",
                "In elaborazione",
                99.90,
                "Via Roma 10",
                "Milano",
                "Italia"
        );

        assertNotNull(ordine);
        assertEquals(10L, ordine.getIdOrdine());
        assertEquals("In elaborazione", ordine.getStatoOrdine());
        assertEquals(99.90, ordine.getTotaleOrdine());
        assertEquals("Via Roma 10", ordine.getIndirizzoConsegna());
        assertEquals("Milano", ordine.getCitta());
        assertEquals("Italia", ordine.getStato());

        assertNotNull(ordine.getUtente());
        assertEquals(5L, ordine.getUtente().getIdNome());

        verify(ps).setLong(1, 5L);
        verify(ps).setDouble(3, 99.90);
        verify(ps).executeUpdate();
        verify(ps).getGeneratedKeys();
    }



    @Test
    void findByUser_ordiniPresenti_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getLong("id_ordine"))
                .thenReturn(1L, 2L);

        when(rs.getString("data_ordine"))
                .thenReturn("2026-08-19 10:30:00", "2026-08-19 11:30:00");

        when(rs.getString("stato_ordine"))
                .thenReturn("In elaborazione", "Spedito");

        when(rs.getDouble("totale_ordine"))
                .thenReturn(50.00, 100.00);

        when(rs.getString("indirizzo_consegna"))
                .thenReturn("Via Roma 10", "Via Milano 20");

        when(rs.getString("citta"))
                .thenReturn("Milano", "Roma");

        when(rs.getString("stato"))
                .thenReturn("Italia", "Italia");

        when(rs.getLong("id_utente"))
                .thenReturn(5L, 5L);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        List<Ordine> ordini = dao.findByUser(5L);

        assertNotNull(ordini);
        assertEquals(2, ordini.size());

        assertEquals(1L, ordini.get(0).getIdOrdine());
        assertEquals(50.00, ordini.get(0).getTotaleOrdine());

        assertEquals(2L, ordini.get(1).getIdOrdine());
        assertEquals(100.00, ordini.get(1).getTotaleOrdine());

        assertEquals(5L, ordini.get(0).getUtente().getIdNome());
        assertEquals(5L, ordini.get(1).getUtente().getIdNome());

        verify(ps).setLong(1, 5L);
        verify(ps).executeQuery();

    }


    @Test
    void findAll_ordiniPresenti_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getLong("id_ordine"))
                .thenReturn(1L, 2L);

        when(rs.getString("data_ordine"))
                .thenReturn("2026-08-19 10:30:00",
                        "2026-08-19 11:30:00");

        when(rs.getString("stato_ordine"))
                .thenReturn("In elaborazione", "Spedito");

        when(rs.getDouble("totale_ordine"))
                .thenReturn(50.00, 100.00);

        when(rs.getString("indirizzo_consegna"))
                .thenReturn("Via Roma 10", "Via Milano 20");

        when(rs.getString("citta"))
                .thenReturn("Milano", "Roma");

        when(rs.getString("stato"))
                .thenReturn("Italia", "Italia");

        when(rs.getLong("id_utente"))
                .thenReturn(5L, 8L);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        List<Ordine> ordini = dao.findAll();

        assertNotNull(ordini);
        assertEquals(2, ordini.size());

        assertEquals(1L, ordini.get(0).getIdOrdine());
        assertEquals(50.00, ordini.get(0).getTotaleOrdine());

        assertEquals(2L, ordini.get(1).getIdOrdine());
        assertEquals(100.00, ordini.get(1).getTotaleOrdine());

        assertEquals(5L, ordini.get(0).getUtente().getIdNome());
        assertEquals(8L, ordini.get(1).getUtente().getIdNome());

        verify(ps).executeQuery();

    }


    @Property
    void findById_ordinePresente_restituisceOrdine(
            @ForAll("idOrdine") long id) throws Exception {


        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_ordine")).thenReturn(id);
        when(rs.getString("data_ordine"))
                .thenReturn("2026-08-19 10:30:00");
        when(rs.getString("stato_ordine"))
                .thenReturn("In elaborazione");
        when(rs.getDouble("totale_ordine"))
                .thenReturn(99.90);
        when(rs.getString("indirizzo_consegna"))
                .thenReturn("Via Roma 10");
        when(rs.getString("citta"))
                .thenReturn("Milano");
        when(rs.getString("stato"))
                .thenReturn("Italia");
        when(rs.getLong("id_utente"))
                .thenReturn(5L);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        Ordine ordine = dao.findById(id);

        assertNotNull(ordine);

        assertEquals(id, ordine.getIdOrdine());
        assertEquals("In elaborazione", ordine.getStatoOrdine());
        assertEquals(99.90, ordine.getTotaleOrdine());
        assertEquals("Via Roma 10", ordine.getIndirizzoConsegna());
        assertEquals("Milano", ordine.getCitta());
        assertEquals("Italia", ordine.getStato());

        assertNotNull(ordine.getUtente());
        assertEquals(5L, ordine.getUtente().getIdNome());

        verify(ps).setLong(1, id);
        verify(ps).executeQuery();

    }



    @Test
    void updateOrdine_statoValido_aggiornaStato() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        dao.updateordine(10L, "Spedito");

        verify(conn).prepareStatement(
                "UPDATE ordine SET stato_ordine = ? WHERE id_ordine = ?"
        );

        verify(ps).setString(1, "Spedito");
        verify(ps).setLong(2, 10L);
        verify(ps).executeUpdate();
    }

    @Test
    void create_statoOrdineNull_usaStatoPredefinito() throws Exception {

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
                "SELECT * FROM ordine WHERE id_ordine = ?"
        )).thenReturn(psFind);

        when(psFind.executeQuery()).thenReturn(rsFind);
        when(rsFind.next()).thenReturn(true);

        when(rsFind.getLong("id_ordine")).thenReturn(20L);
        when(rsFind.getString("data_ordine"))
                .thenReturn("2026-08-19 10:30:00");
        when(rsFind.getString("stato_ordine"))
                .thenReturn("In elaborazione");
        when(rsFind.getDouble("totale_ordine"))
                .thenReturn(50.0);
        when(rsFind.getString("indirizzo_consegna"))
                .thenReturn("Via Roma 10");
        when(rsFind.getString("citta"))
                .thenReturn("Milano");
        when(rsFind.getString("stato"))
                .thenReturn("Italia");
        when(rsFind.getLong("id_utente"))
                .thenReturn(5L);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        Ordine ordine = dao.create(
                0L,
                5L,
                "2026-08-19 10:30:00",
                null,
                50.0,
                "Via Roma 10",
                "Milano",
                "Italia"
        );

        assertNotNull(ordine);
        assertEquals("In elaborazione", ordine.getStatoOrdine());

        verify(ps).setString(7, "In elaborazione");
        verify(ps).executeUpdate();

    }

    @Test
    void findById_ordineNonPresente_restituisceNull() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        Ordine ordine = dao.findById(999L);

        assertNull(ordine);

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

        when(conn.prepareStatement(
                "SELECT * FROM ordine WHERE id_ordine = ?"
        )).thenReturn(psFind);

        when(psFind.executeQuery()).thenReturn(rsFind);

        when(rsFind.next()).thenReturn(false);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        Ordine ordine = dao.create(
                0L,
                5L,
                "2026-08-19 10:30:00",
                "In elaborazione",
                50.0,
                "Via Roma 10",
                "Milano",
                "Italia"
        );

        assertNull(ordine);

        verify(ps).executeUpdate();
        verify(ps).getGeneratedKeys();
        verify(rs).next();

    }


    @Test
    void findByUser_nessunOrdine_restituisceListaVuota() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        List<Ordine> ordini = dao.findByUser(999L);

        assertNotNull(ordini);
        assertTrue(ordini.isEmpty());

        verify(ps).setLong(1, 999L);
        verify(ps).executeQuery();
    }

    @Test
    void findAll_nessunOrdine_restituisceListaVuota() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        List<Ordine> ordini = dao.findAll();

        assertNotNull(ordini);
        assertTrue(ordini.isEmpty());

        verify(ps).executeQuery();
    }

    @Test
    void findByUser_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString()))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenThrow(new SQLException("Errore database"));

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findByUser(5L)
        );

        assertInstanceOf(SQLException.class, exception.getCause());

        verify(ps).setLong(1, 5L);
        verify(ps).executeQuery();

    }


    @Test
    void findAll_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString()))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenThrow(new SQLException("Errore database"));

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findAll()
        );

        assertInstanceOf(SQLException.class, exception.getCause());

        verify(ps).executeQuery();

    }

    @Test
    void findById_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString()))
                .thenReturn(ps);

        when(ps.executeQuery())
                .thenThrow(new SQLException("Errore database"));

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findById(10L)
        );

        assertInstanceOf(SQLException.class, exception.getCause());

        verify(ps).setLong(1, 10L);
        verify(ps).executeQuery();

    }

    @Test
    void updateOrdine_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString()))
                .thenReturn(ps);

        when(ps.executeUpdate())
                .thenThrow(new SQLException("Errore database"));

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.updateordine(10L, "Spedito")
        );

        assertInstanceOf(SQLException.class, exception.getCause());

        verify(ps).setString(1, "Spedito");
        verify(ps).setLong(2, 10L);
        verify(ps).executeUpdate();

    }


    @Test
    void create_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(
                anyString(),
                any(String[].class)
        )).thenReturn(ps);

        when(ps.executeUpdate())
                .thenThrow(new SQLException("Errore database"));

        OrdineDAOPostgreSQLJDBCImpl dao =
                new OrdineDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.create(
                        0L,
                        5L,
                        "2026-08-19 10:30:00",
                        "In elaborazione",
                        50.0,
                        "Via Roma 10",
                        "Milano",
                        "Italia"
                )
        );

        assertInstanceOf(SQLException.class, exception.getCause());

        assertTrue(
                exception.getMessage()
                        .contains("Errore creazione Ordine")
        );

        verify(ps).executeUpdate();

    }



}

