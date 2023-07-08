package com.musify.dao.impl;

import com.musify.dao.MusicBrainzDAO;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class MusicBrainzDAOImpl implements MusicBrainzDAO {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    WebClient webClient;
    @Value("${musicbrainz.api.artist.url}")
    private String apiUrl;
    @Value("${musicbrainz.api.artist.include}")
    private String include;
    @Value("${musicbrainz.api.artist.format}")
    private String format;

    @Override
    public Mono<MusicBrainzResponse> getArtistDetailsFromMBz(String mbid) {

        LOGGER.info("Fetching Artist Details from MusicBrainz");
        final String uri = apiUrl + mbid + "?&fmt=" + format + "&inc=" + include;
        LOGGER.info("MusicBrainz API being called: " + uri);

        return webClient.get()
                .uri(apiUrl + "{id}" + "?&fmt=" + format + "&inc=" + include, mbid)
                .retrieve()
                .bodyToMono(MusicBrainzResponse.class)
                .timeout(Duration.ofMillis(10_000));
    }
}
