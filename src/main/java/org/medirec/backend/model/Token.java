package org.medirec.backend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Token {
    public static final String TABLE = "token";
    private Integer id;
    private Integer user_id;
    private String token;
    private LocalDateTime validity;

    public Token() {
        this.id = 0;
        this.user_id = 0;
        this.token = "";
        this.validity = LocalDateTime.now().plusDays(1);
    }
}
