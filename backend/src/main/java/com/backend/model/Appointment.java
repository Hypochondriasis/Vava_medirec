package com.backend.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Appointment {

	public static final String TABLE = "appointment";
	private Integer id;
	private Integer patient_id;
	private Integer doctor_id;
	private LocalDate appointment_date;
	private String reason;
	private Integer appointment_status_id;

	public Appointment() {
		this.id = 0;
		this.patient_id = 0;
		this.doctor_id = 0;
		this.appointment_date = LocalDate.now();
		this.reason = "";
		this.appointment_status_id = 0;
	}
}
