package com.example.backend.model;

import com.example.backend.util.JwtUtil;
import lombok.Data;

@Data
public class User {
    public static final String TABLE = "\"users\"";
    private Integer id;
    private String email;
    private String password_hash;
    private Integer role_id;
    private Integer doctor_id;
    private String name;

    public User() {
        this.id = 0;
        this.email = "";
        this.password_hash = "";
        this.role_id = 0;
        this.doctor_id = 0;
        this.name = "";

    }
}
