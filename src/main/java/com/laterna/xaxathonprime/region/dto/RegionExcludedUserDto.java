package com.laterna.xaxathonprime.region.dto;


import java.time.LocalDateTime;

public record RegionExcludedUserDto(
        Long id,
        String name,
        String contactEmail,
        LocalDateTime createdAt
) {
}
