package com.example.postapp.request_response_entity.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class RegistrationResponse {

    private String success;

    private HttpStatus httpStatus;

}
