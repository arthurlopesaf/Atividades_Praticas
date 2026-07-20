package DAO;

import Models.Categoria;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO extends BaseDAO {

    public Categoria create(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nome, descricao) VALUES (?, ?)";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir categoria");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categoria.setId(generatedKeys.getInt(1));
                }
            }
        }

        return categoria;
    }

    public Categoria findById(Integer id) throws SQLException {
        String sql = "SELECT id, nome, descricao, criado_em, atualizado_em FROM categorias WHERE id = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }

        return null;
    }

    public Categoria findByNome(String nome) throws SQLException {
        String sql = "SELECT id, nome, descricao, criado_em, atualizado_em FROM categorias WHERE nome = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }

        return null;
    }

    public List<Categoria> findAll() throws SQLException {
        String sql = "SELECT id, nome, descricao, criado_em, atualizado_em FROM categorias ORDER BY nome";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
        }

        return categorias;
    }

    public void update(Categoria categoria) throws SQLException {
        String sql = "UPDATE categorias SET nome = ?, descricao = ? WHERE id = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setInt(3, categoria.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Categoria não encontrada");
            }
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Categoria não encontrada");
            }
        }
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setDescricao(rs.getString("descricao"));

        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) {
            categoria.setCriadoEm(criadoEm.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            categoria.setAtualizadoEm(atualizadoEm.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        return categoria;
    }
}
