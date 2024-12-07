package com.laterna.xaxathonprime.notification.dto;

import java.time.LocalDateTime;

public record NotificationDto(
    Long id,
    Long recipientId,
    String content,
    String type,
    boolean read,
    LocalDateTime createdAt
) {}