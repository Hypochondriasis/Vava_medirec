package org.medirec.medirec.backend.util;

import org.medirec.medirec.backend.model.Doctor;
import org.medirec.medirec.backend.model.Patient;
import org.medirec.medirec.backend.model.User;

import java.io.File;
import java.io.InputStream;

/**
 * PatientImporter - A utility class for importing patient data from XML
 *
 * This class provides simplified methods for importing patient data from XML files
 * when a doctor user is logged in.
 */
public class PatientImporter {
    /**
     * Import a patient from an XML file for the currently logged in doctor
     *
     * @param file The XML file containing patient data
     * @param user The currently logged in user (must be a doctor)
     * @return The imported Patient object
     */
    public static Patient importPatientFromXML(File file, User user) throws Exception {
        validateDoctorUser(user);
        return XMLImporter.importPatientFromXML(file, user.getDoctor_id());
    }

    /**
     * Import a patient from an XML input stream for the currently logged in doctor
     *
     * @param inputStream The XML input stream containing patient data
     * @param user The currently logged in user (must be a doctor)
     * @return The imported Patient object
     */
    public static Patient importPatientFromXML(InputStream inputStream, User user) throws Exception {
        validateDoctorUser(user);
        return XMLImporter.importPatientFromXML(inputStream, user.getDoctor_id());
    }

    /**
     * Import a patient from an XML file for a specific doctor
     *
     * @param file The XML file containing patient data
     * @param doctor The doctor to assign the patient to
     * @return The imported Patient object
     */
    public static Patient importPatientFromXML(File file, Doctor doctor) throws Exception {
        if (doctor == null || doctor.getId() == null || doctor.getId() <= 0) {
            throw new IllegalArgumentException("Invalid doctor provided");
        }
        return XMLImporter.importPatientFromXML(file, doctor.getId());
    }

    /**
     * Import a patient from an XML input stream for a specific doctor
     *
     * @param inputStream The XML input stream containing patient data
     * @param doctor The doctor to assign the patient to
     * @return The imported Patient object
     */
    public static Patient importPatientFromXML(InputStream inputStream, Doctor doctor) throws Exception {
        if (doctor == null || doctor.getId() == null || doctor.getId() <= 0) {
            throw new IllegalArgumentException("Invalid doctor provided");
        }
        return XMLImporter.importPatientFromXML(inputStream, doctor.getId());
    }

    /**
     * Validate that the user is a doctor
     *
     * @param user The user to validate
     * @throws IllegalArgumentException if the user is not a doctor
     */
    private static void validateDoctorUser(User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getRole() == null || !user.getRole().getName().equals("Doctor")) {
            throw new IllegalArgumentException("User must be a doctor to import patients");
        }

        if (user.getDoctor_id() == null || user.getDoctor_id() <= 0) {
            throw new IllegalArgumentException("User does not have a valid doctor ID");
        }
    }
}
