package com.eventpass.dto;

import jakarta.validation.constraints.NotNull;

public record CreateRegistrationRequest(
        @NotNull Long eventId,
        @NotNull Long timeSlotId
) {}
