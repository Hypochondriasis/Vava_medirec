package com.frontend.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class PatientCartController {

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
}
