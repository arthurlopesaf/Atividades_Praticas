package Controllers;

import Models.Categoria;
import DAO.CategoriaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoriaController {
    @FXML private TableView<Categoria> tabela;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colNome;
    @FXML private TableColumn<Categoria, String> colDescricao;
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private Button btnAdicionar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnRemover;
    @FXML private Button btnLimpar;

    private CategoriaDAO categoriaDAO;
    private Categoria categoriaAtual;

    @FXML
    public void initialize() {
        categoriaDAO = new CategoriaDAO();
        configurarTabela();
        carregarDados();

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                categoriaAtual = newVal;
                txtNome.setText(newVal.getNome());
                txtDescricao.setText(newVal.getDescricao());
            }
        });

        btnAdicionar.setOnAction(e -> adicionar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnRemover.setOnAction(e -> remover());
        btnLimpar.setOnAction(e -> limpar());
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    }

    private void carregarDados() {
        try {
            List<Categoria> categorias = categoriaDAO.findAll();
            ObservableList<Categoria> dados = FXCollections.observableArrayList(categorias);
            tabela.setItems(dados);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar categorias", e.getMessage());
        }
    }

    private void adicionar() {
        try {
            if (txtNome.getText().trim().isEmpty()) {
                mostrarAviso("Validação", "Nome da categoria é obrigatório");
                return;
            }

            Categoria categoria = new Categoria(txtNome.getText(), txtDescricao.getText());
            categoriaDAO.create(categoria);

            mostrarSucesso("Categoria adicionada com sucesso");
            carregarDados();
            limpar();
        } catch (SQLException e) {
            mostrarErro("Erro ao adicionar categoria", e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarAviso("Validação", e.getMessage());
        }
    }

    private void atualizar() {
        if (categoriaAtual == null) {
            mostrarAviso("Selecione", "Selecione uma categoria para atualizar");
            return;
        }

        try {
            if (txtNome.getText().trim().isEmpty()) {
                mostrarAviso("Validação", "Nome da categoria é obrigatório");
                return;
            }

            categoriaAtual.setNome(txtNome.getText());
            categoriaAtual.setDescricao(txtDescricao.getText());
            categoriaDAO.update(categoriaAtual);

            mostrarSucesso("Categoria atualizada com sucesso");
            carregarDados();
            limpar();
        } catch (SQLException e) {
            mostrarErro("Erro ao atualizar categoria", e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarAviso("Validação", e.getMessage());
        }
    }

    private void remover() {
        if (categoriaAtual == null) {
            mostrarAviso("Selecione", "Selecione uma categoria para remover");
            return;
        }

        Optional<ButtonType> resultado = confirmar("Confirmação", "Tem certeza que deseja remover esta categoria?");
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                categoriaDAO.delete(categoriaAtual.getId());
                mostrarSucesso("Categoria removida com sucesso");
                carregarDados();
                limpar();
            } catch (SQLException e) {
                mostrarErro("Erro ao remover categoria", e.getMessage());
            }
        }
    }

    private void limpar() {
        txtNome.clear();
        txtDescricao.clear();
        tabela.getSelectionModel().clearSelection();
        categoriaAtual = null;
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
