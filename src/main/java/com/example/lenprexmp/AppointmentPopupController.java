package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;
import com.example.backend.model.Appointment;
import com.example.backend.model.Patient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentPopupController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private ComboBox<Patient> patientComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button addButton;

    private CalendarController calendarController;

    @FXML
    private ResourceBundle resources;

    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());

        // Appointment types from ResourceBundle
        typeComboBox.setItems(FXCollections.observableArrayList(
                resources.getString("appointment.type.consultation"),
                resources.getString("appointment.type.urgent"),
                resources.getString("appointment.type.examination")
        ));

        // Default time options already provided in FXML, just select the first
        timeComboBox.getSelectionModel().selectFirst();

        // Load patients (replace with actual service call)
        List<Patient> patients = getAllPatients();
        patientComboBox.setItems(FXCollections.observableArrayList(patients));
        if (!patients.isEmpty()) {
            patientComboBox.getSelectionModel().selectFirst();
        }

        // Load status options (e.g., "Scheduled", "Completed", "Cancelled")
        statusComboBox.setItems(FXCollections.observableArrayList(getAllStatuses()));
        statusComboBox.getSelectionModel().selectFirst();

        // Handle button click
        addButton.setOnAction(e -> {
            LocalDate date = datePicker.getValue();
            String type = typeComboBox.getValue();
            String time = timeComboBox.getValue();
            Patient selectedPatient = patientComboBox.getValue();
            String status = statusComboBox.getValue();

            if (calendarController != null && date != null && type != null && time != null && selectedPatient != null && status != null) {
                Appointment nove = new Appointment();
                nove.setReason(type);
                nove.setTime(time);
                nove.setAppointment_date(date);
                nove.setPatient_id(selectedPatient.getId());
                nove.setAppointment_status_id(1);

                calendarController.addAppointment(nove);
            }

            ((Stage) addButton.getScene().getWindow()).close();
        });
    }

    public void setCalendarController(CalendarController controller) {
        this.calendarController = controller;
    }

    // Dummy method to simulate patient loading (replace with actual DB/service logic)
    private List<Patient> getAllPatients() {
        List<Patient> pacienti = new ArrayList<>();
        try{
            pacienti = DatabaseController.getAllPatients();
        }
        catch (Exception e){

        }
        return pacienti;
    }

    // Dummy method to simulate appointment status options
    private List<String> getAllStatuses() {
        return List.of("Scheduled", "Completed", "Cancelled");
    }
}
