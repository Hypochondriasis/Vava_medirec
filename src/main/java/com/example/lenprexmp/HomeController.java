package com.example.lenprexmp;


import com.example.backend.controller.DatabaseController;
import com.example.backend.model.Appointment;
import com.example.backend.model.Patient;
import com.example.backend.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController {


    @FXML private Label currentVisitTextArea;
    @FXML private Label completedPatientsTextArea;
    @FXML private Label waitingPatientsTextArea;
    @FXML private Label newRecordsTextArea;


    @FXML private ListView<String> examinationsListView;
    @FXML private ListView<String> tasksListView;


    @FXML private Button newPatientButton;
    @FXML private Button exportDataButton;

    @FXML
    private void initialize() {
        // Simulované naplnenie údajov

        populateExaminations();
        //populateTasks();

    }

    private void populateDashboard(Integer visits, Integer completed, Integer waiting ) {
        currentVisitTextArea.setText(visits.toString());
        completedPatientsTextArea.setText(completed.toString());
        waitingPatientsTextArea.setText(waiting.toString());
        //newRecordsTextArea.setText("7");
    }

    private void populateExaminations() {
        //naplnit appointmentami
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Get today's appointments
        try {
            List<Appointment> appointmentsToday = DatabaseService.getAllInGroupDate(Appointment.class, "appointment_date", date);
            ArrayList<Integer> ids = new ArrayList<>();
            for (Appointment app : appointmentsToday){
                ids.add(app.getPatient_id());
            }
            List<Patient> pacienti = DatabaseController.getMultiplePatients(ids);

            // Build a map for quick lookup
            Map<Integer, Patient> patientMap = pacienti.stream()
                    .collect(Collectors.toMap(Patient::getId, p -> p));
            int totalVisits = 0;
            int completed = 0;
            int waiting = 0;
            ArrayList<String> zoznam = new ArrayList<>();
            for (Appointment app : appointmentsToday) {
                Patient patient = patientMap.get(app.getPatient_id());
                if (patient != null) {
                    String str = app.getTime() + " " + patient.getFirst_name()+" "+patient.getLast_name()+ " " + app.getReason();
                    zoznam.add(str);
                }
                totalVisits++;
                if (app.getAppointment_status_id() == 2) {
                    completed++;
                } else if (app.getAppointment_status_id() == 1) {
                    waiting++;
                }
            }
            examinationsListView.getItems().addAll(
                    zoznam
            );
            populateDashboard(totalVisits, completed, waiting);
        }
        catch(Exception e){

        }

    }

    private void populateTasks() {
        // naplnit taskami na den
        tasksListView.getItems().addAll(
                "Odoslať laboratórne vzorky",
                "Skontrolovať výsledky testov",
                "Zavolať pacientovi",
                "Pripraviť dokumentáciu"
        );

        tasksListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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


                            setStyle("-fx-border-color: transparent transparent transparent " + color + ";" +
                                    "-fx-border-width: 0 0 0 5px; -fx-padding: 5 10;");

                            setGraphic(checkBox);
                            setText(null);
                        }
                    }
                };
            }
        });
    }
@FXML
    public void onNewPatient() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPatientDialog.fxml"), bundle);
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
