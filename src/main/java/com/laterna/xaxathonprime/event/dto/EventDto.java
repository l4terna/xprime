package com.laterna.xaxathonprime.event.dto;

import com.laterna.xaxathonprime.discipline.Discipline;
import com.laterna.xaxathonprime.eventprotocol.dto.EventProtocolDto;
import com.laterna.xaxathonprime.region.dto.RegionExcludedUserDto;

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
        RegionExcludedUserDto region,
        EventProtocolDto eventProtocol
) {}
