package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;
import com.example.backend.model.Patient;
import com.example.backend.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.ResourceBundle;

public class PatientInfoController {

    @FXML
    private ListView<Patient> patientListView;
    @FXML
    private Label nameLabel, rcLabel, insuranceLabel, birthLabel, cityLabel, streetLabel, zipLabel, lastExamLabel, checkLabel;
    /* potreba vytvorit nejaku triedu aby sa to dalo pekne vyuzit v tableviuw
    @FXML
    private TableView<PatientRecord> examinedTable1;
    @FXML
    private TableColumn<PatientRecord, String> examinedTableTime, examinedTableName, examinedTableBirthNumber;
    @FXML
    private TableView<PatientRecord> scheduledTable;
    @FXML
    private TableColumn<PatientRecord, String> scheduledTableTime, scheduledTableName, scheduledTableBirthNumber;
     */
    @FXML
    private FlowPane buttonFlow;
    @FXML
    private HBox root;

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();

    public void initialize() {
        setupPatients();
        setupTableColumns();
        initializeButtons();
    }

    //buttony pre fungovanie ctrl + klavesa
    public void initializeButtons(){
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {

                default:
                    // nič
            }
        });
    }

    private void setupPatients() {
        // setapovanie pacientov na neskorsie zobrazenie
        try{
            List<Patient> zoznam = DatabaseController.getAllPatients();
            for (Patient pat : zoznam) {
                patients.add(pat);
            }

        }
        catch(Exception e){

        }

        patientListView.setItems(patients);

        patientListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Patient selected = patientListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    loadDetail(event);
                }
            } else if (event.getClickCount() == 2) {
                Patient selected = patientListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    onNewPatient(selected);
                }
            }
        });

        patientListView.setOnKeyPressed(event -> {
            Patient selected = patientListView.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            switch (event.getCode()) {
                case ENTER -> onNewPatient(selected);
                case SPACE -> otvorit_vysetrenia(selected);
            }
        });
    }
    public void reloadPatients() {
        try {
            patients.clear(); // Clear current list
            List<Patient> updatedList = DatabaseController.getAllPatients(); // Fetch new list
            patients.addAll(updatedList); // Add new patients to observable list
        } catch (Exception e) {
            e.printStackTrace(); // Handle error as needed
        }
    }
    private void otvorit_vysetrenia(Patient selected){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MedicalRecordsView.fxml"));
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
            loader.setResources(bundle);

            Scene scene = new Scene(loader.load());
            MedicalRecordsController controller = loader.getController();

            controller.setPatient(selected); // ✅ send selected patient

            Stage stage = new Stage();
            stage.setTitle(bundle.getString("patient.records.title")); // example key
            stage.setScene(scene);
            stage.setMaximized(true);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupTableColumns() {
        /* nahravanie do table columms
        examinedTableTime.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        examinedTableName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        examinedTableBirthNumber.setCellValueFactory(cellData -> cellData.getValue().birthNumberProperty());

        scheduledTableTime.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        scheduledTableName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        scheduledTableBirthNumber.setCellValueFactory(cellData -> cellData.getValue().birthNumberProperty());
         */
    }

    @FXML
    private void loadDetail(MouseEvent event) {
        Patient selectedPatient = patientListView.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) return;

        // nacitanie detailov pacienta po kliknuti
        nameLabel.setText(selectedPatient.getFirst_name() + " "+ selectedPatient.getLast_name());
        rcLabel.setText(selectedPatient.getPersonal_id_number());
        insuranceLabel.setText(selectedPatient.getInsurance_number());
        birthLabel.setText(selectedPatient.getBirth_date().toString());
        cityLabel.setText(selectedPatient.getPermanent_city());
        streetLabel.setText(selectedPatient.getStreet());
        zipLabel.setText(selectedPatient.getPostal_code());

/*
        // Load patient records into the tables (simulated)
        examinedTable1.setItems(FXCollections.observableArrayList(
                new PatientRecord("9:00", "Tomáš Kováč", "123456789"),
                new PatientRecord("9:40", "Martin Nevhíbel", "236447879")
        ));

        scheduledTable.setItems(FXCollections.observableArrayList(
                new PatientRecord("11:30", "Peter Novotný", "236477432"),
                new PatientRecord("12:30", "Jozef Tichý", "041163434")
        ));
        */

    }


    @FXML
    private void handleCreateNewCard() {
        // Otvorí formulár na vytvorenie novej zdravotnej karty pacienta
        // Môže otvoriť nové popup okno s prázdnymi poliami na vyplnenie
    }

    @FXML
    private void handleDeleteCard() {
        // Vymaže zdravotnú kartu aktuálne vybraného pacienta
        // Môže obsahovať potvrdenie pomocou Alertu (YES/NO)
    }

    @FXML
    private void handleSearchPatient() {
        // Otvorí vyhľadávací popup kde sa môže filtrovať podľa mena, RČ, atď.
        // Výsledky sa zobrazia v `patientListView`
    }

    @FXML
    private void handleOpenFunctions() {
        // Rozšírené funkcie ako export, synchronizácia, história atď.
        // Otvorí nové okno s prehľadom dostupných operácií
    }

    @FXML
    private void handleWithoutCard() {
        // Zobrazí základné údaje aj pre pacienta, ktorý nemá zdravotnú kartu
        // Môže ísť o návštevníka alebo jednorazový záznam
    }

    @FXML
    private void handlePrintDetails() {
        // Vytlačí alebo exportuje detail pacienta do PDF (alebo iný formát)
        // Môže použiť knižnicu pre generovanie dokumentov
        System.out.println("aaaaa");
    }

    @FXML
    private void handleViewOutputs() {
        // Zobrazí výstupy ako výsledky vyšetrení, záznamy, správy
        // Otvorí nové okno alebo tabuľku s výpisom
    }

    @FXML
    private void handleEditPatient() {
        // Umožní upraviť údaje aktuálneho pacienta
        // Po úprave a uložení sa data aktualizujú aj na UI

    }
    @FXML
    private void handleAddPatient(){


        onNewPatient(new Patient());
    }
    @FXML
    public void onNewPatient(Patient vstup) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPatientDialog.fxml"), bundle);
            Scene scene = new Scene(loader.load());

            AddPatientController controller = loader.getController();
            controller.setParentControllerPatient(this);
            controller.setPatient(vstup);
            controller.setBundle(bundle);


            Stage popup = new Stage();
            popup.setTitle("AddPatientDialogTitle");
            popup.setScene(scene);
            popup.setResizable(false);

            popup.showAndWait();
            reloadPatients();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
