package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.CouponDAO;
import com.glamourglow.glamourglow.model.mo.Coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CouponDAOPostgreSQLJDBCImpl implements CouponDAO {

    private final Connection conn;

    public CouponDAOPostgreSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Coupon findByCode(String codice) {
        Coupon coupon = null;
        String sql = "SELECT * FROM coupon WHERE codice = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codice);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    coupon = read(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore findByCode Coupon: " + e.getMessage(), e);
        }

        return coupon;
    }

    @Override
    public List<Coupon> findAll() {
        List<Coupon> listaCoupon = new ArrayList<>();
        String sql = "SELECT * FROM coupon";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                listaCoupon.add(read(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore findAll Coupon: " + e.getMessage(), e);
        }

        return listaCoupon;
    }

    @Override
    public void ModificaCoupon(String vecchiocodice, String nuovoCodice, int sconto) {

        String sql = "UPDATE coupon SET codice = ?, sconto = ? WHERE codice = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuovoCodice);
            ps.setInt(2, sconto);
            ps.setString(3, vecchiocodice);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore modifica Coupon: " + e.getMessage(), e);
        }
    }

    @Override
    public void creaCoupon(String codice, int sconto) {
        String sql = "INSERT INTO coupon (codice, sconto) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codice);
            ps.setInt(2, sconto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore creazione Coupon: " + e.getMessage(), e);
        }
    }

    private Coupon read(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setCodice(rs.getString("codice"));
        coupon.setSconto(rs.getInt("sconto"));
        return coupon;
    }
}