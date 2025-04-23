package com.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

	//Logger
	private static final Logger logger = LoggerFactory.getLogger(
		LoginResponse.class
	);

	private boolean success;
	private String token;
}
