package com.laterna.xaxathonprime.emailverification.dto;


public record VerifyNewPasswordDto (
        String token,
        String newPassword
) {}
