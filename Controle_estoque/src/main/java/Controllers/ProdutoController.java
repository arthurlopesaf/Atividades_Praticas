package Controllers;

import Models.Produto;
import Models.Categoria;
import DAO.ProdutoDAO;
import DAO.CategoriaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProdutoController {
    @FXML private TableView<Produto> tabela;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, Integer> colSaldo;
    @FXML private TableColumn<Produto, Boolean> colAtivo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField txtPrecoCompra;
    @FXML private TextField txtPrecoVenda;
    @FXML private TextField txtEstoqueMinimo;
    @FXML private Label lblSaldoAtual;
    @FXML private CheckBox chkAtivo;
    @FXML private Button btnAdicionar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnRemover;
    @FXML private Button btnLimpar;

    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;
    private Produto produtoAtual;

    @FXML
    public void initialize() {
        produtoDAO = new ProdutoDAO();
        categoriaDAO = new CategoriaDAO();
        configurarTabela();
        carregarCategorias();
        carregarDados();

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                produtoAtual = newVal;
                preencherCampos(newVal);
            }
        });

        btnAdicionar.setOnAction(e -> adicionar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnRemover.setOnAction(e -> remover());
        btnLimpar.setOnAction(e -> limpar());
    }

    private void configurarTabela() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(
            () -> cellData.getValue().getCategoria() != null ? cellData.getValue().getCategoria().getNome() : ""
        ));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldoAtual"));
        colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
    }

    private void carregarCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.findAll();
            ObservableList<Categoria> dados = FXCollections.observableArrayList(categorias);
            cmbCategoria.setItems(dados);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar categorias", e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            List<Produto> produtos = produtoDAO.findAll();
            
            for (Produto p : produtos) {
                Categoria cat = categoriaDAO.findById(p.getCategoriaId());
                p.setCategoria(cat);
            }
            
            ObservableList<Produto> dados = FXCollections.observableArrayList(produtos);
            tabela.setItems(dados);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar produtos", e.getMessage());
        }
    }

    private void preencherCampos(Produto produto) {
        txtCodigo.setText(produto.getCodigo());
        txtNome.setText(produto.getNome());
        if (produto.getCategoria() != null) {
            cmbCategoria.setValue(produto.getCategoria());
        }
        txtPrecoCompra.setText(produto.getPrecoCompra().toString());
        txtPrecoVenda.setText(produto.getPrecoVenda().toString());
        txtEstoqueMinimo.setText(produto.getEstoqueMinimo().toString());
        lblSaldoAtual.setText("Saldo Atual: " + produto.getSaldoAtual() + " un.");
        chkAtivo.setSelected(produto.isAtivo());

        txtCodigo.setDisable(true);
    }

    private void adicionar() {
        try {
            validarCampos();

            Produto produto = new Produto();
            produto.setCodigo(txtCodigo.getText());
            produto.setNome(txtNome.getText());
            produto.setCategoriaId(cmbCategoria.getValue().getId());
            produto.setPrecoCompra(new BigDecimal(txtPrecoCompra.getText()));
            produto.setPrecoVenda(new BigDecimal(txtPrecoVenda.getText()));
            produto.setEstoqueMinimo(Integer.parseInt(txtEstoqueMinimo.getText()));
            produto.setAtivo(chkAtivo.isSelected());

            produtoDAO.create(produto);
            mostrarSucesso("Produto adicionado com sucesso");
            carregarDados();
            limpar();
        } catch (SQLException e) {
            mostrarErro("Erro ao adicionar produto", e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAviso("Validação", "Valores numéricos inválidos");
        } catch (IllegalArgumentException e) {
            mostrarAviso("Validação", e.getMessage());
        }
    }

    private void atualizar() {
        if (produtoAtual == null) {
            mostrarAviso("Selecione", "Selecione um produto para atualizar");
            return;
        }

        try {
            validarCampos();

            produtoAtual.setNome(txtNome.getText());
            produtoAtual.setCategoriaId(cmbCategoria.getValue().getId());
            produtoAtual.setPrecoCompra(new BigDecimal(txtPrecoCompra.getText()));
            produtoAtual.setPrecoVenda(new BigDecimal(txtPrecoVenda.getText()));
            produtoAtual.setEstoqueMinimo(Integer.parseInt(txtEstoqueMinimo.getText()));
            produtoAtual.setAtivo(chkAtivo.isSelected());

            produtoDAO.update(produtoAtual);
            mostrarSucesso("Produto atualizado com sucesso");
            carregarDados();
            limpar();
        } catch (SQLException e) {
            mostrarErro("Erro ao atualizar produto", e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAviso("Validação", "Valores numéricos inválidos");
        } catch (IllegalArgumentException e) {
            mostrarAviso("Validação", e.getMessage());
        }
    }

    private void remover() {
        if (produtoAtual == null) {
            mostrarAviso("Selecione", "Selecione um produto para remover");
            return;
        }

        Optional<ButtonType> resultado = confirmar("Confirmação", "Tem certeza que deseja remover este produto?");
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                produtoDAO.delete(produtoAtual.getId());
                mostrarSucesso("Produto removido com sucesso");
                carregarDados();
                limpar();
            } catch (SQLException e) {
                mostrarErro("Erro ao remover produto", e.getMessage());
            }
        }
    }

    private void limpar() {
        txtCodigo.clear();
        txtCodigo.setDisable(false);
        txtNome.clear();
        cmbCategoria.setValue(null);
        txtPrecoCompra.clear();
        txtPrecoVenda.clear();
        txtEstoqueMinimo.clear();
        lblSaldoAtual.setText("Saldo Atual: 0 un.");
        chkAtivo.setSelected(true);
        tabela.getSelectionModel().clearSelection();
        produtoAtual = null;
    }

    private void validarCampos() {
        if (txtCodigo.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Código é obrigatório");
        }
        if (txtNome.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (cmbCategoria.getValue() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (txtPrecoCompra.getText().isEmpty() || Double.parseDouble(txtPrecoCompra.getText()) < 0) {
            throw new IllegalArgumentException("Preço de compra não pode ser negativo");
        }
        if (txtPrecoVenda.getText().isEmpty() || Double.parseDouble(txtPrecoVenda.getText()) <= 0) {
            throw new IllegalArgumentException("Preço de venda deve ser maior que zero");
        }
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

    private Optional<ButtonType> confirmar(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        return alert.showAndWait();
    }
}
