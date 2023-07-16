package com.musify.service.impl;

import com.musify.dao.CoverArtArchiveDAO;
import com.musify.dao.MusicBrainzDAO;
import com.musify.dao.WikiDataDAO;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;
import com.musify.service.ArtistDetailsService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class ArtistDetailsServiceImpl implements ArtistDetailsService {

    @Autowired
    MusicBrainzDAO musicBrainzDAO;

    @Autowired
    WikiDataDAO wikiDataDAO;

    @Autowired
    CoverArtArchiveDAO coverArtArchiveDAO;

    @Override
    @Retry(name = "musifyretryclient")
    public Artist getArtistDetails(String mbid) {

        Artist artist = new Artist();

        // Get Artist Details from MusicBrainz
        MusicBrainzResponse mbResponse = musicBrainzDAO.getArtistDetailsFromMBz(mbid);

        if (null != mbResponse) {
            log.info("Artist Found");

            Instant start = Instant.now();

            // Populate artist object with MusicBrainz data
            artist = mapMBResponseToArtist(mbResponse);

            // Execute further methods concurrently
            artist = executeConcurrent(artist, mbResponse);

            Instant finish = Instant.now();
            log.info("Processing Time : " + Duration.between(start, finish).toMillis());

        } else {
            log.info("Error Getting Artist Details");
            artist.setErrorOccurred(true);
        }
        return artist;
    }

    public Artist executeConcurrent(Artist artist, MusicBrainzResponse mbResponse) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            Callable<Artist> wikiCall = new Callable() {
                @Override
                public Artist call() throws Exception {
                    return wikiDataDAO.getArtistDetailsFromWD(artist, mbResponse);
                }
            };

            Callable<Artist> coverArtCall = new Callable() {
                @Override
                public Artist call() throws Exception {
                    return coverArtArchiveDAO.getAlbumCoverArtDetails(artist, mbResponse);
                }
            };


            Callable<Artist> callableTask = () -> {
                TimeUnit.MILLISECONDS.sleep(300);
                return new Artist();
            };

            List<Callable<Artist>> callableTasks = Arrays.asList(wikiCall, coverArtCall);
            List<Future<Artist>> futures = executorService.invokeAll(callableTasks);

            if (null != futures && !futures.isEmpty()) {
                artist.setDescription(futures.get(0).get() != null ?
                        futures.get(0).get().getDescription() : "Description not found");
                artist.setAlbums(futures.get(1).get() != null ?
                        futures.get(1).get().getAlbums() : new ArrayList<>());
            }
        } catch (InterruptedException | ExecutionException e) {
            new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }

        return artist;
    }

    public Artist mapMBResponseToArtist(MusicBrainzResponse mbResponse) {

        Artist artist = new Artist();
        artist.setMbid(mbResponse.getId());
        artist.setName(mbResponse.getName());
        artist.setGender(mbResponse.getGender());
        artist.setCountry(mbResponse.getCountry());
        artist.setDisambiguation(mbResponse.getDisambiguation());
        return artist;
    }
}
