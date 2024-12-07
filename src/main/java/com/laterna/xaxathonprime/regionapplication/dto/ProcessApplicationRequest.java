package com.laterna.xaxathonprime.regionapplication.dto;

import com.laterna.xaxathonprime.regionapplication.enumeration.ApplicationStatus;

public record ProcessApplicationRequest (
    ApplicationStatus status,
    String responseMessage
) {}
