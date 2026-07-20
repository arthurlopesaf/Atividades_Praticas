package DAO;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {
    protected Connection obterConexao() throws SQLException {
        return Conexao.obterConexao();
    }

    protected void fecharConexao(Connection conexao) {
        Conexao.fecharConexao(conexao);
    }
}
