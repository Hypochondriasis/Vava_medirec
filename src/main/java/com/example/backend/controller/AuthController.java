package com.example.backend.controller;

import com.example.backend.model.LoginRequest;
import com.example.backend.model.LoginResponse;
import com.example.backend.service.AuthService;

public class AuthController {

    private AuthController(){

    }
    public static LoginResponse login(LoginRequest vstup){
        return AuthService.login(vstup.getMail(), vstup.getPassword());
    }
}
