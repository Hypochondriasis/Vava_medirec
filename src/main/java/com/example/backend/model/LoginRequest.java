package com.example.backend.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String mail;
    private String password;
}
