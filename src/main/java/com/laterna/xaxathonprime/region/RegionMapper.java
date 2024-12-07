package com.laterna.xaxathonprime.region;

import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.region.dto.RegionExcludedUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    RegionDto toDto(Region region);

    Region toEntity(RegionDto regionDto);
    Region toEntity(RegionExcludedUserDto excludedUserDto);
}
