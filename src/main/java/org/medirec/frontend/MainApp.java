package org.medirec.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.medirec.frontend.controller.AppSettings;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Locale locale = AppSettings.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("org.medirec.frontend.messages", locale);

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("org/medirec/frontend/fxml/login.fxml"), bundle);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/medirec/frontend/fxml/login.fxml"), bundle);
        Scene scene = new Scene(loader.load());

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/medirec/frontend/images/wave.png"))));
        stage.setScene(scene);
        stage.setTitle("MediRec - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}