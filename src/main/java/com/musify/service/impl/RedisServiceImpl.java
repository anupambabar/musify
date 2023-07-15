package com.musify.service.impl;

import com.google.gson.Gson;
import com.musify.entity.Artist;
import com.musify.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    Gson gson;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void setValue(final String mbid, Artist artist) {
        redisTemplate.opsForValue().set(mbid, gson.toJson(artist));
        redisTemplate.expire(mbid, 24, TimeUnit.HOURS);
    }

    @Override
    public Artist getValue(final String mbid) {
        return gson.fromJson(redisTemplate.opsForValue().get(mbid), Artist.class);
    }
}
