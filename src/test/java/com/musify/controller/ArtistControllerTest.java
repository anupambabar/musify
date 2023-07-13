package com.musify.controller;

import com.musify.entity.Artist;
import com.musify.service.ArtistDetailsService;
import com.musify.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ArtistControllerTest {

    @MockBean
    ArtistDetailsService artistDetailsService;
    @MockBean
    RedisService redisService;
    @Autowired
    private MockMvc mockMvc;

    private String mbid;
    private Artist artist;

    @BeforeEach
    void setUp() {

        mbid = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

        artist = Artist.builder()
                .mbid(mbid)
                .name("Michael Jackson")
                .build();

    }

    @Test
    void getArtist() throws Exception {

        given(artistDetailsService.getArtistDetails(mbid)).willReturn(artist);
        given(redisService.getValue(mbid)).willReturn(artist);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/musify/music-artist/details/{id}", mbid));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.mbid", is(artist.getMbid())))
                .andExpect(jsonPath("$.name", is(artist.getName())));
    }
}