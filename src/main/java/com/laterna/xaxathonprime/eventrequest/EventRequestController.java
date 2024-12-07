package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime.eventrequest.dto.CreateEventRequestDto;
import com.laterna.xaxathonprime.eventrequest.dto.EventRequestDto;
import com.laterna.xaxathonprime.eventrequest.dto.RejectEventRequestDto;
import com.laterna.xaxathonprime.eventrequest.enumeration.EventRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-requests")
@RequiredArgsConstructor
public class EventRequestController {
    private final EventRequestService eventRequestService;

    @GetMapping
    @PreAuthorize("hasAuthority('FSP_ADMIN')")
    public ResponseEntity<Page<EventRequestDto>> getEventRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) EventRequestStatus status
            ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<EventRequestDto> regions = eventRequestService.findAll(pageRequest, status);
        return ResponseEntity.ok(regions);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('REGION_ADMIN')")
    public ResponseEntity<EventRequestDto> createRequest(@RequestBody CreateEventRequestDto createRequestDto) {
        return ResponseEntity.ok(eventRequestService.createRequest(createRequestDto));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('FSP_ADMIN', 'REGION_ADMIN')")
    public ResponseEntity<Page<EventRequestDto>> getMyRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) EventRequestStatus status
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<EventRequestDto> regions = eventRequestService.getMyRequests(pageRequest, status);
        return ResponseEntity.ok(regions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('FSP_ADMIN', 'REGION_ADMIN')")
    public ResponseEntity<EventRequestDto> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(eventRequestService.getRequest(id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FSP_ADMIN')")
    public ResponseEntity<EventRequestDto> reject(
            @PathVariable Long id,
            @RequestBody RejectEventRequestDto rejectRequest) {
        return ResponseEntity.ok(eventRequestService.reject(id, rejectRequest));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('FSP_ADMIN')")
    public ResponseEntity<EventRequestDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(eventRequestService.approve(id));
    }
}
