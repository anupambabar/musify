package com.musify.mapper;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtistDataMapper {

    @Mapping(source = "id", target = "mbid")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "disambiguation", target = "disambiguation")
    Artist mapMBResponseToArtist(MusicBrainzResponse mbResponse);
}
