package com.laterna.xaxathonprime.team.dto;

import com.laterna.xaxathonprime.region.dto.RegionExcludedUserDto;
import com.laterna.xaxathonprime.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

public record TeamDto(
        Long id,
        String name,
        String description,
        RegionExcludedUserDto region,
        Set<UserDto> members,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}