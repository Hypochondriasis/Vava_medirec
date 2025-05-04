package org.medirec.medirec.backend.model;

import java.sql.Date;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Appointment {

	public static final String TABLE = "appointment";
	private Integer id;
	private Integer patient_id;
	private Integer doctor_id;
	private Date appointment_date;
	private String reason;
	private Integer appointment_status_id;
	@IgnoreColumn
	private AppointmentStatus appointment_status;
	@IgnoreColumn
	private Doctor doctor;
	@IgnoreColumn
	private Patient patient;

	public Appointment() {
		this.id = 0;
		this.patient_id = 0;
		this.doctor_id = 0;
		this.reason = "";
		this.appointment_status_id = 0;
	}
}
