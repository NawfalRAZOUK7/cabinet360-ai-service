package com.cabinet360.ai.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, String error) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "ERROR");
        body.put("error", error);
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now());
        body.put("service", "ai-service");
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime exception: {}", ex.getMessage(), ex);

        // Check for specific runtime exceptions
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Conversation not found")) {
                return buildErrorResponse("Conversation not found", HttpStatus.NOT_FOUND, "CONVERSATION_NOT_FOUND");
            }
            if (ex.getMessage().contains("Failed to generate AI response")) {
                return buildErrorResponse("AI service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE, "AI_SERVICE_ERROR");
            }
            if (ex.getMessage().contains("Failed to search PubMed")) {
                return buildErrorResponse("PubMed search service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE, "PUBMED_SERVICE_ERROR");
            }
        }

        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return buildErrorResponse("Access denied", HttpStatus.FORBIDDEN, "ACCESS_DENIED");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> body = new HashMap<>();
        body.put("status", "ERROR");
        body.put("error", "VALIDATION_ERROR");
        body.put("message", "Validation failed");
        body.put("fieldErrors", fieldErrors);
        body.put("timestamp", LocalDateTime.now());
        body.put("service", "ai-service");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<Map<String, Object>> handleWebClientException(WebClientException ex) {
        logger.error("External service error: {}", ex.getMessage());
        return buildErrorResponse("External service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE, "EXTERNAL_SERVICE_ERROR");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR");
    }
}

