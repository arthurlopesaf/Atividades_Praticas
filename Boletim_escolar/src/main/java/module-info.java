module com.example.boletim_escolar {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.boletim_escolar to javafx.fxml;
    exports com.example.boletim_escolar;
}