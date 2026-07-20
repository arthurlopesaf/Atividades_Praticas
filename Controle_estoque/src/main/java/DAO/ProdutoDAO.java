package DAO;

import Models.Produto;

import java.sql.*;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO extends BaseDAO {

    public Produto create(Produto produto) throws SQLException {

        String sql = "INSERT INTO produtos (codigo, nome, descricao, id_categoria, preco_compra, preco_venda, estoque_minimo, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setInt(4, produto.getCategoriaId());
            stmt.setBigDecimal(5, produto.getPrecoCompra());
            stmt.setBigDecimal(6, produto.getPrecoVenda());
            stmt.setInt(7, produto.getEstoqueMinimo());
            stmt.setBoolean(8, produto.isAtivo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir produto");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getInt(1));
                }
            }
        }

        return produto;
    }

    public Produto findById(Integer id) throws SQLException {

        String sql = "SELECT id, codigo, nome, descricao, id_categoria, preco_compra, preco_venda, " +
                "estoque_minimo, saldo_atual, ativo, criado_em, atualizado_em " +
                "FROM produtos WHERE id = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        return null;
    }

    public Produto findByCodigo(String codigo) throws SQLException {

        String sql = "SELECT id, codigo, nome, descricao, id_categoria, preco_compra, preco_venda, " +
                "estoque_minimo, saldo_atual, ativo, criado_em, atualizado_em " +
                "FROM produtos WHERE codigo = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        return null;
    }

    public List<Produto> findAll() throws SQLException {

        String sql = "SELECT id, codigo, nome, descricao, id_categoria, preco_compra, preco_venda, " +
                "estoque_minimo, saldo_atual, ativo, criado_em, atualizado_em " +
                "FROM produtos ORDER BY nome";

        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }

        return produtos;
    }

    public List<Produto> findByCategoria(Integer categoriaId) throws SQLException {

        String sql = "SELECT id, codigo, nome, descricao, id_categoria, preco_compra, preco_venda, " +
                "estoque_minimo, saldo_atual, ativo, criado_em, atualizado_em " +
                "FROM produtos WHERE id_categoria = ? ORDER BY nome";

        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, categoriaId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    produtos.add(mapearProduto(rs));
                }
            }
        }

        return produtos;
    }

    public List<Produto> findEstoqueBaixo() throws SQLException {

        String sql = "SELECT id, codigo, nome, descricao, id_categoria, preco_compra, preco_venda, " +
                "estoque_minimo, saldo_atual, ativo, criado_em, atualizado_em " +
                "FROM produtos WHERE saldo_atual <= estoque_minimo " +
                "AND ativo = true ORDER BY saldo_atual";

        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }

        return produtos;
    }

    public List<Produto> findAtivos() throws SQLException {

        String sql = "SELECT id, codigo, nome, descricao, id_categoria, preco_compra, preco_venda, " +
                "estoque_minimo, saldo_atual, ativo, criado_em, atualizado_em " +
                "FROM produtos WHERE ativo = true ORDER BY nome";

        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }

        return produtos;
    }

    public void update(Produto produto) throws SQLException {

        String sql = "UPDATE produtos SET codigo = ?, nome = ?, descricao = ?, id_categoria = ?, " +
                "preco_compra = ?, preco_venda = ?, estoque_minimo = ?, ativo = ? WHERE id = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setInt(4, produto.getCategoriaId());
            stmt.setBigDecimal(5, produto.getPrecoCompra());
            stmt.setBigDecimal(6, produto.getPrecoVenda());
            stmt.setInt(7, produto.getEstoqueMinimo());
            stmt.setBoolean(8, produto.isAtivo());
            stmt.setInt(9, produto.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Produto não encontrado");
            }
        }
    }

    public void updateSaldo(Integer produtoId, Integer novoSaldo, Connection conexao) throws SQLException {

        String sql = "UPDATE produtos SET saldo_atual = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, novoSaldo);
            stmt.setInt(2, produtoId);

            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {

        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Produto não encontrado");
            }
        }
    }

    public Integer contarTotalProdutos() throws SQLException {

        String sql = "SELECT COUNT(*) AS total FROM produtos";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        }

        return 0;
    }

    public Integer contarProdutosAtivos() throws SQLException {

        String sql = "SELECT COUNT(*) AS total FROM produtos WHERE ativo = true";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        }

        return 0;
    }

    public Integer contarTotalEstoque() throws SQLException {

        String sql = "SELECT SUM(saldo_atual) AS total FROM produtos WHERE ativo = true";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {

                Integer total = rs.getInt("total");

                return rs.wasNull() ? 0 : total;
            }
        }

        return 0;
    }

    public BigDecimal calcularValorEstoque() throws SQLException {

        String sql = "SELECT SUM(saldo_atual * preco_venda) AS valor FROM produtos WHERE ativo = true";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {

                BigDecimal valor = rs.getBigDecimal("valor");

                return rs.wasNull() ? BigDecimal.ZERO : valor;
            }
        }

        return BigDecimal.ZERO;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {

        Produto produto = new Produto();

        produto.setId(rs.getInt("id"));
        produto.setCodigo(rs.getString("codigo"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setCategoriaId(rs.getInt("id_categoria"));
        produto.setPrecoCompra(rs.getBigDecimal("preco_compra"));
        produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
        produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
        produto.setSaldoAtual(rs.getInt("saldo_atual"));
        produto.setAtivo(rs.getBoolean("ativo"));

        Timestamp criadoEm = rs.getTimestamp("criado_em");

        if (criadoEm != null) {

            produto.setCriadoEm(
                    criadoEm.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
            );
        }

        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");

        if (atualizadoEm != null) {

            produto.setAtualizadoEm(
                    atualizadoEm.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
            );
        }

        return produto;
    }
}