package com.laterna.xaxathonprime.eventbase.dto;

import com.laterna.xaxathonprime.discipline.Discipline;
import com.laterna.xaxathonprime.region.Region;

import java.time.LocalDateTime;
import java.util.Set;

public record EventBaseDto(
        Long id,
        String name,
        String gender,
        Integer minAge,
        Integer maxAge,
        String location,
        Set<Discipline> disciplines,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer maxParticipants,
        Region region
) {
}
