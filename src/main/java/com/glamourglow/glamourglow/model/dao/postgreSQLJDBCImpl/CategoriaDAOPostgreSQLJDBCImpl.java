package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.CategoriaDAO;
import com.glamourglow.glamourglow.model.mo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOPostgreSQLJDBCImpl implements CategoriaDAO {

    private final Connection conn;

    public CategoriaDAOPostgreSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Categoria findById(Long idCategoria) {
        Categoria categoria = null;
        PreparedStatement ps;

        try {

            String sql = "SELECT * FROM categoria WHERE id_categoria = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idCategoria);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                categoria = read(resultSet);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException("Errore findById Categoria: " + e.getMessage(), e);
        }

        return categoria;
    }

    @Override
    public List<Categoria> findAll() {
        PreparedStatement ps;
        List<Categoria> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM categoria";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                lista.add(read(resultSet));
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException("Errore findAll Categoria: " + e.getMessage(), e);
        }
        return lista;
    }

    private Categoria read(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();

        categoria.setIdCategoria(rs.getLong("id_categoria"));
        categoria.setNomeCategoria(rs.getString("nome_categoria"));
        categoria.setDescrizione(rs.getString("descrizione"));

        return categoria;
    }
}