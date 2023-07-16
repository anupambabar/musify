package com.musify.controller;

import brave.sampler.Sampler;
import com.musify.entity.Artist;
import com.musify.service.ArtistDetailsService;
import com.musify.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/musify/music-artist")
public class ArtistController {

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
    public Optional<Artist> getArtist(@PathVariable("mbid") String mbid) throws Exception {

        Artist artist = null;
        log.info("Retrieving Artist by MBID");

        if (redisEnabled) {
            log.info("Redis is Configured. Getting Data from Cache for :: " + mbid);
            if (null != redisService.getValue(mbid)) {
                log.info("Data FOUND in Cache for :: " + mbid);
                artist = redisService.getValue(mbid);
            } else {
                log.info("Data NOT FOUND in Cache for :: " + mbid);
                artist = artistDetailsService.getArtistDetails(mbid);
                redisService.setValue(mbid, artist);
            }
        } else {
            log.info("Redis Not Configured. Calling Actual Service");
            artist = artistDetailsService.getArtistDetails(mbid);
        }

        return Optional.ofNullable(artist);
    }


}
