package org.medirec.medirec.frontend.controller;

import java.time.LocalDate;

public class Appointment {

	private final LocalDate date;
	private final String type;
	private final String time;

	public Appointment(LocalDate date, String type, String time) {
		this.date = date;
		this.type = type;
		this.time = time;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public String getTime() {
		return time;
	}

	public String toDisplay() {
		return time + " - " + type;
	}
}
