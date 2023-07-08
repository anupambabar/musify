package com.musify.dto.coverartarchive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Serializable {

    private String id;
    private String image;
    private Boolean front;
}
