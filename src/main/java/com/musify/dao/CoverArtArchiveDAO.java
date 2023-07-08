package com.musify.dao;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CoverArtArchiveDAO {

    Mono<Artist> getAlbumCoverArtDetails(Mono<Artist> artist, Mono<MusicBrainzResponse> mbResponse);
}
