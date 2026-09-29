package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.glamourglow.glamourglow.model.mo.Utente;
import com.glamourglow.glamourglow.model.dao.UtenteDAO;

public class UtenteDAOPostgreSQLJDBCImpl implements UtenteDAO {

    private final Connection conn;

    public UtenteDAOPostgreSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Utente create(
            long id_nome,
            String password,
            String nome,
            String cognome,
            String email,
            String telefono,
            Double wallet,
            String statoaccount,
            boolean ruolo
    ) {

        String sql = "INSERT INTO utente "
                + "(nome, cognome, email, password, telefono, ruolo, stato_account, wallet) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, telefono);
            ps.setShort(6, (short) (ruolo ? 1 : 0));
            ps.setString(7, (statoaccount != null) ? statoaccount : "attivo");
            ps.setDouble(8, (wallet != null) ? wallet : 0.0);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore in create Utente: " + e.getMessage(), e);
        }
        return findEmail(email);
    }

    @Override
    public Utente findEmail(String email) {
        Utente utente = null;
        String sql = "SELECT * FROM utente WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    utente = read(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    @Override
    public void update(Utente utente) {

        StringBuilder sql = new StringBuilder("UPDATE utente SET ");
        sql.append("ruolo = ?, ");

        if (utente.getEmail() != null) sql.append("email = ?, ");
        if (utente.getStatoAccount() != null) sql.append("stato_account = ?, ");
        if (utente.getTelefono() != null) sql.append("telefono = ?, ");

        String finalSql = sql.toString().trim();

            finalSql = finalSql.substring(0, finalSql.length() - 1);

        finalSql += " WHERE id_nome = ?";

        try (PreparedStatement ps = conn.prepareStatement(finalSql)) {
            int i = 1;
            ps.setShort(i++, (short) (utente.getRuolo() ? 1 : 0));

            if (utente.getEmail() != null) ps.setString(i++, utente.getEmail());
            if (utente.getStatoAccount() != null) ps.setString(i++, utente.getStatoAccount());
            if (utente.getTelefono() != null) ps.setString(i++, utente.getTelefono());

            ps.setLong(i, utente.getIdNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatewallet(long idNome, Double saldo) {

        String sql = "UPDATE utente SET wallet = ? WHERE id_nome = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, (saldo != null) ? saldo : 0.0);
            ps.setLong(2, idNome);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utente findById(long id) {
        Utente utente = null;
        String sql = "SELECT * FROM utente WHERE id_nome = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) utente = read(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    @Override
    public List<Utente> findSearch() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT * FROM utente";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                utenti.add(read(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utenti;
    }

    private Utente read(ResultSet rs) throws SQLException {
        Utente utente = new Utente();
        utente.setIdNome(rs.getLong("id_nome"));
        utente.setNome(rs.getString("nome"));
        utente.setCognome(rs.getString("cognome"));
        utente.setEmail(rs.getString("email"));
        utente.setPassword(rs.getString("password"));
        utente.setTelefono(rs.getString("telefono"));
        utente.setStatoAccount(rs.getString("stato_account"));
        utente.setWallet(rs.getDouble("wallet"));
        utente.setRuolo(rs.getShort("ruolo") == 1);
        return utente;
    }

    @Override public void delete(Utente utente) {}
    @Override public Utente findLoggedUser() { return null; }
}