package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;

import com.example.backend.model.Diagnosis;
import com.example.backend.model.Medication;
import com.example.backend.model.Patient;
import com.example.backend.model.Point;
import com.example.backend.model.Prescription;
import com.example.backend.model.MedicalRecord;
import com.example.backend.model.RecordDiagnosis;
import com.example.backend.model.RecordPoint;
import com.example.backend.util.JwtUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MedicalRecordsController {

    // Top section
    @FXML private TextField searchField;
    @FXML private Label nameLabel;
    @FXML private TextArea basicInfo;

    // New Record Form
    @FXML private TextArea notesArea;

    @FXML private ComboBox<Medication> medicationCombo;
    @FXML private TextField dosageField, frequencyField;
    @FXML private ListView<String> prescriptionList;

    @FXML private ComboBox<Diagnosis> diagnosisCombo;
    @FXML private ListView<String> diagnosisList;

    @FXML private ComboBox<Point> pointCombo;
    @FXML private ListView<String> pointList;

    // History view
    @FXML private ScrollPane scrollPane;
    @FXML private VBox recordsContainer;

    private Patient currentPatient;

    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<Diagnosis> diagnoses = new ArrayList<>();
    private final List<Point> points = new ArrayList<>();

    private final ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

    public static class RecordData {
        public MedicalRecord record;
        public List<Diagnosis> diagnoses = new ArrayList<>();
        public List<Prescription> prescriptions = new ArrayList<>();
        public List<Point> points = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        try {
            // Load medications
            List<Medication> medications = DatabaseController.getAllMedications();
            medicationCombo.getItems().addAll(medications);
            medicationCombo.setCellFactory(cb -> new ListCell<>() {
                @Override protected void updateItem(Medication m, boolean empty) {
                    super.updateItem(m, empty);
                    setText(empty || m == null ? "" : m.getName());
                }
            });
            medicationCombo.setButtonCell(new ListCell<>() {
                @Override protected void updateItem(Medication m, boolean empty) {
                    super.updateItem(m, empty);
                    setText(empty || m == null ? "" : m.getName());
                }
            });

            // Load diagnoses
            List<Diagnosis> diagnosisList = DatabaseController.getAllDiagnoses();
            diagnosisCombo.getItems().addAll(diagnosisList);
            diagnosisCombo.setCellFactory(cb -> new ListCell<>() {
                @Override protected void updateItem(Diagnosis d, boolean empty) {
                    super.updateItem(d, empty);
                    setText(empty || d == null ? "" : d.getCode() + " - " + d.getName());
                }
            });
            diagnosisCombo.setButtonCell(new ListCell<>() {
                @Override protected void updateItem(Diagnosis d, boolean empty) {
                    super.updateItem(d, empty);
                    setText(empty || d == null ? "" : d.getCode() + " - " + d.getName());
                }
            });

            // Load points
            List<Point> pointList = DatabaseController.getAllPoints();
            pointCombo.getItems().addAll(pointList);
            pointCombo.setCellFactory(cb -> new ListCell<>() {
                @Override protected void updateItem(Point p, boolean empty) {
                    super.updateItem(p, empty);
                    setText(empty || p == null ? "" : p.getLabel() + " (" + p.getPoints() + " pts)");
                }
            });
            pointCombo.setButtonCell(new ListCell<>() {
                @Override protected void updateItem(Point p, boolean empty) {
                    super.updateItem(p, empty);
                    setText(empty || p == null ? "" : p.getLabel() + " (" + p.getPoints() + " pts)");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPatient(Patient patient) {
        this.currentPatient = patient;
        nameLabel.setText(patient.getFirst_name() + " " + patient.getLast_name());
        basicInfo.setText("ID: " + patient.getPersonal_id_number() + "\nPoistenie: " + patient.getInsurance_number());
        renderPatientRecords(patient);
    }


    @FXML
    private void onAddPrescription() {
        Medication selected = medicationCombo.getValue();
        if (selected == null) return;

        Prescription p = new Prescription();
        p.setMedication_id(selected.getId());
        p.setDosage(dosageField.getText());
        p.setFrequency(frequencyField.getText());

        prescriptions.add(p);
        prescriptionList.getItems().add(selected.getName() + " | " + p.getDosage() + " | " + p.getFrequency());




        medicationCombo.getSelectionModel().clearSelection();
        dosageField.clear();
        frequencyField.clear();
    }

    @FXML
    private void onAddDiagnosis() {
        Diagnosis selected = diagnosisCombo.getValue();
        if (selected == null) return;

        diagnoses.add(selected);
        diagnosisList.getItems().add(selected.getCode() + " - " + selected.getName());

        diagnosisCombo.getSelectionModel().clearSelection();
    }

    @FXML
    private void onAddPoint() {
        Point selected = pointCombo.getValue();
        if (selected == null) return;

        points.add(selected);
        pointList.getItems().add(selected.getLabel() + " (" + selected.getPoints() + " pts)");

        pointCombo.getSelectionModel().clearSelection();
    }

    @FXML
    private void onSaveRecord() {
        if (currentPatient == null) {
            showInfo(bundle.getString("select.patient.first"));
            return;
        }

        try {
            MedicalRecord record = new MedicalRecord();
            record.setPatient_id(currentPatient.getId());
            record.setDoctor_id(JwtUtil.getUserFromToken().getDoctor_id());
            record.setNotes(notesArea.getText());


            MedicalRecord saved = DatabaseController.saveOrUpdateMedicalRecord(record);

            for (Prescription p : prescriptions) {
                p.setMedical_record_id(saved.getId());
                DatabaseController.saveOrUpdatePrescription(p);
            }

            for (Diagnosis d : diagnoses) {
                RecordDiagnosis rd = new RecordDiagnosis();
                rd.setMedical_record_id(saved.getId());
                rd.setDiagnosis_id(d.getId());
                DatabaseController.saveOrUpdateRecordDiagnosis(rd);
            }

            for (Point pt : points) {
                RecordPoint rp = new RecordPoint();
                rp.setMedical_record_id(saved.getId());
                rp.setPoint_id(pt.getId());
                DatabaseController.saveOrUpdateRecordPoint(rp);
            }

            clearForm();
            renderPatientRecords(currentPatient);

        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Saving failed.");
        }
    }

    private void clearForm() {
        notesArea.clear();
        prescriptions.clear();
        diagnoses.clear();
        points.clear();
        prescriptionList.getItems().clear();
        diagnosisList.getItems().clear();
        pointList.getItems().clear();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void renderPatientRecords(Patient patient) {
        recordsContainer.getChildren().clear();

        try {
            List<MedicalRecord> records = DatabaseController.getAllMedicalRecordsInGroup("patient_id", patient.getId());
            List<Integer> recordIds = records.stream().map(MedicalRecord::getId).toList();

            Map<Integer, Diagnosis> diagnosisMap = DatabaseController.getAllDiagnoses()
                    .stream().collect(Collectors.toMap(Diagnosis::getId, d -> d));

            Map<Integer, List<Diagnosis>> recordDiagnosisMap = DatabaseController.getAllRecordDiagnoses()
                    .stream().filter(rd -> recordIds.contains(rd.getMedical_record_id()))
                    .collect(Collectors.groupingBy(
                            RecordDiagnosis::getMedical_record_id,
                            Collectors.mapping(rd -> diagnosisMap.get(rd.getDiagnosis_id()), Collectors.toList())
                    ));

            Map<Integer, Medication> medicationMap = DatabaseController.getAllMedications()
                    .stream().collect(Collectors.toMap(Medication::getId, m -> m));

            Map<Integer, List<Prescription>> recordPrescriptionMap = DatabaseController.getAllPrescriptions()
                    .stream()
                    .filter(p -> recordIds.contains(p.getMedical_record_id()))
                    .collect(Collectors.groupingBy(Prescription::getMedical_record_id));


            Map<Integer, Point> pointMap = DatabaseController.getAllPoints()
                    .stream().collect(Collectors.toMap(Point::getId, p -> p));

            Map<Integer, List<Point>> recordPointMap = DatabaseController.getAllRecordPoints()
                    .stream().filter(rp -> recordIds.contains(rp.getMedical_record_id()))
                    .collect(Collectors.groupingBy(
                            RecordPoint::getMedical_record_id,
                            Collectors.mapping(rp -> pointMap.get(rp.getPoint_id()), Collectors.toList())
                    ));

            for (MedicalRecord record : records) {
                RecordData data = new RecordData();
                data.record = record;
                data.diagnoses = recordDiagnosisMap.getOrDefault(record.getId(), Collections.emptyList());
                data.prescriptions = recordPrescriptionMap.getOrDefault(record.getId(), Collections.emptyList());
                data.points = recordPointMap.getOrDefault(record.getId(), Collections.emptyList());
                renderFullRecord(data, medicationMap);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderFullRecord(RecordData data, Map<Integer, Medication> medicationMap){

    VBox box = new VBox();
        box.setSpacing(6);
        box.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Label date = new Label(bundle.getString("exam.date.label") + ": ");
        Label notes = new Label(bundle.getString("notes.label") + ": " + data.record.getNotes());

        VBox diagnosesBox = new VBox(new Label(bundle.getString("diagnoses.label") + ":"));
        data.diagnoses.forEach(d -> diagnosesBox.getChildren().add(new Label("• " + d.getCode() + " - " + d.getName())));

        VBox prescriptionsBox = new VBox(new Label(bundle.getString("prescriptions.label") + ":"));
        for (Prescription p : data.prescriptions) {
            String medName = medicationMap.getOrDefault(p.getMedication_id(), new Medication()).getName();
            prescriptionsBox.getChildren().add(
                    new Label("• " + medName + " | " + p.getDosage() + " | " + p.getFrequency()));
        }


        VBox pointsBox = new VBox(new Label(bundle.getString("points.label") + ":"));
        data.points.forEach(p -> pointsBox.getChildren().add(
                new Label("• " + p.getLabel() + " (" + p.getPoints() + " pts)"))
        );

        box.getChildren().addAll(date, notes, diagnosesBox, prescriptionsBox, pointsBox);
        recordsContainer.getChildren().add(0, box);
    }
}
