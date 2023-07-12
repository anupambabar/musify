package com.musify.service;

import com.musify.entity.Artist;

public interface RedisService {

    void setValue(final String mbid, Artist artist);

    Artist getValue(final String mbid);
}
