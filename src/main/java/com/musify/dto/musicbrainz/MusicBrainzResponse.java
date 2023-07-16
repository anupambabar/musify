package com.musify.dto.musicbrainz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MusicBrainzResponse implements Serializable {

    private String id;
    private String name;
    private String gender;
    private String country;
    private String disambiguation;
    private ArrayList<Relation> relations;

    @JsonProperty("life-span")
    private LifeSpan lifeSpan;

    @JsonProperty("release-groups")
    private ArrayList<ReleaseGroup> releaseGroups;
}
