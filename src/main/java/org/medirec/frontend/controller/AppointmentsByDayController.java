package org.medirec.frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentsByDayController {
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;

    @FXML private ListView<Label> appointmentsListView;

    private final ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());



    public void setDate(LocalDate date) {
        dateLabel.setText(date.toString());
    }

    public void setAppointments(List<Appointment> dailyAppointments) {
        List<Label> labels = new ArrayList<>();

        for (Appointment app : dailyAppointments) {
            String time = app.getTime();
            String type = app.getType();

            Label label = new Label(time + " – " + type);
            CalendarController controller= new CalendarController();
            label.setStyle("-fx-padding: 5 0 5 0; -fx-font-size: 14px; -fx-text-fill: " + controller.getColorByType(type) + ";");
            labels.add(label);
        }

        appointmentsListView.getItems().setAll(labels);
    }


    @FXML
    private void handleClose() {
        ((Stage) titleLabel.getScene().getWindow()).close();
    }

}
