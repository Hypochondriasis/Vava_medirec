package org.medirec.backend.controller;

import org.medirec.backend.model.*;
import org.medirec.backend.service.DatabaseService;
import org.medirec.backend.util.JwtUtil;

import java.util.List;

public class DatabaseController {

    // ===== Appointment =====
    public static List<Appointment> getAllAppointments() throws Exception {
        User prihlaseny = getUserById(JwtUtil.getUserFromToken().getId());
        return getAllAppointmentsInGroup("doctor_id", prihlaseny.getDoctor_id());
    }

    public static Appointment getAppointmentById(int id) throws Exception {
        return DatabaseService.getOne(Appointment.class, id);
    }

    public static List<Appointment> getMultipleAppointments(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Appointment.class, ids);
    }

    public static Appointment saveOrUpdateAppointment(Appointment appointment) throws Exception {
        return DatabaseService.createOrUpdate(appointment);
    }

    public static void deleteAppointment(int id) throws Exception {
        DatabaseService.delete(Appointment.class, id);
    }

    public static List<Appointment> getAllAppointmentsInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Appointment.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== AppointmentStatus =====
    public static List<AppointmentStatus> getAllAppointmentStatuses() throws Exception {
        return DatabaseService.getAll(AppointmentStatus.class);
    }

    public static AppointmentStatus getAppointmentStatusById(int id) throws Exception {
        return DatabaseService.getOne(AppointmentStatus.class, id);
    }

    public static List<AppointmentStatus> getMultipleAppointmentStatuses(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(AppointmentStatus.class, ids);
    }

    public static AppointmentStatus saveOrUpdateAppointmentStatus(AppointmentStatus status) throws Exception {
        return DatabaseService.createOrUpdate(status);
    }

    public static void deleteAppointmentStatus(int id) throws Exception {
        DatabaseService.delete(AppointmentStatus.class, id);
    }

    public static List<AppointmentStatus> getAllAppointmentStatusesInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(AppointmentStatus.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== Diagnosis =====
    public static List<Diagnosis> getAllDiagnoses() throws Exception {
        return DatabaseService.getAll(Diagnosis.class);
    }

    public static Diagnosis getDiagnosisById(int id) throws Exception {
        return DatabaseService.getOne(Diagnosis.class, id);
    }

    public static List<Diagnosis> getMultipleDiagnoses(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Diagnosis.class, ids);
    }

    public static Diagnosis saveOrUpdateDiagnosis(Diagnosis diagnosis) throws Exception {
        return DatabaseService.createOrUpdate(diagnosis);
    }

    public static void deleteDiagnosis(int id) throws Exception {
        DatabaseService.delete(Diagnosis.class, id);
    }

    public static List<Diagnosis> getAllDiagnosesInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Diagnosis.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== DiagnosisType =====
    public static List<DiagnosisType> getAllDiagnosisTypes() throws Exception {
        return DatabaseService.getAll(DiagnosisType.class);
    }

    public static DiagnosisType getDiagnosisTypeById(int id) throws Exception {
        return DatabaseService.getOne(DiagnosisType.class, id);
    }

    public static List<DiagnosisType> getMultipleDiagnosisTypes(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(DiagnosisType.class, ids);
    }

    public static DiagnosisType saveOrUpdateDiagnosisType(DiagnosisType type) throws Exception {
        return DatabaseService.createOrUpdate(type);
    }

    public static void deleteDiagnosisType(int id) throws Exception {
        DatabaseService.delete(DiagnosisType.class, id);
    }

    public static List<DiagnosisType> getAllDiagnosisTypesInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(DiagnosisType.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== Doctor =====
    public static List<Doctor> getAllDoctors() throws Exception {
        return DatabaseService.getAll(Doctor.class);
    }

    public static Doctor getDoctorById(int id) throws Exception {
        return DatabaseService.getOne(Doctor.class, id);
    }

    public static List<Doctor> getMultipleDoctors(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Doctor.class, ids);
    }

    public static Doctor saveOrUpdateDoctor(Doctor doctor) throws Exception {
        return DatabaseService.createOrUpdate(doctor);
    }

    public static void deleteDoctor(int id) throws Exception {
        DatabaseService.delete(Doctor.class, id);
    }

    public static List<Doctor> getAllDoctorsInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Doctor.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== MedicalRecord =====
    public static List<MedicalRecord> getAllMedicalRecords() throws Exception {
        User prihlaseny = getUserById(JwtUtil.getUserFromToken().getId());
        return getAllMedicalRecordsInGroup("doctor_id", prihlaseny.getDoctor_id());

    }

    public static MedicalRecord getMedicalRecordById(int id) throws Exception {
        return DatabaseService.getOne(MedicalRecord.class, id);
    }

    public static List<MedicalRecord> getMultipleMedicalRecords(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(MedicalRecord.class, ids);
    }

    public static MedicalRecord saveOrUpdateMedicalRecord(MedicalRecord record) throws Exception {
        return DatabaseService.createOrUpdate(record);
    }

    public static void deleteMedicalRecord(int id) throws Exception {
        DatabaseService.delete(MedicalRecord.class, id);
    }

    public static List<MedicalRecord> getAllMedicalRecordsInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(MedicalRecord.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== Medication =====
    public static List<Medication> getAllMedications() throws Exception {
        return DatabaseService.getAll(Medication.class);
    }

    public static Medication getMedicationById(int id) throws Exception {
        return DatabaseService.getOne(Medication.class, id);
    }

    public static List<Medication> getMultipleMedications(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Medication.class, ids);
    }

    public static Medication saveOrUpdateMedication(Medication medication) throws Exception {
        return DatabaseService.createOrUpdate(medication);
    }

    public static void deleteMedication(int id) throws Exception {
        DatabaseService.delete(Medication.class, id);
    }

    public static List<Medication> getAllMedicationsInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Medication.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== Patient =====
    public static List<Patient> getAllPatients() throws Exception {
        User prihlaseny = getUserById(JwtUtil.getUserFromToken().getId());
        return getAllPatientsInGroup("doctor_id", prihlaseny.getDoctor_id());

    }

    public static Patient getPatientById(int id) throws Exception {
        return DatabaseService.getOne(Patient.class, id);
    }

    public static List<Patient> getMultiplePatients(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Patient.class, ids);
    }

    public static Patient saveOrUpdatePatient(Patient patient) throws Exception {
        return DatabaseService.createOrUpdate(patient);
    }

    public static void deletePatient(int id) throws Exception {
        DatabaseService.delete(Patient.class, id);
    }

    public static List<Patient> getAllPatientsInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Patient.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== Prescription =====
    public static List<Prescription> getAllPrescriptions() throws Exception {
        return DatabaseService.getAll(Prescription.class);
    }

    public static Prescription getPrescriptionById(int id) throws Exception {
        return DatabaseService.getOne(Prescription.class, id);
    }

    public static List<Prescription> getMultiplePrescriptions(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Prescription.class, ids);
    }

    public static Prescription saveOrUpdatePrescription(Prescription prescription) throws Exception {
        return DatabaseService.createOrUpdate(prescription);
    }

    public static void deletePrescription(int id) throws Exception {
        DatabaseService.delete(Prescription.class, id);
    }

    public static List<Prescription> getAllPrescriptionsInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Prescription.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== RecordDiagnosis =====
    public static List<RecordDiagnosis> getAllRecordDiagnoses() throws Exception {
        return DatabaseService.getAll(RecordDiagnosis.class);
    }

    public static RecordDiagnosis getRecordDiagnosisById(int id) throws Exception {
        return DatabaseService.getOne(RecordDiagnosis.class, id);
    }

    public static List<RecordDiagnosis> getMultipleRecordDiagnoses(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(RecordDiagnosis.class, ids);
    }

    public static RecordDiagnosis saveOrUpdateRecordDiagnosis(RecordDiagnosis rd) throws Exception {
        return DatabaseService.createOrUpdate(rd);
    }

    public static void deleteRecordDiagnosis(int id) throws Exception {
        DatabaseService.delete(RecordDiagnosis.class, id);
    }

    public static List<RecordDiagnosis> getAllRecordDiagnosesInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(RecordDiagnosis.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== Role =====
    public static List<Role> getAllRoles() throws Exception {
        return DatabaseService.getAll(Role.class);
    }

    public static Role getRoleById(int id) throws Exception {
        return DatabaseService.getOne(Role.class, id);
    }

    public static List<Role> getMultipleRoles(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Role.class, ids);
    }

    public static Role saveOrUpdateRole(Role role) throws Exception {
        return DatabaseService.createOrUpdate(role);
    }

    public static void deleteRole(int id) throws Exception {
        DatabaseService.delete(Role.class, id);
    }

    public static List<Role> getAllRolesInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Role.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== Token =====
    public static List<Token> getAllTokens() throws Exception {
        return DatabaseService.getAll(Token.class);
    }

    public static Token getTokenById(int id) throws Exception {
        return DatabaseService.getOne(Token.class, id);
    }

    public static List<Token> getMultipleTokens(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(Token.class, ids);
    }

    public static Token saveOrUpdateToken(Token token) throws Exception {
        return DatabaseService.createOrUpdate(token);
    }

    public static void deleteToken(int id) throws Exception {
        DatabaseService.delete(Token.class, id);
    }

    public static List<Token> getAllTokensInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(Token.class, foreignKeyColumn, foreignKeyId);
    }

    // ===== User =====
    public static List<User> getAllUsers() throws Exception {
        User prihlaseny = getUserById(JwtUtil.getUserFromToken().getId());
        return getAllUsersInGroup("doctor_id", prihlaseny.getDoctor_id());
    }

    public static User getUserById(int id) throws Exception {
        return DatabaseService.getOne(User.class, id);
    }

    public static List<User> getMultipleUsers(List<Integer> ids) throws Exception {
        return DatabaseService.getMultiple(User.class, ids);
    }

    public static User saveOrUpdateUser(User user) throws Exception {
        return DatabaseService.createOrUpdate(user);
    }

    public static void deleteUser(int id) throws Exception {
        DatabaseService.delete(User.class, id);
    }

    public static List<User> getAllUsersInGroup(String foreignKeyColumn, int foreignKeyId) throws Exception {
        return DatabaseService.getAllInGroup(User.class, foreignKeyColumn, foreignKeyId);
    }
}
