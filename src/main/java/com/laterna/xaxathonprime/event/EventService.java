package com.laterna.xaxathonprime.event;

import com.laterna.xaxathonprime.discipline.Discipline;
import com.laterna.xaxathonprime.event.dto.CreateEventDto;
import com.laterna.xaxathonprime.event.dto.EventBaseFilter;
import com.laterna.xaxathonprime.event.dto.EventDto;
import com.laterna.xaxathonprime.event.dto.UpdateEventDto;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.eventbase.EventBaseMapper;
import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import com.laterna.xaxathonprime.region.Region;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventBaseMapper eventBaseMapper;

    @Transactional(readOnly = true)
    public Page<EventBaseDto> findAllApproved(Pageable pageable, EventBaseFilter filter) {
        Specification<Event> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Event, EventBase> baseJoin = root.join("base");

            if (filter.name() != null) {
                predicates.add(cb.like(cb.lower(baseJoin.get("name")), "%" + filter.name().toLowerCase() + "%"));
            }
            if (filter.gender() != null) {
                predicates.add(cb.equal(baseJoin.get("gender"), filter.gender()));
            }
            if (filter.minAge() != null) {
                predicates.add(cb.greaterThanOrEqualTo(baseJoin.get("minAge"), filter.minAge()));
            }
            if (filter.maxAge() != null) {
                predicates.add(cb.lessThanOrEqualTo(baseJoin.get("maxAge"), filter.maxAge()));
            }
            if (filter.location() != null) {
                predicates.add(cb.like(cb.lower(baseJoin.get("location")), "%" + filter.location().toLowerCase() + "%"));
            }
            if (filter.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(baseJoin.get("startDate"), filter.startDate()));
            }
            if (filter.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(baseJoin.get("endDate"), filter.endDate()));
            }
            if (filter.maxParticipants() != null) {
                predicates.add(cb.equal(baseJoin.get("maxParticipants"), filter.maxParticipants()));
            }
            if (filter.regionId() != null) {
                Join<EventBase, Region> regionJoin = baseJoin.join("region");
                predicates.add(cb.equal(regionJoin.get("id"), filter.regionId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return eventRepository.findAll(spec, pageable)
                .map(event -> eventBaseMapper.toDto(event.getBase()));
    }

    @Transactional
    public EventDto createEvent(CreateEventDto createDto) {
        Event event = Event.builder()
                .base(createDto.eventRequest().getBase())
                .request(createDto.eventRequest())
                .build();

        return eventMapper.toDto(eventRepository.save(event));
    }


}
