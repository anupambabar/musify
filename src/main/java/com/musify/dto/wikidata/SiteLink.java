package com.musify.dto.wikidata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SiteLink implements Serializable {

    private String site;
    private String title;
    private String url;
}
