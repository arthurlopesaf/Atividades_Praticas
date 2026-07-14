package com.example.orcamento_de_computadores;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TelaPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 460, 600);
        stage.setTitle("orçamento de computadores");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
