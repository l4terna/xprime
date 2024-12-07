package com.laterna.xaxathonprime.eventprotocol.dto;

import java.time.LocalDateTime;

public record EventProtocolDto(
        Long id,
        String fileName,
        String contentType,
        Long fileSize,
        LocalDateTime createdAt,
        Long eventBaseId,
        Long regionId
) {}