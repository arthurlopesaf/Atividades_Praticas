module com.example.sistema_de_vendas {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.sistema_de_vendas to javafx.fxml;
    exports com.example.sistema_de_vendas;
}