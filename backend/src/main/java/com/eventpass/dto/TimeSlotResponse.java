package com.eventpass.dto;

import com.eventpass.entity.TimeSlot;
import java.time.LocalDateTime;

public record TimeSlotResponse(
        Long id, Long eventId, LocalDateTime startTime, LocalDateTime endTime,
        Integer capacity, Integer availableSeats
) {
    public static TimeSlotResponse from(TimeSlot s) {
        return new TimeSlotResponse(s.getId(), s.getEvent().getId(), s.getStartTime(),
                s.getEndTime(), s.getCapacity(), s.getAvailableSeats());
    }
}
