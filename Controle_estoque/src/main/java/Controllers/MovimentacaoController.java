package Controllers;

import Models.Movimentacao;
import Models.Produto;
import Services.EstoqueService;
import DAO.ProdutoDAO;
import DAO.MovimentacaoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;

public class MovimentacaoController {
    @FXML private ComboBox<Produto> cmbProduto;
    @FXML private Label lblSaldoAtual;
    @FXML private Label lblEstoqueMinimo;
    @FXML private Label lblProdutoAtivo;
    @FXML private RadioButton radEntrada;
    @FXML private RadioButton radSaida;
    @FXML private TextField txtQuantidade;
    @FXML private TextArea txtObservacao;
    @FXML private Button btnRegistrar;
    @FXML private Button btnLimpar;
    @FXML private TableView<Movimentacao> tabela;
    @FXML private TableColumn<Movimentacao, String> colTipo;
    @FXML private TableColumn<Movimentacao, Integer> colQuantidade;
    @FXML private TableColumn<Movimentacao, Integer> colSaldoAnterior;
    @FXML private TableColumn<Movimentacao, Integer> colSaldoNovo;
    @FXML private TableColumn<Movimentacao, String> colObservacao;
    @FXML private TableColumn<Movimentacao, String> colData;

    private EstoqueService estoqueService;
    private ProdutoDAO produtoDAO;
    private MovimentacaoDAO movimentacaoDAO;
    private Produto produtoSelecionado;

    @FXML
    public void initialize() {
        estoqueService = new EstoqueService();
        produtoDAO = new ProdutoDAO();
        movimentacaoDAO = new MovimentacaoDAO();

        ToggleGroup grupo = new ToggleGroup();
        radEntrada.setToggleGroup(grupo);
        radSaida.setToggleGroup(grupo);
        radEntrada.setSelected(true);

        configurarTabela();
        carregarProdutos();
        carregarHistorico();

        cmbProduto.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                produtoSelecionado = newVal;
                atualizarInfoProduto(newVal);
                carregarHistoricoProduto(newVal.getId());
            }
        });

        btnRegistrar.setOnAction(e -> registrarMovimentacao());
        btnLimpar.setOnAction(e -> limpar());
    }

    private void configurarTabela() {
        colTipo.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(
            () -> cellData.getValue().getTipo().toString()
        ));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colSaldoAnterior.setCellValueFactory(new PropertyValueFactory<>("saldoAnterior"));
        colSaldoNovo.setCellValueFactory(new PropertyValueFactory<>("saldoNovo"));
        colObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        colData.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(
            () -> cellData.getValue().getCriadoEm().toString()
        ));
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.findAtivos();
            ObservableList<Produto> dados = FXCollections.observableArrayList(produtos);
            cmbProduto.setItems(dados);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar produtos", e.getMessage());
        }
    }

    private void carregarHistorico() {
        try {
            List<Movimentacao> movimentacoes = movimentacaoDAO.findUltimas(50);
            ObservableList<Movimentacao> dados = FXCollections.observableArrayList(movimentacoes);
            tabela.setItems(dados);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar histórico", e.getMessage());
        }
    }

    private void carregarHistoricoProduto(Integer produtoId) {
        try {
            List<Movimentacao> movimentacoes = movimentacaoDAO.findByProduto(produtoId);
            ObservableList<Movimentacao> dados = FXCollections.observableArrayList(movimentacoes);
            tabela.setItems(dados);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar histórico", e.getMessage());
        }
    }

    private void atualizarInfoProduto(Produto produto) {
        lblSaldoAtual.setText("Saldo Atual: " + produto.getSaldoAtual() + " un.");
        lblEstoqueMinimo.setText("Estoque Mínimo: " + produto.getEstoqueMinimo() + " un.");
        lblProdutoAtivo.setText("Status: " + (produto.isAtivo() ? "Ativo" : "Inativo"));
        lblProdutoAtivo.setStyle(produto.isAtivo() ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    private void registrarMovimentacao() {
        try {
            if (produtoSelecionado == null) {
                mostrarAviso("Validação", "Selecione um produto");
                return;
            }

            if (txtQuantidade.getText().trim().isEmpty()) {
                mostrarAviso("Validação", "Informe a quantidade");
                return;
            }

            Integer quantidade = Integer.parseInt(txtQuantidade.getText());
            Movimentacao.TipoMovimentacao tipo = radEntrada.isSelected() ? 
                Movimentacao.TipoMovimentacao.ENTRADA : Movimentacao.TipoMovimentacao.SAIDA;

            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setProdutoId(produtoSelecionado.getId());
            movimentacao.setTipo(tipo);
            movimentacao.setQuantidade(quantidade);
            movimentacao.setObservacao(txtObservacao.getText());

            estoqueService.registrarMovimentacao(movimentacao);
            mostrarSucesso("Movimentação registrada com sucesso!");
            
            carregarProdutos();
            atualizarInfoProduto(produtoDAO.findById(produtoSelecionado.getId()));
            carregarHistoricoProduto(produtoSelecionado.getId());
            limpar();

        } catch (NumberFormatException e) {
            mostrarAviso("Validação", "Quantidade deve ser um número inteiro");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarAviso("Erro", e.getMessage());
        } catch (SQLException e) {
            mostrarErro("Erro ao registrar movimentação", e.getMessage());
        }
    }

    private void limpar() {
        txtQuantidade.clear();
        txtObservacao.clear();
        radEntrada.setSelected(true);
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
