package com.musify.dao;

import com.musify.dto.musicbrainz.MusicBrainzResponse;

@FunctionalInterface
public interface MusicBrainzDAO {

    MusicBrainzResponse getArtistDetailsFromMBz(String mbid);
}
