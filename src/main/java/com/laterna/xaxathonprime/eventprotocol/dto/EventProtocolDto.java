package com.laterna.xaxathonprime.eventprotocol.dto;

import java.time.LocalDateTime;

public record EventProtocolDto(
        Long id,
        String originalFileName,
        String storedFileName,
        String contentType,
        Long fileSize,
        LocalDateTime createdAt
) {}