package org.medirec.backend.controller;

import org.medirec.backend.model.LoginRequest;
import org.medirec.backend.model.LoginResponse;
import org.medirec.backend.service.AuthService;

public class AuthController {

    private AuthController(){

    }
    public static LoginResponse login(LoginRequest vstup){
        return AuthService.login(vstup.getMail(), vstup.getPassword());
    }
}
