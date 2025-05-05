package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;
import com.example.backend.model.Doctor;
import com.example.backend.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.*;

public class SettingsController implements Initializable {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]{8,}$";


    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private ComboBox<String> doctorSelectComboBox;

    @FXML private VBox doctorFields;
    @FXML private VBox nurseFields;

    @FXML private TextField specializationField;
    @FXML private TextField doctorCodeField;

    private ResourceBundle bundle;
    private Doctor selectedDoctor;
    private User selectedUser;



    private final ObservableList<String> doctorNames = FXCollections.observableArrayList(
            "Dr. Novak", "Dr. Zelená", "Dr. Horváth", "Dr. Čierna"
    );
    private boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }





    private void initDoctorSelectBox() {
        ObservableList<String> source = FXCollections.observableArrayList(doctorNames);
        FilteredList<String> filtered = new FilteredList<>(source, s -> true);

        doctorSelectComboBox.setItems(filtered);
        doctorSelectComboBox.setEditable(true);

        TextField editor = doctorSelectComboBox.getEditor();


        doctorSelectComboBox.setOnHidden(event -> {

            String typedText = editor.getText();
            if (!typedText.isEmpty()) {
                Optional<String> match = source.stream()
                        .filter(name -> name.equalsIgnoreCase(typedText))
                        .findFirst();
                match.ifPresent(doctorSelectComboBox::setValue);
            }
        });

        editor.textProperty().addListener((obs, oldVal, newVal) -> {
            final String filter = newVal == null ? "" : newVal.toLowerCase().trim();

            Platform.runLater(() -> {
                filtered.setPredicate(item ->
                        filter.isEmpty() || item.toLowerCase().contains(filter)
                );

                if (!doctorSelectComboBox.isShowing()) {
                    doctorSelectComboBox.show();
                }
            });
        });

        doctorSelectComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                Platform.runLater(() -> editor.setText(newVal));
            }
        });
    }


    @FXML
    private void handleRoleChange() {
        doctorFields.setVisible(true);
        doctorFields.setManaged(true);
    }

    @FXML
    private void handleAddUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        String specialization = specializationField.getText();
        String doctorCode = doctorCodeField.getText();

        StringBuilder errors = new StringBuilder();

        if (username == null || username.trim().isEmpty()) {
            errors.append("- ").append(bundle.getString("validation.username")).append("\n");
        }
        if (email == null || email.trim().isEmpty()) {
            errors.append("- ").append(bundle.getString("validation.email")).append("\n");
        } else if (!isEmailValid(email)) {
            errors.append("- ").append(bundle.getString("validation.invalid_email")).append("\n");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.append("- ").append(bundle.getString("validation.password")).append("\n");
        } else if (!isPasswordValid(password)) {
            errors.append("- ").append(bundle.getString("validation.invalid_password")).append("\n");
        }

        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("validation.title"));
            alert.setHeaderText(bundle.getString("validation.header"));
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return;
        }

        createDoctor(username, specialization, doctorCode, email, password);
        System.out.println("✅ Všetky údaje sú v poriadku, používateľ môže byť pridaný.");
    }

    @FXML private ListView<Doctor> doctorListView;

    private final ObservableList<Doctor> doctorList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
        selectedDoctor = new Doctor();

        loaddata();
        // Populate doctor list (normally fetched from DB)

    }
    private void loaddata(){
        doctorList.clear();
        try {
            List<Doctor> zoznam= DatabaseController.getAllDoctors();
            for (Doctor doc : zoznam){
                doctorList.add(doc);
            }
        }
        catch (Exception e ){

        }

        doctorListView.setItems(doctorList);
        doctorListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor doc, boolean empty) {
                super.updateItem(doc, empty);
                setText((empty || doc == null) ? null : doc.getFirst_name() + " " + doc.getLast_name());
            }
        });

        doctorListView.getSelectionModel().selectedItemProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                loadDoctorData(newDoc);
            }
        });
    }

    private void loadDoctorData(Doctor doctor) {
        selectedDoctor = doctor;
        selectedUser = null;

        usernameField.setText(doctor.getFirst_name());
        specializationField.setText(doctor.getSpecialization());
        doctorCodeField.setText(doctor.getCode());

        try {
            List<User> users = DatabaseController.getAllUsersInGroup("doctor_id", doctor.getId());
            for (User user : users) {
                if (user.getRole_id() == 2) { // Assuming 2 = doctor
                    selectedUser = user;
                    emailField.setText(user.getEmail());
                    passwordField.setText(user.getPassword_hash()); // Never show real password
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void createDoctor(String name, String specialization, String code, String email, String password) {
        //String[] parts = name.split(" ", 2);
        selectedDoctor.setFirst_name(name);

        selectedDoctor.setSpecialization(specialization);
        selectedDoctor.setCode(code);
        try {
            DatabaseController.saveOrUpdateDoctor(selectedDoctor);
            if (selectedUser == null) {
                selectedUser = new User();
                selectedUser.setRole_id(2);
                selectedUser.setDoctor_id(selectedDoctor.getId());
            }
            selectedUser.setName(name);
            selectedUser.setDoctor_id(selectedDoctor.getId());
            selectedUser.setRole_id(2);
            selectedUser.setEmail(email);

            selectedUser.setPassword_hash(password);
            DatabaseController.saveOrUpdateUser(selectedUser);
        }
        catch (Exception e ){

        }
    loaddata();
    }

}
