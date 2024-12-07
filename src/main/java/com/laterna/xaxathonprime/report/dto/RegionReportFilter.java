package com.laterna.xaxathonprime.report.dto;

import java.time.LocalDateTime;

public record RegionReportFilter(
    Long regionId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String eventName,
    Integer minParticipants,
    Integer maxParticipants
) {}