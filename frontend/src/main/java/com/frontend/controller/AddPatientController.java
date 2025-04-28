package com.frontend.controller;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddPatientController {

	private ResourceBundle bundle;

	private HomeController parentController;

	@FXML
	private TextField nameField;

	@FXML
	private DatePicker birthdatePicker;

	@FXML
	private ComboBox<String> genderComboBox;

	@FXML
	private TextField emailField;

	@FXML
	private TextField phoneField;

	@FXML
	private TextField birthCityField;

	@FXML
	private TextField currentCityField;

	@FXML
	private TextField streetField;

	@FXML
	private TextField postalCodeField;

	@FXML
	private TextField insuranceNumberField;

	@FXML
	private TextField bloodPressureField;

	@FXML
	private TextField bmiField;

	@FXML
	private TextField weightField;

	@FXML
	private TextField heightField;

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
		if (genderComboBox != null) {
			genderComboBox.getItems().clear();
			genderComboBox
				.getItems()
				.addAll(getString("gender.male"), getString("gender.female"));
		}
	}

	@FXML
	private void initialize() {}

	@FXML
	private void submit() {
		if (!areRequiredFieldsValid()) {
			showAlert(
				AlertType.ERROR,
				getString("alert.error.title"),
				getString("alert.error.required_fields")
			);

			return;
		}

		try {
			Patient patient = new Patient(
				nameField.getText(),
				birthdatePicker.getValue(),
				genderComboBox.getValue(),
				emailField.getText(),
				phoneField.getText(),
				birthCityField.getText(),
				currentCityField.getText(),
				streetField.getText(),
				postalCodeField.getText(),
				insuranceNumberField.getText(),
				emptyIfBlank(bloodPressureField),
				parseDoubleOrNull(bmiField),
				parseDoubleOrNull(weightField),
				parseDoubleOrNull(heightField)
			);

			// Môžeš tu pridať kód na uloženie pacienta do DB alebo inej logiky
			showAlert(
				AlertType.INFORMATION,
				getString("alert.success.title"),
				getString("alert.success.patient_added")
			);

			clearForm();
		} catch (Exception e) {
			showAlert(
				AlertType.ERROR,
				getString("alert.error.title"),
				MessageFormat.format(
					getString("alert.error.processing"),
					e.getMessage()
				)
			);
		}
	}

	@FXML
	private void cancel() {
		Stage stage = (Stage) nameField.getScene().getWindow();
		stage.close();
	}

	private boolean areRequiredFieldsValid() {
		return (
			!isEmpty(nameField) &&
			birthdatePicker.getValue() != null &&
			genderComboBox.getValue() != null &&
			!isEmpty(emailField) &&
			!isEmpty(phoneField) &&
			!isEmpty(birthCityField) &&
			!isEmpty(currentCityField) &&
			!isEmpty(streetField) &&
			!isEmpty(postalCodeField) &&
			!isEmpty(insuranceNumberField)
		);
	}

	private boolean isEmpty(TextField field) {
		return field.getText() == null || field.getText().trim().isEmpty();
	}

	private String emptyIfBlank(TextField field) {
		String text = field.getText();
		return (text == null || text.trim().isEmpty()) ? null : text.trim();
	}

	private Double parseDoubleOrNull(TextField field) {
		try {
			String text = field.getText();
			return (text == null || text.trim().isEmpty())
				? null
				: Double.parseDouble(text.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private void clearForm() {
		nameField.clear();
		birthdatePicker.setValue(null);
		genderComboBox.setValue(null);
		emailField.clear();
		phoneField.clear();
		birthCityField.clear();
		currentCityField.clear();
		streetField.clear();
		postalCodeField.clear();
		insuranceNumberField.clear();
		bloodPressureField.clear();
		bmiField.clear();
		weightField.clear();
		heightField.clear();
	}

	private void showAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void setParentController(HomeController controller) {
		this.parentController = controller;
	}

	private String getString(String key) {
		return bundle != null ? bundle.getString(key) : key;
	}
}
