package com.musify.controller;

import brave.sampler.Sampler;
import com.musify.entity.Artist;
import com.musify.service.ArtistDetailsService;
import com.musify.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/musify/music-artist")
public class ArtistController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ArtistDetailsService artistDetailsService;

    @Autowired
    RedisService redisService;

    @Value("${spring.data.redis.repositories.enabled}")
    private boolean redisEnabled;

    @Bean
    public Sampler alwaysSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @GetMapping(value = "/details/{mbid}", produces = "application/json")
    public Optional<Artist> getArtist(@PathVariable("mbid") String id) {

        Artist artist = null;
        LOGGER.info("Retrieving Artist by MBID");

        if (redisEnabled) {
            if (null != redisService.getValue(id)) {
                artist = redisService.getValue(id);
            } else {
                artist = artistDetailsService.getArtistDetails(id);
                redisService.setValue(id, artist);
            }
        } else {
            artist = artistDetailsService.getArtistDetails(id);
        }

        return Optional.ofNullable(artist);
    }


}
