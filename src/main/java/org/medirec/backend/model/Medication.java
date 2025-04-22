package org.medirec.backend.model;

import lombok.Data;

@Data
public class Medication {
    public static final String TABLE = "medication";
    private Integer id;
    private String code;
    private String name;

    public Medication() {
        this.id = 0;
        this.code = "";
        this.name = "";
    }
}
