package com.example.boletim_escolar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class TelaPrincipalController {

    String nomeAluno;
    String turma;
    String situacao;

    double nota1;
    double nota2;
    double nota3;
    double nota4;
    double media;

    int frequencia;

    boolean resultadoCalculado = false;

    @FXML
    private TextField txtAluno;

    @FXML
    private TextField txtTurma;

    @FXML
    private TextField txtNota1;

    @FXML
    private TextField txtNota2;

    @FXML
    private TextField txtNota3;

    @FXML
    private TextField txtNota4;

    @FXML
    private TextField txtResultadoAluno;

    @FXML
    private Spinner<Integer> spinnerFrequencia;

    @FXML
    private ListView<String> lstResultados;

    @FXML
    public void initialize() {
        spinnerFrequencia.setValueFactory(
                new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0)
        );
    }

    @FXML
    private void btnCalcular(ActionEvent event) {

        if (txtAluno.getText().isEmpty() || txtTurma.getText().isEmpty() ||
                txtNota1.getText().isEmpty() || txtNota2.getText().isEmpty() ||
                txtNota3.getText().isEmpty() || txtNota4.getText().isEmpty()) {

            txtResultadoAluno.setText("Preencha todos os campos!");
            return;
        }

        try {

            nomeAluno = txtAluno.getText();
            turma = txtTurma.getText();

            nota1 = Double.parseDouble(txtNota1.getText());
            nota2 = Double.parseDouble(txtNota2.getText());
            nota3 = Double.parseDouble(txtNota3.getText());
            nota4 = Double.parseDouble(txtNota4.getText());

            frequencia = spinnerFrequencia.getValue();

            if (nota1 < 0 || nota1 > 10 ||
                    nota2 < 0 || nota2 > 10 ||
                    nota3 < 0 || nota3 > 10 ||
                    nota4 < 0 || nota4 > 10) {

                txtResultadoAluno.setText("Notas devem estar entre 0 e 10!");
                return;
            }

            media = (nota1 + nota2 + nota3 + nota4) / 4;

            if (frequencia < 75) {

                situacao = "Reprovado por frequência";

            } else if (media >= 7) {

                situacao = "Aprovado";

            } else if (media >= 5) {

                situacao = "Recuperação";

            } else {

                situacao = "Reprovado por nota";
            }

            String mediaTexto = String.valueOf(media).replace(".", ",");

            txtResultadoAluno.setText(
                    "Aluno: " + nomeAluno +
                            " | Turma: " + turma +
                            " | Média: " + mediaTexto +
                            " | Frequência: " + frequencia + "%" +
                            " | Situação: " + situacao
            );

            resultadoCalculado = true;

        } catch (Exception e) {

            txtResultadoAluno.setText("Digite apenas números nas notas!");
        }
    }

    @FXML
    private void btnAdicionarResultado(ActionEvent event) {

        if (resultadoCalculado == false) {

            txtResultadoAluno.setText("Calcule o resultado primeiro!");
            return;
        }

        lstResultados.getItems().add(
                txtResultadoAluno.getText()
        );

    }

    @FXML
    private void btnGerarResumo(ActionEvent event) {

        int aprovados = 0;
        int recuperacao = 0;
        int reprovadosNota = 0;
        int reprovadosFrequencia = 0;

        for (String resultado : lstResultados.getItems()) {

            if (resultado.contains("Situação: Aprovado")) {

                aprovados++;

            } else if (resultado.contains("Situação: Recuperação")) {

                recuperacao++;

            } else if (resultado.contains("Situação: Reprovado por nota")) {

                reprovadosNota++;

            } else if (resultado.contains("Situação: Reprovado por frequência")) {

                reprovadosFrequencia++;
            }
        }

        String resumo =
                "Total de estudantes: " + lstResultados.getItems().size() +
                        "\nAprovados: " + aprovados +
                        "\nRecuperação: " + recuperacao +
                        "\nReprovados por nota: " + reprovadosNota +
                        "\nReprovados por frequência: " + reprovadosFrequencia;

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Resumo da Turma");
        alerta.setHeaderText("Resumo dos estudantes");
        alerta.setContentText(resumo);

        alerta.showAndWait();
    }

    @FXML
    private void btnLimparCampos(ActionEvent event) {
        txtAluno.clear();
        txtTurma.clear();
        txtNota1.clear();
        txtNota2.clear();
        txtNota3.clear();
        txtNota4.clear();
        txtResultadoAluno.clear();

        spinnerFrequencia.getValueFactory().setValue(0);

        resultadoCalculado = false;
    }

    @FXML
    private void btnLimparLista(ActionEvent event) {
        lstResultados.getItems().clear();
    }

    @FXML
    private void btnRemoverSelecao(ActionEvent event) {
        int indice = lstResultados.getSelectionModel().getSelectedIndex();

        if (indice >= 0) {
            lstResultados.getItems().remove(indice);
        }
    }
}