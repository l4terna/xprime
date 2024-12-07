package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime.event.EventService;
import com.laterna.xaxathonprime.event.dto.CreateEventDto;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.eventbase.EventBaseMapper;
import com.laterna.xaxathonprime.eventbase.EventBaseService;
import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import com.laterna.xaxathonprime.eventrequest.dto.CreateEventRequestDto;
import com.laterna.xaxathonprime.eventrequest.dto.EventRequestDto;
import com.laterna.xaxathonprime.eventrequest.dto.RejectEventRequestDto;
import com.laterna.xaxathonprime.eventrequest.enumeration.EventRequestStatus;
import com.laterna.xaxathonprime.notification.event.EventApprovedEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final EventRequestMapper eventRequestMapper;
    private final EventBaseService eventBaseService;
    private final EventBaseMapper eventBaseMapper;
    private final EventService eventService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public EventRequestDto createRequest(CreateEventRequestDto createRequestDto) {
        EventBaseDto baseDto = eventBaseService.create(eventRequestMapper.toCreateEventBaseDto(createRequestDto));

        EventRequest eventRequest = EventRequest.builder()
                .status(EventRequestStatus.PENDING)
                .base(eventBaseMapper.toEntity(baseDto))
                .build();

        return eventRequestMapper.toDto(eventRequestRepository.save(eventRequest));
    }

    @Transactional(readOnly = true)
    public Page<EventRequestDto> findAll(Pageable pageable, EventRequestStatus status) {
        if(status != null) {
            System.out.println(eventRequestRepository.findAllByStatus(status, pageable));
            return eventRequestRepository.findAllByStatus(status, pageable).map(eventRequestMapper::toDto);
        }

        return eventRequestRepository.findAll(pageable).map(eventRequestMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EventRequestDto getRequest(Long id) {
        return eventRequestRepository.findById(id)
                .map(eventRequestMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Event Request not found with id: " + id));
    }

    @Transactional
    public EventRequestDto reject(Long id, RejectEventRequestDto rejectDto) {
        EventRequest request = eventRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event request not found"));

        request.setStatus(EventRequestStatus.REJECTED);
        request.setModerationComment(rejectDto.comment());

        return eventRequestMapper.toDto(eventRequestRepository.save(request));
    }

    @Transactional
    public EventRequestDto approve(Long id) {
        EventRequest request = eventRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event request not found"));

        request.setStatus(EventRequestStatus.APPROVED);
        eventService.createEvent(new CreateEventDto(request));

        eventPublisher.publishEvent(new EventApprovedEvent(
                request.getId(),
                request.getBase().getName(),
                request.getBase().getRegion().getUser() != null ? request.getBase().getRegion().getUser().getId() : null,
                LocalDateTime.now()
        ));

        return eventRequestMapper.toDto(eventRequestRepository.save(request));
    }
}
