package com.laterna.xaxathonprime.event.dto;

import com.laterna.xaxathonprime.discipline.Discipline;
import com.laterna.xaxathonprime.eventrequest.enumeration.EventRequestStatus;
import com.laterna.xaxathonprime.region.dto.RegionDto;

import java.time.LocalDateTime;
import java.util.Set;

public record EventDto (
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
        RegionDto region
) {}
