package com.laterna.xaxathonprime.team;

import com.laterna.xaxathonprime.team.dto.TeamDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamDto toDto(Team team);
}
