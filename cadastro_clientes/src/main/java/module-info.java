module com.example.cadastro_clientes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.cadastro_clientes to javafx.fxml;
    exports com.example.cadastro_clientes;
}