package com.laterna.xaxathonprime.regionapplication.dto;

public record CreateApplicationRequest (
    Long regionId,
    String title,
    String description
) {}