package org.medirec.medirec.backend.model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Data;

@Data
public class MedicalRecord {

	public static final String TABLE = "medical_record";
	private Integer id;
	private Integer patient_id;
	private Integer doctor_id;
	private String notes;
	private Time created_at;
	private Time updated_at;
	@IgnoreColumn
	private Doctor doctor;
	@IgnoreColumn
	private Patient patient;
	@IgnoreColumn
	private ArrayList<Prescription> prescriptions;
	@IgnoreColumn
	private ArrayList<RecordDiagnosis> recordDiagnoses;

	public MedicalRecord() {
		this.id = 0;
		this.patient_id = 0;
		this.doctor_id = 0;
		this.notes = "";
	}
}
