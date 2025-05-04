package org.medirec.medirec.backend.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import org.medirec.medirec.backend.model.*;

/**
 * BatchXMLExporter - A utility class for exporting complete datasets to XML
 *
 * This class provides functionality to export medical records data in bulk,
 * either as a complete export of the database or as a specific subset of data.
 */
public class BatchXMLExporter {

    // Default export directory
    private static final String DEFAULT_EXPORT_DIR = "exports";

    // XML file names for different entity types
    private static final String PATIENTS_XML = "patients.xml";
    private static final String DOCTORS_XML = "doctors.xml";
    private static final String MEDICAL_RECORDS_XML = "medical_records.xml";
    private static final String MEDICATIONS_XML = "medications.xml";
    private static final String DIAGNOSES_XML = "diagnoses.xml";

    /**
     * Export a complete dataset to XML files
     *
     * @param patients List of patients
     * @param doctors List of doctors
     * @param medicalRecords List of medical records
     * @param medications List of medications
     * @param diagnoses List of diagnoses
     * @param exportDir Directory where the XML files should be saved
     * @return Path to the export directory
     */
    public static String exportCompleteDataset(List<Patient> patients, List<Doctor> doctors, List<MedicalRecord> medicalRecords, List<Diagnosis> diagnoses, List<Medication> medications, String exportDir) throws Exception {
        // Create timestamped export directory if not provided
        if (exportDir == null || exportDir.isEmpty()) {
            exportDir = createTimestampedExportDir();
        } else {
            // Make sure the directory exists
            File dir = new File(exportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        // Export each entity type to a separate XML file
        exportToFile(patients, exportDir + File.separator + PATIENTS_XML);
        exportToFile(doctors, exportDir + File.separator + DOCTORS_XML);
        exportToFile(medicalRecords, exportDir + File.separator + MEDICAL_RECORDS_XML);
        exportToFile(medications, exportDir + File.separator + MEDICATIONS_XML);
        exportToFile(diagnoses, exportDir + File.separator + DIAGNOSES_XML);

        return exportDir;
    }

    /**
     * Export patient data for a specific doctor
     *
     * @param doctor The doctor
     * @param patients List of patients for the doctor
     * @param medicalRecords List of medical records for the doctor's patients
     * @param exportDir Directory where the XML files should be saved
     * @return Path to the export directory
     */
    public static String exportDoctorData(Doctor doctor, List<Patient> patients, List<MedicalRecord> medicalRecords, String exportDir) throws Exception{
        // Create doctor-specific export directory if not provided
        if (exportDir == null || exportDir.isEmpty()) {
            exportDir = createTimestampedExportDir() + File.separator +
                    "doctor_" + doctor.getId() + "_" + doctor.getLast_name();
        } else {
            // Make sure the directory exists
            File dir = new File(exportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        // Exporting Doctor data
        XMLExporter.exportToXMLFile(doctor, exportDir + File.separator + "doctor.xml");

        // Exporting patient and medical record data
        exportToFile(patients, exportDir + File.separator + PATIENTS_XML);
        exportToFile(medicalRecords, exportDir + File.separator + MEDICAL_RECORDS_XML);

        return exportDir;
    }

    /**
     * Export patient data with all related records
     *
     * @param patient The patient
     * @param medicalRecords Medical records for the patient
     * @param exportDir Directory where the XML files should be saved
     * @return Path to the export directory
     */
    public static String exportPatientData(Patient patient, List<MedicalRecord> medicalRecords, String exportDir) throws Exception{
        // Create patient-specific export directory if not provided
        if (exportDir == null || exportDir.isEmpty()) {
            exportDir = createTimestampedExportDir() + File.separator +
                    "patient_" + patient.getId() + "_" + patient.getLast_name();
        } else {
            // Make sure the directory exists
            File dir = new File(exportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        // Exporting patient data
        XMLExporter.exportToXMLFile(patient, exportDir + File.separator + "patient.xml");

        // Exporting medical records
        exportToFile(medicalRecords, exportDir + File.separator + MEDICAL_RECORDS_XML);

        return exportDir;
    }

    /**
     * Helper method to export a collection to a file
     *
     * @param collection The collection to export
     * @param filePath Path where the XML file should be saved
     */
    private static void exportToFile(Collection<?> collection, String filePath) throws Exception{
        if(collection != null && !collection.isEmpty()) {
            XMLExporter.exportToXMLFile(collection, filePath);
        }
    }

    /**
     * Create a timestamped export directory
     *
     * @return Path to the created directory
     */
    private static String createTimestampedExportDir() throws IOException {
        // Creating a timestamp for the export directory
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);

        // Creating the directory
        String dirPath = DEFAULT_EXPORT_DIR + File.separator + "export_" + timestamp;
        File dir = new File(dirPath);

        if (!dir.exists()) {
            // Creating parent directories if they don't exist
            if(!dir.getParentFile().exists()) {
                dir.getParentFile().mkdirs();
            }

            // Create the export directory
            if (!dir.mkdir()) {
                throw new IOException("Failed to create export directory: " + dirPath);
            }
        }
        return dirPath;
    }
}