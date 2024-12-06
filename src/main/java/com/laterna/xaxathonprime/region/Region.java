package com.laterna.xaxathonprime.region;

import com.laterna.xaxathonprime._shared.model.BaseEntity;
import com.laterna.xaxathonprime.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "regions")
@EntityListeners(AuditingEntityListener.class)
public class Region extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String contactEmail;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
