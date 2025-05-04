package org.medirec.medirec.backend.model;

import lombok.Data;

@Data
public class RecordDiagnosis {

	public static final String TABLE = "record_diagnosis";
	private Integer id;
	private Integer medical_record_id;
	private Integer diagnosis_id;
	@IgnoreColumn
	private MedicalRecord medical_record;
	@IgnoreColumn
	private Diagnosis diagnosis;

	public RecordDiagnosis() {
		this.id = 0;
		this.medical_record_id = 0;
		this.diagnosis_id = 0;
	}
}
