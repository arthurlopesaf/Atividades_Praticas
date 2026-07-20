module controleestoque.controle_estoque {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports Launcher;
    opens Launcher to javafx.fxml;

    exports Controllers;
    opens Controllers to javafx.fxml;

    exports Models;
    exports DAO;
    exports Services;
}