module com.example.orcamento_de_computadores {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.orcamento_de_computadores to javafx.fxml;
    exports com.example.orcamento_de_computadores;
}