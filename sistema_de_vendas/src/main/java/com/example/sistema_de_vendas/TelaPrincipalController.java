package com.example.sistema_de_vendas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TelaPrincipalController {

    String nomeCliente;
    String nomeProduto;
    double valorProduto;
    int quantidade;

    @FXML
    private TextField txtCliente;

    @FXML
    private TextField txtProduto;

    @FXML
    private TextField txtValor;

    @FXML
    private Spinner<Integer> spinnerQuantidade;

    @FXML
    private CheckBox chkGarantia;

    @FXML
    private CheckBox chkEntrega;

    @FXML
    private RadioButton rdbPix;

    @FXML
    private RadioButton rdbCredito;

    @FXML
    private RadioButton rdbDinheiro;

    @FXML
    private RadioButton rdbDebito;

    @FXML
    private ToggleGroup pagamento;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spinnerQuantidade.setValueFactory(valueFactory);
    }

    public void btnNovaVenda(ActionEvent actionEvent) {
        txtCliente.clear();
        txtProduto.clear();
        txtValor.clear();

        spinnerQuantidade.getValueFactory().setValue(1);

        chkEntrega.setSelected(false);
        chkGarantia.setSelected(false);

        pagamento.selectToggle(null);

        txtCliente.requestFocus();
    }

    public void btnCalcular(ActionEvent actionEvent) {

        nomeCliente = txtCliente.getText().trim();
        nomeProduto = txtProduto.getText().trim();
        quantidade = spinnerQuantidade.getValue();

        if (nomeCliente.isEmpty() || nomeProduto.isEmpty() || txtValor.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os campos!");
            alert.showAndWait();
            return;
        }

        if (quantidade <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("A quantidade deve ser maior que zero!");
            alert.showAndWait();
            return;
        }

        try {
            valorProduto = Double.parseDouble(txtValor.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Digite um valor válido!");
            alert.showAndWait();
            return;
        }

        if (valorProduto <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("O valor do produto deve ser maior que zero!");
            alert.showAndWait();
            return;
        }

        if (pagamento.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma forma de pagamento!");
            alert.showAndWait();
            return;
        }

        double subtotal = valorProduto * quantidade;

        if (chkEntrega.isSelected()) {
            subtotal += 25.00;
        }

        double desconto = 0;
        String formaPagamento = "";

        if (rdbPix.isSelected()) {
            formaPagamento = "Pix";
            desconto += 8;
        } else if (rdbCredito.isSelected()) {
            formaPagamento = "Crédito";
        } else if (rdbDebito.isSelected()) {
            formaPagamento = "Débito";
            desconto += 3;
        } else if (rdbDinheiro.isSelected()) {
            formaPagamento = "Dinheiro";
            desconto += 10;
        }

        if (subtotal > 1000) {
            desconto += 2;
        }

        if (desconto > 15) {
            desconto = 15;
        }

        double total = subtotal * (1 - desconto / 100);

        if (chkGarantia.isSelected()) {
            total *= 1.05;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Venda");
        alert.setHeaderText("Resumo da Venda");
        alert.setContentText(
                "Cliente: " + nomeCliente +
                        "\nProduto: " + nomeProduto +
                        "\nValor Unitário: R$ " + String.format("%.2f", valorProduto) +
                        "\nQuantidade: " + quantidade +
                        "\nForma de Pagamento: " + formaPagamento +
                        "\nEntrega: " + (chkEntrega.isSelected() ? "Sim" : "Não") +
                        "\nGarantia: " + (chkGarantia.isSelected() ? "Sim" : "Não") +
                        "\nDesconto: " + desconto + "%" +
                        "\nTotal: R$ " + String.format("%.2f", total)
        );
        alert.showAndWait();
    }

    public void btnSair(ActionEvent actionEvent) {
        System.exit(0);
    }
}