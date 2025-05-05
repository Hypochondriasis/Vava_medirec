package com.example.backend.model;

import lombok.Data;

@Data
public class Prescription {
    public static final String TABLE = "prescription";
    private Integer id;
    private Integer medical_record_id;
    private Integer medication_id;
    private String dosage;
    private String frequency;
    private String duration;

    public Prescription() {
        this.id = 0;
        this.medical_record_id = 0;
        this.medication_id = 0;
        this.dosage = "";
        this.frequency = "";
        this.duration = "";
    }
}
