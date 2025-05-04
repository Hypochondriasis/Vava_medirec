package org.medirec.medirec.frontend.controller;

import org.medirec.medirec.backend.model.Role;
import org.medirec.medirec.backend.model.User;
import org.medirec.medirec.backend.service.DatabaseService;
import org.medirec.medirec.backend.util.JwtUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.medirec.medirec.backend.model.LoginResponse;
import org.medirec.medirec.backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    //FXML elements
    @FXML
    private AnchorPane root;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox loginPane;
    @FXML
    private VBox resetPane;
    @FXML
    private ImageView imageLayout;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private MFXButton loginButton;
    @FXML
    private Label forgotPasswordLabel;
    @FXML
    private MFXButton langButton;
    @FXML
    private TextField nameResetField;
    @FXML
    private TextField emailResetField;
    @FXML
    private Button passwordResetButton;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Label backToLoginLabel;
    @FXML
    private MFXButton langButton1;

    //Resource bundle
    private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        resetPane.setVisible(false);
        resetPane.setManaged(false);
    }

    @FXML
    private void handleLogin() {
        //Clear any previous error styles
        usernameField.setStyle(null);
        passwordField.setStyle(null);

        String email = usernameField.getText().trim();
        String pw = passwordField.getText();

        //Validate mandatory fields
        boolean hasError = false;
        if (email.isEmpty()) {
            usernameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            hasError = true;
        }
        if (pw.isEmpty()) {
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            hasError = true;
        }
        if (hasError) {
            showAlert(Alert.AlertType.WARNING, bundle.getString("login.error"));
            return;
        }

        //Delegate to AuthService
        LoginResponse resp = AuthService.login(email, pw);
        if (resp.isSuccess()) {
            logger.info("Login successful, token={}", resp.getToken());
            try {
                String token = resp.getToken();
                System.out.println(token);

                // Get the User object from the token
                User currentUser;
                try {
                    currentUser = JwtUtil.getUserFromToken();
                    logger.info("Retrieved user: {}", currentUser);
                    currentUser.setRole(DatabaseService.getOne(Role.class, currentUser.getRole_id()));
                } catch (Exception e) {
                    logger.error("Failed to get user from token", e);
                    showAlert(Alert.AlertType.WARNING, bundle.getString("login.error"));
                    return;
                }

                if(currentUser.getRole().getName().equals("Doctor")) {
                    //1) Load the next FXML
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/org/medirec/medirec/frontend/fxml/main.fxml"),
                            bundle
                    );

                    // Pass the user to the next controller
                    // This requires your MainController to have a setUser method
                    Parent dashboardRoot = loader.load();
                    MainController mainController = loader.getController();
                    if (mainController != null) {
                        mainController.setUser(currentUser);
                        mainController.setUserInfo();
                    }

                    //2) Wrap in a Scene
                    Scene dashboardScene = new Scene(dashboardRoot);

                    //3) Grab the current Stage via any node
                    Stage stage = (Stage) loginButton.getScene().getWindow();

                    //4) Set and show
                    stage.setScene(dashboardScene);
                    stage.setTitle(bundle.getString("login.title"));
                    stage.show();
                }

            } catch (IOException e) {
                logger.error("Failed to load dashboard.fxml", e);
                showAlert(Alert.AlertType.WARNING, bundle.getString("login.error"));
            }
        } else {
            showAlert(Alert.AlertType.WARNING, bundle.getString("login.error"));
        }
    }

    //Helper function to show alerts
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type, msg);
        alert.setHeaderText(null);
        alert.initOwner(root.getScene().getWindow());
        alert.showAndWait();
    }

    @FXML
    private void onLanguageSwitch(){
        // Toggle to local
        Locale current = AppSettings.getLocale();
        Locale next = current.getLanguage().equals("sk") ? Locale.ENGLISH : new Locale("sk", "SK");
        AppSettings.setLocale(next);

        // Updating the UI text without needing to reload the pane
        updateUILanguage(next);
    }

    private void updateUILanguage(Locale locale) {
        try {
            // Getting the new resource bundle
            ResourceBundle newBundle = ResourceBundle.getBundle("org.medirec.medirec.frontend.messages", locale);
            // Updating login pane elements
            if (usernameField != null) {
                usernameField.setPromptText(newBundle.getString("login.username"));
            }
            if (passwordField != null) {
                passwordField.setPromptText(newBundle.getString("login.password"));
            }
            if (loginButton != null) {
                loginButton.setText(newBundle.getString("login.button"));
            }
            if (forgotPasswordLabel != null) {
                forgotPasswordLabel.setText(newBundle.getString("login.forgot"));
            }
            if (langButton != null) {
                langButton.setText(newBundle.getString("login.lang"));
            }
            // Updating reset pane elements
            if (emailResetField != null) {
                emailResetField.setPromptText(newBundle.getString("email"));
            }
            if (nameResetField != null) {
                nameResetField.setPromptText(newBundle.getString("name"));
            }
            if (newPasswordField != null) {
                newPasswordField.setPromptText(newBundle.getString("reset.newpass"));
            }
            if (backToLoginLabel != null) {
                backToLoginLabel.setText(newBundle.getString("reset.back"));
            }
            if (passwordResetButton != null) {
                passwordResetButton.setText(newBundle.getString("reset.back"));
            }
            if (langButton1 != null) {
                langButton1.setText(newBundle.getString("login.lang"));
            }
        }catch (java.util.MissingResourceException e){
            logger.error("Missing resource key: {}", e.getKey());
        }catch (Exception e) {
            logger.error("Failed to update UI language", e);
        }
    }

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