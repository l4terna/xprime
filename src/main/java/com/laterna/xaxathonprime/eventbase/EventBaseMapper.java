package com.laterna.xaxathonprime.eventbase;

import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventBaseMapper {
    EventBase toEntity(EventBaseDto eventBaseDto);
    EventBaseDto toDto(EventBase eventBase);
}
