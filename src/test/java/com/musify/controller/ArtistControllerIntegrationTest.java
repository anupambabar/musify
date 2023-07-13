package com.musify.controller;

import com.musify.entity.Artist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArtistControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @Test
    void getArtist() {

        String mbid = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

        Artist artist = restTemplate
                .getForObject("http://localhost:" + randomServerPort
                        + "/musify/music-artist/details/" + mbid, Artist.class);

        assertThat(artist.getMbid()).isEqualTo(mbid);
        assertThat(artist.getName()).isEqualTo("Michael Jackson");
        assertThat(artist.getGender()).isEqualTo("Male");
    }

}