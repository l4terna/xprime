package com.laterna.xaxathonprime.event;

import com.laterna.xaxathonprime.event.dto.EventBaseFilter;
import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<EventBaseDto>> getApprovedEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer maxParticipants,
            @RequestParam(required = false) Long regionId
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        EventBaseFilter filter = EventBaseFilter.builder()
                .name(name)
                .gender(gender)
                .minAge(minAge)
                .maxAge(maxAge)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .maxParticipants(maxParticipants)
                .regionId(regionId)
                .build();

        return ResponseEntity.ok(eventService.findAllApproved(pageRequest, filter));
    }
}
