package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;
import com.example.backend.model.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.text.MessageFormat;
import java.util.ResourceBundle;



public class AddPatientController {
    private ResourceBundle bundle;

    private HomeController parentController;
    private PatientInfoController parentControllerPatient;
    private Patient patient;
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{7,15}$";


    @FXML private TextField nameField;
    @FXML private DatePicker birthdatePicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField birthCityField;
    @FXML private TextField currentCityField;
    @FXML private TextField streetField;
    @FXML private TextField postalCodeField;
    @FXML private TextField insuranceNumberField;
    @FXML private TextField patientidField;
    @FXML private TextField bloodPressureField;
    @FXML private TextField bmiField;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        if (genderComboBox != null) {
            genderComboBox.getItems().clear();
            genderComboBox.getItems().addAll(
                    getString("gender.male"),
                    getString("gender.female")
            );
        }
    }


    @FXML
    private void initialize() {



    }
    private boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private boolean isPhoneValid(String phone) {
        return phone != null && phone.matches(PHONE_REGEX);
    }

    @FXML
    private void submit() {
        if (!areRequiredFieldsValid()) {
            showAlert(AlertType.ERROR, getString("alert.error.title"), getString("alert.error.required_fields"));

            return;
        }

        try {

            patient.setFirst_name(nameField.getText());
            patient.setBirth_date(birthdatePicker.getValue());
            patient.setGender(genderComboBox.getValue());
            patient.setEmail(emailField.getText());
            patient.setPhone_number(phoneField.getText());
            patient.setBirth_city(birthCityField.getText());
            patient.setPermanent_city(currentCityField.getText());
            patient.setStreet(streetField.getText());
            patient.setPostal_code(postalCodeField.getText());
            patient.setInsurance_number(insuranceNumberField.getText());
            patient.setPersonal_id_number(patientidField.getText());
            patient.setBlood_pressure(emptyIfBlank(bloodPressureField));
            patient.setBMI(parseFloatOrNull(bmiField));
            patient.setWeight(parseFloatOrNull(weightField));
            patient.setHeight(parseFloatOrNull(heightField));

            DatabaseController.saveOrUpdatePatient(patient);

            // Môžeš tu pridať kód na uloženie pacienta do DB alebo inej logiky
            showAlert(AlertType.INFORMATION, getString("alert.success.title"), getString("alert.success.patient_added"));

            clearForm();
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert(AlertType.ERROR, getString("alert.error.title"),
                    MessageFormat.format(getString("alert.error.processing"), e.getMessage()));

        }
    }
    private void populateFieldsFromPatient() {
        if (patient == null) return;

        nameField.setText(patient.getFirst_name());
        birthdatePicker.setValue(patient.getBirth_date());
        genderComboBox.setValue(patient.getGender());
        emailField.setText(patient.getEmail());
        phoneField.setText(patient.getPhone_number());
        birthCityField.setText(patient.getBirth_city());
        currentCityField.setText(patient.getPermanent_city());
        streetField.setText(patient.getStreet());
        postalCodeField.setText(patient.getPostal_code());
        insuranceNumberField.setText(patient.getInsurance_number());
        patientidField.setText(patient.getPersonal_id_number());
        bloodPressureField.setText(patient.getBlood_pressure());
        bmiField.setText(patient.getBMI() != null ? String.valueOf(patient.getBMI()) : "");
        weightField.setText(patient.getWeight() != null ? String.valueOf(patient.getWeight()) : "");
        heightField.setText(patient.getHeight() != null ? String.valueOf(patient.getHeight()) : "");
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private boolean areRequiredFieldsValid() {
        if (isEmpty(nameField) ||
                birthdatePicker.getValue() == null ||
                genderComboBox.getValue() == null ||
                isEmpty(emailField) ||
                isEmpty(phoneField) ||
                isEmpty(birthCityField) ||
                isEmpty(currentCityField) ||
                isEmpty(streetField) ||
                isEmpty(postalCodeField) ||
                isEmpty(insuranceNumberField)) {
            return false;
        }

        if (!isEmailValid(emailField.getText())) {
            showAlert(AlertType.ERROR, getString("alert.error.title"), getString("alert.error.invalid_email"));
            return false;
        }

        if (!isPhoneValid(phoneField.getText())) {
            showAlert(AlertType.ERROR, getString("alert.error.title"), getString("alert.error.invalid_phone"));
            return false;
        }

        return true;
    }


    private boolean isEmpty(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private String emptyIfBlank(TextField field) {
        String text = field.getText();
        return (text == null || text.trim().isEmpty()) ? null : text.trim();
    }

    private Float parseFloatOrNull(TextField field) {
        try {
            String text = field.getText();
            return (text == null || text.trim().isEmpty()) ? null : Float.parseFloat(text.trim());
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
    public void setParentControllerPatient(PatientInfoController controller) {
        this.parentControllerPatient = controller;
    }
    public void setPatient(Patient vstup){
        this.patient = vstup;
        populateFieldsFromPatient();
    }
    private String getString(String key) {
        return bundle != null ? bundle.getString(key) : key;
    }
}
