package com.example.projekt.model;

import lombok.Data;

@Data
public class Role {
    public static final String TABLE = "role";
    private Integer id;
    private String name;

    public Role() {
        this.id = 0;
        this.name = "";
    }
}
