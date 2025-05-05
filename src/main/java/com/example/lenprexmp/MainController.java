package com.example.lenprexmp;

import com.example.backend.model.User;
import com.example.backend.util.JwtUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {
    private User prihlaseny_user;
    @FXML
    private Button languageButton;
    @FXML
    private ImageView avatar;
    @FXML
    private Label nameLabel;
    @FXML
    private ResourceBundle resources;
    @FXML
    private BorderPane root;
    @FXML
    private StackPane mainContent;
    @FXML private Button homeButton;
    @FXML private Button calendarButton;
    @FXML private Button patientsButton;
    @FXML private Button reportsButton;
    @FXML private Button addNurseButton;
    @FXML private Button settingsButton;


    private Locale currentLocale = Locale.getDefault();

    @FXML
    public void initialize() {



        currentLocale = AppSettings.getLocale();
        languageButton.setText(resources.getString("button.switchLanguage"));

        // Fiktívne nastavenie mena (bude neskôr z DB)
        setUserInfo();
    }

    public void setUserInfo() {
        try {
            prihlaseny_user = JwtUtil.getUserFromToken();
            nameLabel.setText(prihlaseny_user.getName());
            if (prihlaseny_user.getRole_id() == 1) {
                // Show only "Pridať lekára" button
                homeButton.setVisible(false);
                calendarButton.setVisible(false);
                patientsButton.setVisible(false);
                reportsButton.setVisible(false);
                settingsButton.setVisible(true);
                addNurseButton.setVisible(false);
            }
            else if (prihlaseny_user.getRole_id() == 2){
                homeButton.setVisible(true);
                calendarButton.setVisible(true);
                patientsButton.setVisible(true);
                reportsButton.setVisible(true);
                settingsButton.setVisible(false);
                addNurseButton.setVisible(true);
                loadView("Home.fxml");

            }else {
                // Show all buttons for other roles
                homeButton.setVisible(true);
                calendarButton.setVisible(true);
                patientsButton.setVisible(true);
                reportsButton.setVisible(true);
                settingsButton.setVisible(false);
                addNurseButton.setVisible(false);
                loadView("Home.fxml");// optional: hide from non-doctors
            }
        }
        catch (Exception e){
            System.err.println("User retrieval failed: " + e.getMessage());
            redirectToLogin();
        }
        // avatar.setImage(...); // Načítaš z DB alebo URL neskôr
    }
    private void redirectToLogin() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"), bundle);
            Parent loginRoot = loader.load();

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Platform.exit(); // fallback if login screen fails
        }
    }

    private void loadView(String fxmlPath) {
        try {
            System.out.println("Loading: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath), resources);
            Node content = loader.load();
            mainContent.getChildren().setAll(content);
        } catch (IOException e) {
            System.err.println("Chyba pri načítaní FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }



    @FXML
    private void onHomeClicked() {
        loadView("Home.fxml");
    }
    @FXML
    private void onSettingsClicked(){
        //pridaj nejake overenie aby sa tu dostal len admin
        loadView("settings.fxml");}
    @FXML
    private void onCalendarClicked() {
        loadView("calendar.fxml");
    }

    @FXML
    private void onPatientsClicked() {
        loadView("patients.fxml");
    }

    @FXML
    private void onReportsClicked() {
        loadView("ExportRecordsView.fxml");
    }
    @FXML
    private void onAddNurseClicked() {
        //je potrebne pridat nejake overeanie aby mal k tomu pristup lekar


        loadView("nurseAdd.fxml");
    }

    @FXML
    private void onLanguageSwitch(ActionEvent event) throws IOException {
        Locale currentLocale = AppSettings.getLocale();

        Locale newLocale = currentLocale.getLanguage().equals("sk")
                ? new Locale("en")
                : new Locale("sk");

        AppSettings.setLocale(newLocale);

        ResourceBundle bundle = ResourceBundle.getBundle("messages", newLocale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"), bundle);
        Parent root = loader.load();

        Stage stage = (Stage) languageButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
