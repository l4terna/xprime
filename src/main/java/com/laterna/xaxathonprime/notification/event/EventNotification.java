package com.laterna.xaxathonprime.notification.event;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class EventNotification implements DomainEvent {
    private final Long eventId;
    private final Long recipientId;
    private final String content;
    private final String type;
    private final LocalDateTime timestamp;

    @Override
    public String getType() {
        return type;
    }
}