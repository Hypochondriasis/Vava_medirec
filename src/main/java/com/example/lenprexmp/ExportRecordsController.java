package com.example.lenprexmp;

import com.example.backend.controller.DatabaseController;
import com.example.backend.model.MedicalRecord;
import com.example.backend.model.Point;
import com.example.backend.model.RecordPoint;
import com.example.backend.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ExportRecordsController {

    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private Button exportButton;

    @FXML
    private void initialize() {
        yearComboBox.getItems().addAll(IntStream.rangeClosed(2000, 2030).boxed().toList());
        for (Month month : Month.values()) {
            monthComboBox.getItems().add(month.name());
        }
    }

    @FXML
    private void handleExport() {
        Integer selectedYear = yearComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();

        if (selectedYear == null || selectedMonth == null) {
            showAlert("Please select both year and month.");
            return;
        }

        try {

            Map<Integer, Point> dict = new HashMap<>();
            List<Point> points = DatabaseController.getAllPoints();
            for (Point po : points){
                dict.put(po.getId(),po);
            }
            int monthValue = Month.valueOf(selectedMonth).getValue();


            LocalDate startDate = LocalDate.of(selectedYear, monthValue, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

// Convert to Timestamp
            Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
            Timestamp endTimestamp = Timestamp.valueOf(endDate.atTime(LocalTime.MAX));

// use those in your query
            List<MedicalRecord> records = DatabaseService.getAllInDateRangeTimestamp(
                    MedicalRecord.class,
                    "created_at",
                    startTimestamp,
                    endTimestamp
            );
            System.out.println(records);
            // Get all medical records in the selected date range

            // Optionally, preload all record points in the date range as well
            List<RecordPoint> allPoints = DatabaseController.getAllRecordPoints();

            // Generate XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("MedicalRecords");
            doc.appendChild(rootElement);

            for (MedicalRecord record : records) {
                Element recordElement = doc.createElement("MedicalRecord");
                recordElement.setAttribute("id", String.valueOf(record.getId()));

                // Filter points that belong to this record
                for (RecordPoint point : allPoints) {
                    if (point.getMedical_record_id() == record.getId()) {
                        Point body = dict.get(point.getPoint_id());
                        Element pointElement = doc.createElement("RecordPoint");
                        pointElement.setAttribute("type",body.getLabel());
                        pointElement.setTextContent(body.getPrice().toString());
                        recordElement.appendChild(pointElement);
                    }
                }

                rootElement.appendChild(recordElement);
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save XML File");
            fileChooser.setInitialFileName("records.xml");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
            File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

            if (file != null) {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(new DOMSource(doc), new StreamResult(file));
                showAlert("XML exported successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred during export.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
