package com.eventpass.config;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

// Keeps error responses simple for the frontend.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Something went wrong"));
    }
}
