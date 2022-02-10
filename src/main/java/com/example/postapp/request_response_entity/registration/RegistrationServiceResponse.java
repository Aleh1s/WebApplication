package com.example.postapp.request_response_entity.registration;

import com.example.postapp.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationServiceResponse {

    private UserEntity user;

    private Boolean status;

}
