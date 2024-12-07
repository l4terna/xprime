package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime.eventbase.dto.CreateEventBaseDto;
import com.laterna.xaxathonprime.eventrequest.dto.CreateEventRequestDto;
import com.laterna.xaxathonprime.eventrequest.dto.EventRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventRequestMapper {

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
    @Mapping(target = "base_id", source = "base.id")
    EventRequestDto toDto(EventRequest eventRequest);
    CreateEventBaseDto toCreateEventBaseDto(CreateEventRequestDto createEventRequestDto);
}
