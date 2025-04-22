package org.medirec.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController {
// v podstate login controller
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label forgotPasswordLabel;

    @FXML
    private void initialize() {

    }

    @FXML
    private void onLanguageSwitch(ActionEvent event) throws IOException {
        Locale currentLocale = AppSettings.getLocale();
        Locale newLocale = currentLocale.getLanguage().equals("sk") ? new Locale("en") : new Locale("sk");

        AppSettings.setLocale(newLocale);

        ResourceBundle bundle = ResourceBundle.getBundle("messages", newLocale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"), bundle);
        Parent root = loader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        // Tu pôjde tvoja autentifikácia (napr. kontrola používateľa z DB)
        String username="admin";
        String password="admin123";
        if (usernameField.getText().equals(username) && passwordField.getText().equals(password)) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"), bundle);
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MediRec - Domov");
            stage.show();
        }
        else{
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
            forgotPasswordLabel.setText(bundle.getString("login.error"));
            forgotPasswordLabel.setStyle("-fx-text-fill: red;");

        }
    }
    @FXML
    private VBox loginPane;

    @FXML
    private VBox resetPane;

    @FXML
    private TextField nameResetField;
    @FXML
    private TextField emailResetField;
    @FXML
    private PasswordField newPasswordField;

    @FXML
    private void showResetPane() {
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        resetPane.setVisible(true);
        resetPane.setManaged(true);
    }

    @FXML
    private void showLoginPane() {
        resetPane.setVisible(false);
        resetPane.setManaged(false);
        loginPane.setVisible(true);
        loginPane.setManaged(true);
    }

    @FXML
    private void handlePasswordReset() {
        String name = nameResetField.getText();
        String email = emailResetField.getText();
        String newPassword = newPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
            // zobraziť chybu
            System.out.println("Všetky polia sú povinné!");
            return;
        }

        // Tu môžeš spraviť logiku na overenie a uloženie nového hesla
        System.out.println("Heslo pre " + name + " bolo obnovené.");

        // Späť na login
        showLoginPane();
    }

}
