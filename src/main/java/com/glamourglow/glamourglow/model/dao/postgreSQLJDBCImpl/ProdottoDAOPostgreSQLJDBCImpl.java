package com.glamourglow.glamourglow.model.dao.postgreSQLJDBCImpl;

import com.glamourglow.glamourglow.model.dao.ProdottoDAO;
import com.glamourglow.glamourglow.model.mo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAOPostgreSQLJDBCImpl implements ProdottoDAO {

    private final Connection conn;

    public ProdottoDAOPostgreSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Prodotto> cerca(String nomeProdotto, String marchioprodotto, String categoria, boolean promo) {
        List<Prodotto> list = new ArrayList<>();
        if (nomeProdotto == null) nomeProdotto = "";

        try {

            StringBuilder sql = new StringBuilder("SELECT p.* FROM prodotto p");

            if (marchioprodotto != null && !marchioprodotto.isEmpty()) {
                sql.append(" JOIN marchio m ON p.id_marchio = m.id_marchio");
            }
            if (categoria != null && !categoria.isEmpty() && !categoria.equals("null")) {
                sql.append(" JOIN categoria c ON p.id_categoria = c.id_categoria");
            }

            sql.append(" WHERE p.nome_prodotto ILIKE ? AND p.stato_prodotto = FALSE");

            if (marchioprodotto != null && !marchioprodotto.isEmpty()) {
                sql.append(" AND m.nome_marchio = ?");
            }
            if (categoria != null && !categoria.isEmpty() && !categoria.equals("null")) {
                sql.append(" AND c.nome_categoria = ?");
            }
            if (promo) {
                sql.append(" AND p.in_promozione = TRUE");
            }

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int i = 1;
            ps.setString(i++, "%" + nomeProdotto + "%");

            if (marchioprodotto != null && !marchioprodotto.isEmpty()) {
                ps.setString(i++, marchioprodotto);
            }
            if (categoria != null && !categoria.isEmpty() && !categoria.equals("null")) {
                ps.setString(i++, categoria);
            }

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                list.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Prodotto findById(long id) {
        Prodotto prodotto = null;
        try {

            String sql = "SELECT * FROM prodotto WHERE id_prodotto = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prodotto = read(rs);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotto;
    }

    @Override
    public void modifica(Prodotto prodotto) {
        try {

            String sql = "UPDATE prodotto SET nome_prodotto = ?, quantita_disponibile = ?, " +
                    "prezzo = ?, in_promozione = ?, prezzo_scontato = ?, stato_prodotto = ? " +
                    "WHERE id_prodotto = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, prodotto.getNomeProdotto());
            ps.setInt(2, prodotto.getQuantitaDispo());
            ps.setDouble(3, prodotto.getPrezzo());
            ps.setBoolean(4, prodotto.getInPromo());
            ps.setDouble(5, prodotto.getPrezzoSconto());
            ps.setBoolean(6, prodotto.isStatoprodotto());
            ps.setLong(7, prodotto.getIdProdotto());

            int rows = ps.executeUpdate();
            System.out.println("ROWS UPDATED: " + rows);


           ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Prodotto> findPromo() {
        List<Prodotto> lista = new ArrayList<>();
        try {

            String sql = "SELECT * FROM prodotto WHERE in_promozione = TRUE AND stato_prodotto = FALSE";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(read(rs));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public void create(Prodotto prodotto) {
        try {

            String sql = "INSERT INTO prodotto (nome_prodotto, descrizione, prezzo, " +
                    "quantita_disponibile, id_categoria, id_marchio, in_promozione, " +
                    "immagine, prezzo_scontato, stato_prodotto) VALUES (?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, prodotto.getNomeProdotto());
            ps.setString(2, prodotto.getDescrizione());
            ps.setDouble(3, prodotto.getPrezzo());
            ps.setInt(4, prodotto.getQuantitaDispo());
            ps.setLong(5, prodotto.getCategoria().getIdCategoria());
            ps.setLong(6, prodotto.getMarchio().getIdMarchio());
            ps.setBoolean(7, prodotto.getInPromo());
            ps.setString(8, prodotto.getImmagine());
            ps.setDouble(9, prodotto.getPrezzoSconto());
            ps.setBoolean(10, prodotto.isStatoprodotto());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Prodotto> cercaadmin(String nomeProdotto, String marchioProdotto, String categoria) {

        List<Prodotto> list = new ArrayList<>();

        if (nomeProdotto == null) nomeProdotto = "";

        try {

            StringBuilder sql = new StringBuilder(
                    "SELECT p.* FROM prodotto p"
            );

            if (marchioProdotto != null && !marchioProdotto.isEmpty()) {
                sql.append(" JOIN marchio m ON p.id_marchio = m.id_marchio");
            }

            if (categoria != null && !categoria.isEmpty() && !categoria.equals("null")) {
                sql.append(" JOIN categoria c ON p.id_categoria = c.id_categoria");
            }

            sql.append(" WHERE p.nome_prodotto ILIKE ?");

            if (marchioProdotto != null && !marchioProdotto.isEmpty()) {
                sql.append(" AND m.nome_marchio = ?");
            }

            if (categoria != null && !categoria.isEmpty() && !categoria.equals("null")) {
                sql.append(" AND c.nome_categoria = ?");
            }

            sql.append(" ORDER BY p.id_prodotto ASC");
            PreparedStatement ps = conn.prepareStatement(sql.toString());

            int i = 1;

            ps.setString(i++, "%" + nomeProdotto + "%");

            if (marchioProdotto != null && !marchioProdotto.isEmpty()) {
                ps.setString(i++, marchioProdotto);
            }

            if (categoria != null && !categoria.isEmpty() && !categoria.equals("null")) {
                ps.setString(i++, categoria);
            }


            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(read(rs));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private Prodotto read(ResultSet rs) throws SQLException {
        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(rs.getLong("id_prodotto"));
        prodotto.setNomeProdotto(rs.getString("nome_prodotto"));
        prodotto.setDescrizione(rs.getString("descrizione"));
        prodotto.setPrezzo(rs.getDouble("prezzo"));
        prodotto.setQuantitaDispo(rs.getInt("quantita_disponibile"));

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getLong("id_categoria"));
        prodotto.setCategoria(categoria);

        Marchio marchio = new Marchio();
        marchio.setIdMarchio(rs.getLong("id_marchio"));
        prodotto.setMarchio(marchio);

        prodotto.setInPromo(rs.getBoolean("in_promozione"));
        prodotto.setImmagine(rs.getString("immagine"));
        prodotto.setPrezzoSconto(rs.getDouble("prezzo_scontato"));
        prodotto.setStatoprodotto(rs.getBoolean("stato_prodotto"));

        return prodotto;
    }
}