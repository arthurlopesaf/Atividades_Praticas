package com.example.orcamento_de_computadores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

import static com.example.orcamento_de_computadores.ValoresComponentes.*;

public class TelaPrincipalController {

    @FXML
    private TextField txtCliente;

    @FXML
    private ComboBox<String> cboPerfil;

    @FXML
    private ComboBox<String> cboProcessador;

    @FXML
    private ComboBox<String> cboPlacaMae;

    @FXML
    private ComboBox<String> cboMemoriaRam;

    @FXML
    private ComboBox<String> cboSsd;

    @FXML
    private ComboBox<String> cboPlacaVideo;

    @FXML
    private ComboBox<String> cboFonte;

    @FXML
    private ComboBox<String> cboGabinete;

    @FXML
    private CheckBox chkMonitor;

    @FXML
    private CheckBox chkTecladoMouse;

    @FXML
    private CheckBox chkHeadset;

    @FXML
    private Spinner<Integer> Desconto;

    @FXML
    public void initialize() {

        cboProcessador.getItems().addAll(
                "Processador básico",
                "Processador intermediário",
                "Processador avançado",
                "Processador de alto desempenho"
        );

        cboPlacaMae.getItems().addAll(
                "Placa-mãe básica",
                "Placa-mãe intermediária",
                "Placa-mãe avançada"
        );

        cboMemoriaRam.getItems().addAll(
                "8 GB",
                "16 GB",
                "32 GB"
        );

        cboSsd.getItems().addAll(
                "SSD 240 GB",
                "SSD 500 GB",
                "SSD 1 TB"
        );

        cboPlacaVideo.getItems().addAll(
                "Vídeo integrado",
                "Dedicada básica",
                "Dedicada intermediária"
        );

        cboFonte.getItems().addAll(
                "Fonte 500 W",
                "Fonte 650 W",
                "Fonte 750 W"
        );

        cboGabinete.getItems().addAll(
                "Gabinete básico",
                "Gabinete intermediário",
                "Gabinete gamer"
        );

        cboPerfil.getItems().addAll(
                "Administrativo",
                "Professor de Tecnologia",
                "Desenvolvedor de Software",
                "Desenvolvedor de Jogos"

        );
        Desconto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0));
    }

    public void btnCalcularRecomendacao(ActionEvent actionEvent) {

        if (cboPerfil.getValue() == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Perfil");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecione um perfil antes de carregar a recomendação.");
            alerta.showAndWait();
            return;
        }

        PerfilComputador perfil = null;

        switch (cboPerfil.getValue()) {

            case "Administrativo":
                perfil = Perfis.ADMINISTRATIVO;
                break;

            case "Professor de Tecnologia":
                perfil = Perfis.PROFESSOR_TECNOLOGIA;
                break;

            case "Desenvolvedor de Software":
                perfil = Perfis.DESENVOLVEDOR_SOFTWARE;
                break;

            case "Desenvolvedor de Jogos":
                perfil = Perfis.DESENVOLVEDOR_JOGOS;
                break;
        }

        if (perfil != null) {

            cboProcessador.setValue(perfil.getProcessador());
            cboPlacaMae.setValue(perfil.getPlacaMae());
            cboMemoriaRam.setValue(perfil.getMemoria());
            cboSsd.setValue(perfil.getSsd());
            cboPlacaVideo.setValue(perfil.getPlacaVideo());
            cboFonte.setValue(perfil.getFonte());
            cboGabinete.setValue(perfil.getGabinete());
        }
    }

    public void btnOrcamento(ActionEvent actionEvent) {

        if (txtCliente.getText().isEmpty()
                || cboProcessador.getValue() == null
                || cboPlacaMae.getValue() == null
                || cboMemoriaRam.getValue() == null
                || cboSsd.getValue() == null
                || cboPlacaVideo.getValue() == null
                || cboFonte.getValue() == null
                || cboGabinete.getValue() == null) {

            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos obrigatórios");
            alerta.setHeaderText(null);
            alerta.setContentText("Preencha todos os campos antes de calcular.");
            alerta.showAndWait();

            return;
        }

        double subtotal = valorProcessador()
                + valorMemoriaRam()
                + valorSsd()
                + valorGabinete()
                + valorOpcionais()
                + valorPlacaMae()
                + valorPlacaVideo()
                + valorFonte();

        double taxaMontagem = 10.0;

        double valorMontagem = subtotal * taxaMontagem / 100;

        int porcentagemDesconto = Desconto.getValue();

        double valorDesconto = subtotal * porcentagemDesconto / 100;

        double totalFinal = subtotal + valorMontagem - valorDesconto;

        String analise = analisarConfiguracao();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Orçamento");
        alert.setHeaderText("Resultado do orçamento");

        alert.setContentText(
                "Cliente: " + txtCliente.getText() +

                        "\n----------------------------" +

                        "\nSubtotal: R$ " + String.format("%.2f", subtotal) +

                        "\nMontagem (10%): R$ " + String.format("%.2f", valorMontagem) +

                        "\nDesconto: " + porcentagemDesconto + "%" +

                        "\nValor descontado: R$ " + String.format("%.2f", valorDesconto) +

                        "\n----------------------------" +

                        "\nTotal Final: R$ " + String.format("%.2f", totalFinal) +

                        "\n\n" + analise
        );

        alert.showAndWait();
    }

    public void btnLimpar(ActionEvent actionEvent) {

        txtCliente.clear();

        cboPerfil.getSelectionModel().clearSelection();
        cboProcessador.getSelectionModel().clearSelection();
        cboPlacaMae.getSelectionModel().clearSelection();
        cboMemoriaRam.getSelectionModel().clearSelection();
        cboSsd.getSelectionModel().clearSelection();
        cboPlacaVideo.getSelectionModel().clearSelection();
        cboFonte.getSelectionModel().clearSelection();
        cboGabinete.getSelectionModel().clearSelection();

        chkMonitor.setSelected(false);
        chkTecladoMouse.setSelected(false);
        chkHeadset.setSelected(false);

        Desconto.getValueFactory().setValue(0);
    }

    private double valorProcessador() {
        switch (cboProcessador.getValue()) {
            case "Processador básico":
                return PROCESSADOR_BASICO;
            case "Processador intermediário":
                return PROCESSADOR_INTERMEDIARIO;
            case "Processador avançado":
                return PROCESSADOR_AVANCADO;
            case "Processador de alto desempenho":
                return PROCESSADOR_ALTO_DESEMPENHO;
            default:
                return 0;
        }
    }

    private double valorPlacaMae() {
        switch (cboPlacaMae.getValue()) {
            case "Placa-mãe básica":
                return PLACA_MAE_BASICA;
            case "Placa-mãe intermediária":
                return PLACA_MAE_INTERMEDIARIA;
            case "Placa-mãe avançada":
                return PLACA_MAE_AVANCADA;
            default:
                return 0;
        }
    }

    private double valorMemoriaRam() {
        switch (cboMemoriaRam.getValue()) {
            case "8 GB":
                return MEMORIA_8GB;
            case "16 GB":
                return MEMORIA_16GB;
            case "32 GB":
                return MEMORIA_32GB;
            default:
                return 0;
        }
    }

    private double valorSsd() {
        switch (cboSsd.getValue()) {
            case "SSD 240 GB":
                return SSD_240GB;
            case "SSD 500 GB":
                return SSD_500GB;
            case "SSD 1 TB":
                return SSD_1TB;
            default:
                return 0;
        }
    }

    private double valorPlacaVideo() {
        switch (cboPlacaVideo.getValue()) {
            case "Vídeo integrado":
                return VIDEO_INTEGRADO;
            case "Dedicada básica":
                return VIDEO_DEDICADA_BASICA;
            case "Dedicada intermediária":
                return VIDEO_DEDICADA_INTERMEDIARIA;
            default:
                return 0;
        }
    }

    private double valorFonte() {
        switch (cboFonte.getValue()) {
            case "Fonte 500 W":
                return FONTE_500W;
            case "Fonte 650 W":
                return FONTE_650W;
            case "Fonte 750 W":
                return FONTE_750W;
            default:
                return 0;
        }
    }

    private double valorGabinete() {
        switch (cboGabinete.getValue()) {
            case "Gabinete básico":
                return GABINETE_BASICO;
            case "Gabinete intermediário":
                return GABINETE_INTERMEDIARIO;
            case "Gabinete gamer":
                return GABINETE_GAMER;
            default:
                return 0;
        }
    }

    private double valorOpcionais() {
        double total = 0;

        if (chkMonitor.isSelected()) {
            total += MONITOR_24;
        }

        if (chkTecladoMouse.isSelected()) {
            total += TECLADO_MOUSE;
        }

        if (chkHeadset.isSelected()) {
            total += HEADSET;
        }

        return total;
    }

    private String analisarConfiguracao() {

        ArrayList<String> problemas = new ArrayList<>();

        String perfil = cboPerfil.getValue();

        if (perfil == null) {
            return "Nenhum perfil selecionado.";
        }

        switch (perfil) {

            case "Administrativo":

                if (!cboProcessador.getValue().equals("Processador básico") &&
                        !cboProcessador.getValue().equals("Processador intermediário") &&
                        !cboProcessador.getValue().equals("Processador avançado") &&
                        !cboProcessador.getValue().equals("Processador de alto desempenho")) {

                    problemas.add("Processador não atende ao mínimo.");
                }

                if (!cboMemoriaRam.getValue().equals("8 GB") &&
                        !cboMemoriaRam.getValue().equals("16 GB") &&
                        !cboMemoriaRam.getValue().equals("32 GB")) {

                    problemas.add("Memória RAM abaixo do recomendado.");
                }

                break;

            case "Professor de Tecnologia":

                if (cboMemoriaRam.getValue().equals("8 GB")) {
                    problemas.add("Recomendado utilizar 16 GB de RAM.");
                }

                if (cboSsd.getValue().equals("SSD 240 GB")) {
                    problemas.add("Recomendado utilizar SSD de 500 GB.");
                }

                break;

            case "Desenvolvedor de Software":

                if (cboMemoriaRam.getValue().equals("8 GB")) {
                    problemas.add("Memória RAM insuficiente.");
                }

                if (cboSsd.getValue().equals("SSD 240 GB")) {
                    problemas.add("SSD abaixo do recomendado.");
                }

                if (cboPlacaVideo.getValue().equals("Vídeo integrado")) {
                    problemas.add("Recomendação: utilizar placa de vídeo dedicada.");
                }

                break;

            case "Desenvolvedor de Jogos":

                if (!cboProcessador.getValue().equals("Processador de alto desempenho")) {
                    problemas.add("Processador de alto desempenho obrigatório.");
                }

                if (!cboMemoriaRam.getValue().equals("32 GB")) {
                    problemas.add("32 GB de RAM é obrigatório.");
                }

                if (!cboSsd.getValue().equals("SSD 1 TB")) {
                    problemas.add("SSD de 1 TB é recomendado.");
                }

                if (!cboPlacaVideo.getValue().equals("Dedicada intermediária")) {
                    problemas.add("Placa de vídeo dedicada intermediária obrigatória.");
                }

                break;
        }

        if (problemas.isEmpty()) {

            return "Situação: ADEQUADA\n\n" +
                    "Configuração adequada para o perfil selecionado.";

        } else {

            if (perfil.equals("Desenvolvedor de Jogos")
                    && (problemas.contains("Processador de alto desempenho obrigatório.")
                    || problemas.contains("32 GB de RAM é obrigatório.")
                    || problemas.contains("Placa de vídeo dedicada intermediária obrigatória."))) {

                return "Situação: INCOMPATÍVEL\n\n" +

                        "O orçamento foi calculado, porém a configuração não é recomendada para o perfil.\n\n" +

                        "Problemas:\n" +

                        String.join("\n", problemas);

            } else {

                return "Situação: ADEQUADA COM RESSALVA\n\n" +

                        "A configuração atende, mas possui uma recomendação de melhoria.\n\n" +

                        "Recomendações:\n" +

                        String.join("\n", problemas);
            }
        }
    }
}

