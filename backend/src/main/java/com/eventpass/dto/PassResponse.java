package com.eventpass.dto;

import com.eventpass.entity.Pass;
import java.time.LocalDateTime;

public record PassResponse(
        Long id, String passCode, Long registrationId, String status,
        LocalDateTime checkInAt, LocalDateTime checkOutAt
) {
    public static PassResponse from(Pass p) {
        return new PassResponse(p.getId(), p.getPassCode(), p.getRegistration().getId(),
                p.getStatus().name(), p.getCheckInAt(), p.getCheckOutAt());
    }
}
