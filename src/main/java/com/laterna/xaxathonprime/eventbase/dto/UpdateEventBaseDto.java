package com.laterna.xaxathonprime.eventbase.dto;

import com.laterna.xaxathonprime.region.Region;

import java.time.LocalDateTime;
import java.util.Set;

public record UpdateEventBaseDto (
    String name,
    String gender,
    Integer minAge,
    Integer maxAge,
    String location,
    Set<Long> disciplines,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Integer maxParticipants,
    Region region
) {}
