package com.musify.service.impl;

import com.musify.dao.CoverArtArchiveDAO;
import com.musify.dao.MusicBrainzDAO;
import com.musify.dao.WikiDataDAO;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;
import com.musify.service.ArtistDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistDetailsServiceImpl implements ArtistDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MusicBrainzDAO musicBrainzDAO;

    @Autowired
    WikiDataDAO wikiDataDAO;

    @Autowired
    CoverArtArchiveDAO coverArtArchiveDAO;

    @Override
    public Artist getArtistDetails(String mbid) {

        Artist artist = new Artist();

        // Get Artist Details from MusicBrainz
        MusicBrainzResponse mbResponse = musicBrainzDAO.getArtistDetailsFromMBz(mbid);

        if (null != mbResponse) {
            LOGGER.info("Artist Found");

            // Populate artist object with MusicBrainz data
            artist = mapMBResponseToArtist(mbResponse);

            // Populate artist object with Wikipedia Description
            artist = wikiDataDAO.getArtistDetailsFromWD(artist, mbResponse);

            // Populate artist object with Cover Art Archive Data
            artist = coverArtArchiveDAO.getAlbumCoverArtDetails(artist, mbResponse);

        } else {
            LOGGER.info("Error Getting Artist Details");
            artist.setErrorOccurred(true);
        }
        return artist;
    }

    private Artist mapMBResponseToArtist(MusicBrainzResponse mbResponse) {

        Artist artist = new Artist();
        artist.setMbid(mbResponse.getId());
        artist.setName(mbResponse.getName());
        artist.setGender(mbResponse.getGender());
        artist.setCountry(mbResponse.getCountry());
        artist.setDisambiguation(mbResponse.getDisambiguation());
        return artist;
    }
}
