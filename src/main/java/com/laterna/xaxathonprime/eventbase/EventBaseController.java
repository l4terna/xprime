package com.laterna.xaxathonprime.eventbase;

import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import com.laterna.xaxathonprime.eventbase.dto.UpdateEventBaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-bases")
@RequiredArgsConstructor
public class EventBaseController {
    private final EventBaseService eventBaseService;

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('FSP_ADMIN', 'REGION_ADMIN')")
    public ResponseEntity<EventBaseDto> updateEvent(@PathVariable Long id, @RequestBody UpdateEventBaseDto eventBaseDto) {
        return ResponseEntity.ok(eventBaseService.updateEvent(id, eventBaseDto));
    }
}
