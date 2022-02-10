package com.example.postapp.service;

import com.example.postapp.entity.UserEntity;
import com.example.postapp.exception.UserNotFoundException;
import com.example.postapp.repository.UserRepository;
import com.example.postapp.request_response_entity.authorization.AuthorizationRequest;
import com.example.postapp.request_response_entity.authorization.AuthorizationResponse;
import com.example.postapp.security.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

import static com.example.postapp.entity.Status.ACTIVE;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity<Object> auth (AuthorizationRequest request){

        String email = request.getEmail();
        UserEntity user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        boolean isPasswordValid = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!isPasswordValid) {
            throw new IllegalStateException("Bad password");
        }

        if(!user.getStatus().equals(ACTIVE)) {
            throw new IllegalStateException("You are blocked, try to confirm your email");
        }

        String token = tokenProvider.generateToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);
        AuthorizationResponse authorizationResponse = new AuthorizationResponse(token, refreshToken);

        return new ResponseEntity<>(authorizationResponse, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String refreshToken = tokenProvider.resolveToken(request);

        if(refreshToken == null) {
            throw new IllegalStateException("Refresh token is not provided");
        }

        if(!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalStateException("Token is invalid");
        }

        String email = tokenProvider.getEmailByToken(refreshToken);
        UserEntity user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String token = tokenProvider.generateToken(user);

        AuthorizationResponse authorizationResponse = new AuthorizationResponse(token, refreshToken);

        return new ResponseEntity<>(authorizationResponse, HttpStatus.ACCEPTED);
    }

}
