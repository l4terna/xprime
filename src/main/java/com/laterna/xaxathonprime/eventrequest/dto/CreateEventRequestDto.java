package com.laterna.xaxathonprime.eventrequest.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateEventRequestDto (
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
){}
