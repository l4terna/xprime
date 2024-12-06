package com.laterna.xaxathonprime.user.dto;

import com.laterna.xaxathonprime.role.dto.RoleDto;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String firstname,
        String lastname,
        String patronymic,
        String email,
        RoleDto role,
        LocalDateTime createdAt
) {
}
