package com.glamourglow.glamourglow.model.dao.mySQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.CategoriaDAO;
import com.glamourglow.glamourglow.model.mo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOMySQLJDBCImpl implements CategoriaDAO {

    private final Connection conn;

    public CategoriaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Categoria findById(Long idCategoria) {
        Categoria categoria = null;
        PreparedStatement ps;

        try {
            // Rimosse le virgolette e convertito in minuscolo
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
            // Rimosse le virgolette e convertito in minuscolo
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

    // Metodo helper per mappare i dati dal Database all'oggetto Java
    private Categoria read(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();

        // Nomi delle colonne convertiti in minuscolo per il ResultSet di PostgreSQL
        categoria.setIdCategoria(rs.getLong("id_categoria"));
        categoria.setNomeCategoria(rs.getString("nome_categoria"));
        categoria.setDescrizione(rs.getString("descrizione"));

        return categoria;
    }
}