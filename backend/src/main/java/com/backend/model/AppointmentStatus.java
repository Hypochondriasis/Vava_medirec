package com.backend.model;

import lombok.Data;

@Data
public class AppointmentStatus {

	public static final String TABLE = "appointment_status";
	private Integer id;
	private String name;

	public AppointmentStatus() {
		this.id = 0;
		this.name = "";
	}
}
