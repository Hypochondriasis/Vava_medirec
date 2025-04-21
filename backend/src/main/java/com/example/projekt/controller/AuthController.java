package com.example.projekt.controller;

import com.example.projekt.model.LoginRequest;
import com.example.projekt.model.LoginResponse;
import com.example.projekt.service.AuthService;

public class AuthController {

    private AuthController(){

    }
    public static LoginResponse login(LoginRequest vstup){
        return AuthService.login(vstup.getMail(), vstup.getPassword());
    }
}
