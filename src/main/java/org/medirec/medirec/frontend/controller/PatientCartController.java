package org.medirec.medirec.frontend.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.medirec.medirec.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientCartController {
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(PatientCartController.class);
	//User session
	private User user;

	private Patient currentPatient;

	@FXML
	private TextField birthDateField;

	@FXML
	private TextField idNumberField;

	@FXML
	private TextField addressField;

	@FXML
	private TextField phoneField;

	@FXML
	private TextField lastVisitField;

	@FXML
	private TextField bloodTypeField;

	@FXML
	private TextField nextVisitField;

	@FXML
	private TextField emailField;

	@FXML
	private TextField pressureField;

	@FXML
	private TextField weightField;

	@FXML
	private TextField heightField;

	@FXML
	private TextField bmiField;

	@FXML
	private TextField newAllergyField;

	@FXML
	private ListView<String> allergyListView;

	@FXML
	private Button addAllergyButton;

	@FXML
	private ListView<String> diagnosesListView;

	@FXML
	private TextField newDiagnosisField;
	@FXML
	private Label patientCartLabel;
	@FXML
	private Button newRecordButton;
	@FXML
	private TitledPane infoTitledPane;
	@FXML
	private Label birthdateLabel;
	@FXML
	private Label numberLabel;
	@FXML
	private Label addressLabel;
	@FXML
	private Label phoneLabel;
	@FXML
	private Label lastVisitLabel;
	@FXML
	private Label bloodTypeLabel;
	@FXML
	private Label nextVisitLabel;
	@FXML
	private Label emailLabel;
	@FXML
	private TitledPane healthInfoTitledPane;
	@FXML
	private Label pressureLabel;
	@FXML
	private Label weightLabel;
	@FXML
	private Label heightLabel;
	@FXML
	private Label bmiLabel;
	@FXML
	private Label allergiesLabel;
	@FXML
	private TitledPane chronicTitledPane;

	@FXML
	private void handleAddDiagnosis() {
		String diagnosis = newDiagnosisField.getText();
		if (diagnosis != null && !diagnosis.trim().isEmpty()) {
			diagnosesListView.getItems().add(diagnosis.trim());
			newDiagnosisField.clear();
		}
	}

	@FXML
	private void initialize() {
		allergyListView.setOnMouseClicked(this::handleAllergyDoubleClick);
	}

	public void setPatient(Patient patient) {
		this.currentPatient = patient;
		initPatient(patient);
	}

	public void initPatient(Patient patient) {
		if (patient == null) {
			System.err.println("initPatient called with null patient!");
			return;
		}

		System.out.println("initPatient called for: " + patient.getName());
		System.out.println(
			"Setting birth date to: " + formatDate(patient.getBirthdate())
		);
		birthDateField.setText(formatDate(patient.getBirthdate()));

		emailField.setText(safe(patient.getEmail()));
		phoneField.setText(safe(patient.getPhone()));
		addressField.setText(
			safe(patient.getStreet()) +
			", " +
			safe(patient.getCurrentCity()) +
			" " +
			safe(patient.getPostalCode())
		);
		idNumberField.setText(safe(patient.getInsuranceNumber()));
		bloodTypeField.setText(""); // Ak bude getter pre krvnú skupinu, použi

		pressureField.setText(safe(patient.getBloodPressure()));
		bmiField.setText(
			patient.getBmi() != null
				? String.format("%.2f", patient.getBmi())
				: ""
		);
		weightField.setText(
			patient.getWeight() != null
				? String.format("%.1f", patient.getWeight())
				: ""
		);
		heightField.setText(
			patient.getHeight() != null
				? String.format("%.1f", patient.getHeight())
				: ""
		);
		// Diagnózy – keď bude getter na diagnózy, môžeš ich načítať
		// diagnosesListView.getItems().setAll(...);
	}

	@FXML
	private void handleAddAllergy() {
		String allergy = newAllergyField.getText().trim();
		if (!allergy.isEmpty()) {
			allergyListView.getItems().add(allergy);
			newAllergyField.clear();
		}
	}

	private void handleAllergyDoubleClick(MouseEvent event) {
		if (event.getClickCount() == 2) {
			String selected = allergyListView
				.getSelectionModel()
				.getSelectedItem();
			if (selected != null) {
				allergyListView.getItems().remove(selected);
			}
		}
	}

	private String safe(String value) {
		return value == null ? "" : value;
	}

	private String formatDate(LocalDate date) {
		return date == null
			? ""
			: date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	@FXML
	private void handleChangeCart() {
		/* pre zmenu udajov
        // Textové údaje
        String birthDateText = birthDateField.getText().trim();
        String idNumber = idNumberField.getText().trim();
        String addressText = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String lastVisit = lastVisitField.getText().trim();
        String nextVisit = nextVisitField.getText().trim();
        String bloodType = bloodTypeField.getText().trim();
        String email = emailField.getText().trim();

        // Zdravotné údaje
        String pressure = pressureField.getText().trim();
        String weightText = weightField.getText().trim();
        String heightText = heightField.getText().trim();
        String bmiText = bmiField.getText().trim();

        // Parsovanie číselných a dátumových údajov
        LocalDate birthDate = parseDate(birthDateText);
        LocalDate lastVisitDate = parseDate(lastVisit);
        LocalDate nextVisitDate = parseDate(nextVisit);
        Double weight = parseDouble(weightText);
        Double height = parseDouble(heightText);
        Double bmi = parseDouble(bmiText);

        // Alergie
        var allergies = allergyListView.getItems();

        // Diagnózy
        var diagnoses = diagnosesListView.getItems();

        // Rozbitie adresy (ak potrebuješ samostatné časti)
        String street = getStreetPart(addressText);
        String city = getCityPart(addressText);
        String postalCode = getPostalCodePart(addressText);

        // Výpis pre kontrolu
        System.out.println("----- FORMULÁR PACIENTA -----");
        System.out.println("Dátum narodenia: " + birthDate);
        System.out.println("Rodné číslo: " + idNumber);
        System.out.println("Ulica: " + street);
        System.out.println("Mesto: " + city);
        System.out.println("PSČ: " + postalCode);
        System.out.println("Telefón: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Posledná návšteva: " + lastVisitDate);
        System.out.println("Ďalšia návšteva: " + nextVisitDate);
        System.out.println("Krvná skupina: " + bloodType);
        System.out.println("Tlak: " + pressure);
        System.out.println("Hmotnosť: " + weight);
        System.out.println("Výška: " + height);
        System.out.println("BMI: " + bmi);
        System.out.println("Alergie: " + allergies);
        System.out.println("Diagnózy: " + diagnoses);

         */
	}

	protected void updateUILanguage(Locale locale) {
		try {
			// Getting the new resource bundle
			ResourceBundle newBundle = ResourceBundle.getBundle("org.medirec.medirec.frontend.messages", locale);
			if (patientCartLabel != null) {
				patientCartLabel.setText(newBundle.getString("patient.cart"));
			}
			if (newRecordButton != null) {
				newRecordButton.setText(newBundle.getString("edit.record"));
			}
			if (infoTitledPane != null) {
				infoTitledPane.setText(newBundle.getString("personal.info"));
			}
			if (birthdateLabel != null) {
				birthdateLabel.setText(newBundle.getString("birth.date"));
			}
			if (numberLabel != null) {
				numberLabel.setText(newBundle.getString("id.number"));
			}
			if (addressLabel != null) {
				addressLabel.setText(newBundle.getString("address"));
			}
			if (phoneLabel != null) {
				phoneLabel.setText(newBundle.getString("phone"));
			}
			if (lastVisitLabel != null) {
				lastVisitLabel.setText(newBundle.getString("last.visit"));
			}
			if (bloodTypeLabel != null) {
				bloodTypeLabel.setText(newBundle.getString("blood.type"));
			}
			if (nextVisitLabel != null) {
				nextVisitLabel.setText(newBundle.getString("next.visit"));
			}
			if (emailLabel != null) {
				emailLabel.setText(newBundle.getString("email"));
			}
			if (healthInfoTitledPane != null) {
				healthInfoTitledPane.setText(newBundle.getString("health.info"));
			}
			if (pressureLabel != null) {
				pressureLabel.setText(newBundle.getString("pressure"));
			}
			if (weightLabel != null) {
				weightLabel.setText(newBundle.getString("weight"));
			}
			if (heightLabel != null) {
				heightLabel.setText(newBundle.getString("height"));
			}
			if (bmiLabel != null) {
				bmiLabel.setText(newBundle.getString("bmi"));
			}
			if (allergiesLabel != null) {
				allergiesLabel.setText(newBundle.getString("allergies"));
			}
			if (newAllergyField != null) {
				newAllergyField.setText(newBundle.getString("add.new.alergie"));
			}
			if (chronicTitledPane != null) {
				chronicTitledPane.setText(newBundle.getString("name.label"));
			}
			if (newDiagnosisField != null) {
				newDiagnosisField.setText(newBundle.getString("add.new.diagnosis"));
			}
		}catch (java.util.MissingResourceException e){
			logger.error("Missing resource key: {}", e.getKey());
		}catch (Exception e) {
			logger.error("Failed to update UI language", e);
		}
	}
}
