package com.musify;

import com.musify.service.impl.JWTServiceImpl;
import com.musify.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(MusifyApplicationTests.class)
class MusifyApplicationTests {

    @MockBean
    JWTServiceImpl jwtService;
    @MockBean
    private UserServiceImpl userService;

    @Test
    void contextLoads() {
    }

}
