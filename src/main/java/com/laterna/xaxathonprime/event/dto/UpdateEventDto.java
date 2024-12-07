package com.laterna.xaxathonprime.event.dto;

import com.laterna.xaxathonprime.eventrequest.EventRequest;

public record UpdateEventDto(
        EventRequest eventRequest
) {
}
