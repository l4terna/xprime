package com.laterna.xaxathonprime._shared.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        ApiErrorResponse error = ApiErrorResponse.of(
                "Access denied: " + ex.getMessage(),
                HttpStatus.FORBIDDEN,
                request.getDescription(false)
        );

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUncaughtException(
            Exception ex, WebRequest request, HttpServletResponse response) {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());

        if (status == HttpStatus.FORBIDDEN) {
            ApiErrorResponse error = ApiErrorResponse.of(
                    "Access denied: " + ex.getMessage(),
                    HttpStatus.FORBIDDEN,
                    request.getDescription(false)
            );
            return ResponseEntity.status(error.getStatus()).body(error);
        }

        ApiErrorResponse error = ApiErrorResponse.of(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request.getDescription(false)
        );

        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
