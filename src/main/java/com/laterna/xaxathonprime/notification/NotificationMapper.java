package com.laterna.xaxathonprime.notification;

import com.laterna.xaxathonprime.notification.dto.NotificationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
}
