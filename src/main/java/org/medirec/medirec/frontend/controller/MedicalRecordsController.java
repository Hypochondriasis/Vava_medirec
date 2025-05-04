package org.medirec.medirec.frontend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MedicalRecordsController {

	@FXML
	private TextField searchField;

	@FXML
	private Label nameLabel;

	@FXML
	private TextArea basicInfo;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private VBox recordsContainer;

	private Map<String, Patient> fakeDatabase = new HashMap<>();
	private Patient currentPatient;

	@FXML
	private void initialize() {
		// Simulovaná databáza
		// Kompletný pacient
		Patient p1 = new Patient(
			"Ján Novák", // meno
			LocalDate.of(1979, 4, 12), // dátum narodenia
			"M", // pohlavie
			"jan.novak@example.com", // email
			"+421 911 123 456", // telefón
			"Bratislava", // miesto narodenia
			"Trnava", // aktuálne bydlisko
			"Hlavná 123", // ulica
			"91701", // PSČ
			"AB123456", // číslo poistenca
			"130/85", // krvný tlak
			24.5, // BMI
			78.0, // váha
			178.0 // výška
		);

		// Diagnostické záznamy
		p1.addRecord(
			new MedicalRecord(
				"01.01.2023",
				"Kontrola krvného tlaku",
				"Prestarium, Magnézium",
				"Záznam OK"
			)
		);
		p1.addRecord(
			new MedicalRecord(
				"15.02.2023",
				"EKG vyšetrenie",
				"Bez liekov",
				"Záznam OK"
			)
		);

		// Pridanie do fake databázy
		fakeDatabase.put("Ján Novák", p1);
		// ak chceš, môžeš tu predvyplniť niečo na test
	}

	@FXML
	private void onSearchClicked() {
		String meno = searchField.getText().trim();
		Patient found = fakeDatabase.get(meno);
		ResourceBundle bundle = ResourceBundle.getBundle(
			"messages",
			AppSettings.getLocale()
		);
		if (found != null) {
			currentPatient = found;
			nameLabel.setText(found.getName());
			basicInfo.setText(found.getBasicInfo(bundle));

			renderPatientRecords(found);
		} else {
			nameLabel.setText(bundle.getString("patient.not.found"));
			basicInfo.clear();
			recordsContainer.getChildren().clear();
		}
	}

	public void addRecord(
		String datum,
		String typ,
		String lieky,
		String diagnosis
	) {
		if (currentPatient != null) {
			MedicalRecord record = new MedicalRecord(
				datum,
				typ,
				lieky,
				diagnosis
			);
			currentPatient.addRecord(record);
			renderRecordOnTop(record); // iba pridať jeden
		}
	}

	private void renderPatientRecords(Patient patient) {
		recordsContainer.getChildren().clear();
		for (MedicalRecord r : patient.getRecords()) {
			renderRecord(r);
		}
	}

	private void renderRecordOnTop(MedicalRecord r) {
		VBox recordBox = new VBox();
		recordBox.setSpacing(5);
		recordBox.setStyle(
			"-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;"
		);

		ResourceBundle bundle = ResourceBundle.getBundle(
			"messages",
			AppSettings.getLocale()
		);
		Label dateLabel = new Label(
			bundle.getString("exam.date.label") + ": " + r.getDate()
		);
		Label typeLabel = new Label(r.getExamType());
		Label medsLabel = new Label(r.getMeds());
		Label diagnosis = new Label(r.getDiagnosis());
		VBox detailText = new VBox(typeLabel, medsLabel, diagnosis);
		detailText.setSpacing(3);

		recordBox.getChildren().addAll(dateLabel, detailText);
		recordsContainer.getChildren().add(0, recordBox); // pridá na začiatok
		recordBox.getStyleClass().add("record-box");
		dateLabel.getStyleClass().add("date-label");
		typeLabel.getStyleClass().add("exam-type-label");
		medsLabel.getStyleClass().add("meds-label");
	}

	private void renderRecord(MedicalRecord r) {
		VBox recordBox = new VBox();
		recordBox.setSpacing(5);
		recordBox.setStyle(
			"-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;"
		);

		ResourceBundle bundle = ResourceBundle.getBundle(
			"messages",
			AppSettings.getLocale()
		);
		Label dateLabel = new Label(
			bundle.getString("exam.date.label") + ": " + r.getDate()
		);

		Label typeLabel = new Label(r.getExamType());
		Label medsLabel = new Label(r.getMeds());
		Label diagnosis = new Label(r.getDiagnosis());
		VBox detailText = new VBox(typeLabel, medsLabel, diagnosis);
		detailText.setSpacing(3);

		recordBox.getChildren().addAll(dateLabel, detailText);
		recordsContainer.getChildren().add(recordBox);
		recordBox.getStyleClass().add("record-box");
		dateLabel.getStyleClass().add("date-label");
		typeLabel.getStyleClass().add("exam-type-label");
		medsLabel.getStyleClass().add("meds-label");
	}

	@FXML
	private void onAddRecordClicked() {
		if (currentPatient == null) {
			ResourceBundle bundle = ResourceBundle.getBundle(
				"messages",
				AppSettings.getLocale()
			);

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle(bundle.getString("no.patient.selected"));
			alert.setHeaderText(null);
			alert.setContentText(bundle.getString("select.patient.first"));
			alert.showAndWait();
			return;
		}

		try {
			ResourceBundle bundle = ResourceBundle.getBundle(
				"messages",
				AppSettings.getLocale()
			);

			FXMLLoader loader = new FXMLLoader(
				getClass().getResource("newRecord.fxml"),
				bundle
			);
			Scene scene = new Scene(loader.load());

			NewRecordDialogController controller = loader.getController();
			controller.setParentController(this);

			Stage popup = new Stage();
			popup.setTitle(bundle.getString("new.record")); // napr. Nový záznam
			popup.setScene(scene);
			popup.setResizable(false);
			popup.initOwner(searchField.getScene().getWindow());
			popup.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void onShowPatientCardClicked() {
		if (currentPatient == null) {
			ResourceBundle bundle = ResourceBundle.getBundle(
				"messages",
				AppSettings.getLocale()
			);

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle(bundle.getString("no.patient.selected"));
			alert.setHeaderText(null);
			alert.setContentText(bundle.getString("select.patient.first"));
			alert.showAndWait();
			return;
		}

		try {
			ResourceBundle bundle = ResourceBundle.getBundle(
				"messages",
				AppSettings.getLocale()
			);

			FXMLLoader loader = new FXMLLoader(
				getClass().getResource("patientCart.fxml"),
				bundle
			);
			VBox patientCard = loader.load();

			scrollPane.setContent(patientCard);

			PatientCartController controller = loader.getController();
			controller.setPatient(currentPatient);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
