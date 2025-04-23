package com.backend.model;

import lombok.Data;

@Data
public class Diagnosis {

	public static final String TABLE = "diagnosis";
	private Integer id;
	private String code;
	private String name;
	private Integer diagnosis_type_id;

	public Diagnosis() {
		this.id = 0;
		this.code = "";
		this.name = "";
		this.diagnosis_type_id = 0;
	}
}
