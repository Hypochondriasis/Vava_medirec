package com.example.backend.model;

import lombok.Data;

@Data
public class RecordPoint {
    public static final String TABLE = "record_points";

    private Integer id;
    private Integer medical_record_id;
    private Integer point_id;

    public RecordPoint() {
        this.id = 0;
        this.medical_record_id = 0;
        this.point_id = 0;
    }
}
