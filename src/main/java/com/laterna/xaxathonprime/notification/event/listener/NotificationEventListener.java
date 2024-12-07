package com.laterna.xaxathonprime.notification.event.listener;

import com.laterna.xaxathonprime.notification.NotificationService;
import com.laterna.xaxathonprime.notification.dto.CreateNotificationDto;
import com.laterna.xaxathonprime.notification.event.EventApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
class NotificationEventListener {
    private final NotificationService notificationService;

    @EventListener
    public void handleEventApproved(EventApprovedEvent event) {
        notificationService.createNotification(new CreateNotificationDto(
                event.getType(),
                String.format("Ваше мероприятие \"%s\" было одобрено", event.eventName()),
                Map.of(
                        "eventId", event.eventId(),
                        "approvedAt", event.approvedAt()
                ),
                event.regionAdminId()
            )
        );
    }
}