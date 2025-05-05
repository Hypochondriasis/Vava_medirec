package com.example.backend.model;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String token;

}
