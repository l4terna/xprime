package com.laterna.xaxathonprime.report.dto;

import java.time.LocalDateTime;

public record EventReportDto(
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer maxParticipants,
        String location
) {}