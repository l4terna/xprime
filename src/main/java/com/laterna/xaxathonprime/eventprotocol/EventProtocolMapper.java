package com.laterna.xaxathonprime.eventprotocol;

import com.laterna.xaxathonprime.eventprotocol.dto.EventProtocolDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventProtocolMapper {
    EventProtocolDto toDto(EventProtocol eventProtocol);
}
