package com.laterna.xaxathonprime.notification;

import com.laterna.xaxathonprime._shared.converter.JsonConverter;
import com.laterna.xaxathonprime._shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {

    private Long recipientId;
    private String content;
    private String type;
    private boolean read;
    
    @CreatedDate
    private LocalDateTime createdAt;
}