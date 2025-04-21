package com.example.projekt.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalRecord {
    public static final String TABLE = "medical_record";
    private Integer id;
    private Integer patient_id;
    private Integer doctor_id;
    private String notes;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public MedicalRecord() {
        this.id = 0;
        this.patient_id = 0;
        this.doctor_id = 0;
        this.notes = "";
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
}
