package org.medirec.medirec.frontend.controller;

import lombok.Getter;
import lombok.Setter;
import org.medirec.medirec.backend.model.IgnoreColumn;
import org.medirec.medirec.backend.model.MedicalRecord;

import java.sql.Timestamp;
import java.sql.Time;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Patient {
	public static final String TABLE = "patient";

	private int id;
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
	private int doctor_id;

	@IgnoreColumn
	private List<MedicalRecord> records = new ArrayList<>();

	public Patient() {
		// required for reflection
	}

	public String getFullName() {
		return first_name + " " + last_name;
	}

	public String getBasicInfo(ResourceBundle bundle) {
		int vek = calculateAge();
		String genderValue = gender != null ? gender : bundle.getString("not.set");
		String insuranceValue = insurance_number != null ? insurance_number : bundle.getString("not.set");

		return bundle.getString("age") + ": " + vek + " | " +
				bundle.getString("gender.info") + ": " + genderValue + " | " +
				bundle.getString("insurance.number") + ": " + insuranceValue;
	}

	private int calculateAge() {
		if (birth_date == null) return 0;
		return java.time.Period.between(
				new java.sql.Date(birth_date.getTime()).toLocalDate(),
				java.time.LocalDate.now()
		).getYears();
	}

	public void addRecord(MedicalRecord record) {
		this.records.add(record);
	}

	public int getId() {
		return id;
	}

	public Patient setId(int id) {
		this.id = id;
		return this;
	}

	public String getFirst_name() {
		return first_name;
	}

	public Patient setFirst_name(String first_name) {
		this.first_name = first_name;
		return this;
	}

	public String getLast_name() {
		return last_name;
	}

	public Patient setLast_name(String last_name) {
		this.last_name = last_name;
		return this;
	}

	public Date getBirth_date() {
		return birth_date;
	}

	public Patient setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
		return this;
	}

	public String getPersonal_id_number() {
		return personal_id_number;
	}

	public Patient setPersonal_id_number(String personal_id_number) {
		this.personal_id_number = personal_id_number;
		return this;
	}

	public String getGender() {
		return gender;
	}

	public Patient setGender(String gender) {
		this.gender = gender;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Patient setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public Patient setPhone_number(String phone_number) {
		this.phone_number = phone_number;
		return this;
	}

	public String getBirth_city() {
		return birth_city;
	}

	public Patient setBirth_city(String birth_city) {
		this.birth_city = birth_city;
		return this;
	}

	public String getPermanent_city() {
		return permanent_city;
	}

	public Patient setPermanent_city(String permanent_city) {
		this.permanent_city = permanent_city;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public Patient setStreet(String street) {
		this.street = street;
		return this;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public Patient setPostal_code(String postal_code) {
		this.postal_code = postal_code;
		return this;
	}

	public String getInsurance_number() {
		return insurance_number;
	}

	public Patient setInsurance_number(String insurance_number) {
		this.insurance_number = insurance_number;
		return this;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public Patient setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
		return this;
	}

	public Time getUpdated_at() {
		return updated_at;
	}

	public Patient setUpdated_at(Time updated_at) {
		this.updated_at = updated_at;
		return this;
	}

	public String getBlood_pressure() {
		return blood_pressure;
	}

	public Patient setBlood_pressure(String blood_pressure) {
		this.blood_pressure = blood_pressure;
		return this;
	}

	public Float getWeight() {
		return weight;
	}

	public Patient setWeight(Float weight) {
		this.weight = weight;
		return this;
	}

	public Float getHeight() {
		return height;
	}

	public Patient setHeight(Float height) {
		this.height = height;
		return this;
	}

	public Float getBmi() {
		return bmi;
	}

	public Patient setBmi(Float bmi) {
		this.bmi = bmi;
		return this;
	}

	public int getDoctor_id() {
		return doctor_id;
	}

	public Patient setDoctor_id(int doctor_id) {
		this.doctor_id = doctor_id;
		return this;
	}

	public List<MedicalRecord> getRecords() {
		return records;
	}

	public Patient setRecords(List<MedicalRecord> records) {
		this.records = records;
		return this;
	}
}
