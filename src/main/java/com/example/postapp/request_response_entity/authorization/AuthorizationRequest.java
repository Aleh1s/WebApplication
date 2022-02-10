package com.example.postapp.request_response_entity.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizationRequest {

    private String email;

    private String password;

}
