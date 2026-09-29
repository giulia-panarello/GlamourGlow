package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;
import com.glamourglow.glamourglow.model.mo.Utente;
import com.glamourglow.glamourglow.model.dao.UtenteDAO;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.mockito.Mockito.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class UtenteDAOPostgreSQLJDBCImplTest {


    @Test
    void findEmail_utentePresente_restituisceUtente() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement("SELECT * FROM utente WHERE email = ?"))
                .thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_nome")).thenReturn(1L);
        when(rs.getString("nome")).thenReturn("Mario");
        when(rs.getString("cognome")).thenReturn("Rossi");
        when(rs.getString("email")).thenReturn("mario.rossi@email.it");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("telefono")).thenReturn("123456789");
        when(rs.getString("stato_account")).thenReturn("attivo");
        when(rs.getDouble("wallet")).thenReturn(50.0);
        when(rs.getShort("ruolo")).thenReturn((short) 0);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.findEmail("mario.rossi@email.it");

        assertEquals(
                "mario.rossi@email.it",
                utente.getEmail()
        );
    }




    @Test
    void findById_utentePresente_restituisceUtente() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement("SELECT * FROM utente WHERE id_nome = ?"))
                .thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_nome")).thenReturn(1L);
        when(rs.getString("nome")).thenReturn("Mario");
        when(rs.getString("cognome")).thenReturn("Rossi");
        when(rs.getString("email")).thenReturn("mario.rossi@email.it");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("telefono")).thenReturn("123456789");
        when(rs.getString("stato_account")).thenReturn("attivo");
        when(rs.getDouble("wallet")).thenReturn(50.0);
        when(rs.getShort("ruolo")).thenReturn((short) 0);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.findById(1L);

        assertEquals(1L, utente.getIdNome());
    }



    @Test
    void findById_utenteNonPresente_restituisceNull() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement("SELECT * FROM utente WHERE id_nome = ?"))
                .thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.findById(999L);

        assertNull(utente);
    }



    @Test
    void findSearch_utentiPresenti_restituisceLista() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement("SELECT * FROM utente"))
                .thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);

        when(rs.getLong("id_nome")).thenReturn(1L);
        when(rs.getString("nome")).thenReturn("Mario");
        when(rs.getString("cognome")).thenReturn("Rossi");
        when(rs.getString("email")).thenReturn("mario.rossi@email.it");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("telefono")).thenReturn("123456789");
        when(rs.getString("stato_account")).thenReturn("attivo");
        when(rs.getDouble("wallet")).thenReturn(50.0);
        when(rs.getShort("ruolo")).thenReturn((short) 0);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        List<Utente> utenti = dao.findSearch();

        assertEquals(1, utenti.size());
    }



    @Test
    void findSearch_nessunUtente_restituisceListaVuota()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement("SELECT * FROM utente"))
                .thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        List<Utente> utenti = dao.findSearch();

        assertTrue(utenti.isEmpty());
    }



    @Test
    void updateWallet_saldoValido_impostaSaldoEdEsegueUpdate()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(
                "UPDATE utente SET wallet = ? WHERE id_nome = ?"))
                .thenReturn(ps);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        dao.updatewallet(1L, 100.0);

        verify(ps).setDouble(1, 100.0);
        verify(ps).setLong(2, 1L);
        verify(ps).executeUpdate();
    }


    @Test
    void updateWallet_saldoNull_usaZero() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(
                "UPDATE utente SET wallet = ? WHERE id_nome = ?"))
                .thenReturn(ps);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        dao.updatewallet(1L, null);

        verify(ps).setDouble(1, 0.0);
        verify(ps).setLong(2, 1L);
        verify(ps).executeUpdate();
    }



    @Test
    void update_tuttiCampiPresenti_impostaParametriEdEsegueUpdate()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        Utente utente = new Utente();
        utente.setIdNome(1L);
        utente.setRuolo(true);
        utente.setEmail("nuova@email.it");
        utente.setStatoAccount("attivo");
        utente.setTelefono("123456789");

        when(conn.prepareStatement(
                "UPDATE utente SET ruolo = ?, email = ?, stato_account = ?, telefono = ? WHERE id_nome = ?"))
                .thenReturn(ps);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        dao.update(utente);

        verify(ps).setShort(1, (short) 1);
        verify(ps).setString(2, "nuova@email.it");
        verify(ps).setString(3, "attivo");
        verify(ps).setString(4, "123456789");
        verify(ps).setLong(5, 1L);
        verify(ps).executeUpdate();
    }


    @Test
    void update_soloRuoloPresente_aggiornaSoloRuolo()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        Utente utente = new Utente();
        utente.setIdNome(1L);
        utente.setRuolo(false);
        utente.setEmail(null);
        utente.setStatoAccount(null);
        utente.setTelefono(null);

        when(conn.prepareStatement(
                "UPDATE utente SET ruolo = ? WHERE id_nome = ?"))
                .thenReturn(ps);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        dao.update(utente);

        verify(ps).setShort(1, (short) 0);
        verify(ps).setLong(2, 1L);
        verify(ps).executeUpdate();
    }


    @Test
    void create_utenteValido_restituisceUtente() throws Exception {

        Connection conn = mock(Connection.class);

        PreparedStatement insertPs = mock(PreparedStatement.class);
        PreparedStatement selectPs = mock(PreparedStatement.class);

        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(
                "INSERT INTO utente (nome, cognome, email, password, telefono, ruolo, stato_account, wallet) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        )).thenReturn(insertPs);

        when(conn.prepareStatement(
                "SELECT * FROM utente WHERE email = ?"
        )).thenReturn(selectPs);

        when(selectPs.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_nome")).thenReturn(1L);
        when(rs.getString("nome")).thenReturn("Mario");
        when(rs.getString("cognome")).thenReturn("Rossi");
        when(rs.getString("email")).thenReturn("mario.rossi@email.it");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("telefono")).thenReturn("123456789");
        when(rs.getString("stato_account")).thenReturn("attivo");
        when(rs.getDouble("wallet")).thenReturn(50.0);
        when(rs.getShort("ruolo")).thenReturn((short) 0);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.create(
                1L,
                "password",
                "Mario",
                "Rossi",
                "mario.rossi@email.it",
                "123456789",
                50.0,
                "attivo",
                false
        );

        assertEquals("mario.rossi@email.it", utente.getEmail());
    }



    @Test
    void create_walletEStatoNull_usaValoriPredefiniti()
            throws Exception {

        Connection conn = mock(Connection.class);

        PreparedStatement insertPs = mock(PreparedStatement.class);
        PreparedStatement selectPs = mock(PreparedStatement.class);

        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(
                "INSERT INTO utente (nome, cognome, email, password, telefono, ruolo, stato_account, wallet) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        )).thenReturn(insertPs);

        when(conn.prepareStatement(
                "SELECT * FROM utente WHERE email = ?"
        )).thenReturn(selectPs);

        when(selectPs.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_nome")).thenReturn(2L);
        when(rs.getString("nome")).thenReturn("Luigi");
        when(rs.getString("cognome")).thenReturn("Verdi");
        when(rs.getString("email")).thenReturn("luigi.verdi@email.it");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("telefono")).thenReturn("987654321");
        when(rs.getString("stato_account")).thenReturn("attivo");
        when(rs.getDouble("wallet")).thenReturn(0.0);
        when(rs.getShort("ruolo")).thenReturn((short) 0);

        UtenteDAO dao = new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.create(
                2L,
                "password",
                "Luigi",
                "Verdi",
                "luigi.verdi@email.it",
                "987654321",
                null,
                null,
                false
        );

        verify(insertPs).setString(7, "attivo");
        verify(insertPs).setDouble(8, 0.0);

        assertEquals(0.0, utente.getWallet());
    }



    @Test
    void findEmail_utenteNonPresente_restituisceNull()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.findEmail("inesistente@test.it");

        assertNull(utente);

        verify(ps).setString(1, "inesistente@test.it");
        verify(ps).executeQuery();
    }


    @Test
    void create_utenteNonRecuperabile_restituisceNull()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        ResultSet rs = mock(ResultSet.class);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente risultato = dao.create(
                0L,
                "password",
                "Mario",
                "Rossi",
                "mario@test.it",
                "3331234567",
                null,
                null,
                true
        );

        assertNull(risultato);

        verify(ps).setString(1, "Mario");
        verify(ps).setString(2, "Rossi");
        verify(ps).setString(3, "mario@test.it");
        verify(ps).setString(4, "password");
        verify(ps).setString(5, "3331234567");

        verify(ps).setShort(6, (short) 1);
        verify(ps).setString(7, "attivo");
        verify(ps).setDouble(8, 0.0);

        verify(ps).executeUpdate();
    }



    @Test
    void findById_ruoloFalse_restituisceUtenteNonAmministratore()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_nome")).thenReturn(10L);
        when(rs.getString("nome")).thenReturn("Mario");
        when(rs.getString("cognome")).thenReturn("Rossi");
        when(rs.getString("email")).thenReturn("mario@test.it");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("telefono")).thenReturn("3331234567");
        when(rs.getString("stato_account")).thenReturn("attivo");
        when(rs.getDouble("wallet")).thenReturn(100.0);
        when(rs.getShort("ruolo")).thenReturn((short) 0);

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.findById(10L);

        assertEquals(10L, utente.getIdNome());
        assertFalse(utente.getRuolo());
    }



    @Test
    void findById_ruoloTrue_restituisceUtenteAmministratore()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id_nome")).thenReturn(10L);
        when(rs.getString("nome")).thenReturn("Mario");
        when(rs.getString("cognome")).thenReturn("Rossi");
        when(rs.getString("email")).thenReturn("mario@test.it");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("telefono")).thenReturn("3331234567");
        when(rs.getString("stato_account")).thenReturn("attivo");
        when(rs.getDouble("wallet")).thenReturn(100.0);
        when(rs.getShort("ruolo")).thenReturn((short) 1);

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = dao.findById(10L);

        assertTrue(utente.getRuolo());
    }


    @Test
    void delete_utenteValido_nonEsegueOperazioni() {

        Connection conn = mock(Connection.class);

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = new Utente();

        dao.delete(utente);
    }


    @Test
    void findLoggedUser_nessunUtenteLoggato_restituisceNull() {

        Connection conn = mock(Connection.class);

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente risultato = dao.findLoggedUser();

        assertNull(risultato);
    }


    @Test
    void update_soloEmailPresente_aggiornaEmail()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(
                "UPDATE utente SET ruolo = ?, email = ? WHERE id_nome = ?"
        )).thenReturn(ps);

        Utente utente = new Utente();
        utente.setIdNome(10L);
        utente.setRuolo(true);
        utente.setEmail("nuova@email.it");
        utente.setStatoAccount(null);
        utente.setTelefono(null);

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        dao.update(utente);

        verify(ps).setShort(1, (short) 1);
        verify(ps).setString(2, "nuova@email.it");
        verify(ps).setLong(3, 10L);
        verify(ps).executeUpdate();
    }


    @Test
    void update_emailETelefonoPresenti_aggiornaCampi()
            throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(conn.prepareStatement(
                "UPDATE utente SET ruolo = ?, email = ?, telefono = ? WHERE id_nome = ?"
        )).thenReturn(ps);

        Utente utente = new Utente();

        utente.setIdNome(10L);
        utente.setRuolo(true);
        utente.setEmail("mario@test.it");
        utente.setStatoAccount(null);
        utente.setTelefono("3331234567");

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        dao.update(utente);

        verify(ps).setShort(1, (short) 1);
        verify(ps).setString(2, "mario@test.it");
        verify(ps).setString(3, "3331234567");
        verify(ps).setLong(4, 10L);
        verify(ps).executeUpdate();
    }


    @Test
    void findEmail_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore test"));

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findEmail("test@test.it")
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }


    @Test
    void findById_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore test"));

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findById(1L)
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }


    @Test
    void findSearch_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore test"));

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.findSearch()
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }


    @Test
    void update_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore test"));

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        Utente utente = new Utente();
        utente.setIdNome(1L);
        utente.setRuolo(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.update(utente)
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }


    @Test
    void updateWallet_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore test"));

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.updatewallet(1L, 100.0)
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }


    @Test
    void create_erroreSQL_lanciaRuntimeException() throws Exception {

        Connection conn = mock(Connection.class);

        when(conn.prepareStatement(anyString()))
                .thenThrow(new java.sql.SQLException("Errore test"));

        UtenteDAOPostgreSQLJDBCImpl dao =
                new UtenteDAOPostgreSQLJDBCImpl(conn);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dao.create(
                        1L,
                        "password",
                        "Mario",
                        "Rossi",
                        "test@test.it",
                        "3331234567",
                        50.0,
                        "attivo",
                        false
                )
        );

        assertInstanceOf(
                java.sql.SQLException.class,
                exception.getCause()
        );
    }




}