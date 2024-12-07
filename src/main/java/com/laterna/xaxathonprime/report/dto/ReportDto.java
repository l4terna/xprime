package com.laterna.xaxathonprime.report.dto;

import java.util.List;

public record ReportDto(
        String regionName,
        int eventsCount,
        int teamsCount,
        int eventParticipantsCount,
        List<EventReportDto> events
) {}