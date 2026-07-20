package DAO;

import Models.Movimentacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO extends BaseDAO {

    public Movimentacao create(Movimentacao movimentacao, Connection conexao) throws SQLException {

        String sql = "INSERT INTO movimentacoes (id_produto, tipo, quantidade, saldo_anterior, saldo_novo, observacao) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, movimentacao.getProdutoId());
            stmt.setString(2, movimentacao.getTipo().toString());
            stmt.setInt(3, movimentacao.getQuantidade());
            stmt.setInt(4, movimentacao.getSaldoAnterior());
            stmt.setInt(5, movimentacao.getSaldoNovo());
            stmt.setString(6, movimentacao.getObservacao());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir movimentação");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    movimentacao.setId(generatedKeys.getInt(1));
                }
            }
        }

        return movimentacao;
    }

    public Movimentacao findById(Integer id) throws SQLException {

        String sql = "SELECT id, id_produto, tipo, quantidade, saldo_anterior, saldo_novo, observacao, criado_em " +
                "FROM movimentacoes WHERE id = ?";

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearMovimentacao(rs);
                }
            }
        }

        return null;
    }

    public List<Movimentacao> findByProduto(Integer produtoId) throws SQLException {

        String sql = "SELECT id, id_produto, tipo, quantidade, saldo_anterior, saldo_novo, observacao, criado_em " +
                "FROM movimentacoes WHERE id_produto = ? ORDER BY criado_em DESC";

        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    movimentacoes.add(mapearMovimentacao(rs));
                }
            }
        }

        return movimentacoes;
    }

    public List<Movimentacao> findAll() throws SQLException {

        String sql = "SELECT id, id_produto, tipo, quantidade, saldo_anterior, saldo_novo, observacao, criado_em " +
                "FROM movimentacoes ORDER BY criado_em DESC LIMIT 100";

        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movimentacoes.add(mapearMovimentacao(rs));
            }
        }

        return movimentacoes;
    }

    public List<Movimentacao> findUltimas(int quantidade) throws SQLException {

        String sql = "SELECT id, id_produto, tipo, quantidade, saldo_anterior, saldo_novo, observacao, criado_em " +
                "FROM movimentacoes ORDER BY criado_em DESC LIMIT ?";

        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, quantidade);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    movimentacoes.add(mapearMovimentacao(rs));
                }
            }
        }

        return movimentacoes;
    }

    public List<Movimentacao> findByProdutoComData(Integer produtoId, LocalDateTime dataInicio, LocalDateTime dataFim)
            throws SQLException {

        String sql = "SELECT id, id_produto, tipo, quantidade, saldo_anterior, saldo_novo, observacao, criado_em " +
                "FROM movimentacoes " +
                "WHERE id_produto = ? AND criado_em BETWEEN ? AND ? " +
                "ORDER BY criado_em DESC";

        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conexao = obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);
            stmt.setTimestamp(2, Timestamp.valueOf(dataInicio));
            stmt.setTimestamp(3, Timestamp.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    movimentacoes.add(mapearMovimentacao(rs));
                }
            }
        }

        return movimentacoes;
    }

    private Movimentacao mapearMovimentacao(ResultSet rs) throws SQLException {

        Movimentacao movimentacao = new Movimentacao();

        movimentacao.setId(rs.getInt("id"));
        movimentacao.setProdutoId(rs.getInt("id_produto"));
        movimentacao.setTipo(Movimentacao.TipoMovimentacao.valueOf(rs.getString("tipo")));
        movimentacao.setQuantidade(rs.getInt("quantidade"));
        movimentacao.setSaldoAnterior(rs.getInt("saldo_anterior"));
        movimentacao.setSaldoNovo(rs.getInt("saldo_novo"));
        movimentacao.setObservacao(rs.getString("observacao"));

        Timestamp criadoEm = rs.getTimestamp("criado_em");

        if (criadoEm != null) {

            movimentacao.setCriadoEm(
                    criadoEm.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
            );
        }

        return movimentacao;
    }
}