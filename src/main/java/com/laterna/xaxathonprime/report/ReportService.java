package com.laterna.xaxathonprime.report;

import com.laterna.xaxathonprime.event.Event;
import com.laterna.xaxathonprime.event.EventService;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.region.RegionService;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.report.dto.*;
import com.laterna.xaxathonprime.team.TeamService;
import com.laterna.xaxathonprime.user.UserService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final EventService eventService;
    private final RegionService regionService;
    private final TeamService teamService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public ReportDto generateReport(RegionReportFilter filter) {
        List<RegionDto> regions = filter.regionId() != null ?
                List.of(regionService.findById(filter.regionId())) :
                regionService.findAll();

        List<Event> events = eventService.findAll(createSpecification(filter));

        LocalDateTime now = LocalDateTime.now();
        int currentEventsCount = (int) events.stream()
                .filter(event -> isEventCurrent(event, now))
                .count();

        int totalTeams = regions.stream()
                .mapToInt(region -> teamService.countTeamsByRegionId(region.id()))
                .sum();

        int totalParticipants = regions.stream()
                .mapToInt(region -> teamService.countParticipantsByRegionId(region.id()))
                .sum();

        return new ReportDto(
                filter.regionId() != null ? regions.getFirst().name() : null,
                events.size(),
                currentEventsCount,
                totalTeams,
                totalParticipants,
                events.stream().map(this::mapToEventReport).toList(),
                filter.regionId() != null ? null : generateRegionEventReports(regions),
                regions.size()
        );
    }

    private List<EventReportDto> generateRegionEventReports(List<RegionDto> regions) {
        return regions.stream()
                .flatMap(region -> {
                    RegionReportFilter filter = new RegionReportFilter(region.id(), null, null, null, null, null);
                    List<Event> regionEvents = eventService.findAll(createSpecification(filter));
                    return regionEvents.stream().map(this::mapToEventReport);
                })
                .toList();
    }

    private EventReportDto mapToEventReport(Event event) {
        EventBase base = event.getBase();
        return new EventReportDto(
                base.getName(),
                base.getStartDate(),
                base.getEndDate(),
                base.getMaxParticipants(),
                base.getLocation()
        );
    }



    private boolean isEventCurrent(Event event, LocalDateTime now) {
        LocalDateTime startDate = event.getBase().getStartDate();
        LocalDateTime endDate = event.getBase().getEndDate();
        return startDate != null && endDate != null &&
                !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    private List<ReportDto> generateRegionReports(List<RegionDto> regions) {
        LocalDateTime now = LocalDateTime.now();
        return regions.stream()
                .map(region -> {
                    RegionReportFilter filter = new RegionReportFilter(region.id(), null, null, null, null, null);
                    List<Event> regionEvents = eventService.findAll(createSpecification(filter));

                    int currentEvents = (int) regionEvents.stream()
                            .filter(event -> isEventCurrent(event, now))
                            .count();

                    return new ReportDto(
                            region.name(),
                            regionEvents.size(),
                            currentEvents,
                            teamService.countTeamsByRegionId(region.id()),
                            teamService.countParticipantsByRegionId(region.id()),
                            regionEvents.stream().map(this::mapToEventReport).toList(),
                            null,
                            1
                    );
                })
                .toList();
    }

    private Specification<Event> createSpecification(RegionReportFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Event, EventBase> baseJoin = root.join("base");

            if (filter.regionId() != null) {
                predicates.add(cb.equal(baseJoin.get("region").get("id"), filter.regionId()));
            }

            if (filter.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(baseJoin.get("startDate"), filter.startDate()));
            }

            if (filter.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(baseJoin.get("endDate"), filter.endDate()));
            }

            if (filter.eventName() != null) {
                predicates.add(cb.like(cb.lower(baseJoin.get("name")),
                        "%" + filter.eventName().toLowerCase() + "%"));
            }

            if (filter.minParticipants() != null) {
                predicates.add(cb.greaterThanOrEqualTo(baseJoin.get("maxParticipants"),
                        filter.minParticipants()));
            }

            if (filter.maxParticipants() != null) {
                predicates.add(cb.lessThanOrEqualTo(baseJoin.get("maxParticipants"),
                        filter.maxParticipants()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public long getCountOfTeams() {
        return teamService.countAll();
    }

    public long getCountOfUsers() {
        return userService.countAll();
    }
}