package org.medirec.medirec.backend.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {

	public static final String TABLE = "medical_record";

	private int id;
	private int patient_id;
	private int doctor_id;
	private String notes;
	private Timestamp created_at;
	private Timestamp updated_at;
	private String exam_type;
	private String meds;
	@IgnoreColumn
	private List<RecordDiagnosis> diagnosis = new ArrayList<>();
	@IgnoreColumn
	private String displayTitle;
	@IgnoreColumn
	private List<Prescription> prescriptions = new ArrayList<>();

	public MedicalRecord() {
		// Required for reflection
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public int getDoctor_id() {
		return doctor_id;
	}

	public void setDoctor_id(int doctor_id) {
		this.doctor_id = doctor_id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}

	public String getExam_type() {
		return exam_type;
	}

	public MedicalRecord setExam_type(String exam_type) {
		this.exam_type = exam_type;
		return this;
	}

	public String getMeds() {
		return meds;
	}

	public MedicalRecord setMeds(String meds) {
		this.meds = meds;
		return this;
	}

	public List<RecordDiagnosis> getDiagnosis() {
		return diagnosis;
	}

	public MedicalRecord setDiagnosis(List<RecordDiagnosis> diagnosis) {
		this.diagnosis = diagnosis;
		return this;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public MedicalRecord setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
		return this;
	}
	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(List<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

}
