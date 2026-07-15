package com.glamourglow.glamourglow.model.dao.mySQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.OrdineDAO;
import com.glamourglow.glamourglow.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAOMySQLJDBCImpl implements OrdineDAO {

    private final Connection conn;

    public OrdineDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Ordine create(long idOrdine, long idNome, String formattedDateTime, String statoOrdine, double totalecarrello, String indirizzo, String citta, String stato) {
        PreparedStatement ps;
        try {
            // Rimosse le virgolette e convertito tutto in minuscolo
            String sql = "INSERT INTO ordine "
                    + "(id_utente, data_ordine, totale_ordine, indirizzo_consegna, citta, stato, stato_ordine) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Per PostgreSQL, specifichiamo la chiave generata rigorosamente in minuscolo
            ps = conn.prepareStatement(sql, new String[]{"id_ordine"});
            ps.setLong(1, idNome);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(formattedDateTime));
            ps.setDouble(3, totalecarrello);
            ps.setString(4, indirizzo);
            ps.setString(5, citta);
            ps.setString(6, stato);
            ps.setString(7, (statoOrdine != null) ? statoOrdine : "In elaborazione");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            long generatedId = 0;
            if (rs.next()) {
                generatedId = rs.getLong(1);
            }
            rs.close();
            ps.close();

            return findById(generatedId);
        } catch (SQLException e) {
            throw new RuntimeException("Errore creazione Ordine: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ordine> findByUser(long idNome) {
        PreparedStatement ps;
        List<Ordine> ordini = new ArrayList<>();

        try {
            // Rimosse le virgolette
            String sql = "SELECT * FROM ordine WHERE id_utente = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, idNome);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                ordini.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordini;
    }

    @Override
    public List<Ordine> findAll() {
        PreparedStatement ps;
        List<Ordine> ordini = new ArrayList<>();

        try {
            // Rimosse le virgolette
            String sql = "SELECT * FROM ordine";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                ordini.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordini;
    }

    public Ordine findById(long id) {
        PreparedStatement ps;
        Ordine ordine = null;

        try {
            // Rimosse le virgolette
            String sql = "SELECT * FROM ordine WHERE id_ordine = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                ordine = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordine;
    }

    @Override
    public void updateordine(long id_ordine, String stato) {
        PreparedStatement ps;
        try {
            // Rimosse le virgolette
            String sql = "UPDATE ordine SET stato_ordine = ? WHERE id_ordine = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, stato);
            ps.setLong(2, id_ordine);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Ordine read(ResultSet rs) throws SQLException {
        Ordine ordine = new Ordine();

        // Mappatura convertita interamente in minuscolo per il ResultSet di Postgres
        ordine.setIdOrdine(rs.getLong("id_ordine"));
        ordine.setData(rs.getString("data_ordine"));
        ordine.setStatoOrdine(rs.getString("stato_ordine"));
        ordine.setTotaleOrdine(rs.getDouble("totale_ordine"));
        ordine.setIndirizzoConsegna(rs.getString("indirizzo_consegna"));
        ordine.setCitta(rs.getString("citta"));
        ordine.setStato(rs.getString("stato"));

        Utente utente = new Utente();
        utente.setIdNome(rs.getLong("id_utente"));
        ordine.setUtente(utente);

        return ordine;
    }
}