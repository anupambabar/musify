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
public class LifeSpan implements Serializable {

    private String begin;
    private String end;
    private Boolean ended;
}
