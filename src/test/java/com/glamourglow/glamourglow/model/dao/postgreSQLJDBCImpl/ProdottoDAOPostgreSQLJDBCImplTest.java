
package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.mo.*;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;



class ProdottoDAOPostgreSQLJDBCImplTest {


    @Test
    void findById_prodottoPresente_restituisceProdotto() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        when(rs.getLong("id_prodotto")).thenReturn(1L);
        when(rs.getString("nome_prodotto")).thenReturn("Rossetto");
        when(rs.getString("descrizione")).thenReturn("Rossetto rosso");
        when(rs.getDouble("prezzo")).thenReturn(20.0);
        when(rs.getInt("quantita_disponibile")).thenReturn(10);
        when(rs.getLong("id_categoria")).thenReturn(2L);
        when(rs.getLong("id_marchio")).thenReturn(3L);
        when(rs.getBoolean("in_promozione")).thenReturn(true);
        when(rs.getString("immagine")).thenReturn("rosso.jpg");
        when(rs.getDouble("prezzo_scontato")).thenReturn(15.0);
        when(rs.getBoolean("stato_prodotto")).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        Prodotto prodotto = dao.findById(1L);

        assertEquals(1L, prodotto.getIdProdotto());
        assertEquals("Rossetto", prodotto.getNomeProdotto());
        assertEquals(20.0, prodotto.getPrezzo());
        assertEquals(10, prodotto.getQuantitaDispo());
        assertEquals(2L, prodotto.getCategoria().getIdCategoria());
        assertEquals(3L, prodotto.getMarchio().getIdMarchio());
        assertTrue(prodotto.getInPromo());
        assertEquals("rosso.jpg", prodotto.getImmagine());
        assertEquals(15.0, prodotto.getPrezzoSconto());
        assertFalse(prodotto.isStatoprodotto());

        verify(ps).setLong(1, 1L);
        verify(ps).executeQuery();
    }



    @Test
    void findById_prodottoNonPresente_restituisceNull() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        Prodotto prodotto = dao.findById(999L);

        assertNull(prodotto);

        verify(ps).setLong(1, 999L);
        verify(ps).executeQuery();
    }


    @Test
    void findPromo_prodottiInPromozione_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);

        when(rs.getLong("id_prodotto")).thenReturn(1L);
        when(rs.getString("nome_prodotto")).thenReturn("Rossetto");
        when(rs.getString("descrizione")).thenReturn("Rossetto rosso");
        when(rs.getDouble("prezzo")).thenReturn(20.0);
        when(rs.getInt("quantita_disponibile")).thenReturn(10);
        when(rs.getLong("id_categoria")).thenReturn(2L);
        when(rs.getLong("id_marchio")).thenReturn(3L);
        when(rs.getBoolean("in_promozione")).thenReturn(true);
        when(rs.getString("immagine")).thenReturn("rosso.jpg");
        when(rs.getDouble("prezzo_scontato")).thenReturn(15.0);
        when(rs.getBoolean("stato_prodotto")).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti = dao.findPromo();

        assertEquals(1, prodotti.size());

        Prodotto prodotto = prodotti.get(0);

        assertEquals(1L, prodotto.getIdProdotto());
        assertEquals("Rossetto", prodotto.getNomeProdotto());
        assertEquals(20.0, prodotto.getPrezzo());
        assertTrue(prodotto.getInPromo());

        verify(ps).executeQuery();
    }


    @Test
    void findPromo_nessunProdottoInPromozione_restituisceListaVuota()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti = dao.findPromo();

        assertTrue(prodotti.isEmpty());

        verify(ps).executeQuery();
    }



    @Test
    void modifica_prodottoValido_impostaParametriEdEsegueUpdate()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        Prodotto prodotto = new Prodotto();

        prodotto.setIdProdotto(1L);
        prodotto.setNomeProdotto("Rossetto");
        prodotto.setQuantitaDispo(20);
        prodotto.setPrezzo(25.0);
        prodotto.setInPromo(true);
        prodotto.setPrezzoSconto(18.0);
        prodotto.setStatoprodotto(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        dao.modifica(prodotto);

        verify(ps).setString(1, "Rossetto");
        verify(ps).setInt(2, 20);
        verify(ps).setDouble(3, 25.0);
        verify(ps).setBoolean(4, true);
        verify(ps).setDouble(5, 18.0);
        verify(ps).setBoolean(6, false);
        verify(ps).setDouble(5, 18.0);
        verify(ps).setBoolean(6, false);
        verify(ps).setLong(7, 1L);

        verify(ps).executeUpdate();
    }



    @Test
    void create_prodottoValido_impostaParametriEdEsegueInsert()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(2L);

        Marchio marchio = new Marchio();
        marchio.setIdMarchio(3L);

        Prodotto prodotto = new Prodotto();
        prodotto.setNomeProdotto("Rossetto");
        prodotto.setDescrizione("Rossetto rosso");
        prodotto.setPrezzo(20.0);
        prodotto.setQuantitaDispo(10);
        prodotto.setCategoria(categoria);
        prodotto.setMarchio(marchio);
        prodotto.setInPromo(true);
        prodotto.setImmagine("rosso.jpg");
        prodotto.setPrezzoSconto(15.0);
        prodotto.setStatoprodotto(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        dao.create(prodotto);

        verify(ps).setString(1, "Rossetto");
        verify(ps).setString(2, "Rossetto rosso");
        verify(ps).setDouble(3, 20.0);
        verify(ps).setInt(4, 10);
        verify(ps).setLong(5, 2L);
        verify(ps).setLong(6, 3L);
        verify(ps).setBoolean(7, true);
        verify(ps).setString(8, "rosso.jpg");
        verify(ps).setDouble(9, 15.0);
        verify(ps).setBoolean(10, false);

        verify(ps).executeUpdate();
    }



    @Test
    void cerca_nomeProdottoPresente_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);

        when(rs.getLong("id_prodotto")).thenReturn(1L);
        when(rs.getString("nome_prodotto")).thenReturn("Rossetto");
        when(rs.getString("descrizione")).thenReturn("Rossetto rosso");
        when(rs.getDouble("prezzo")).thenReturn(20.0);
        when(rs.getInt("quantita_disponibile")).thenReturn(10);
        when(rs.getLong("id_categoria")).thenReturn(2L);
        when(rs.getLong("id_marchio")).thenReturn(3L);
        when(rs.getBoolean("in_promozione")).thenReturn(true);
        when(rs.getString("immagine")).thenReturn("rosso.jpg");
        when(rs.getDouble("prezzo_scontato")).thenReturn(15.0);
        when(rs.getBoolean("stato_prodotto")).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cerca("Rossetto", "", "", false);

        assertEquals(1, prodotti.size());

        Prodotto prodotto = prodotti.get(0);

        assertEquals(1L, prodotto.getIdProdotto());
        assertEquals("Rossetto", prodotto.getNomeProdotto());
        assertEquals(20.0, prodotto.getPrezzo());
        assertEquals(10, prodotto.getQuantitaDispo());

        verify(ps).setString(1, "%Rossetto%");
        verify(ps).executeQuery();
    }



    @Test
    void cerca_nomeMarchioCategoriaEPromo_restituisceProdotto()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);

        when(rs.getLong("id_prodotto")).thenReturn(1L);
        when(rs.getString("nome_prodotto")).thenReturn("Rossetto");
        when(rs.getString("descrizione")).thenReturn("Rossetto rosso");
        when(rs.getDouble("prezzo")).thenReturn(20.0);
        when(rs.getInt("quantita_disponibile")).thenReturn(10);
        when(rs.getLong("id_categoria")).thenReturn(2L);
        when(rs.getLong("id_marchio")).thenReturn(3L);
        when(rs.getBoolean("in_promozione")).thenReturn(true);
        when(rs.getString("immagine")).thenReturn("rosso.jpg");
        when(rs.getDouble("prezzo_scontato")).thenReturn(15.0);
        when(rs.getBoolean("stato_prodotto")).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cerca("Rossetto", "Maybelline", "Makeup", true);

        assertEquals(1, prodotti.size());

        Prodotto prodotto = prodotti.get(0);

        assertEquals("Rossetto", prodotto.getNomeProdotto());
        assertEquals(20.0, prodotto.getPrezzo());
        assertTrue(prodotto.getInPromo());

        verify(ps).setString(1, "%Rossetto%");
        verify(ps).setString(2, "Maybelline");
        verify(ps).setString(3, "Makeup");

        verify(ps).executeQuery();
    }


    @Test
    void cercaadmin_nomeProdottoPresente_restituisceLista()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);

        when(rs.getLong("id_prodotto")).thenReturn(1L);
        when(rs.getString("nome_prodotto")).thenReturn("Rossetto");
        when(rs.getString("descrizione")).thenReturn("Rossetto rosso");
        when(rs.getDouble("prezzo")).thenReturn(20.0);
        when(rs.getInt("quantita_disponibile")).thenReturn(10);
        when(rs.getLong("id_categoria")).thenReturn(2L);
        when(rs.getLong("id_marchio")).thenReturn(3L);
        when(rs.getBoolean("in_promozione")).thenReturn(true);
        when(rs.getString("immagine")).thenReturn("rosso.jpg");
        when(rs.getDouble("prezzo_scontato")).thenReturn(15.0);
        when(rs.getBoolean("stato_prodotto")).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cercaadmin("Rossetto", "", "");

        assertEquals(1, prodotti.size());

        Prodotto prodotto = prodotti.get(0);

        assertEquals(1L, prodotto.getIdProdotto());
        assertEquals("Rossetto", prodotto.getNomeProdotto());
        assertEquals(20.0, prodotto.getPrezzo());

        verify(ps).setString(1, "%Rossetto%");
        verify(ps).executeQuery();
    }



    @Test
    void cercaadmin_nomeMarchioCategoria_restituisceLista()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);

        when(rs.getLong("id_prodotto")).thenReturn(1L);
        when(rs.getString("nome_prodotto")).thenReturn("Rossetto");
        when(rs.getString("descrizione")).thenReturn("Rossetto rosso");
        when(rs.getDouble("prezzo")).thenReturn(20.0);
        when(rs.getInt("quantita_disponibile")).thenReturn(10);
        when(rs.getLong("id_categoria")).thenReturn(2L);
        when(rs.getLong("id_marchio")).thenReturn(3L);
        when(rs.getBoolean("in_promozione")).thenReturn(true);
        when(rs.getString("immagine")).thenReturn("rosso.jpg");
        when(rs.getDouble("prezzo_scontato")).thenReturn(15.0);
        when(rs.getBoolean("stato_prodotto")).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cercaadmin("Rossetto", "Maybelline", "Makeup");

        assertEquals(1, prodotti.size());

        Prodotto prodotto = prodotti.get(0);

        assertEquals(1L, prodotto.getIdProdotto());
        assertEquals("Rossetto", prodotto.getNomeProdotto());

        verify(ps).setString(1, "%Rossetto%");
        verify(ps).setString(2, "Maybelline");
        verify(ps).setString(3, "Makeup");

        verify(ps).executeQuery();
    }



    @Test
    void cerca_nessunRisultato_restituisceListaVuota() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cerca("ProdottoInesistente", "", "", false);

        assertTrue(prodotti.isEmpty());

        verify(ps).setString(1, "%ProdottoInesistente%");
        verify(ps).executeQuery();
    }



    @Test
    void cercaadmin_nessunRisultato_restituisceListaVuota()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cercaadmin("ProdottoInesistente", "", "");

        assertTrue(prodotti.isEmpty());

        verify(ps).setString(1, "%ProdottoInesistente%");
        verify(ps).executeQuery();
    }



    @Test
    void modifica_prodottoNonPresente_nonModificaNessunaRiga()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(0);

        Prodotto prodotto = new Prodotto();

        prodotto.setIdProdotto(999L);
        prodotto.setNomeProdotto("Prodotto inesistente");
        prodotto.setQuantitaDispo(0);
        prodotto.setPrezzo(0.0);
        prodotto.setInPromo(false);
        prodotto.setPrezzoSconto(0.0);
        prodotto.setStatoprodotto(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        dao.modifica(prodotto);

        verify(ps).executeUpdate();
    }



    @Test
    void cerca_parametriNull_restituisceListaVuota() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cerca(null, null, null, false);

        assertTrue(prodotti.isEmpty());

        verify(ps).setString(1, "%%");
        verify(ps).executeQuery();
    }



    @Test
    void cerca_categoriaNullStringa_ignoraFiltroCategoria()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cerca("Rossetto", "", "null", false);

        assertTrue(prodotti.isEmpty());

        verify(ps).setString(1, "%Rossetto%");
        verify(ps).executeQuery();
    }



    @Test
    void cercaadmin_parametriNull_restituisceListaVuota()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cercaadmin(null, null, null);

        assertTrue(prodotti.isEmpty());

        verify(ps).setString(1, "%%");
        verify(ps).executeQuery();
    }


    @Test
    void cercaadmin_categoriaNullStringa_ignoraFiltroCategoria()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        List<Prodotto> prodotti =
                dao.cercaadmin("Rossetto", "", "null");

        assertTrue(prodotti.isEmpty());

        verify(ps).setString(1, "%Rossetto%");
        verify(ps).executeQuery();
    }



    @Test
    void findById_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("Errore database"));

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findById(1L)
        );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }



    @Test
    void findPromo_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("Errore database"));

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findPromo()
        );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }



    @Test
    void modifica_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("Errore database"));

        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(1L);
        prodotto.setNomeProdotto("Rossetto");
        prodotto.setQuantitaDispo(10);
        prodotto.setPrezzo(20.0);
        prodotto.setInPromo(false);
        prodotto.setPrezzoSconto(0.0);
        prodotto.setStatoprodotto(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.modifica(prodotto)
        );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }



    @Test
    void create_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("Errore database"));

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);

        Marchio marchio = new Marchio();
        marchio.setIdMarchio(1L);

        Prodotto prodotto = new Prodotto();
        prodotto.setNomeProdotto("Rossetto");
        prodotto.setDescrizione("Rossetto rosso");
        prodotto.setPrezzo(20.0);
        prodotto.setQuantitaDispo(10);
        prodotto.setCategoria(categoria);
        prodotto.setMarchio(marchio);
        prodotto.setInPromo(false);
        prodotto.setImmagine("rosso.jpg");
        prodotto.setPrezzoSconto(0.0);
        prodotto.setStatoprodotto(false);

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.create(prodotto)
        );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }



    @Test
    void cerca_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("Errore database"));

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.cerca("Rossetto", "", "", false)
        );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }



    @Test
    void cercaadmin_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new SQLException("Errore database"));

        ProdottoDAOPostgreSQLJDBCImpl dao =
                new ProdottoDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.cercaadmin("Rossetto", "", "")
        );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }


}
