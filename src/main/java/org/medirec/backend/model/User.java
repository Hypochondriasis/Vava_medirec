package org.medirec.backend.model;

import lombok.Data;

@Data
public class User {
    public static final String TABLE = "\"user\"";
    private Integer id;
    private String email;
    private String password_hash;
    private Integer role_id;
    private Integer doctor_id;

    public User() {
        this.id = 0;
        this.email = "";
        this.password_hash = "";
        this.role_id = 0;
        this.doctor_id = 0;
    }
}
