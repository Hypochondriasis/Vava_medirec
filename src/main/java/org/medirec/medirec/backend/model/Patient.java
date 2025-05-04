package org.medirec.medirec.backend.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Patient {

	public static final String TABLE = "patient";
	private Integer id;
	private String first_name;
	private String last_name;
	private Date birth_date;
	private String personal_id_number;
	private String gender;
	private String email;
	private String phone_number;
	private String birth_city;
	private String permanent_city;
	private String street;
	private String postal_code;
	private String insurance_number;
	private Timestamp created_at;
	private Time updated_at;
	private String blood_pressure;
	private Float weight;
	private Float height;
	private Float bmi;
	private Integer doctor_id;
	@IgnoreColumn
	private Doctor doctor;
	@IgnoreColumn
	private ArrayList<Appointment> appointments;

	public Patient() {
		this.id = 0;
		this.first_name = "";
		this.last_name = "";
		this.personal_id_number = "";
		this.gender = "";
		this.email = "";
		this.phone_number = "";
		this.birth_city = "";
		this.permanent_city = "";
		this.street = "";
		this.postal_code = "";
		this.insurance_number = "";
		this.blood_pressure = "";
		this.weight = 0.0F;
		this.height = 0.0F;
		this.bmi = 0.0F;
		this.doctor_id = 0;
	}
}
