package com.laterna.xaxathonprime.report.dto;

import java.util.List;

public record AllRegionsReportDto(
    int totalRegions,
    int totalEvents,
    int totalTeams,
    int totalEventParticipants,
    List<ReportDto> regionReports
) {}