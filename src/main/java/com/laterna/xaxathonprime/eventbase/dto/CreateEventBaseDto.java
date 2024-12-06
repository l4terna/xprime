package com.laterna.xaxathonprime.eventbase.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateEventBaseDto (
        String name,
        String gender,
        Integer minAge,
        Integer maxAge,
        String location,
        Set<Long> disciplines,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer maxParticipants,
        Long regionId
) {}
