package com.laterna.xaxathonprime.region.dto;

public record CreateRegionDto(
    String name,
    String description,
    String contactEmail,
    Long userId,
    String imageUrl
) {
}