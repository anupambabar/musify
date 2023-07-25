package com.musify.controller;

import com.musify.dto.RegisterRequest;
import com.musify.entity.Role;
import com.musify.entity.User;
import com.musify.service.impl.UserServiceImpl;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ArtistControllerIntegrationTest {

    @Container
    static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis"))
            .withExposedPorts(6379);

    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int randomServerPort;
    private String accessToken;
    private RegisterRequest registerRequest;
    private String mbid;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        redis.start();
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }

    @BeforeEach
    public void setup() throws JSONException {

        // Test MBID
        mbid = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

        // RegisterRequest Object to fetch Mock Token
        registerRequest = RegisterRequest.builder()
                .firstName("Abhijeet")
                .lastName("Babar")
                .email("abhijeet.babar12@gmail.com")
                .password("babar")
                .build();

        HttpEntity<RegisterRequest> request = new HttpEntity<>(registerRequest, null);

        ResponseEntity<String> response = restTemplate
                .postForEntity("http://localhost:" + randomServerPort
                                + "/musify/auth/getToken",
                        request,
                        String.class
                );
        accessToken = response.getBody();

        // Mock User
        User user = User.builder()
                .firstName("Abhijeet")
                .lastName("Babar")
                .email("abhijeet.babar12@gmail.com")
                .password("babar")
                .role(Role.USER)
                .build();
        when(userService.loadUserByUsername("abhijeet.babar12@gmail.com")).thenReturn(user);
    }

    @Test
    void getArtist_whenToken_thenOk() throws Exception {

        mockMvc.perform(get("/musify/music-artist/details/{mbid}", mbid)
                        .header("AUTHORIZATION", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getArtist_whenNoToken_thenForbidden() throws Exception {

        mockMvc.perform(get("/musify/music-artist/details/{mbid}", mbid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}