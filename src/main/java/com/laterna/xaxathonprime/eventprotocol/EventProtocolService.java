package com.laterna.xaxathonprime.eventprotocol;

import com.laterna.xaxathonprime.eventbase.EventBaseMapper;
import com.laterna.xaxathonprime.eventbase.EventBaseService;
import com.laterna.xaxathonprime.eventbase.dto.EventBaseDto;
import com.laterna.xaxathonprime.eventprotocol.dto.EventProtocolDto;
import com.laterna.xaxathonprime.region.Region;
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

    private static final String UPLOAD_DIR = "uploads";

    @Transactional
    public EventProtocolDto uploadProtocol(Long eventBaseId, Long regionId, MultipartFile file) {
        // Проверяем существование предыдущего протокола
        eventProtocolRepository.findByEventBaseIdAndRegionId(eventBaseId, regionId)
                .ifPresent(existingProtocol -> {
                    try {
                        // Удаляем старый файл
                        Files.deleteIfExists(Paths.get(existingProtocol.getFilePath()));
                        // Удаляем запись из БД
                        eventProtocolRepository.delete(existingProtocol);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete existing file", e);
                    }
                });

        EventBaseDto eventBase = eventBaseService.findById(eventBaseId);
        RegionDto region = regionService.findById(regionId);

        try {
            // Create uploads directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(storedFileName);

            // Save file to disk
            Files.write(filePath, file.getBytes());

            // Create and save protocol entity
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

            // Return DTO
            return new EventProtocolDto(
                    protocol.getId(),
                    protocol.getOriginalFileName(),
                    protocol.getContentType(),
                    protocol.getFileSize(),
                    protocol.getCreatedAt(),
                    protocol.getEventBase().getId(),
                    protocol.getRegion().getId()
            );
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
            // Delete file from disk
            Path filePath = Paths.get(protocol.getFilePath());
            Files.deleteIfExists(filePath);

            // Delete record from database
            eventProtocolRepository.delete(protocol);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}