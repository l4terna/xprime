package com.laterna.xaxathonprime._shared.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String message;
    private String error;
    private String endpoint;

    private Map<String, String> errors = new HashMap<>();

    public static ApiErrorResponse of(String message, HttpStatus status) {
        return ApiErrorResponse.builder()
                .status(status.value())
                .message(message)
                .error(status.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiErrorResponse of(String message, HttpStatus status, String endpoint) {
        return ApiErrorResponse.builder()
                .status(status.value())
                .endpoint(endpoint)
                .message(message)
                .error(status.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiErrorResponse of(String message, HttpStatus status, String endpoint, Map<String, String> errors) {
        return ApiErrorResponse.builder()
                .status(status.value())
                .endpoint(endpoint)
                .message(message)
                .error(status.getReasonPhrase())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
