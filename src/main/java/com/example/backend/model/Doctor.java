package com.example.backend.model;

import lombok.Data;

@Data
public class Doctor {
    public static final String TABLE = "doctor";
    private Integer id;
    private String first_name;
    private String last_name;
    private String specialization;
    private String code;

    public Doctor() {
        this.id = 0;
        this.first_name = "";
        this.last_name = "";
        this.specialization = "";
        this.code = "";
    }
}