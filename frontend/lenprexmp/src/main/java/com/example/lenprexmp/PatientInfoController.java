package com.example.lenprexmp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PatientInfoController {

    @FXML
    private ListView<String> patientListView;
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

    private final ObservableList<String> patients = FXCollections.observableArrayList();

    public void initialize() {
        setupPatients();
        setupTableColumns();
        initializeButtons();
    }
    //buttony pre fungovanie ctrl + klavesa
    public void initializeButtons(){
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F2:
                    handleSearchPatient();
                    break;
                case F3:
                    handleOpenFunctions();
                    break;
                case F5:
                    handleWithoutCard();
                    break;
                case F6:
                    handlePrintDetails();
                    break;
                case F7:
                    handleViewOutputs();
                    break;
                case F9:
                    handleEditPatient();
                    break;
                case DELETE:
                    handleDeleteCard();
                    break;
                case INSERT:
                    handleCreateNewCard();
                    break;
                default:
                    // nič
            }
        });
    }

    private void setupPatients() {
        // setapovanie pacientov na neskorsie zobrazenie
        patients.addAll("Jakub Mráz", "Dominika Poliaková", "Milan Kerestes", "Lenka Oravcová");
        patientListView.setItems(patients);
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
        String selectedPatient = patientListView.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) return;

        // nacitanie detailov pacienta po kliknuti
        nameLabel.setText(selectedPatient);
        rcLabel.setText("1234567890");
        insuranceLabel.setText("24");
        birthLabel.setText("25.09.1989");
        cityLabel.setText("Streženice");
        streetLabel.setText("Cintorínska 12");
        zipLabel.setText("020 01");
        lastExamLabel.setText("05.05.2023");
        checkLabel.setText("-");
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
}
