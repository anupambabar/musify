package com.musify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musify.dto.LoginRequest;
import com.musify.dto.RegisterRequest;
import com.musify.entity.User;
import com.musify.service.ArtistDetailsService;
import com.musify.service.impl.AuthServiceImpl;
import com.musify.service.impl.JWTServiceImpl;
import com.musify.service.impl.RedisServiceImpl;
import com.musify.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class AuthControllerTest {

    @MockBean
    ArtistDetailsService artistDetailsService;
    @MockBean
    RedisServiceImpl redisService;
    @MockBean
    AuthServiceImpl authService;
    @MockBean
    JWTServiceImpl jwtService;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    User user;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    RegisterRequest registerRequest;
    @MockBean
    LoginRequest loginRequest;
    @MockBean
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void register() {
    }

    @Test
    void login() {
    }
}