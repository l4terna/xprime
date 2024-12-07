package com.laterna.xaxathonprime.notification.event;

import java.time.LocalDateTime;

public record EventApprovedEvent(
    Long eventId,
    String eventName,
    Long regionAdminId,
    LocalDateTime approvedAt
) implements DomainEvent{
    @Override
    public String getType() {
        return "EVENT_APPROVED";
    }

    @Override
    public LocalDateTime getTimestamp() {
        return approvedAt;
    }
}
