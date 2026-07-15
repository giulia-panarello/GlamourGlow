package com.glamourglow.glamourglow.model.dao.mySQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.DettagliOrdineDAO;
import com.glamourglow.glamourglow.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DettagliOrdineDAOMySQLJDBCImpl implements DettagliOrdineDAO {

    private final Connection conn;

    public DettagliOrdineDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean usoCoupon(long idNome, String codiceCoupon) {
        PreparedStatement ps;
        boolean usato = false;

        try {
            // Rimosse le virgolette e convertito in minuscolo
            String sql = "SELECT 1 FROM dettagli_ordine WHERE coupon = ? LIMIT 1";

            ps = conn.prepareStatement(sql);
            ps.setString(1, codiceCoupon);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                usato = true;
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException("Errore verifica coupon: " + e.getMessage(), e);
        }

        return usato;
    }

    @Override
    public DettagliOrdine create(Long idDettagli, Long idOrdine, Long idProdotto, int qta, Double prezzo, String coupon) {
        PreparedStatement ps;
        try {
            // Rimosse le virgolette e convertito in minuscolo
            String sql = "INSERT INTO dettagli_ordine "
                    + "(id_ordine, id_prodotto, quantita, prezzo_unitario, coupon) "
                    + "VALUES (?, ?, ?, ?, ?)";

            // Per getGeneratedKeys, specifichiamo la colonna in minuscolo per PostgreSQL
            ps = conn.prepareStatement(sql, new String[]{"id_dettaglio"});
            ps.setLong(1, idOrdine);
            ps.setLong(2, idProdotto);
            ps.setInt(3, qta);
            ps.setDouble(4, prezzo);
            ps.setString(5, coupon);

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
            throw new RuntimeException("Errore creazione dettaglio ordine: " + e.getMessage(), e);
        }
    }

    @Override
    public DettagliOrdine findById(long id) {
        PreparedStatement ps;
        DettagliOrdine dettagliOrdine = null;

        try {
            // Rimosse le virgolette e convertito in minuscolo
            String sql = "SELECT * FROM dettagli_ordine WHERE id_dettaglio = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                dettagliOrdine = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dettagliOrdine;
    }

    @Override
    public List<DettagliOrdine> findByIdOrdine(Long idOrdine) {
        PreparedStatement ps;
        List<DettagliOrdine> lista = new ArrayList<>();

        try {
            // Rimosse le virgolette e convertito in minuscolo
            String sql = "SELECT * FROM dettagli_ordine WHERE id_ordine = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, idOrdine);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                lista.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    private DettagliOrdine read(ResultSet rs) throws SQLException {
        DettagliOrdine dettagliOrdine = new DettagliOrdine();

        // Tutti i nomi delle colonne nel ResultSet vanno ora in minuscolo
        dettagliOrdine.setIdDettaglio(rs.getLong("id_dettaglio"));
        dettagliOrdine.setQuantita(rs.getInt("quantita"));
        dettagliOrdine.setPrezzoUnitario(rs.getDouble("prezzo_unitario"));

        // Gestione oggetti collegati (MO)
        Coupon coupon = new Coupon();
        coupon.setCodice(rs.getString("coupon"));
        dettagliOrdine.setCoupon(coupon);

        Ordine ordine = new Ordine();
        ordine.setIdOrdine(rs.getLong("id_ordine"));
        dettagliOrdine.setOrdine(ordine);

        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(rs.getLong("id_prodotto"));
        dettagliOrdine.setProdotto(prodotto);

        return dettagliOrdine;
    }
}