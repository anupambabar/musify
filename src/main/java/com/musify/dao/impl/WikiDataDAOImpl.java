package com.musify.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musify.dao.WikiDataDAO;
import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.dto.musicbrainz.Relation;
import com.musify.dto.wikidata.SiteLink;
import com.musify.entity.Artist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class WikiDataDAOImpl implements WikiDataDAO {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${wikipedia.api.url}")
    private String apiUrl;

    @Override
    public Mono<Artist> getArtistDetailsFromWD(Mono<Artist> artist, Mono<MusicBrainzResponse> mbResponse) {

        LOGGER.info("Get Artist Wiki Details");

        // Get Artist WikiData Relation
        // Relation relation = (Relation) mbResponse.subscribe(mbr -> getArtistWikiDataRelation(mbr.getRelations()));
        Relation relation = (Relation) mbResponse.subscribe(mbr -> getArtistWikiDataRelation(mbr.getRelations()));

        if (null != relation.getUrl() && null != relation.getUrl().getResource()
                && !relation.getUrl().getResource().isEmpty()
                && relation.getUrl().getResource().contains("/")) {

            // Get entityId
            String entityId = relation.getUrl().getResource().substring(relation.getUrl().getResource().lastIndexOf('/') + 1);

            // Get Artist WikiData URL
            String wikiDataAPIUrl = getWIkiDataAPIURL(relation, entityId);

            // Get SiteLink Data
            SiteLink siteLink = getSiteLinkData(wikiDataAPIUrl, entityId);

            // Get Wikipedia Description and Assign Description to Artist
            artist.map(a -> {
                a.setDescription(getDescFromWikiLink(siteLink));
                return a;
            });
        } else {
            artist.map(a -> {
                a.setDescription("Description Not Found");
                return a;
            });
        }

        return artist;
    }

    private Relation getArtistWikiDataRelation(ArrayList<Relation> relations) {

        LOGGER.info("Fetching WikiData Relation");

        Predicate<Relation> relationNotNull = relation -> Objects.nonNull(relation);
        Predicate<Relation> relationTypeNotNull = relation -> Objects.nonNull(relation.getType());
        Predicate<Relation> relationType = relation -> relation.getType().equalsIgnoreCase("wikidata");

        if (!relations.stream().anyMatch(relationNotNull.and(relationTypeNotNull.and(relationType))))
            return new Relation();
        else
            return relations
                    .stream()
                    .filter(relationNotNull.and(relationTypeNotNull.and(relationType)))
                    .collect(Collectors.toList())
                    .get(0);
    }

    private String getWIkiDataAPIURL(Relation relation, String entityId) {

        LOGGER.info("Fetching WikiData API URL");
        return relation.getUrl().getResource().replace(entityId, "Special:EntityData/" + entityId + ".json");
    }

    private SiteLink getSiteLinkData(String wikiDataAPIUrl, String entityId) {

        LOGGER.info("Fetching SiteLink Data");

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

    private String getDescFromWikiLink(SiteLink siteLink) {

        LOGGER.info("Fetching Description from Wikipedia");

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String description = "";

        try {
            /*.replace(" ", "%20")*/
            final String uri = apiUrl + siteLink.getTitle();
            LOGGER.info("Wikipedia API being called: " + uri);

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
