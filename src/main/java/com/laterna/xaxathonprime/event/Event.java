package com.laterna.xaxathonprime.event;

import com.laterna.xaxathonprime._shared.model.BaseEntity;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.eventrequest.EventRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "events")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "base_id")
    private EventBase base;

    @OneToOne
    @JoinColumn(name = "request_id")
    private EventRequest request;
}