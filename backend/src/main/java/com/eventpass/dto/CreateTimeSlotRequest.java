package com.eventpass.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateTimeSlotRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull Integer capacity
) {}
