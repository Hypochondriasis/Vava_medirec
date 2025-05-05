package com.example.backend.model;

import lombok.Data;

@Data
public class RecordDiagnosis {
    public static final String TABLE = "record_diagnosis";
    private Integer id;
    private Integer medical_record_id;
    private Integer diagnosis_id;

    public RecordDiagnosis() {
        this.id = 0;
        this.medical_record_id = 0;
        this.diagnosis_id = 0;
    }
}