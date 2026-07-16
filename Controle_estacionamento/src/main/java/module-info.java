module com.example.controle_estacionamento {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.controle_estacionamento to javafx.fxml;
    exports com.example.controle_estacionamento;
}