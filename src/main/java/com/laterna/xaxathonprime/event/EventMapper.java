package com.laterna.xaxathonprime.event;

import com.laterna.xaxathonprime.event.dto.CreateEventDto;
import com.laterna.xaxathonprime.event.dto.EventDto;
import com.laterna.xaxathonprime.eventrequest.EventRequest;
import com.laterna.xaxathonprime.eventrequest.dto.EventRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "name", source = "base.name")
    @Mapping(target = "gender", source = "base.gender")
    @Mapping(target = "minAge", source = "base.minAge")
    @Mapping(target = "maxAge", source = "base.maxAge")
    @Mapping(target = "location", source = "base.location")
    @Mapping(target = "disciplines", source = "base.disciplines")
    @Mapping(target = "startDate", source = "base.startDate")
    @Mapping(target = "endDate", source = "base.endDate")
    @Mapping(target = "maxParticipants", source = "base.maxParticipants")
    @Mapping(target = "region", source = "base.region")
    EventDto toDto(Event event);

    EventRequest toEntity(EventRequestDto eventRequestDto);

    CreateEventDto toCreateEventDto(EventRequest eventRequest);
}
