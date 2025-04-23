package com.frontend.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainController {

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

	private Locale currentLocale = Locale.getDefault();

	@FXML
	public void initialize() {
		loadView("home.fxml");

		currentLocale = AppSettings.getLocale();
		languageButton.setText(resources.getString("button.switchLanguage"));

		// Fiktívne nastavenie mena (bude neskôr z DB)
		setUserInfo();
	}

	public void setUserInfo() {
		nameLabel.setText("Name");
		// avatar.setImage(...); // Načítaš z DB alebo URL neskôr
	}

	private void loadView(String fxmlPath) {
		try {
			System.out.println("Loading: " + fxmlPath);
			FXMLLoader loader = new FXMLLoader(
				getClass().getResource(fxmlPath),
				resources
			);
			Node content = loader.load();
			mainContent.getChildren().setAll(content);
		} catch (IOException e) {
			System.err.println("Chyba pri načítaní FXML: " + fxmlPath);
			e.printStackTrace();
		}
	}

	@FXML
	private void onHomeClicked() {
		loadView("home.fxml");
	}

	@FXML
	private void onSettingsClicked() {
		//pridaj nejake overenie aby sa tu dostal len admin
		loadView("settings.fxml");
	}

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
		loadView("mRecords.fxml");
	}

	@FXML
	private void onLanguageSwitch(ActionEvent event) throws IOException {
		Locale currentLocale = AppSettings.getLocale();

		Locale newLocale = currentLocale.getLanguage().equals("sk")
			? new Locale("en")
			: new Locale("sk");

		AppSettings.setLocale(newLocale);

		ResourceBundle bundle = ResourceBundle.getBundle("messages", newLocale);
		FXMLLoader loader = new FXMLLoader(
			getClass().getResource("main.fxml"),
			bundle
		);
		Parent root = loader.load();

		Stage stage = (Stage) languageButton.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
	}
}
