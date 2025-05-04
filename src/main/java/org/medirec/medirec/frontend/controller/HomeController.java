package org.medirec.medirec.frontend.controller;

import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HomeController {

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
	private void initialize() {
		// Simulované naplnenie údajov
		populateDashboard();
		populateExaminations();
		populateTasks();
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
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(
				"messages",
				AppSettings.getLocale()
			);

			FXMLLoader loader = new FXMLLoader(
				getClass().getResource("AddPatientDialog.fxml"),
				bundle
			);
			Scene scene = new Scene(loader.load());

			AddPatientController controller = loader.getController();
			controller.setParentController(this);
			controller.setBundle(bundle);

			Stage popup = new Stage();
			popup.setTitle("AddPatientDialogTitle");
			popup.setScene(scene);
			popup.setResizable(false);

			popup.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void onExportData() {
		// neviem presene co chcu
		System.out.println("Dáta boli exportované.");
	}
}
