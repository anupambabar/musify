package com.musify.controller;

import com.musify.dto.JwtAuthenticationResponse;
import com.musify.dto.LoginRequest;
import com.musify.dto.RegisterRequest;
import com.musify.service.AuthService;
import com.musify.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/musify/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JWTService jwtService;

    @PostMapping("/getToken")
    public ResponseEntity<String> getAuthToken(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.getAuthToken(request));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}