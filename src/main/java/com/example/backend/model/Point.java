package com.example.backend.model;

import lombok.Data;

@Data
public class Point {
    public static final String TABLE = "points";

    private Integer id;
    private String label;
    private String description;
    private Float price;
    private Integer points;

    public Point() {
        this.id = 0;
        this.label = "";
        this.description = "";
        this.price = 0.0F;
        this.points = 0;
    }
}
