package com.musify.dao;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MusicBrainzDAO {

    Mono<MusicBrainzResponse> getArtistDetailsFromMBz(String mbid);
}
