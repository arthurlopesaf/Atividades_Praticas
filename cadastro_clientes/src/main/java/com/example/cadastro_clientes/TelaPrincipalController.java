package com.example.cadastro_clientes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;

public class TelaPrincipalController {

    @FXML
    private TextField txtPesquisa;

    @FXML
    private TableView<Clientes> dgvClientes;

    @FXML
    private TableColumn<Clientes, Integer> colId;

    @FXML
    private TableColumn<Clientes, String> colNome;

    @FXML
    private TableColumn<Clientes, String> colCPF;

    @FXML
    private TableColumn<Clientes, String> colEmail;

    @FXML
    private TableColumn<Clientes, String> colTelefone;

    @FXML
    private TableColumn<Clientes, String> colCidade;

    @FXML
    private TableColumn<Clientes, Boolean> colAtivo;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCPF;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtCidade;

    @FXML
    private CheckBox chkAtivo;

    private final ClientesDAO dao = new ClientesDAO();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));

        colNome.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));

        colCPF.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getCpf()));

        colEmail.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));

        colTelefone.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefone()));

        colCidade.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getCidade()));

        colAtivo.setCellValueFactory(c ->
                new javafx.beans.property.SimpleBooleanProperty(c.getValue().isAtivo()).asObject());

        carregarTabela();

        dgvClientes.setOnMouseClicked(e -> selecionarCliente());
    }

    public void btnNovo(ActionEvent event) {

        limparCampos();
        chkAtivo.setSelected(true);

    }

    public void btnSalvar(ActionEvent event) {

        try {

            Clientes cliente = new Clientes();

            cliente.setNome(txtNome.getText());
            cliente.setCpf(txtCPF.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setTelefone(txtTelefone.getText());
            cliente.setCidade(txtCidade.getText());
            cliente.setAtivo(chkAtivo.isSelected());
            cliente.setDataCadastro(LocalDateTime.now());

            dao.inserir(cliente);

            carregarTabela();
            limparCampos();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void btnEditar(ActionEvent event) {

        try {

            Clientes cliente = new Clientes();

            cliente.setId(Integer.parseInt(txtId.getText()));
            cliente.setNome(txtNome.getText());
            cliente.setCpf(txtCPF.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setTelefone(txtTelefone.getText());
            cliente.setCidade(txtCidade.getText());
            cliente.setAtivo(chkAtivo.isSelected());

            dao.atualizar(cliente);

            carregarTabela();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void btnExcluir(ActionEvent event) {

        try {

            dao.excluir(Integer.parseInt(txtId.getText()));

            carregarTabela();
            limparCampos();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void btnAtivarDesativar(ActionEvent event) {

        try {

            dao.alterarSituacao(
                    Integer.parseInt(txtId.getText()),
                    !chkAtivo.isSelected()
            );

            carregarTabela();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void btnPesquisar(ActionEvent event) {

        try {

            dgvClientes.setItems(
                    FXCollections.observableArrayList(
                            dao.pesquisarPorNome(txtPesquisa.getText())
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void btnMostrarTodos(ActionEvent event) {

        carregarTabela();

    }

    public void btnCancelar(ActionEvent event) {

        limparCampos();

    }

    private void carregarTabela() {

        try {

            ObservableList<Clientes> lista =
                    FXCollections.observableArrayList(
                            dao.listar()
                    );

            dgvClientes.setItems(lista);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void selecionarCliente() {

        Clientes cliente =
                dgvClientes.getSelectionModel().getSelectedItem();

        if (cliente != null) {

            txtId.setText(String.valueOf(cliente.getId()));
            txtNome.setText(cliente.getNome());
            txtCPF.setText(cliente.getCpf());
            txtEmail.setText(cliente.getEmail());
            txtTelefone.setText(cliente.getTelefone());
            txtCidade.setText(cliente.getCidade());
            chkAtivo.setSelected(cliente.isAtivo());

        }

    }

    private void limparCampos() {

        txtId.clear();
        txtNome.clear();
        txtCPF.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtCidade.clear();
        chkAtivo.setSelected(false);

    }

}