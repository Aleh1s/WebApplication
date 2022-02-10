package com.example.postapp.request_response_entity.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class AuthorizationResponse {

    private String token;

    private String refreshToken;

}
