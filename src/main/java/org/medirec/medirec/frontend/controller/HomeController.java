package org.medirec.medirec.frontend.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.medirec.medirec.backend.controller.DatabaseController;
import org.medirec.medirec.backend.model.Doctor;
import org.medirec.medirec.backend.model.MedicalRecord;
import org.medirec.medirec.backend.model.Patient;
import org.medirec.medirec.backend.model.User;
import org.medirec.medirec.backend.util.BatchXMLExporter;
import org.medirec.medirec.backend.util.PatientImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeController {
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	//User session
	private User user;
	@FXML
	private Label currentVisitTextArea;
	@FXML
	private Label completedPatientsTextArea;
	@FXML
	private Label waitingPatientsTextArea;
	@FXML
	private Label newRecordsTextArea;
	@FXML
	private ListView<String> examinationsListView;
	@FXML
	private ListView<String> tasksListView;
	@FXML
	private Button newPatientButton;
	@FXML
	private Button exportDataButton;
	@FXML
	private Label dayOverviewLabel;
	@FXML
	private Label todaysVisitsLabel;
	@FXML
	private Label completedPatientsLabel;
	@FXML
	private Label waitingPatientsLabel;
	@FXML
	private Label newRecordsLabel;
	@FXML
	private Label todaysExaminationsLabel;
	@FXML
	private Label todaysTasksLabel;

	// Locale
	private Locale currentLocale = Locale.getDefault();

	@FXML
	private void initialize() {
		// Simulované naplnenie údajov
		populateDashboard();
		populateExaminations();
		populateTasks();

		currentLocale = AppSettings.getLocale();
		updateUILanguage(currentLocale);
	}

	private void populateDashboard() {
		currentVisitTextArea.setText("5");
		completedPatientsTextArea.setText("12");
		waitingPatientsTextArea.setText("3");
		newRecordsTextArea.setText("7");
	}

	private void populateExaminations() {
		//naplnit appointmentami
		examinationsListView
			.getItems()
			.addAll(
				"09:00 - MUDr. Ján Novák - Kontrola",
				"09:30 - MUDr. Eva Bieliková - Očkovanie",
				"10:00 - MUDr. Ján Novák - Konzultácia"
			);
	}

	private void populateTasks() {
		// naplnit taskami na den
		tasksListView
			.getItems()
			.addAll(
				"Odoslať laboratórne vzorky",
				"Skontrolovať výsledky testov",
				"Zavolať pacientovi",
				"Pripraviť dokumentáciu"
			);

		tasksListView.setCellFactory(
			new Callback<ListView<String>, ListCell<String>>() {
				@Override
				public ListCell<String> call(ListView<String> listView) {
					return new ListCell<String>() {
						private final CheckBox checkBox = new CheckBox();

						@Override
						protected void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);

							if (empty || item == null) {
								setText(null);
								setGraphic(null);
								setStyle("");
							} else {
								checkBox.setText(item);

								// Farba podľa indexu
								int index = getIndex();
								String color = "#bdc3c7"; // default

								switch (index) {
									case 0:
										color = "#e74c3c"; // červená
										break;
									case 1:
										color = "#e67e22"; // oranžová
										break;
									case 2:
										color = "#f1c40f"; // žltá
										break;
									case 3:
										color = "#2ecc71"; // zelená
										break;
								}

								setStyle(
									"-fx-border-color: transparent transparent transparent " +
									color +
									";" +
									"-fx-border-width: 0 0 0 5px; -fx-padding: 5 10;"
								);

								setGraphic(checkBox);
								setText(null);
							}
						}
					};
				}
			}
		);
	}

	@FXML
	public void onNewPatient() {
		try{
			logger.info("Attempting to import a new patient...");
			if(user.getRole().getName().equals("Doctor")){
				// Filechooser
				FileChooser fileChooser = new FileChooser();
				String text = AppSettings.getLocale().getLanguage().equals("sk") ? "Zvoľte XML súbor s informáciami o pacientovi" : "Select Patient XML file";
				// Setting the extension filter to only show XML files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
				fileChooser.getExtensionFilters().add(extFilter);
				// Show open file dialog
				File selectedFile = fileChooser.showOpenDialog(newPatientButton.getScene().getWindow());
				// Import from File
				if (selectedFile != null) {
					// Proceed with importing the patient
					Patient importedPatient = PatientImporter.importPatientFromXML(selectedFile, user);
					System.out.println("Patient imported successfully: " + importedPatient.getFirst_name() + " " + importedPatient.getLast_name());

					showAlert(Alert.AlertType.INFORMATION, "Patient successfully uploaded", "Pacient úspešne nahratý");
				} else {
					logger.info("File selection was canceled.");
				}
			}
			showAlert(Alert.AlertType.INFORMATION, "Pacient sucessfully uploaded", "Pacient úpešne nahratý");
		} catch (Exception e) {
			logger.error("Error importing patient", e);
			showAlert(Alert.AlertType.ERROR, "Error importing pacient", "Vyskytol sa problém pri importe pacienta");
		}
	}

	@FXML
	private void onExportData() {
		try {
			logger.info("Attempting to export data...");
			if (user.getRole().getName().equals("Doctor")) {
				Doctor doctor = user.getDoctor();
				List<Patient> patients = DatabaseController.getAllPatientsInGroup("doctor_id", user.getDoctor_id());
				ArrayList<MedicalRecord> records = doctor.getMedicalRecords();

				String exportDir = BatchXMLExporter.exportDoctorData(doctor, patients, records, null);
				System.out.println("Export completed to: " + exportDir);
				showAlert(Alert.AlertType.INFORMATION, "Export completed successfully", "Export bol vykonaný úspešne");
			}
		}catch (Exception e){
			logger.error("Error exporting data", e);
			showAlert(Alert.AlertType.ERROR, "Error exporting data", "Vyskytol sa problém pri exporte dát");
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
			if (!user.getRole().getName().equals("Doctor")) {
				exportDataButton.setVisible(false);
				newPatientButton.setVisible(false);
			}
			// Getting the new resource bundle
			ResourceBundle newBundle = ResourceBundle.getBundle("org.medirec.medirec.frontend.messages", locale);
			if(newPatientButton != null) {
				newPatientButton.setText(newBundle.getString("main.newPatient"));
			}
			if (exportDataButton != null) {
				exportDataButton.setText(newBundle.getString("main.exportData"));
			}
			if (dayOverviewLabel != null) {
				dayOverviewLabel.setText(newBundle.getString("main.dayOverviewTitle"));
			}
			if (todaysVisitsLabel != null) {
				todaysVisitsLabel.setText(newBundle.getString("main.todaysVisits"));
			}
			if (completedPatientsLabel != null) {
				completedPatientsLabel.setText(newBundle.getString("main.completedPatients"));
			}
			if (waitingPatientsLabel != null) {
				waitingPatientsLabel.setText(newBundle.getString("main.waitingPatients"));
			}
			if (newRecordsLabel != null) {
				newRecordsLabel.setText(newBundle.getString("main.newRecords"));
			}
			if (todaysExaminationsLabel != null) {
				todaysExaminationsLabel.setText(newBundle.getString("main.todaysExaminations"));
			}
			if (todaysTasksLabel != null) {
				todaysTasksLabel.setText(newBundle.getString("main.todaysTasks"));
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
}
