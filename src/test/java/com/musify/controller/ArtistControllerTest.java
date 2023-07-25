package com.musify.controller;

import com.musify.entity.Artist;
import com.musify.entity.Role;
import com.musify.entity.User;
import com.musify.service.ArtistDetailsService;
import com.musify.service.impl.AuthServiceImpl;
import com.musify.service.impl.JWTServiceImpl;
import com.musify.service.impl.RedisServiceImpl;
import com.musify.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ArtistControllerTest {

    @MockBean
    ArtistDetailsService artistDetailsService;
    @MockBean
    RedisServiceImpl redisService;
    @MockBean
    AuthServiceImpl authService;
    @MockBean
    JWTServiceImpl jwtService;
    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;
    private String mbid;
    private String accessToken;
    private Artist artist;

    @BeforeEach
    void setUp() {

        // Test MBID
        mbid = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

        // Mock Artist
        artist = Artist.builder()
                .mbid(mbid)
                .name("Michael Jackson")
                .build();

        accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmhpamVldC5iYWJhcjEyQGdtYWlsLmNvbSIsImlhdCI6MTY5MDMwODc0MCwiZXhwIjoxNjkwMzEwMTgwfQ.fbbtxEfjLoe0OC1UUW5pEhmMC0M4gXl5HZfm2gtn288";

        // Mock User
        User user = User.builder()
                .firstName("Abhijeet")
                .lastName("Babar")
                .email("abhijeet.babar12@gmail.com")
                .password("babar")
                .role(Role.USER)
                .build();
        given(userService.loadUserByUsername("abhijeet.babar12@gmail.com")).willReturn(user);
    }

    @Test
    @WithMockUser(username = "abhijeet.babar12@gmail.com", password = "babar", roles = "USER")
    void getArtist_whenToken_thenOk() throws Exception {

        given(artistDetailsService.getArtistDetails(mbid)).willReturn(artist);
        given(redisService.getValue(mbid)).willReturn(artist);

        mockMvc.perform(get("/musify/music-artist/details/{mbid}", mbid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.mbid", is(artist.getMbid())))
                .andExpect(jsonPath("$.name", is(artist.getName())));
    }

    @Test
    void getArtist_whenNoToken_thenForbidden() throws Exception {

        mockMvc.perform(get("/musify/music-artist/details/{id}", mbid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}