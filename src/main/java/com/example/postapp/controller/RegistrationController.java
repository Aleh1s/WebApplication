package com.example.postapp.controller;

import com.example.postapp.request_response_entity.registration.RegistrationRequest;
import com.example.postapp.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register (@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping("/confirmToken")
    public ResponseEntity<Object> confirmToken(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
