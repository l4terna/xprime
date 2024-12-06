package com.laterna.xaxathonprime.eventbase;

import com.laterna.xaxathonprime._shared.context.UserContext;
import com.laterna.xaxathonprime.discipline.Discipline;
import com.laterna.xaxathonprime.discipline.DisciplineMapper;
import com.laterna.xaxathonprime.discipline.DisciplineService;
import com.laterna.xaxathonprime.eventbase.dto.CreateEventBaseDto;
import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import com.laterna.xaxathonprime.region.Region;
import com.laterna.xaxathonprime.region.RegionMapper;
import com.laterna.xaxathonprime.region.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventBaseService {
    private final DisciplineService disciplineService;
    private final RegionMapper regionMapper;
    private final RegionService regionService;
    private final UserContext userContext;
    private final DisciplineMapper disciplineMapper;
    private final EventBaseMapper eventBaseMapper;
    private final EventBaseRepository eventBaseRepository;

    @Transactional
    public EventBaseDto create(CreateEventBaseDto createEventBaseDto) {
        Region region = regionMapper.toEntity(regionService.getRegion(userContext.getCurrentUserId()));
        Set<Discipline> disciplines = disciplineService.findAllById(createEventBaseDto.disciplines())
                .stream()
                .map(disciplineMapper::toEntity)
                .collect(Collectors.toSet());

        EventBase eventBase = EventBase.builder()
                .name(createEventBaseDto.name())
                .gender(createEventBaseDto.gender())
                .minAge(createEventBaseDto.minAge())
                .maxAge(createEventBaseDto.maxAge())
                .location(createEventBaseDto.location())
                .startDate(createEventBaseDto.startDate())
                .endDate(createEventBaseDto.endDate())
                .maxParticipants(createEventBaseDto.maxParticipants())
                .region(region)
                .disciplines(disciplines)
                .build();
        eventBase.getDisciplines().addAll(disciplines);

        EventBase savedBase = eventBaseRepository.save(eventBase);


        return eventBaseMapper.toDto(savedBase);
    }
}
