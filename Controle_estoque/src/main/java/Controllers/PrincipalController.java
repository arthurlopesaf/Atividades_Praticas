package Controllers;

import Models.Produto;
import Models.Movimentacao;
import Services.EstoqueService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PrincipalController {



    @FXML private Label lblTotalProdutos;
    @FXML private Label lblProdutosAtivos;
    @FXML private Label lblTotalEstoque;
    @FXML private Label lblValorEstoque;
    @FXML private Label lblProdutosBaixo;
    @FXML private TableView<Movimentacao> tableMovimentacoes;
    @FXML private TableColumn<Movimentacao, String> colTipo;
    @FXML private TableColumn<Movimentacao, Integer> colQuantidade;
    @FXML private TableColumn<Movimentacao, String> colObservacao;
    @FXML private TableColumn<Movimentacao, String> colData;
    @FXML private Button btnCategorias;
    @FXML private Button btnProdutos;
    @FXML private Button btnMovimentacoes;
    @FXML private Button btnAtualizarDados;

    private EstoqueService estoqueService;

    @FXML
    public void initialize() {
        estoqueService = new EstoqueService();
        configurarTabela();
        atualizarDados();

        btnCategorias.setOnAction(e -> abrirCategorias());
        btnProdutos.setOnAction(e -> abrirProdutos());
        btnMovimentacoes.setOnAction(e -> abrirMovimentacoes());
        btnAtualizarDados.setOnAction(e -> atualizarDados());
    }

    private void configurarTabela() {
        colTipo.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(
            () -> cellData.getValue().getTipo().toString()
        ));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        colData.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(
            () -> cellData.getValue().getCriadoEm().toString()
        ));
    }

    private void atualizarDados() {
        try {
            int totalProdutos = estoqueService.getProdutoDAO().contarTotalProdutos();
            int produtosAtivos = estoqueService.getProdutoDAO().contarProdutosAtivos();
            int totalEstoque = estoqueService.getProdutoDAO().contarTotalEstoque();
            BigDecimal valorEstoque = estoqueService.getProdutoDAO().calcularValorEstoque();
            List<Produto> produtosBaixo = estoqueService.getProdutoDAO().findEstoqueBaixo();

            lblTotalProdutos.setText(String.valueOf(totalProdutos));
            lblProdutosAtivos.setText(String.valueOf(produtosAtivos));
            lblTotalEstoque.setText(String.valueOf(totalEstoque));
            lblValorEstoque.setText("R$ " + String.format("%.2f", valorEstoque));
            lblProdutosBaixo.setText(String.valueOf(produtosBaixo.size()));

            List<Movimentacao> ultimasMovimentacoes = estoqueService.getMovimentacaoDAO().findUltimas(10);
            tableMovimentacoes.getItems().clear();
            tableMovimentacoes.getItems().addAll(ultimasMovimentacoes);

        } catch (SQLException e) {
            mostrarErro("Erro ao atualizar dados", e.getMessage());
        }
    }

    private void abrirCategorias() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Categorias.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Categorias");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro", "Não foi possível abrir a tela de Categorias");
        }
    }

    private void abrirProdutos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Produtos.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Produtos");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro", "Não foi possível abrir a tela de Produtos");
        }
    }

    private void abrirMovimentacoes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Movimentacoes.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Movimentações");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro", "Não foi possível abrir a tela de Movimentações");
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

