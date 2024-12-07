package com.laterna.xaxathonprime.eventprotocol;

import com.laterna.xaxathonprime.eventbase.EventBaseMapper;
import com.laterna.xaxathonprime.eventbase.EventBaseService;
import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import com.laterna.xaxathonprime.eventprotocol.dto.EventProtocolDto;
import com.laterna.xaxathonprime.region.RegionMapper;
import com.laterna.xaxathonprime.region.RegionService;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventProtocolService {
    private final EventProtocolRepository eventProtocolRepository;
    private final EventBaseService eventBaseService;
    private final EventBaseMapper eventBaseMapper;
    private final RegionService regionService;
    private final RegionMapper regionMapper;
    private final EventProtocolMapper eventProtocolMapper;

    private static final String UPLOAD_DIR = "uploads";

    @Transactional
    public EventProtocolDto uploadProtocol(Long eventBaseId, Long regionId, MultipartFile file) {
        eventProtocolRepository.findByEventBaseIdAndRegionId(eventBaseId, regionId)
                .ifPresent(existingProtocol -> {
                    try {
                        Files.deleteIfExists(Paths.get(existingProtocol.getFilePath()));
                        eventProtocolRepository.delete(existingProtocol);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete existing file", e);
                    }
                });

        EventBaseDto eventBase = eventBaseService.findById(eventBaseId);
        RegionDto region = regionService.findById(regionId);

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(storedFileName);

            Files.write(filePath, file.getBytes());

            EventProtocol protocol = EventProtocol.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storedFileName)
                    .filePath(filePath.toString())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .eventBase(eventBaseMapper.toEntity(eventBase))
                    .region(regionMapper.toEntity(region))
                    .build();

            protocol = eventProtocolRepository.save(protocol);

            return eventProtocolMapper.toDto(protocol);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public ResponseEntity<Resource> downloadProtocolByEventBaseAndRegion(Long eventBaseId, Long regionId) {
        EventProtocol protocol = eventProtocolRepository.findByEventBaseIdAndRegionId(eventBaseId, regionId)
                .orElseThrow(() -> new EntityNotFoundException("Protocol not found for eventBase: " + eventBaseId + " and region: " + regionId));

        try {
            Path filePath = Paths.get(protocol.getFilePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(protocol.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + protocol.getOriginalFileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @Transactional
    public void deleteProtocolByEventBaseAndRegion(Long eventBaseId, Long regionId) {
        EventProtocol protocol = eventProtocolRepository.findByEventBaseIdAndRegionId(eventBaseId, regionId)
                .orElseThrow(() -> new EntityNotFoundException("Protocol not found for eventBase: " + eventBaseId + " and region: " + regionId));

        try {
            Path filePath = Paths.get(protocol.getFilePath());
            Files.deleteIfExists(filePath);

            eventProtocolRepository.delete(protocol);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}