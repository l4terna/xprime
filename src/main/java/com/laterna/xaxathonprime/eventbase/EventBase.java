package com.laterna.xaxathonprime.eventbase;

import com.laterna.xaxathonprime._shared.model.BaseEntity;
import com.laterna.xaxathonprime.discipline.Discipline;
import com.laterna.xaxathonprime.eventprotocol.EventProtocol;
import com.laterna.xaxathonprime.region.Region;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "event_base")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EventBase extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String gender;
    private Integer minAge;
    private Integer maxAge;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipants;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToMany
    @JoinTable(
            name = "event_disciplines",
            joinColumns = @JoinColumn(name = "event_base_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private Set<Discipline> disciplines = new HashSet<>();

    @OneToOne(mappedBy = "eventBase")
    @JoinColumn(name = "event_protocol_id")
    private EventProtocol eventProtocol;
}
