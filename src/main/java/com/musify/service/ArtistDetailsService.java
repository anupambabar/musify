package com.musify.service;

import com.musify.entity.Artist;

@FunctionalInterface
public interface ArtistDetailsService {

    Artist getArtistDetails(String mbid);
}
