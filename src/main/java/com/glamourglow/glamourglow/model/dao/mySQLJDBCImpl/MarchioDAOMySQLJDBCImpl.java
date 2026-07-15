package com.glamourglow.glamourglow.model.dao.mySQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.MarchioDAO;
import com.glamourglow.glamourglow.model.mo.Marchio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarchioDAOMySQLJDBCImpl implements MarchioDAO {

    private final Connection conn;

    public MarchioDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Marchio> findAll() {
        PreparedStatement ps;
        List<Marchio> lista = new ArrayList<>();

        try {
            // Rimosse le virgolette e convertito in minuscolo
            String sql = "SELECT * FROM marchio";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                lista.add(read(resultSet));
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero dei marchi: " + e.getMessage(), e);
        }
        return lista;
    }

    private Marchio read(ResultSet rs) throws SQLException {
        Marchio marchio = new Marchio();

        // Mappatura convertita in minuscolo per PostgreSQL
        marchio.setIdMarchio(rs.getLong("id_marchio"));
        marchio.setNomeMarchio(rs.getString("nome_marchio"));

        return marchio;
    }
}