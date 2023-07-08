package com.musify.dao.impl;

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
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class CoverArtArchiveDAOImpl implements CoverArtArchiveDAO {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WebClient webClient;

    @Value("${coverart.api.url}")
    private String apiUrl;


    @Override
    public Mono<Artist> getAlbumCoverArtDetails(Mono<Artist> artist, Mono<MusicBrainzResponse> mbResponse) {

        LOGGER.info("Fetching Album Cover Art Details from Cover Art Archive");

        ArrayList<Album> albums = new ArrayList<Album>();
        ArrayList<ReleaseGroup> releaseGroups = (ArrayList<ReleaseGroup>) mbResponse.subscribe(mbr -> mbr.getReleaseGroups());

        if (null != releaseGroups && !releaseGroups.isEmpty()) {

            // Collect all ids from ReleaseGroup Collection
            List<String> releaseGroupIds = releaseGroups.stream()
                    .map(ReleaseGroup::getId).collect(Collectors.toList());

            Flux<Album> fluxAlbums = Flux.fromIterable(releaseGroups).flatMap(this::getAlbum);
            fluxAlbums.collectList().subscribe(albums::addAll);
            artist.map(a -> {
                a.setAlbums(albums);
                return a;
            });

        }

        return artist;
    }

    @SneakyThrows
    private Mono<Album> getAlbum(ReleaseGroup releaseGroup) {

        ArrayList<Image> images = new ArrayList<Image>();

        // Get Cover Art Archive Details
        Mono<Image> image = getCoverArtArchiveDetails(releaseGroup.getId());

        // Create Album Object
        Album album = new Album();
        album.setId(releaseGroup.getId());
        album.setTitle(releaseGroup.getTitle());
        album.setImageUrl(image.toFuture().get().getImage());

        return Mono.just(album);
    }

    private Image getFrontImageUrl(ArrayList<Image> images) {

        Predicate<Image> frontImage = image -> image.getFront();
        if (null != images && !images.stream().anyMatch(frontImage))
            return new Image("", "No cover art found", true);
        else
            return images.stream().filter(frontImage).collect(Collectors.toList()).get(0);
    }

    private Mono<Image> getCoverArtArchiveDetails(String id) {

        LOGGER.info("Fetching Album Image Details from Cover Art Archive");

        try {
            LOGGER.info("Cover Art Archive API being called: " + apiUrl + id);

            return webClient.get()
                    .uri(apiUrl + "{id}", id)
                    .retrieve()
                    .bodyToMono(CoverArtArchiveResponse.class)
                    .timeout(Duration.ofMillis(10_000))
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
}
