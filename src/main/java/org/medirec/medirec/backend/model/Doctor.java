package org.medirec.medirec.backend.model;

import lombok.Data;
import org.medirec.medirec.backend.model.MedicalRecord;

import java.util.ArrayList;

@Data
public class Doctor {

	public static final String TABLE = "doctor";
	private Integer id;
	private String first_name;
	private String last_name;
	private String specialization;
	private String code;
	@IgnoreColumn
	private ArrayList<MedicalRecord> medicalRecords;
	@IgnoreColumn
	private ArrayList<Appointment> appointments;

	public Doctor() {
		this.id = 0;
		this.first_name = "";
		this.last_name = "";
		this.specialization = "";
		this.code = "";
	}
}
