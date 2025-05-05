package com.example.backend.model;

import lombok.Data;

@Data
public class DiagnosisType {
    public static final String TABLE = "diagnosis_type";
    private Integer id;
    private String name;

    public DiagnosisType() {
        this.id = 0;
        this.name = "";
    }
}