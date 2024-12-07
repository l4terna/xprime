package com.laterna.xaxathonprime.report.dto;

import java.time.LocalDateTime;

public record EventReportDto(
    String eventName,
    LocalDateTime startDate,
    LocalDateTime endDate,
    int participantCount,
    int teamCount,
    String location
) {}