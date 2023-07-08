package com.musify.mapper;

import com.musify.dto.musicbrainz.MusicBrainzResponse;
import com.musify.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtistDataMapper {

    ArtistDataMapper ARTIST_DATA_MAPPER = Mappers.getMapper(ArtistDataMapper.class);

    @Mapping(source = "id", target = "mbid")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "disambiguation", target = "disambiguation")
    Artist mapMBResponseToArtist(MusicBrainzResponse mbResponse);
}
