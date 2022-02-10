package com.example.postapp.request_response_entity.registration;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String name;

    private String email;

    private String password;

}
