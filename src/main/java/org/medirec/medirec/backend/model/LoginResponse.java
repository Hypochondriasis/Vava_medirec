package org.medirec.medirec.backend.model;

import lombok.*;
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
