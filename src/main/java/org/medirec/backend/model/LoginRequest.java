package org.medirec.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
public class LoginRequest {
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(LoginRequest.class);

    private String mail;
    private String password;

    //So that we can't create an empty request
    private LoginRequest() {}
}
