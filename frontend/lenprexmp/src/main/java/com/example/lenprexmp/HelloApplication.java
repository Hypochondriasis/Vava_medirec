package com.example.lenprexmp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Locale locale = AppSettings.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"), bundle);
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("MediRec - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}