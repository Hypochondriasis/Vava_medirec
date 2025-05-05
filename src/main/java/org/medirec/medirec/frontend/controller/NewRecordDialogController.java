package org.medirec.medirec.frontend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewRecordDialogController {

	@FXML
	private TextField dateField;

	@FXML
	private TextField examTypeField;

	@FXML
	private VBox medsContainer;

	@FXML
	private VBox diagnosisContainer;

	// tu treba nahrat choroby a lieky
	private final ObservableList<String> availableMeds =
		FXCollections.observableArrayList(
			"Paracetamol",
			"Ibuprofen",
			"Amoxicillin",
			"Aspirin",
			"Metformin"
		);

	private final ObservableList<String> availableDiagnoses =
		FXCollections.observableArrayList(
			"Hypertenzia",
			"Diabetes",
			"Astma",
			"Covid-19",
			"Chrípka"
		);

	private MedicalRecordsController parentController;

	public void setParentController(MedicalRecordsController controller) {
		this.parentController = controller;
	}

	@FXML
	public void initialize() {
		addMedField();
		addDiagnosisField();
	}

	@FXML
	private void onAddMedField() {
		addMedField();
	}

	@FXML
	private void onAddDiagnosisField() {
		addDiagnosisField();
	}

	private void addMedField() {
		ComboBox<String> medComboBox = new ComboBox<>(availableMeds);
		medComboBox.setEditable(true);
		ResourceBundle bundle = ResourceBundle.getBundle(
			"org.medirec.medirec.frontend.messages",
			AppSettings.getLocale()
		);
		medComboBox.setPromptText(bundle.getString("med.prompt"));
		medComboBox.setMaxWidth(Double.MAX_VALUE);
		medsContainer.getChildren().add(medComboBox);
	}

	private void addDiagnosisField() {
		ComboBox<String> diagnosisBox = new ComboBox<>(availableDiagnoses);
		diagnosisBox.setEditable(true);
		ResourceBundle bundle = ResourceBundle.getBundle(
			"org.medirec.medirec.frontend.messages",
			AppSettings.getLocale()
		);

		diagnosisBox.setPromptText(bundle.getString("diagnosis.prompt"));
		diagnosisBox.setMaxWidth(Double.MAX_VALUE);
		diagnosisContainer.getChildren().add(diagnosisBox);
	}

	@FXML
	private void onCancel() {
		Stage stage = (Stage) dateField.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void onAdd() {
		String datum = dateField.getText();
		String typ = examTypeField.getText();

		List<String> liekyList = new ArrayList<>();
		for (javafx.scene.Node node : medsContainer.getChildren()) {
			if (node instanceof ComboBox<?>) {
				ComboBox<?> comboBox = (ComboBox<?>) node;
				Object value = comboBox.getValue();
				if (value != null && !value.toString().trim().isEmpty()) {
					liekyList.add(value.toString());
				}
			}
		}

		List<String> diagnozyList = new ArrayList<>();
		for (javafx.scene.Node node : diagnosisContainer.getChildren()) {
			if (node instanceof ComboBox<?>) {
				ComboBox<?> comboBox = (ComboBox<?>) node;
				Object value = comboBox.getValue();
				if (value != null && !value.toString().trim().isEmpty()) {
					diagnozyList.add(value.toString());
				}
			}
		}

		String lieky = String.join(", ", liekyList);
		String diagnozy = String.join(", ", diagnozyList);

		if (parentController != null) {
			String fullNotes = "Typ: " + typ + "; Lieky: " + lieky + "; Diagnóza: " + diagnozy;
			parentController.addRecord(fullNotes);
		}

		Stage stage = (Stage) dateField.getScene().getWindow();
		stage.close();
	}
}
