package com.laterna.xaxathonprime.notification.event;

import java.time.LocalDateTime;

interface DomainEvent {
    String getType();
    LocalDateTime getTimestamp();
}
