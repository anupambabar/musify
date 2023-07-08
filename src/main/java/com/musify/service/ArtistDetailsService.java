package com.musify.service;

import com.musify.entity.Artist;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ArtistDetailsService {

    Mono<Artist> getArtistDetails(String mbid);
}
