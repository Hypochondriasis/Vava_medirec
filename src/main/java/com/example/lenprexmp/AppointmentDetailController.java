package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;
import com.example.backend.model.Appointment;
import com.example.backend.model.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppointmentDetailController {

    @FXML
    private Label infoLabel;

    @FXML
    private Button deleteButton;

    private Labeled targetLabel; // Label v bunke, ktorý sa má zmazať


        private Appointment appointment;
        private CalendarController calendarController;


    public void setData(Appointment appointment, CalendarController calendarController) {
        this.appointment = appointment;
        this.calendarController = calendarController;
        Patient pacient;
        try {
            pacient = DatabaseController.getPatientById(appointment.getPatient_id());
        }
        catch (Exception e ){
            pacient = new Patient();
        }

        infoLabel.setText(appointment.getAppointment_date() + " - " + appointment.getTime() + " - " + appointment.getReason()+ "\n "+pacient.getFirst_name()+" "+pacient.getLast_name());

        deleteButton.setOnAction(e -> {
            calendarController.removeAppointment(appointment);
            ((Stage) deleteButton.getScene().getWindow()).close();
        });
    }


        @FXML
        private void initialize() {
            deleteButton.setOnAction(e -> {
                calendarController.removeAppointment(appointment);
                ((Stage) deleteButton.getScene().getWindow()).close();
            });
        }
    }


