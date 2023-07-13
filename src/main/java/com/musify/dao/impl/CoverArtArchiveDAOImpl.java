package com.musify.dao.impl;

import com.musify.dao.CoverArtArchiveDAO;
import com.musify.dto.coverartarchive.CoverArtArchiveResponse;
import com.musify.dto.coverartarchive.Image;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.dto.musicbrainz.ReleaseGroup;
import com.musify.entity.Album;
import com.musify.entity.Artist;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class CoverArtArchiveDAOImpl implements CoverArtArchiveDAO {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RestTemplate restTemplate;

    @Value("${coverart.api.url}")
    private String apiUrl;

    @Override
    @CircuitBreaker(name = "musifycircuitbreakerclient")
    public Artist getAlbumCoverArtDetails(Artist artist, MusicBrainzResponse mbResponse) {

        LOGGER.info("Fetching Album Cover Art Details from Cover Art Archive");

        ArrayList<Image> images = new ArrayList<Image>();
        String imageUrl = "";
        ArrayList<Album> albums = new ArrayList<Album>();

        if (null != mbResponse.getReleaseGroups() && !mbResponse.getReleaseGroups().isEmpty()) {

            albums = (ArrayList<Album>) mbResponse.getReleaseGroups()
                    .parallelStream()
                    .map(releaseGroup -> {
                        return getCoverArtArchiveDetails(releaseGroup);
                    })
                    .collect(Collectors.toList());

            artist.setAlbums(albums);
        }
        return artist;
    }

    private String getFrontImageUrl(ArrayList<Image> images) {

        Predicate<Image> frontImage = image -> image.getFront();
        if (!images.stream().anyMatch(frontImage))
            return "No cover art found";
        else
            return images.stream().filter(frontImage).collect(Collectors.toList()).get(0).getImage();
    }

    private Album getCoverArtArchiveDetails(ReleaseGroup releaseGroup) {

        LOGGER.info("Fetching Album Image Details from Cover Art Archive");

        ArrayList<Image> images = new ArrayList<Image>();
        String imageUrl = "";
        Album album = new Album();

        try {
            final String uri = apiUrl + releaseGroup.getId();
            LOGGER.info("Cover Art Archive API being called: " + uri);

            ResponseEntity<CoverArtArchiveResponse> response = restTemplate.exchange(
                    uri, HttpMethod.GET, null,
                    new ParameterizedTypeReference<CoverArtArchiveResponse>() {
                    });

            images = response.getBody().getImages();

            // Get Front Image URL
            imageUrl = getFrontImageUrl(images);

            // Create Album Object
            album.setId(releaseGroup.getId());
            album.setTitle(releaseGroup.getTitle());
            album.setImageUrl(imageUrl);

        } catch (HttpStatusCodeException e) {

            LOGGER.info("Error Occurred while Fetching Album Image Details from Cover Art Archive.");
            LOGGER.info("Error Log: " + e.getMessage());

            if ("404".equals(e.getRawStatusCode())) {
                album.setImageUrl("No cover art found");
            } else {
                new RuntimeException(e);
            }
        }
        return album;
    }
}
