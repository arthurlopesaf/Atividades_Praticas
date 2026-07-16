package com.example.controle_estacionamento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

public class TelaPrincipalController {

    @FXML
    private TextField txtPlaca;

    @FXML
    private TextField txtModelo;

    @FXML
    private ComboBox<String> cboTipoVeiculo;

    @FXML
    private DatePicker dtpEntrada;

    @FXML
    private DatePicker dtpSaida;

    @FXML
    private TextField txtPesquisarPlaca;

    @FXML
    private Button btnPesquisar;

    @FXML
    private Button btnMostrarTodos;

    @FXML
    private TableView<Veiculo> dgvVeiculos;

    @FXML
    private TableColumn<Veiculo, String> colPlaca;

    @FXML
    private TableColumn<Veiculo, String> colModelo;

    @FXML
    private TableColumn<Veiculo, String> colTipo;

    @FXML
    private TableColumn<Veiculo, LocalDateTime> colEntrada;

    @FXML
    private TableColumn<Veiculo, LocalDateTime> colSaida;

    @FXML
    private TableColumn<Veiculo, Integer> colHoras;

    @FXML
    private TableColumn<Veiculo, BigDecimal> colValor;

    @FXML
    private TableColumn<Veiculo, String> colSituacao;

    @FXML
    private Label lblEstacionados;

    @FXML
    private Label lblFinalizados;

    @FXML
    private Label lblTotalRecebido;

    private final ObservableList<Veiculo> listaVeiculos =
            FXCollections.observableArrayList();

    public void initialize() {

        cboTipoVeiculo.getItems().addAll(
                "Moto",
                "Carro",
                "Utilitário"
        );

        dtpEntrada.setValue(LocalDate.now());
        dtpSaida.setValue(LocalDate.now());

        configurarTabela();

        dgvVeiculos.setItems(listaVeiculos);

        AtualizarIndicadores();
    }

    private void configurarTabela() {

        colPlaca.setCellValueFactory(
                new PropertyValueFactory<>("placa")
        );

        colModelo.setCellValueFactory(
                new PropertyValueFactory<>("modelo")
        );

        colTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo")
        );

        colEntrada.setCellValueFactory(
                new PropertyValueFactory<>("entrada")
        );

        colSaida.setCellValueFactory(
                new PropertyValueFactory<>("saida")
        );

        colHoras.setCellValueFactory(
                new PropertyValueFactory<>("horas")
        );

        colValor.setCellValueFactory(
                new PropertyValueFactory<>("valor")
        );

        colSituacao.setCellValueFactory(
                new PropertyValueFactory<>("situacao")
        );
    }

    private boolean ValidarEntrada() {

        if (txtPlaca.getText().trim().isEmpty()) {

            mensagem("Informe a placa do veículo.");
            txtPlaca.requestFocus();

            return false;
        }

        if (txtModelo.getText().trim().isEmpty()) {

            mensagem("Informe o modelo do veículo.");
            txtModelo.requestFocus();

            return false;
        }

        if (cboTipoVeiculo.getValue() == null) {

            mensagem("Selecione o tipo do veículo.");

            return false;
        }

        return true;
    }

    private boolean PlacaJaEstacionada(String placa) {

        for (Veiculo v : listaVeiculos) {

            if (v.getPlaca().equalsIgnoreCase(placa)
                    &&
                    v.getSituacao().equals("Estacionado")) {

                return true;
            }
        }

        return false;
    }

    public void btnRegistrarEntrada(ActionEvent event) {

        if (!ValidarEntrada())
            return;

        String placa =
                txtPlaca.getText()
                        .trim()
                        .toUpperCase();

        if (PlacaJaEstacionada(placa)) {

            mensagem(
                    "Esta placa já possui uma entrada em aberto."
            );

            return;
        }

        LocalDateTime entrada =
                LocalDateTime.of(
                        dtpEntrada.getValue(),
                        LocalTime.now()
                );

        Veiculo v = new Veiculo();

        v.setPlaca(placa);

        v.setModelo(
                txtModelo.getText().trim()
        );

        v.setTipo(
                cboTipoVeiculo.getValue()
        );

        v.setEntrada(entrada);

        v.setHoras(0);

        v.setValor(BigDecimal.ZERO);

        v.setSituacao("Estacionado");

        listaVeiculos.add(v);

        AtualizarIndicadores();

        LimparCampos();
    }

    private int CalcularHoras(LocalDateTime entrada,
                              LocalDateTime saida) {

        Duration duracao =
                Duration.between(
                        entrada,
                        saida
                );

        return (int)
                Math.ceil(
                        duracao.toMinutes() / 60.0
                );
    }

    private BigDecimal ObterValorHora(String tipo) {

        if (tipo.equals("Moto"))
            return BigDecimal.valueOf(4);

        if (tipo.equals("Carro"))
            return BigDecimal.valueOf(7);

        return BigDecimal.valueOf(10);
    }

    public void btnRegistrarSaida(ActionEvent event) {

        Veiculo v =
                dgvVeiculos
                        .getSelectionModel()
                        .getSelectedItem();

        if (v == null) {

            mensagem("Selecione um veículo.");

            return;
        }

        if (v.getSituacao().equals("Finalizado")) {

            mensagem(
                    "A saída deste veículo já foi registrada."
            );

            return;
        }

        LocalDateTime saida =
                LocalDateTime.of(
                        dtpSaida.getValue(),
                        LocalTime.now()
                );

        if (saida.isBefore(v.getEntrada())) {

            mensagem(
                    "A saída não pode ser anterior à entrada."
            );

            return;
        }

        int horas =
                CalcularHoras(
                        v.getEntrada(),
                        saida
                );

        BigDecimal valor =
                ObterValorHora(v.getTipo())
                        .multiply(
                                BigDecimal.valueOf(horas)
                        );

        v.setSaida(saida);

        v.setHoras(horas);

        v.setValor(valor);

        v.setSituacao("Finalizado");

        dgvVeiculos.refresh();

        AtualizarIndicadores();
    }

    public void btnPesquisar(ActionEvent event) {

        String pesquisa =
                txtPesquisarPlaca
                        .getText()
                        .trim()
                        .toUpperCase();

        ObservableList<Veiculo> filtrados =
                FXCollections.observableArrayList();

        for (Veiculo v : listaVeiculos) {

            if (v.getPlaca()
                    .contains(pesquisa)) {

                filtrados.add(v);
            }

        }

        dgvVeiculos.setItems(filtrados);
    }

    public void btnMostrarTodos(ActionEvent event) {

        txtPesquisarPlaca.clear();

        dgvVeiculos.setItems(listaVeiculos);
    }

    private void AtualizarIndicadores() {

        int estacionados = 0;

        int finalizados = 0;

        BigDecimal total =
                BigDecimal.ZERO;

        for (Veiculo v : listaVeiculos) {

            if (v.getSituacao()
                    .equals("Estacionado")) {

                estacionados++;

            } else {

                finalizados++;

                total =
                        total.add(
                                v.getValor()
                        );
            }
        }

        lblEstacionados.setText(
                String.valueOf(estacionados)
        );

        lblFinalizados.setText(
                String.valueOf(finalizados)
        );

        lblTotalRecebido.setText(
                NumberFormat
                        .getCurrencyInstance(
                                new Locale("pt", "BR")
                        )
                        .format(total)
        );
    }

    public void btnRemover(ActionEvent event) {

        Veiculo v = dgvVeiculos
                .getSelectionModel()
                .getSelectedItem();

        if (v == null) {

            mensagem("Selecione um veículo.");

            return;
        }

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setHeaderText(null);

        alert.setContentText(
                "Deseja remover o registro selecionado?"
        );

        Optional<ButtonType> resposta =
                alert.showAndWait();

        if (resposta.isPresent()
                && resposta.get() == ButtonType.OK) {

            listaVeiculos.remove(v);

            AtualizarIndicadores();

        }

    }

    public void btnLimpar(ActionEvent event) {

        LimparCampos();

    }

    private void LimparCampos() {

        txtPlaca.clear();

        txtModelo.clear();

        cboTipoVeiculo
                .getSelectionModel()
                .clearSelection();

        dtpEntrada.setValue(
                LocalDate.now()
        );

        dtpSaida.setValue(
                LocalDate.now()
        );

        txtPlaca.requestFocus();

    }

    public void btnEncerrar(ActionEvent event) {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setHeaderText(null);

        alert.setContentText(
                "Deseja encerrar o sistema?"
        );

        Optional<ButtonType> resposta =
                alert.showAndWait();

        if (resposta.isPresent()
                && resposta.get() == ButtonType.OK) {

            System.exit(0);

        }

    }

    private void mensagem(String texto) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setHeaderText(null);

        alert.setContentText(texto);

        alert.showAndWait();

    }
}