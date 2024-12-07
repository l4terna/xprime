package com.laterna.xaxathonprime.region.dto;

public record UpdateRegionDto(
        String name,
        String description,
        String contactEmail,
        Long userId
) {
}
