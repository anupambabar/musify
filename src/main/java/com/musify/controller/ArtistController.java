package com.musify.controller;

import com.musify.entity.Artist;
import com.musify.service.ArtistDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/musify/music-artist")
public class ArtistController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*@Bean
    public Sampler alwaysSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }*/

    @Autowired
    ArtistDetailsService artistDetailsService;

    @GetMapping(value = "/details/{mbid}", produces = "application/json")
    public Optional<Artist> getArtist(@PathVariable("mbid") String id) {

        LOGGER.info("Retrieving Artist by MBID");

        Artist artist = artistDetailsService.getArtistDetails(id);

        return Optional.ofNullable(artist);
    }


}
