package com.musify.dao;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;

public interface WikiDataDAO {

    Artist getArtistDetailsFromWD(Artist artist, MusicBrainzResponse mbResponse);

    String getArtistDescriptionFromWD(MusicBrainzResponse mbResponse);
}
