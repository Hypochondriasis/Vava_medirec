package com.backend.util;

import com.backend.model.Token;
import com.backend.model.User;
import com.backend.service.DatabaseService;
import java.security.SecureRandom;
import java.util.Base64;

public class JwtUtil {

	private static Token token;
	private static final SecureRandom secureRandom = new SecureRandom(); // strong RNG
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder()
		.withoutPadding();

	private JwtUtil() {}

	public static User getUserFromToken() throws Exception {
		return DatabaseService.getOne(User.class, token.getUser_id());
	}

	public static String generateToken() {
		byte[] randomBytes = new byte[40];
		secureRandom.nextBytes(randomBytes);
		String generovany = base64Encoder.encodeToString(randomBytes);

		return generovany;
	}

	public static void saveToken(Token vstup) {
		token = vstup;
	}
}
