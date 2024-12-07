package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime._shared.model.BaseEntity;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.eventrequest.enumeration.EventRequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NamedEntityGraph(
        name = "EventRequest.withBase",
        attributeNodes = {
                @NamedAttributeNode("base")
        }
)
@Entity
@Table(name = "event_requests")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventRequest extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_id", nullable = false)
    private EventBase base;

    @Enumerated(EnumType.STRING)
    private EventRequestStatus status;

    private String moderationComment;
}