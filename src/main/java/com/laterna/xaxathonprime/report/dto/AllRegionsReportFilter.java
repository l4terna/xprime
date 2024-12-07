package com.laterna.xaxathonprime.report.dto;

import java.time.LocalDateTime;

public record AllRegionsReportFilter(
    LocalDateTime startDate,
    LocalDateTime endDate,
    String eventName,
    Integer minParticipants,
    Integer maxParticipants
) {}