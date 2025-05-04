package org.medirec.medirec.frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.medirec.medirec.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.ResourceBundle;

public class PatientInfoController {
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(PatientInfoController.class);
	//User session
	private User user;

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
	@FXML
	private Label patientsListLabel;
	@FXML
	private Label patientDetailsLabel;
	@FXML
	private Label patientNameLabel;
	@FXML
	private Label patientRCLabel;
	@FXML
	private Label patientInsuranceLabel;
	@FXML
	private Label patientBirthLabel;
	@FXML
	private Label patientCityLabel;
	@FXML
	private Label patientStreetLabel;
	@FXML
	private Label patientZipLabel;
	@FXML
	private Label patientLastExamLabel;
	@FXML
	private Label patientCheckLabel;
	@FXML
	private Button newCardButton;
	@FXML
	private Button deleteCardButton;
	@FXML
	private Button searchButton;
	@FXML
	private Button functionsButton;
	@FXML
	private Button withoutCardButton;
	@FXML
	private Button printButton;
	@FXML
	private Button outputsButton;
	@FXML
	private Button editButton;
	@FXML
	private Label patientTableLabel;
	@FXML
	private TableColumn<Patient, String> examinedTableTime;
	@FXML
	private TableColumn<Patient, String> examinedTableName;
	@FXML
	private TableColumn<Patient, String> examinedTableBirthNumber;
	@FXML
	private Label scheduledLabel;
	@FXML
	private TableColumn<Patient, String> scheduledTableTime;
	@FXML
	private TableColumn<Patient, String> scheduledTableName;
	@FXML
	private TableColumn<Patient, String> scheduledTableBirthNumber;

	private final ObservableList<String> patients =
		FXCollections.observableArrayList();

	// Locale
	private Locale currentLocale = Locale.getDefault();

	public void initialize() {
		setupPatients();
		setupTableColumns();
		initializeButtons();
		currentLocale = AppSettings.getLocale();
		updateUILanguage(currentLocale);
	}

	//buttony pre fungovanie ctrl + klavesa
	public void initializeButtons() {
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
			}
		});
	}

	private void setupPatients() {
		// setapovanie pacientov na neskorsie zobrazenie
		patients.addAll(
			"Jakub Mráz",
			"Dominika Poliaková",
			"Milan Kerestes",
			"Lenka Oravcová"
		);
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
		String selectedPatient = patientListView
			.getSelectionModel()
			.getSelectedItem();
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

	protected void onLanguageSwitch(){
		// Toggle to local
		Locale current = AppSettings.getLocale();
		Locale next = current.getLanguage().equals("sk") ? Locale.ENGLISH : new Locale("sk", "SK");
		AppSettings.setLocale(next);

		// Updating the UI text without needing to reload the pane
		updateUILanguage(next);
	}

	protected void updateUILanguage(Locale locale) {
		try {
			// Getting the new resource bundle
			ResourceBundle newBundle = ResourceBundle.getBundle("org.medirec.medirec.frontend.messages", locale);
			if (patientsListLabel != null) {
				patientsListLabel.setText(newBundle.getString("patient.list.title"));
			}
			if (patientDetailsLabel != null) {
				patientDetailsLabel.setText(newBundle.getString("patient.details.title"));
			}
			if (patientNameLabel != null) {
				patientNameLabel.setText(newBundle.getString("patient.label.name"));
			}
			if (patientRCLabel != null) {
				patientRCLabel.setText(newBundle.getString("patient.label.rc"));
			}
			if (patientInsuranceLabel != null) {
				patientInsuranceLabel.setText(newBundle.getString("patient.label.insurance"));
			}if (patientBirthLabel != null) {
				patientBirthLabel.setText(newBundle.getString("patient.label.birth"));
			}
			if (patientCityLabel != null){
				patientCityLabel.setText(newBundle.getString("patient.label.city"));
			}
			if (patientStreetLabel != null) {
				patientStreetLabel.setText(newBundle.getString("patient.label.street"));
			}
			if (patientZipLabel != null) {
				patientZipLabel.setText(newBundle.getString("patient.label.zip"));
			}
			if (patientLastExamLabel != null) {
				patientLastExamLabel.setText(newBundle.getString("patient.label.lastExam"));
			}
			if (patientCheckLabel != null) {
				patientCheckLabel.setText(newBundle.getString("patient.label.check"));
			}
			if (newCardButton != null) {
				newCardButton.setText(newBundle.getString("patient.label.newCard"));
			}
			if (deleteCardButton != null) {
				deleteCardButton.setText(newBundle.getString("patient.label.deleteCard"));
			}
			if (searchButton != null){
				searchButton.setText(newBundle.getString("patient.label.search"));
			}
			if (functionsButton != null){
				functionsButton.setText(newBundle.getString("patient.label.functions"));
			}
			if (withoutCardButton != null){
				withoutCardButton.setText(newBundle.getString("patient.label.withoutCard"));
			}
			if (printButton != null){
				printButton.setText(newBundle.getString("patient.label.print"));
			}
			if (outputsButton != null){
				outputsButton.setText(newBundle.getString("patient.label.outputs"));
			}
			if (editButton != null){
				editButton.setText(newBundle.getString("patient.label.edit"));
			}
			if (patientTableLabel != null){
				patientTableLabel.setText(newBundle.getString("patient.table.examined.title"));
			}
			if (examinedTableName != null){
				examinedTableName.setText(newBundle.getString("patient.table.column.name"));
			}
			if (examinedTableTime != null){
				examinedTableTime.setText(newBundle.getString("patient.table.column.time"));
			}
			if (examinedTableBirthNumber != null){
				examinedTableBirthNumber.setText(newBundle.getString("patient.table.column.rc"));
			}
			if (scheduledLabel != null){
				scheduledLabel.setText(newBundle.getString("patient.table.scheduled.title"));
			}
			if (scheduledTableTime != null){
				scheduledTableTime.setText(newBundle.getString("patient.table.column.time"));
			}
			if (scheduledTableName != null){
				scheduledTableName.setText(newBundle.getString("patient.table.column.name"));
			}
			if (scheduledTableBirthNumber != null){
				scheduledTableBirthNumber.setText(newBundle.getString("patient.table.column.rc"));
			}
		}catch (java.util.MissingResourceException e){
			logger.error("Missing resource key: {}", e.getKey());
		}catch (Exception e) {
			logger.error("Failed to update UI language", e);
		}
	}

	protected User getUser() {
		return this.user;
	}

	protected void setUser(User user) {
		this.user = user;
	}
}
