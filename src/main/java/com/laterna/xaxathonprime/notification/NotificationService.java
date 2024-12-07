package com.laterna.xaxathonprime.notification;

import com.laterna.xaxathonprime._shared.context.UserContext;
import com.laterna.xaxathonprime.notification.dto.CreateNotificationDto;
import com.laterna.xaxathonprime.notification.dto.NotificationDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserContext userContext;

    @Transactional(readOnly = true)
    public Page<NotificationDto> getNotifications(Boolean read, Pageable pageable) {
        Specification<Notification> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("recipientId"), userContext.getCurrentUserId()));

            if (read != null) {
                predicates.add(cb.equal(root.get("read"), read));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return notificationRepository.findAll(spec, pageable)
                .map(notificationMapper::toDto);
    }

    @Transactional
    public NotificationDto markAsRead(Long id) {
        Notification notification = notificationRepository.findByIdAndRecipientId(id, userContext.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        notification.setRead(true);
        return notificationMapper.toDto(notificationRepository.save(notification));
    }

    @Transactional
    public void markAllAsRead() {
        notificationRepository.markAllAsRead(userContext.getCurrentUserId());
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount() {
        return notificationRepository.countByRecipientIdAndReadFalse(userContext.getCurrentUserId());
    }

    @Transactional
    public void createNotification(CreateNotificationDto notificationDto) {
        Notification notification = Notification.builder()
                .type(notificationDto.type())
                .content(notificationDto.content())
                .recipientId(notificationDto.recipientId())
                .read(false)
                .build();

        notificationRepository.save(notification);
    }
}
