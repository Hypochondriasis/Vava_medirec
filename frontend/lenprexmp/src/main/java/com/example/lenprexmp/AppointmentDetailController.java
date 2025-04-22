package com.example.lenprexmp;

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

        infoLabel.setText(appointment.getDate() + " - " + appointment.getTime() + " - " + appointment.getType());

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


