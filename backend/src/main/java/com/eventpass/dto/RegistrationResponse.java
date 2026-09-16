package com.eventpass.dto;

import com.eventpass.entity.Registration;
import java.time.LocalDateTime;

public record RegistrationResponse(
        Long id, Long userId, Long eventId, Long timeSlotId,
        String status, LocalDateTime registeredAt
) {
    public static RegistrationResponse from(Registration r) {
        return new RegistrationResponse(r.getId(), r.getUser().getId(), r.getEvent().getId(),
                r.getTimeSlot().getId(), r.getStatus().name(), r.getRegisteredAt());
    }
}
