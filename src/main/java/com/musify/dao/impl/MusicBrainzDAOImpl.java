package com.musify.dao.impl;

import com.musify.dao.MusicBrainzDAO;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MusicBrainzDAOImpl implements MusicBrainzDAO {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RestTemplate restTemplate;
    @Value("${musicbrainz.api.artist.url}")
    private String apiUrl;
    @Value("${musicbrainz.api.artist.include}")
    private String include;
    @Value("${musicbrainz.api.artist.format}")
    private String format;

    @Override
    @CircuitBreaker(name = "musifycircuitbreakerclient")
    public MusicBrainzResponse getArtistDetailsFromMBz(String mbid) {

        LOGGER.info("Fetching Artist Details from MusicBrainz");

        final String uri = apiUrl + mbid + "?&fmt=" + format + "&inc=" + include;
        LOGGER.info("MusicBrainz API being called: " + uri);

        ResponseEntity<MusicBrainzResponse> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<MusicBrainzResponse>() {
                });

        return response.getBody();
    }
}
