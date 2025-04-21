package com.example.projekt.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Patient {
    public static final String TABLE = "patient";
    private Integer id;
    private String first_name;
    private String last_name;
    private LocalDate birth_date;
    private String personal_id_number;
    private String gender;
    private String email;
    private String phone_number;
    private String birth_city;
    private String permanent_city;
    private String street_text;
    private String postal_code;
    private String insurance_number;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String blood_pressure;
    private Double weight;
    private Double height;
    private Double BMI;
    private Integer doctor_id;

    public Patient() {
        this.id = 0;
        this.first_name = "";
        this.last_name = "";
        this.birth_date = LocalDate.now();
        this.personal_id_number = "";
        this.gender = "";
        this.email = "";
        this.phone_number = "";
        this.birth_city = "";
        this.permanent_city = "";
        this.street_text = "";
        this.postal_code = "";
        this.insurance_number = "";
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.blood_pressure = "";
        this.weight = 0.0;
        this.height = 0.0;
        this.BMI = 0.0;
        this.doctor_id = 0;
    }
}