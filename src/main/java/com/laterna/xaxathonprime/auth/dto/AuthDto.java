package com.laterna.xaxathonprime.auth.dto;

import com.laterna.xaxathonprime.user.dto.UserDto;

public record AuthDto(
        String token,
        UserDto user
) { }
