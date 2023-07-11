package com.musify.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musify.dao.CoverArtArchiveDAO;
import com.musify.dto.coverartarchive.CoverArtArchiveResponse;
import com.musify.dto.coverartarchive.Image;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.dto.musicbrainz.ReleaseGroup;
import com.musify.entity.Album;
import com.musify.entity.Artist;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class CoverArtArchiveDAOImpl implements CoverArtArchiveDAO {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WebClient webClient;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${coverart.api.url}")
    private String apiUrl;


    @Override
    public Mono<Artist> getAlbumCoverArtDetails(Mono<MusicBrainzResponse> mbResponse) {

        LOGGER.info("Fetching Album Cover Art Details from Cover Art Archive");

        return mbResponse.map(mbr -> {
            Artist caArtist = new Artist();
            caArtist.setAlbums(getAlbumList(mbr.getReleaseGroups()));
            return caArtist;
        });
    }

    private ArrayList<Album> getAlbumList(ArrayList<ReleaseGroup> releaseGroups) {

        ArrayList<Album> albums = new ArrayList<>();
        Flux<Album> fluxAlbums = Flux.empty();
        if (null != releaseGroups && !releaseGroups.isEmpty()) {

            // Collect all ids from ReleaseGroup Collection
            List<String> releaseGroupIds = releaseGroups.stream()
                    .map(ReleaseGroup::getId).collect(Collectors.toList());

            fluxAlbums = Flux.fromIterable(releaseGroups).flatMap(this::getAlbum);
            fluxAlbums.collectList().subscribe(albums::addAll);
        }
        return albums;
    }

    @SneakyThrows
    private Mono<Album> getAlbum(ReleaseGroup releaseGroup) {

        // Get Cover Art Archive Details
        Mono<Image> image = getCoverArtArchiveDetails(releaseGroup.getId());

        // Create Album Object
        return image.map(img -> {
            Album alb = new Album();
            alb.setId(releaseGroup.getId());
            alb.setTitle(releaseGroup.getTitle());
            LOGGER.info("Release ID" + releaseGroup.getId() + " :: Image : " + img.getImage());
            alb.setImageUrl(img.getImage() != null ? img.getImage() : "No cover are found");
            return alb;
        });
    }

    private Mono<Image> getCoverArtArchiveDetails(String id) {

        LOGGER.info("Fetching Album Image Details from Cover Art Archive");

        try {
            LOGGER.info("Cover Art Archive API being called: " + apiUrl + id);

            return webClient.get()
                    .uri(apiUrl + "{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            return response.bodyToMono(CoverArtArchiveResponse.class);
                        } else {
                            // Turn to error
                            return Mono.just(new CoverArtArchiveResponse());
                        }
                    })
                    .map(coverArtArchiveResponse -> {
                        return getFrontImageUrl(coverArtArchiveResponse.getImages());
                    });

        } catch (HttpStatusCodeException hsce) {

            LOGGER.info("Error Occurred while Fetching Album Image Details from Cover Art Archive.");
            LOGGER.info("Error Log: " + hsce.getMessage());

            Image image = new Image();
            image.setFront(true);
            if ("404".equals(hsce.getRawStatusCode())) {
                image.setImage("No cover art found");
            } else {
                image.setImage("Cover art API is not available");
            }

            return Mono.just(image);
        }
    }

    private Image getFrontImageUrl(ArrayList<Image> images) {

        Predicate<Image> frontImage = image -> image.getFront().equals(true);
        if (null == images || !images.stream().anyMatch(frontImage))
            return new Image("", "No cover art found", true);
        else
            return images.stream().filter(frontImage).collect(Collectors.toList()).get(0);
    }
}
