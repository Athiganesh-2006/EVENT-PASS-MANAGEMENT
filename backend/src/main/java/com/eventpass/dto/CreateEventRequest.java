package com.eventpass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateEventRequest(
        @NotBlank String title,
        String description,
        String eventType,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotBlank String venue,
        @NotNull Integer capacity
) {}
