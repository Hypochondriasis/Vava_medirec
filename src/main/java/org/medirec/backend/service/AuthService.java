package org.medirec.backend.service;

import org.medirec.backend.config.DatabaseConfig;
import org.medirec.backend.model.LoginResponse;
import org.medirec.backend.model.Token;
import org.medirec.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public static LoginResponse login(String mail, String heslo) {
        logger.debug("Entering login() with email={}", mail);
        String query = "SELECT id FROM User WHERE email = ? AND password_hash = ?";
        LoginResponse odpoved = new LoginResponse();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            logger.trace("Preparing statement: {}", query);
            statement.setString(1, mail);
            statement.setString(2, heslo); // TODO: password should be hashed before comparison!
            logger.debug("Executing query for email={}", mail);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int userId = result.getInt("id");
                logger.info("User {} authenticated successfully with id={}", mail, userId);

                String token = JwtUtil.generateToken();
                Token novy = new Token();
                novy.setUser_id(userId);
                novy.setToken(token);
                JwtUtil.saveToken(novy);
                logger.debug("Generated and saved JWT token for userId={}", userId);

                odpoved.setSuccess(true);
                odpoved.setToken(token);
            } else {
                logger.warn("Authentication failed for email={}", mail);
                odpoved.setSuccess(false);
                odpoved.setToken(null);
            }

        } catch (SQLException e) {
            logger.error("Database error during login for email={}", mail, e);
            odpoved.setSuccess(false);
        }

        logger.debug("Exiting login() with success={}", odpoved.isSuccess());
        return odpoved;
    }
}
