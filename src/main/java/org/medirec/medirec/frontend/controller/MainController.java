package org.medirec.medirec.frontend.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.medirec.medirec.backend.model.*;
import org.medirec.medirec.backend.model.Appointment;
import org.medirec.medirec.backend.model.MedicalRecord;
import org.medirec.medirec.backend.model.Patient;
import org.medirec.medirec.backend.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	//User session
	private User user;
	// FXML elements
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
	@FXML
	private Label medirecLabel;
	@FXML
	private Button homeButton;
	@FXML
	private Button calendarButton;
	@FXML
	private Button patientsButton;
	@FXML
	private Button reportsButton;
	@FXML
	private Label settingsLabel;

	private Locale currentLocale = Locale.getDefault();

	@FXML
	public void initialize() {
		loadView("Home.fxml");

		currentLocale = AppSettings.getLocale();
		languageButton.setText(resources.getString("button.switchLanguage"));

		// Get information about the
		//setUserInfo();
	}

	public void setUserInfo() {
		try{
			//Getting the Doctor info
			user.setDoctor(DatabaseService.getOne(Doctor.class, user.getDoctor_id()));
			//Getting the medical records
			user.getDoctor().setMedicalRecords(DatabaseService.getAllInGroup(MedicalRecord.class, "doctor_id", user.getDoctor().getId()));
			//Getting Patients
			for(MedicalRecord record : user.getDoctor().getMedicalRecords()) {
				record.setDoctor(user.getDoctor());
				record.setPatient(DatabaseService.getOne(Patient.class, record.getPatient_id()));

				// Diagnoses
				record.setRecordDiagnoses(DatabaseService.getAllInGroup(RecordDiagnosis.class, "medical_record_id", record.getId()));
				for(RecordDiagnosis recordDiagnosis : record.getRecordDiagnoses()) {
					// Binding Medical record
					recordDiagnosis.setMedical_record(record);
					// Binding Diagnosis
					recordDiagnosis.setDiagnosis(DatabaseService.getOne(Diagnosis.class, recordDiagnosis.getDiagnosis_id()));
					// Diagnosis type
					recordDiagnosis.getDiagnosis().setDiagnosis_type(DatabaseService.getOne(DiagnosisType.class, recordDiagnosis.getDiagnosis().getDiagnosis_type_id()));
				}

				// Prescriptions
				record.setPrescriptions(DatabaseService.getAllInGroup(Prescription.class, "medical_record_id", record.getId()));
				for(Prescription prescription : record.getPrescriptions()) {
					prescription.setMedical_record(record);
					// Medication
					prescription.setMedication(DatabaseService.getOne(Medication.class, prescription.getMedication_id()));
				}
			}

			// Appointments
			user.getDoctor().setAppointments(DatabaseService.getAllInGroup(Appointment.class, "doctor_id", user.getDoctor().getId()));
			for(Appointment appointment : user.getDoctor().getAppointments()) {
				appointment.setDoctor(user.getDoctor());
				appointment.setPatient(DatabaseService.getOne(Patient.class, appointment.getPatient_id()));
				appointment.setAppointment_status(DatabaseService.getOne(AppointmentStatus.class, appointment.getAppointment_status_id()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			nameLabel.setText("Name");
		}

		if (user.getRole().getName().equals("Doctor")) {
			nameLabel.setText("Dr. "+user.getDoctor().getFirst_name()+" "+user.getDoctor().getLast_name());
		}
	}

	private void loadView(String fxmlPath) {
		try {
			System.out.println("Loading: " + fxmlPath);
			FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/medirec/medirec/frontend/fxml/" + fxmlPath),
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
		loadView("Home.fxml");
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
			if (medirecLabel != null) {
				medirecLabel.setText(newBundle.getString("app.title"));
			}
			if (homeButton != null) {
				homeButton.setText(newBundle.getString("menu.home"));
			}
			if (calendarButton != null) {
				calendarButton.setText(newBundle.getString("menu.calendar"));
			}
			if (patientsButton != null) {
				patientsButton.setText(newBundle.getString("menu.patients"));
			}
			if (reportsButton != null) {
				reportsButton.setText(newBundle.getString("menu.reports"));
			}
			if (settingsLabel != null) {
				settingsLabel.setText(newBundle.getString("menu.settings"));
			}
			if (languageButton != null) {
				languageButton.setText(newBundle.getString("button.switchLanguage"));
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
