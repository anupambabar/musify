package com.musify.dto.coverartarchive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CoverArtArchiveResponse implements Serializable {

    private ArrayList<Image> images;
    private String release;
}
