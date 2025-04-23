package com.frontend.controller;

import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class AppointmentPopupController {

	@FXML
	private DatePicker datePicker;

	@FXML
	private ComboBox<String> typeComboBox;

	@FXML
	private ComboBox<String> timeComboBox;

	@FXML
	private Button addButton;

	private CalendarController calendarController;

	@FXML
	private ResourceBundle resources; // Toto načíta bundle automaticky z FXMLLoadera

	@FXML
	private void initialize() {
		datePicker.setValue(LocalDate.now());

		// Naplnenie ComboBox-u s lokalizovanými textami
		typeComboBox.setItems(
			FXCollections.observableArrayList(
				resources.getString("appointment.type.consultation"),
				resources.getString("appointment.type.urgent"),
				resources.getString("appointment.type.examination")
			)
		);

		timeComboBox.getSelectionModel().selectFirst();

		addButton.setOnAction(e -> {
			LocalDate date = datePicker.getValue();
			String type = typeComboBox.getValue();
			String time = timeComboBox.getValue();

			if (
				calendarController != null &&
				date != null &&
				type != null &&
				time != null
			) {
				calendarController.addAppointment(date, type, time);
			}

			((Stage) addButton.getScene().getWindow()).close();
		});
	}

	public void setCalendarController(CalendarController controller) {
		this.calendarController = controller;
	}
}
