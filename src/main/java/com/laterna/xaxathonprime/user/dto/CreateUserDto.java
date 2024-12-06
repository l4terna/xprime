package com.laterna.xaxathonprime.user.dto;

public record CreateUserDto(
        String firstname,
        String lastname,
        String patronymic,
        String email,
        String password
) {
}
