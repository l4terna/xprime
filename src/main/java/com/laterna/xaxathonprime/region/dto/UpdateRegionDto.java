package com.laterna.xaxathonprime.region.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateRegionDto(
        @JsonProperty(required = false) String name,
        @JsonProperty(required = false) String description,
        @JsonProperty(required = false) String contactEmail,
        @JsonProperty(required = false) Long userId
) {
}
