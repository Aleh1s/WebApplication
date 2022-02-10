package com.example.postapp.controller;

import com.example.postapp.request_response_entity.authorization.AuthorizationRequest;
import com.example.postapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<Object> refreshToken (HttpServletRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> auth (@RequestBody AuthorizationRequest request){
        return authService.auth(request);
    }

}
