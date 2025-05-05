package org.medirec.medirec.frontend.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.medirec.medirec.backend.controller.DatabaseController;
import org.medirec.medirec.backend.model.MedicalRecord;
import org.medirec.medirec.backend.model.User;
import org.medirec.medirec.backend.service.DatabaseService;
import org.medirec.medirec.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MedicalRecordsController {
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordsController.class);
	//User session
	private User user;

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
	@FXML
	private Button searchButton;
	@FXML
	private Button newRecordButton;
	@FXML
	private Button showPatientCardButton;
	@FXML
	private Label historyLabel;

	// Locale
	private Locale currentLocale = Locale.getDefault();

	private Patient currentPatient;

	@FXML
	private void initialize() {
		currentLocale = AppSettings.getLocale();
		updateUILanguage(currentLocale);
		basicInfo.setEditable(false);
	}

	private List<MedicalRecord> loadRecordsForPatient(int patientId) {
		try {
			return DatabaseService.getAllInGroup(MedicalRecord.class, "patient_id", patientId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@FXML
	private void onSearchClicked() {
		// ak je searchField prazdny tak vycisti vsetky textove udaje
		if(searchField.getText().trim().isEmpty()){
			ResourceBundle bundle = ResourceBundle.getBundle(
					"org.medirec.medirec.frontend.messages",
					AppSettings.getLocale()
			);
			nameLabel.setText(bundle.getString("patient.not.found"));
			basicInfo.clear();
			recordsContainer.getChildren().clear();
			return;
		}

		String query = searchField.getText().trim();
		Pattern pattern;
		try {
			pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException e) {
			nameLabel.setText("Neplatný regulárny výraz.");
			basicInfo.clear();
			recordsContainer.getChildren().clear();
			return;
		}

		List<Patient> allPatients;
		try {
			allPatients = DatabaseService.getAll(Patient.class);
		} catch (Exception e) {
			nameLabel.setText("Chyba pri načítaní z databázy.");
			e.printStackTrace();
			return;
		}

		recordsContainer.getChildren().clear();
		int count = 0;

		for (Patient patient : allPatients) {
			String fullName = patient.getFirst_name() + " " + patient.getLast_name();
			if (pattern.matcher(fullName).find()) {
				currentPatient = patient;

				// nacitaj zaznamy pacienta z DB
				List<MedicalRecord> records = loadRecordsForPatient(patient.getId());
				currentPatient.setRecords(records);

				// zobraz meno a zakladne udaje podla lokalizacie
				ResourceBundle bundle = ResourceBundle.getBundle(
						"org.medirec.medirec.frontend.messages",
						AppSettings.getLocale()
				);
				nameLabel.setText(fullName);
				basicInfo.setText(patient.getBasicInfo(bundle));

				// vykresli vsetky zaznamy
				renderPatientRecords(patient);

				count++;
				break;
			}
		}

		if (count == 0) {
			nameLabel.setText("Pacient nebol nájdený.");
			basicInfo.clear();
			recordsContainer.getChildren().clear();
		}
	}

	public void addRecord(String notes) {
		if (currentPatient != null) {
			try {
				MedicalRecord record = new MedicalRecord();
				record.setPatient_id(currentPatient.getId());
				record.setDoctor_id(Math.toIntExact(JwtUtil.getUserFromToken().getDoctor_id()));
				record.setNotes(notes);
				record.setCreated_at(new Timestamp(System.currentTimeMillis()));
				record.setUpdated_at(new Timestamp(System.currentTimeMillis()));

				// Save the record to the database
				MedicalRecord savedRecord = DatabaseController.saveOrUpdateMedicalRecord(record);

				// Update the record with the ID from the database
				currentPatient.addRecord(savedRecord);
				renderRecordOnTop(savedRecord);
			} catch (Exception e) {
				// Logging the exception
                logger.error("Error saving medical record: {}", e.getMessage());
				showAlert(Alert.AlertType.ERROR, "Failed to add the medical record", "Problém s pridaním nového záznamu");
			}
		}
	}

	//Helper function to show alerts
	private void showAlert(Alert.AlertType type, String msg_en, String msg_sk) {
		Locale current = AppSettings.getLocale();
		String msg;
		if (current.getLanguage().equals("sk")) {
			msg = msg_sk;
		}else{
			msg = msg_en;
		}
		Alert alert = new Alert(type, msg);
		alert.setHeaderText(null);
		alert.showAndWait();
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
		recordBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");

		ResourceBundle bundle = ResourceBundle.getBundle("org.medirec.medirec.frontend.messages", AppSettings.getLocale());
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String formattedDate = r.getCreated_at() != null ? sdf.format(r.getCreated_at()) : "—";

		Label dateLabel = new Label(bundle.getString("exam.date.label") + ": " + formattedDate);
		Label notesLabel = new Label(r.getNotes() != null ? r.getNotes() : "(žiadne poznámky)");

		recordBox.getChildren().addAll(dateLabel, notesLabel);
		recordsContainer.getChildren().add(0, recordBox);
		recordBox.getStyleClass().add("record-box");
		dateLabel.getStyleClass().add("date-label");
		notesLabel.getStyleClass().add("exam-type-label");
	}


	private void renderRecord(MedicalRecord r) {
		VBox recordBox = new VBox();
		recordBox.setSpacing(5);
		recordBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");

		ResourceBundle bundle = ResourceBundle.getBundle("org.medirec.medirec.frontend.messages", AppSettings.getLocale());
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String formattedDate = r.getCreated_at() != null ? sdf.format(r.getCreated_at()) : "—";

		Label dateLabel = new Label(bundle.getString("exam.date.label") + ": " + formattedDate);
		Label notesLabel = new Label(r.getNotes() != null ? r.getNotes() : "(žiadne poznámky)");

		recordBox.getChildren().addAll(dateLabel, notesLabel);
		recordsContainer.getChildren().add(recordBox);
		recordBox.getStyleClass().add("record-box");
	}


	@FXML
	private void onAddRecordClicked() {
		if (currentPatient == null) {
			ResourceBundle bundle = ResourceBundle.getBundle(
				"org.medirec.medirec.frontend.messages",
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
				"org.medirec.medirec.frontend.messages",
				AppSettings.getLocale()
			);

			FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/medirec/medirec/frontend/fxml/newRecord.fxml"),
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
				"org.medirec.medirec.frontend.messages",
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
				"org.medirec.medirec.frontend.messages",
				AppSettings.getLocale()
			);

			FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/medirec/medirec/frontend/fxml/patientCart.fxml"),
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

	protected void onLanguageSwitch(){
		// Toggle to local
		Locale current = AppSettings.getLocale();
		Locale next = current.getLanguage().equals("sk") ? Locale.ENGLISH : new Locale("sk", "SK");
		AppSettings.setLocale(next);

		// Updating the UI text without needing to reload the pane
		updateUILanguage(next);
	}

	protected void updateUILanguage(Locale locale) {
		try {
			// Getting the new resource bundle
			ResourceBundle newBundle = ResourceBundle.getBundle("org.medirec.medirec.frontend.messages", locale);
			if (searchField != null) {
				searchField.setPromptText(newBundle.getString("enter.name"));
			}
			if (searchButton != null) {
				searchButton.setText(newBundle.getString("search.button"));
			}
			if (nameLabel != null) {
				nameLabel.setText(newBundle.getString("name.label"));
			}
			if (newRecordButton != null) {
				newRecordButton.setText(newBundle.getString("new.record"));
			}
			if (showPatientCardButton != null){
				showPatientCardButton.setText(newBundle.getString("show.patient.card"));
			}
			if (historyLabel != null){
				historyLabel.setText(newBundle.getString("patient.history"));
			}
		}catch (java.util.MissingResourceException e){
			logger.error("Missing resource key: {}", e.getKey());
		}catch (Exception e) {
			logger.error("Failed to update UI language", e);
		}
	}

	protected User getUser() {
		return this.user;
	}

	protected void setUser(User user) {
		this.user = user;
	}
}
