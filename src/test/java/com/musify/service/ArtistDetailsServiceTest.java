package com.musify.service;

import com.musify.dao.impl.CoverArtArchiveDAOImpl;
import com.musify.dao.impl.MusicBrainzDAOImpl;
import com.musify.dao.impl.WikiDataDAOImpl;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Album;
import com.musify.entity.Artist;
import com.musify.service.impl.ArtistDetailsServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@WebMvcTest
class ArtistDetailsServiceTest {

    @MockBean
    ArtistDetailsServiceImpl artistDetailsService;
    @MockBean
    MusicBrainzDAOImpl musicBrainzDAO;
    @MockBean
    WikiDataDAOImpl wikiDataDAO;
    @MockBean
    CoverArtArchiveDAOImpl coverArtArchiveDAO;
    @MockBean
    RedisService redisService;

    private String mbid;
    private MusicBrainzResponse mbResponse;
    private Artist artist;


    @BeforeEach
    void setUp() {

        mbid = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

        mbResponse = MusicBrainzResponse.builder()
                .id(mbid)
                .name("Michael Jackson")
                .gender("Male")
                .country("US")
                .disambiguation("King of Pop")
                .build();

        ArrayList<Album> albums = new ArrayList<>();
        albums.add(new Album());

        artist = Artist.builder()
                .mbid(mbid)
                .name("Michael Jackson")
                .gender("Male")
                .country("US")
                .disambiguation("King of Pop")
                .description("Description")
                .albums(albums)
                .build();
    }

    @Test
    void getArtistDetails() throws InterruptedException {

        given(musicBrainzDAO.getArtistDetailsFromMBz(mbid)).willReturn(mbResponse);
        given(artistDetailsService.mapMBResponseToArtist(mbResponse)).willReturn(artist);
        given(artistDetailsService.executeConcurrent(artist, mbResponse)).willReturn(artist);

        ExecutorService executorService = mock(ExecutorService.class);
        Future<Object> mockedFuture = mock(Future.class);
        List<Future<Object>> list = Lists.newArrayList(mockedFuture);

        given(executorService.invokeAll(any())).willReturn(list);

        given(artistDetailsService.getArtistDetails(mbid)).willReturn(artist);

        Artist testArtist = artistDetailsService.getArtistDetails(mbid);
        assertThat(testArtist).isNotNull();
        assertThat(testArtist.getMbid()).isEqualTo(mbid);
    }
}