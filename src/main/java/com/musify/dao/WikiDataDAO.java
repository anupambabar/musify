package com.musify.dao;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;

@FunctionalInterface
public interface WikiDataDAO {

    Artist getArtistDetailsFromWD(Artist artist, MusicBrainzResponse mbResponse);
}
