package com.example.projekt.model;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String token;

}
