
package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.mo.Marchio;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MarchioDAOPostgreSQLJDBCImplTest {

    @Test
    void findAll_marchiPresenti_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getLong("id_marchio"))
                .thenReturn(1L, 2L);

        when(rs.getString("nome_marchio"))
                .thenReturn("Dior", "Chanel");

        MarchioDAOPostgreSQLJDBCImpl dao =
                new MarchioDAOPostgreSQLJDBCImpl(conn);

        List<Marchio> marchi = dao.findAll();

        assertNotNull(marchi);
        assertEquals(2, marchi.size());

        assertEquals(1L, marchi.get(0).getIdMarchio());
        assertEquals("Dior", marchi.get(0).getNomeMarchio());

        assertEquals(2L, marchi.get(1).getIdMarchio());
        assertEquals("Chanel", marchi.get(1).getNomeMarchio());

        verify(ps).executeQuery();
    }


    @Test
    void findAll_nessunMarchio_restituisceListaVuota() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        MarchioDAOPostgreSQLJDBCImpl dao =
                new MarchioDAOPostgreSQLJDBCImpl(conn);

        List<Marchio> marchi = dao.findAll();

        assertNotNull(marchi);
        assertTrue(marchi.isEmpty());

        verify(ps).executeQuery();
    }


    @Test
    void findAll_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("Errore database"));

        MarchioDAOPostgreSQLJDBCImpl dao =
                new MarchioDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findAll()
        );

        assertTrue(exception.getMessage()
                .contains("Errore nel recupero dei marchi"));

        assertInstanceOf(SQLException.class, exception.getCause());

        verify(conn).prepareStatement(anyString());
    }


}
