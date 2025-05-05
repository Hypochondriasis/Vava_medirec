package com.example.backend.model;

import com.example.backend.util.JwtUtil;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String street;
    private String postal_code;
    private String insurance_number;

    private String blood_pressure;
    private Float weight;
    private Float height;
    private Float BMI;
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
        this.street = "";
        this.postal_code = "";
        this.insurance_number = "";

        this.blood_pressure = "";
        this.weight = 0.0F;
        this.height = 0.0F;
        this.BMI = 0.0F;
        this.doctor_id = 0;
        try {
            this.doctor_id = JwtUtil.getUserFromToken().getDoctor_id();
        }
        catch(Exception e){

        }
    }
    @Override
    public String toString() {
        return this.first_name+" "+this.last_name;
    }
}