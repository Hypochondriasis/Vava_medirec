package org.medirec.medirec.frontend.controller;

import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SettingsController implements Initializable {

	@FXML
	private TextField usernameField;

	@FXML
	private TextField emailField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private ChoiceBox<String> roleChoiceBox;

	@FXML
	private ComboBox<String> doctorSelectComboBox;

	@FXML
	private VBox doctorFields;

	@FXML
	private VBox nurseFields;

	@FXML
	private TextField specializationField;

	@FXML
	private TextField doctorCodeField;

	private ResourceBundle bundle;

	private final ObservableList<String> doctorNames =
		FXCollections.observableArrayList(
			"Dr. Novak",
			"Dr. Zelená",
			"Dr. Horváth",
			"Dr. Čierna"
		);

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		this.bundle = ResourceBundle.getBundle(
			"messages",
			AppSettings.getLocale()
		);

		initRoleChoiceBox();
		initDoctorSelectBox();

		// Presunuté sem – teraz bude doctorList už inicializovaný
		handleRoleChange();
	}

	private void initRoleChoiceBox() {
		roleChoiceBox
			.getItems()
			.addAll(
				bundle.getString("role.admin"),
				bundle.getString("role.doctor"),
				bundle.getString("role.nurse")
			);
		roleChoiceBox.setValue(bundle.getString("role.admin")); // default
		roleChoiceBox.setOnAction(e -> handleRoleChange());
	}

	private void initDoctorSelectBox() {
		ObservableList<String> source = FXCollections.observableArrayList(
			doctorNames
		);
		FilteredList<String> filtered = new FilteredList<>(source, s -> true);

		doctorSelectComboBox.setItems(filtered);
		doctorSelectComboBox.setEditable(true);

		TextField editor = doctorSelectComboBox.getEditor();

		doctorSelectComboBox.setOnHidden(event -> {
			String typedText = editor.getText();
			if (!typedText.isEmpty()) {
				Optional<String> match = source
					.stream()
					.filter(name -> name.equalsIgnoreCase(typedText))
					.findFirst();
				match.ifPresent(doctorSelectComboBox::setValue);
			}
		});

		editor
			.textProperty()
			.addListener((obs, oldVal, newVal) -> {
				final String filter = newVal == null
					? ""
					: newVal.toLowerCase().trim();

				Platform.runLater(() -> {
					filtered.setPredicate(
						item ->
							filter.isEmpty() ||
							item.toLowerCase().contains(filter)
					);

					if (!doctorSelectComboBox.isShowing()) {
						doctorSelectComboBox.show();
					}
				});
			});

		doctorSelectComboBox
			.valueProperty()
			.addListener((obs, oldVal, newVal) -> {
				if (newVal != null && !newVal.isEmpty()) {
					Platform.runLater(() -> editor.setText(newVal));
				}
			});
	}

	@FXML
	private void handleRoleChange() {
		String selectedRole = roleChoiceBox.getValue();

		boolean isDoctor = selectedRole.equals(bundle.getString("role.doctor"));
		boolean isNurse = selectedRole.equals(bundle.getString("role.nurse"));

		doctorFields.setVisible(isDoctor);
		doctorFields.setManaged(isDoctor);

		nurseFields.setVisible(isNurse);
		nurseFields.setManaged(isNurse);
	}

	@FXML
	private void handleAddUser() {
		String username = usernameField.getText();
		String email = emailField.getText();
		String password = passwordField.getText();
		String role = roleChoiceBox.getValue();

		String specialization = specializationField.getText();
		String doctorCode = doctorCodeField.getText();
		String assignedDoctor = doctorSelectComboBox.getEditor().getText();

		StringBuilder errors = new StringBuilder();

		if (username == null || username.trim().isEmpty()) {
			errors
				.append("- ")
				.append(bundle.getString("validation.username"))
				.append("\n");
		}
		if (email == null || email.trim().isEmpty()) {
			errors
				.append("- ")
				.append(bundle.getString("validation.email"))
				.append("\n");
		}
		if (password == null || password.trim().isEmpty()) {
			errors
				.append("- ")
				.append(bundle.getString("validation.password"))
				.append("\n");
		}

		if (role.equals(bundle.getString("role.doctor"))) {
			if (specialization == null || specialization.trim().isEmpty()) {
				errors
					.append("- ")
					.append(bundle.getString("validation.specialization"))
					.append("\n");
			}
			if (doctorCode == null || doctorCode.trim().isEmpty()) {
				errors
					.append("- ")
					.append(bundle.getString("validation.doctorcode"))
					.append("\n");
			}
		}

		if (role.equals(bundle.getString("role.nurse"))) {
			if (assignedDoctor == null || assignedDoctor.trim().isEmpty()) {
				errors
					.append("- ")
					.append(bundle.getString("validation.assigneddoctor"))
					.append("\n");
			} else if (!doctorNames.contains(assignedDoctor)) {
				errors
					.append("- ")
					.append(bundle.getString("validation.invaliddoctor"))
					.append("\n");
			}
		}

		if (errors.length() > 0) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle(bundle.getString("validation.title"));
			alert.setHeaderText(bundle.getString("validation.header"));
			alert.setContentText(errors.toString());
			alert.showAndWait();
			return;
		}

		// TODO: Save user to database or service
		System.out.println(
			"✅ Všetky údaje sú v poriadku, používateľ môže byť pridaný."
		);
	}
}
