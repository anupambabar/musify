package com.musify.service.impl;

import com.musify.dao.CoverArtArchiveDAO;
import com.musify.dao.MusicBrainzDAO;
import com.musify.dao.WikiDataDAO;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;
import com.musify.mapper.ArtistDataMapper;
import com.musify.service.ArtistDetailsService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ArtistDetailsServiceImpl implements ArtistDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MusicBrainzDAO musicBrainzDAO;

    @Autowired
    WikiDataDAO wikiDataDAO;

    @Autowired
    CoverArtArchiveDAO coverArtArchiveDAO;

    @Autowired
    ArtistDataMapper artistDataMapper;

    @SneakyThrows
    @Override
    public Mono<Artist> getArtistDetails(String mbid) {

        Artist artist = new Artist();
        Mono<Artist> mbArtist = Mono.just(new Artist());
        Mono<Artist> descArtist = Mono.just(new Artist());
        Mono<Artist> caArtist = Mono.just(new Artist());

        // Get Artist Details from MusicBrainz
        Mono<MusicBrainzResponse> mbResponse = musicBrainzDAO.getArtistDetailsFromMBz(mbid);

        if (null != mbResponse) {
            LOGGER.info("Artist Found");

            // Populate artist object with MusicBrainz data
            mbArtist = mbResponse.map(mbr -> {
                return artistDataMapper.ARTIST_DATA_MAPPER.mapMBResponseToArtist(mbr);
            });

            // Populate artist object with Wikipedia Description
            descArtist = wikiDataDAO.getArtistDetailsFromWD(mbResponse);

            // Populate artist object with Cover Art Archive Data
            caArtist = coverArtArchiveDAO.getAlbumCoverArtDetails(mbResponse);

            return Mono.zip(mbArtist, descArtist, caArtist).flatMap(data -> {
                artist.setMbid(data.getT1().getMbid());
                artist.setName(data.getT1().getName());
                artist.setGender(data.getT1().getGender());
                artist.setCountry(data.getT1().getCountry());
                artist.setDisambiguation(data.getT1().getDisambiguation());
                artist.setDescription(data.getT2().getDescription());
                
                artist.setAlbums(data.getT3().getAlbums());

                return Mono.just(artist);
            });

        } else {
            LOGGER.info("Error Getting Artist Details");
            artist.setErrorOccurred(true);
            return Mono.just(artist);
        }
    }
}
