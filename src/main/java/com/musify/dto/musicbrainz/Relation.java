package com.musify.dto.musicbrainz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Relation implements Serializable {

    private String type;
    private String typeId;
    private String targetType;
    private Url url;
}
