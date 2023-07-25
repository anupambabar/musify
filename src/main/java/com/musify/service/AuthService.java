package com.musify.service;

import com.musify.dto.JwtAuthenticationResponse;
import com.musify.dto.LoginRequest;
import com.musify.dto.RegisterRequest;

public interface AuthService {

    String getAuthToken(RegisterRequest request);

    JwtAuthenticationResponse register(RegisterRequest request);

    JwtAuthenticationResponse login(LoginRequest request);
}
