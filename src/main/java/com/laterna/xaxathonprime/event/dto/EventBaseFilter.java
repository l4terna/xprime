package com.laterna.xaxathonprime.event.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventBaseFilter (
    String name,
    String gender,
    Integer minAge,
    Integer maxAge,
    String location,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Integer maxParticipants,
    Long regionId
) {}