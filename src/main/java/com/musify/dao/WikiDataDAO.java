package com.musify.dao;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface WikiDataDAO {

    Mono<Artist> getArtistDetailsFromWD(Mono<MusicBrainzResponse> mbResponse);
}
