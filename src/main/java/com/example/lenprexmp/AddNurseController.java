package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;
import com.example.backend.model.User;
import com.example.backend.util.JwtUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.ResourceBundle;

public class AddNurseController {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]{8,}$";

    @FXML private Button addUserButton;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;
    @FXML private ListView<User> userListView;
    private User selectedUser;
    private final ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        selectedUser = new User();
        selectedUser.setRole_id(3);

        loaddata();
        // Dummy users

    }
    private void loaddata(){
        users.clear();
        try {
            List<User> pouzivatelia = DatabaseController.getAllUsers();
            for(User usr: pouzivatelia){
                users.add(usr);
            }
        }
        catch (Exception e){

        }




        userListView.setItems(users);
        userListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getName());
            }
        });

        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser != null) {
                loadUserIntoForm(newUser);
            }
        });
    }

    private void loadUserIntoForm(User user) {
        selectedUser = user;
        usernameField.setText(user.getName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword_hash()); // You may decide to leave this empty
    }
    private boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }

    private void createUser(String email, String name, String password) {


        selectedUser.setEmail(email);
        selectedUser.setName(name);
        selectedUser.setPassword_hash(password); // or dummy value

        try{
            selectedUser.setDoctor_id(JwtUtil.getUserFromToken().getDoctor_id());
            DatabaseController.saveOrUpdateUser(selectedUser);
        }
        catch (Exception e ){

        }
        loaddata();

    }

    @FXML
    private void handleAddUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

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

        createUser(email, username, password);
        System.out.println("✅ User data valid. Can be saved.");
    }

}
