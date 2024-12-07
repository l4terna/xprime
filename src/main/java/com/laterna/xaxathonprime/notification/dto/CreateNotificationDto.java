package com.laterna.xaxathonprime.notification.dto;

import java.util.Map;

public record CreateNotificationDto(
        String type,
        String content,
        Map<String, Object> metadata,
        Long recipientId
) {
}
