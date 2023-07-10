package com.musify.dao;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;

@FunctionalInterface
public interface CoverArtArchiveDAO {

    Artist getAlbumCoverArtDetails(Artist artist, MusicBrainzResponse mbResponse);
}
