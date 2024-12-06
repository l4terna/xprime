package com.laterna.xaxathonprime.region;

import com.laterna.xaxathonprime.region.dto.RegionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    RegionDto toDto(Region region);
    Region toEntity(RegionDto regionDto);
}
