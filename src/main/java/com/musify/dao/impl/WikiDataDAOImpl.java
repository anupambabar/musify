package com.musify.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musify.dao.WikiDataDAO;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.dto.musicbrainz.Relation;
import com.musify.dto.wikidata.SiteLink;
import com.musify.entity.Artist;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class WikiDataDAOImpl implements WikiDataDAO {

    @Autowired
    RestTemplate restTemplate;

    @Value("${wikipedia.api.url}")
    private String apiUrl;

    @Override
    public Artist getArtistDetailsFromWD(Artist artist, MusicBrainzResponse mbResponse) {

        log.info("Get Artist Wiki Details");

        // Get Artist WikiData Relation
        Relation relation = getArtistWikiDataRelation(mbResponse);

        // Get entityId
        String entityId = relation.getUrl().getResource().substring(relation.getUrl().getResource().lastIndexOf('/') + 1);

        // Get Artist WikiData URL
        String wikiDataAPIUrl = getWIkiDataAPIURL(relation, entityId);

        // Get SiteLink Data
        SiteLink siteLink = getSiteLinkData(wikiDataAPIUrl, entityId);

        // Get Wikipedia Description and Assign Description to Artist
        artist.setDescription(getDescFromWikiLink(siteLink));

        return artist;
    }

    @Override
    public String getArtistDescriptionFromWD(MusicBrainzResponse mbResponse) {
        log.info("Get Artist Wiki Details");

        // Get Artist WikiData Relation
        Relation relation = getArtistWikiDataRelation(mbResponse);

        // Get entityId
        String entityId = relation.getUrl().getResource().substring(relation.getUrl().getResource().lastIndexOf('/') + 1);

        // Get Artist WikiData URL
        String wikiDataAPIUrl = getWIkiDataAPIURL(relation, entityId);

        // Get SiteLink Data
        SiteLink siteLink = getSiteLinkData(wikiDataAPIUrl, entityId);

        // return Description
        return getDescFromWikiLink(siteLink);
    }

    private Relation getArtistWikiDataRelation(MusicBrainzResponse mbResponse) {

        log.info("Fetching WikiData Relation");

        Predicate<Relation> relationNotNull = relation -> Objects.nonNull(relation);
        Predicate<Relation> relationTypeNotNull = relation -> Objects.nonNull(relation.getType());
        Predicate<Relation> relationType = relation -> relation.getType().equalsIgnoreCase("wikidata");

        return mbResponse.getRelations()
                .stream()
                .filter(relationNotNull.and(relationTypeNotNull.and(relationType)))
                .collect(Collectors.toList())
                .get(0);
    }

    private String getWIkiDataAPIURL(Relation relation, String entityId) {

        log.info("Fetching WikiData API URL");

        String wikiDataAPIUrl = "";

        if (null != relation && null != relation.getUrl() && null != relation.getUrl().getResource()) {
            String url = relation.getUrl().getResource();
            wikiDataAPIUrl = url.replace(entityId, "Special:EntityData/" + entityId + ".json");
        }

        return wikiDataAPIUrl;
    }

    private SiteLink getSiteLinkData(String wikiDataAPIUrl, String entityId) {

        log.info("Fetching SiteLink Data");

        SiteLink siteLink = new SiteLink();
        try {
            // Get JSON Object from APIUrl
            JsonNode json = new ObjectMapper().readTree(new URL(wikiDataAPIUrl));

            // Set property values to siteLink object
            siteLink.setSite(json.get("entities").get(entityId).get("sitelinks").get("enwiki").get("site").textValue());
            siteLink.setTitle(json.get("entities").get(entityId).get("sitelinks").get("enwiki").get("title").textValue());
            siteLink.setUrl(json.get("entities").get(entityId).get("sitelinks").get("enwiki").get("url").textValue());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return siteLink;
    }

    @CircuitBreaker(name = "musifycircuitbreakerclient")
    private String getDescFromWikiLink(SiteLink siteLink) {

        log.info("Fetching Description from Wikipedia");

        ObjectMapper mapper = new ObjectMapper();
        String description = "";

        try {
            /*.replace(" ", "%20")*/
            final String uri = apiUrl + siteLink.getTitle();
            log.info("Wikipedia API being called: " + uri);

            ResponseEntity<String> response = restTemplate.exchange(
                    uri, HttpMethod.GET, null,
                    new ParameterizedTypeReference<String>() {
                    });

            // Get JSON Object from response body
            JsonNode json = new ObjectMapper().readTree(response.getBody());
            description = json.get("extract_html").textValue();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return description;
    }
}
