package org.medirec.medirec.backend.model;

import lombok.Data;

@Data
public class User {

	public static final String TABLE = "\"user\"";
	private Integer id;
	private String email;
	private String password_hash;
	private Integer role_id;
	private Integer doctor_id;
	private String name;
	@IgnoreColumn
	private Doctor doctor;
	@IgnoreColumn
	private Role role;

	public User() {
		this.id = 0;
		this.email = "";
		this.password_hash = "";
		this.role_id = 0;
		this.doctor_id = 0;
		this.doctor = null;
		this.role = null;
	}
}
