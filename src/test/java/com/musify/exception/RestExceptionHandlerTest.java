package com.musify.exception;

import com.musify.controller.ArtistController;
import com.musify.service.ArtistDetailsService;
import com.musify.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class RestExceptionHandlerTest {

    @MockBean
    ArtistDetailsService artistDetailsService;
    @MockBean
    RedisService redisService;
    @MockBean
    private ArtistController artistController;
    @Autowired
    private MockMvc mockMvc;

    private String mbid;

    @BeforeEach
    void setUp() {
        mbid = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

        this.mockMvc = MockMvcBuilders.standaloneSetup(artistController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void handleResponseStatusException() throws Exception {

        when(artistController.getArtist(mbid)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/musify/music-artist/details/{id}", mbid))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleHttpServerErrorException() throws Exception {

        when(artistController.getArtist(mbid)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/musify/music-artist/details/{id}", mbid))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void handleHttpStatusCodeException() throws Exception {

        when(artistController.getArtist(mbid)).thenThrow(new HttpServerErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/musify/music-artist/details/{id}", mbid))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleAllExceptions() throws Exception {

        when(artistController.getArtist(mbid)).thenThrow(new RuntimeException("Runtime Exception"));

        mockMvc.perform(get("/musify/music-artist/details/{id}", mbid))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}