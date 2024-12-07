package com.laterna.xaxathonprime.eventprotocol;

import com.laterna.xaxathonprime.eventprotocol.dto.EventProtocolDto;
import com.laterna.xaxathonprime.eventprotocol.dto.UploadProtocolDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/event-protocols")
@RequiredArgsConstructor
public class EventProtocolController {
    private final EventProtocolService eventProtocolService;

    @GetMapping("/{eventBaseId}/region/{regionId}")
    public ResponseEntity<Resource> downloadProtocolByEventBaseAndRegion(
            @PathVariable Long eventBaseId,
            @PathVariable Long regionId) {
        return eventProtocolService.downloadProtocolByEventBaseAndRegion(eventBaseId, regionId);
    }

    @PostMapping("/{eventBaseId}/region/{regionId}/upload")
    public EventProtocolDto uploadProtocol(
            @PathVariable Long eventBaseId,
            @PathVariable Long regionId,
            @RequestParam("file") MultipartFile file
    ) {
        return eventProtocolService.uploadProtocol(eventBaseId, regionId, file);
    }

    @DeleteMapping("/{eventBaseId}/region/{regionId}")
    public void deleteProtocolByEventBaseAndRegion(
            @PathVariable Long eventBaseId,
            @PathVariable Long regionId) {
        eventProtocolService.deleteProtocolByEventBaseAndRegion(eventBaseId, regionId);
    }
}