package com.laterna.xaxathonprime.region.dto;


import com.laterna.xaxathonprime.user.dto.UserDto;

import java.time.LocalDateTime;

public record RegionDto (
    Long id,
    String name,
    String contactEmail,
    LocalDateTime createdAt,
    UserDto user
) {}

