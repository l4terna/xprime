package com.laterna.xaxathonprime.team;

import com.laterna.xaxathonprime.team.dto.TeamDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    @Mapping(target = "members", source = "users")
    TeamDto toDto(Team team);
}
