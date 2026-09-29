package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;
import com.glamourglow.glamourglow.model.mo.Categoria;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;

public class CategoriaDAOPostgreSQLJDBCImplTest {

    @Provide
    Arbitrary<Long> idCategoria() {
        return Arbitraries.longs().between(1L, 1000L);
    }

    @Property
    void findById_categoriaPresente_restituisceCategoria(
            @ForAll("idCategoria") long idCategoria) throws Exception {


        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);

        when(rs.getLong("id_categoria")).thenReturn(idCategoria);
        when(rs.getString("nome_categoria")).thenReturn("Makeup");
        when(rs.getString("descrizione"))
                .thenReturn("Prodotti per il trucco");

        CategoriaDAOPostgreSQLJDBCImpl dao =
                new CategoriaDAOPostgreSQLJDBCImpl(conn);

        Categoria categoria = dao.findById(idCategoria);

        assertNotNull(categoria);
        assertEquals(idCategoria, categoria.getIdCategoria());
        assertEquals("Makeup", categoria.getNomeCategoria());
        assertEquals(
                "Prodotti per il trucco",
                categoria.getDescrizione()
        );

        verify(ps).setLong(1, idCategoria);
        verify(ps).executeQuery();

    }

    @Property
    void findById_categoriaNonPresente_restituisceNull(
            @ForAll("idCategoria") long idCategoria) throws Exception {


        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        CategoriaDAOPostgreSQLJDBCImpl dao =
                new CategoriaDAOPostgreSQLJDBCImpl(conn);

        Categoria categoria = dao.findById(idCategoria);

        assertNull(categoria);

        verify(ps).setLong(1, idCategoria);
        verify(ps).executeQuery();

    }

    @Test
    void findAll_categoriePresenti_restituisceLista() throws Exception {


        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getLong("id_categoria"))
                .thenReturn(1L, 2L);

        when(rs.getString("nome_categoria"))
                .thenReturn("Makeup", "Skincare");

        when(rs.getString("descrizione"))
                .thenReturn(
                        "Prodotti per il trucco",
                        "Prodotti per la cura della pelle"
                );

        CategoriaDAOPostgreSQLJDBCImpl dao =
                new CategoriaDAOPostgreSQLJDBCImpl(conn);

        List<Categoria> categorie = dao.findAll();

        assertNotNull(categorie);
        assertEquals(2, categorie.size());

        assertEquals(1L, categorie.get(0).getIdCategoria());
        assertEquals("Makeup", categorie.get(0).getNomeCategoria());

        assertEquals(2L, categorie.get(1).getIdCategoria());
        assertEquals("Skincare", categorie.get(1).getNomeCategoria());

        verify(ps).executeQuery();


    }


    @Test
    void findAll_nessunaCategoria_restituisceListaVuota() throws Exception {


        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        CategoriaDAOPostgreSQLJDBCImpl dao =
                new CategoriaDAOPostgreSQLJDBCImpl(conn);

        List<Categoria> categorie = dao.findAll();

        assertNotNull(categorie);
        assertTrue(categorie.isEmpty());

        verify(ps).executeQuery();

    }


    @Test
    void findById_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore database"));

        CategoriaDAOPostgreSQLJDBCImpl dao =
                new CategoriaDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findById(1L)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Errore findById Categoria")
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

        CategoriaDAOPostgreSQLJDBCImpl dao =
                new CategoriaDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findAll()
        );

        assertTrue(
                exception.getMessage()
                        .contains("Errore findAll Categoria")
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );


    }



}

