package com.laterna.xaxathonprime.regionapplication;

import com.laterna.xaxathonprime._shared.model.BaseEntity;
import com.laterna.xaxathonprime.region.Region;
import com.laterna.xaxathonprime.regionapplication.enumeration.ApplicationStatus;
import com.laterna.xaxathonprime.user.User;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "region_applications")
@EntityListeners(AuditingEntityListener.class)
public class RegionApplication extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String responseMessage;
}