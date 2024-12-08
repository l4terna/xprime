package com.laterna.xaxathonprime.report.dto;

import java.util.List;

public record ReportDto(
        String regionName,
        int eventsCount,
        int currentEventsCount,
        int teamsCount,
        int eventParticipantsCount,
        List<EventReportDto> events,      // список мероприятий текущего уровня
        List<EventReportDto> regionReports, // список мероприятий по регионам
        int regionsCount                // количество регионов
) {}