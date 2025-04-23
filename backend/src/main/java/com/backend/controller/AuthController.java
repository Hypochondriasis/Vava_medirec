package com.backend.controller;

import com.backend.model.LoginRequest;
import com.backend.model.LoginResponse;
import com.backend.service.AuthService;

public class AuthController {

	private AuthController() {}

	public static LoginResponse login(LoginRequest vstup) {
		return AuthService.login(vstup.getMail(), vstup.getPassword());
	}
}
