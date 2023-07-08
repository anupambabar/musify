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
public class ReleaseGroup implements Serializable {

    private String id;
    private String title;
    private String primaryType;
}
