package com.laterna.xaxathonprime.team.dto;

import java.time.LocalDateTime;

public record TeamDto(
        Long id,
        String name,
        String description,
        Long regionId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}