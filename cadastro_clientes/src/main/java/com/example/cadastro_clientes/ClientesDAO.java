package com.example.cadastro_clientes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientesDAO {

    private final Conexao conexao = new Conexao();

    public void inserir(Clientes cliente) throws SQLException {

        String sql = "INSERT INTO clientes " +
                "(nome, cpf, email, telefone, cidade, ativo, data_cadastro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencherParametros(stmt, cliente);

            stmt.setTimestamp(7,
                    Timestamp.valueOf(cliente.getDataCadastro()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Clientes cliente) throws SQLException {

        String sql = "UPDATE clientes SET nome=?, cpf=?, email=?, " +
                "telefone=?, cidade=?, ativo=? WHERE id=?";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            preencherParametros(stmt, cliente);

            stmt.setInt(7, cliente.getId());

            stmt.executeUpdate();
        }
    }

    public void alterarSituacao(int id, boolean ativo) throws SQLException {

        String sql = "UPDATE clientes SET ativo=? WHERE id=?";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setBoolean(1, ativo);
            stmt.setInt(2, id);

            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM clientes WHERE id=?";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    public List<Clientes> listar() throws SQLException {

        List<Clientes> lista = new ArrayList<>();

        String sql = "SELECT * FROM clientes ORDER BY nome";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                lista.add(mapearCliente(rs));

            }
        }

        return lista;
    }

    public List<Clientes> pesquisarPorNome(String nome) throws SQLException {

        List<Clientes> lista = new ArrayList<>();

        String sql = "SELECT * FROM clientes " +
                "WHERE nome LIKE ? ORDER BY nome";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                lista.add(mapearCliente(rs));

            }

        }

        return lista;
    }

    public Clientes buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM clientes WHERE id=?";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return mapearCliente(rs);

            }

        }

        return null;
    }

    public boolean cpfExistente(String cpf, Integer idAtual) throws SQLException {

        String sql = "SELECT COUNT(*) FROM clientes " +
                "WHERE cpf=? AND id<>?";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            stmt.setInt(2,
                    idAtual == null ? 0 : idAtual);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return rs.getInt(1) > 0;

            }

        }

        return false;
    }

    private void preencherParametros(PreparedStatement stmt, Clientes cliente)
            throws SQLException {

        stmt.setString(1, cliente.getNome());

        stmt.setString(2, cliente.getCpf());

        stmt.setString(3, cliente.getEmail());

        stmt.setString(4, cliente.getTelefone());

        stmt.setString(5, cliente.getCidade());

        stmt.setBoolean(6, cliente.isAtivo());

    }

    private Clientes mapearCliente(ResultSet rs) throws SQLException {

        Clientes cliente = new Clientes();

        cliente.setId(rs.getInt("id"));

        cliente.setNome(rs.getString("nome"));

        cliente.setCpf(rs.getString("cpf"));

        cliente.setEmail(rs.getString("email"));

        cliente.setTelefone(rs.getString("telefone"));

        cliente.setCidade(rs.getString("cidade"));

        cliente.setAtivo(rs.getBoolean("ativo"));

        Timestamp data = rs.getTimestamp("data_cadastro");

        if (data != null) {

            cliente.setDataCadastro(
                    data.toLocalDateTime()
            );

        }

        return cliente;
    }
}