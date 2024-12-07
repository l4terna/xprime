package com.laterna.xaxathonprime.report;

import com.laterna.xaxathonprime.event.Event;
import com.laterna.xaxathonprime.event.EventService;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.region.RegionService;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.report.dto.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final EventService eventService;
    private final RegionService regionService;

    @Transactional(readOnly = true)
    public AllRegionsReportDto generateAllRegionsReport(AllRegionsReportFilter filter) {
        // Получаем все регионы
        List<RegionDto> regions = regionService.findAll();

        // Получаем все события с применением фильтров
        Specification<Event> spec = createSpecification(filter);
        List<Event> allEvents = eventService.findAll(spec);

        // Группируем события по регионам
        Map<Long, List<Event>> eventsByRegion = allEvents.stream()
                .collect(Collectors.groupingBy(event -> event.getBase().getRegion().getId()));

        // Формируем отчеты по каждому региону
        List<ReportDto> regionReports = regions.stream()
                .map(region -> {
                    List<Event> regionEvents = eventsByRegion.getOrDefault(region.id(), new ArrayList<>());
                    return createRegionReport(region, regionEvents);
                })
                .toList();

        // Формируем общий отчет
        return new AllRegionsReportDto(
                regions.size(),
                allEvents.size(),
                calculateTotalTeams(allEvents),
                calculateTotalParticipants(allEvents),
                regionReports
        );
    }

    private Specification<Event> createSpecification(AllRegionsReportFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Event, EventBase> baseJoin = root.join("base");

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

    private ReportDto createRegionReport(RegionDto region, List<Event> events) {
        List<EventReportDto> eventReports = events.stream()
                .map(this::mapToEventReport)
                .toList();

        return new ReportDto(
                region.name(),
                events.size(),
                calculateTotalTeams(events),
                calculateTotalParticipants(events),
                eventReports
        );
    }

    private EventReportDto mapToEventReport(Event event) {
        EventBase base = event.getBase();
        return new EventReportDto(
                base.getName(),
                base.getStartDate(),
                base.getEndDate(),
                base.getMaxParticipants(),
                calculateTeamCount(event),
                base.getLocation()
        );
    }

    private int calculateTotalTeams(List<Event> events) {
        return events.stream()
                .mapToInt(this::calculateTeamCount)
                .sum();
    }

    private int calculateTotalParticipants(List<Event> events) {
        return events.stream()
                .mapToInt(event -> event.getBase().getMaxParticipants())
                .sum();
    }

    private int calculateTeamCount(Event event) {
        // TODO: Имплементировать подсчет команд на основе вашей модели данных
        return 0;
    }
}
