package com.laterna.xaxathonprime.report;

import com.laterna.xaxathonprime.report.dto.AllRegionsReportDto;
import com.laterna.xaxathonprime.report.dto.AllRegionsReportFilter;
import com.laterna.xaxathonprime.report.dto.RegionReportFilter;
import com.laterna.xaxathonprime.report.dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/regions")
    public ResponseEntity<ReportDto> generateReport(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) Integer minParticipants,
            @RequestParam(required = false) Integer maxParticipants
    ) {
        RegionReportFilter filter = new RegionReportFilter(
                regionId, startDate, endDate, eventName,
                minParticipants, maxParticipants
        );

        return ResponseEntity.ok(reportService.generateReport(filter));
    }

    @GetMapping("/teams")
    public ResponseEntity<Long> generateTeams() {
        return ResponseEntity.ok(reportService.getCountOfTeams());
    }

    @GetMapping("/users")
    public ResponseEntity<Long> generateUsers() {
        return ResponseEntity.ok(reportService.getCountOfUsers());
    }
}
