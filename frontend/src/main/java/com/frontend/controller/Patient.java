package com.frontend.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Patient {

	private String name;
	private String basicInfo;
	private List<MedicalRecord> records = new ArrayList<>();

	public Patient(String name, String basicInfo) {
		this.name = name;
		this.basicInfo = basicInfo;
	}

	public String getName() {
		return name;
	}

	// vytvorenie basic info pre neskorsie zobrazenie v gui
	public String getBasicInfo(ResourceBundle bundle) {
		int vek = calculateAgeFromBirthdate();

		String genderLabel = bundle.getString("gender.info");
		String ageLabel = bundle.getString("age");
		String insuranceLabel = bundle.getString("insurance.number");

		String genderValue = gender != null
			? gender
			: bundle.getString("not.set");
		String insuranceValue = insuranceNumber != null
			? insuranceNumber
			: bundle.getString("not.set");

		return (
			ageLabel +
			": " +
			vek +
			" | " +
			genderLabel +
			": " +
			genderValue +
			" | " +
			insuranceLabel +
			": " +
			insuranceValue
		);
	}

	private int calculateAgeFromBirthdate() {
		if (birthdate == null) return 0;
		return java.time.Period.between(birthdate, LocalDate.now()).getYears();
	}

	public List<MedicalRecord> getRecords() {
		return records;
	}

	public void addRecord(MedicalRecord record) {
		records.add(record);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}

	public void setRecords(List<MedicalRecord> records) {
		this.records = records;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public String getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public Double getBmi() {
		return bmi;
	}

	public void setBmi(Double bmi) {
		this.bmi = bmi;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	private LocalDate birthdate;
	private String gender;
	private String email;
	private String phone;
	private String birthCity;
	private String currentCity;
	private String street;
	private String postalCode;
	private String insuranceNumber;

	private String bloodPressure;
	private Double bmi;
	private Double weight;
	private Double height;

	public Patient(
		String name,
		LocalDate birthdate,
		String gender,
		String email,
		String phone,
		String birthCity,
		String currentCity,
		String street,
		String postalCode,
		String insuranceNumber,
		String bloodPressure,
		Double bmi,
		Double weight,
		Double height
	) {
		this.name = name;
		this.birthdate = birthdate;
		this.gender = gender;
		this.email = email;
		this.phone = phone;
		this.birthCity = birthCity;
		this.currentCity = currentCity;
		this.street = street;
		this.postalCode = postalCode;
		this.insuranceNumber = insuranceNumber;
		this.bloodPressure = bloodPressure;
		this.bmi = bmi;
		this.weight = weight;
		this.height = height;
	}
}
