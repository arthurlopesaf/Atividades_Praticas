package Services;

import DAO.MovimentacaoDAO;
import DAO.ProdutoDAO;
import Models.Movimentacao;
import Models.Produto;
import DAO.Conexao;
import java.sql.Connection;
import java.sql.SQLException;

public class EstoqueService {
    private ProdutoDAO produtoDAO;
    private MovimentacaoDAO movimentacaoDAO;

    public EstoqueService() {
        this.produtoDAO = new ProdutoDAO();
        this.movimentacaoDAO = new MovimentacaoDAO();
    }

    public void registrarMovimentacao(Movimentacao movimentacao) throws SQLException {
        if (movimentacao.getProdutoId() == null || movimentacao.getProdutoId() <= 0) {
            throw new IllegalArgumentException("Produto é obrigatório");
        }

        if (movimentacao.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de movimentação é obrigatório");
        }

        if (movimentacao.getQuantidade() == null || movimentacao.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        Produto produto = produtoDAO.findById(movimentacao.getProdutoId());
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        if (!produto.isAtivo()) {
            throw new IllegalStateException("Não é permitido fazer movimentação em produto desativado");
        }

        Integer novoSaldo;
        if (movimentacao.getTipo() == Movimentacao.TipoMovimentacao.ENTRADA) {
            novoSaldo = produto.getSaldoAtual() + movimentacao.getQuantidade();
        } else {
            if (movimentacao.getQuantidade() > produto.getSaldoAtual()) {
                throw new IllegalStateException("Saída superior ao estoque disponível. Disponível: " + produto.getSaldoAtual() + " un.");
            }
            novoSaldo = produto.getSaldoAtual() - movimentacao.getQuantidade();
        }

        Connection conexao = null;
        try {
            conexao = Conexao.obterConexao();
            conexao.setAutoCommit(false);

            movimentacao.setSaldoAnterior(produto.getSaldoAtual());
            movimentacao.setSaldoNovo(novoSaldo);

            movimentacaoDAO.create(movimentacao, conexao);
            produtoDAO.updateSaldo(produto.getId(), novoSaldo, conexao);

            conexao.commit();

        } catch (SQLException e) {
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conexao != null) {
                try {
                    conexao.setAutoCommit(true);
                    conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ativarProduto(Integer produtoId) throws SQLException {
        Produto produto = produtoDAO.findById(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        produto.setAtivo(true);
        produtoDAO.update(produto);
    }

    public void desativarProduto(Integer produtoId) throws SQLException {
        Produto produto = produtoDAO.findById(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        produto.setAtivo(false);
        produtoDAO.update(produto);
    }

    public ProdutoDAO getProdutoDAO() {
        return produtoDAO;
    }

    public MovimentacaoDAO getMovimentacaoDAO() {
        return movimentacaoDAO;
    }
}
