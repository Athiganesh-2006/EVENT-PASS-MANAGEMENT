package com.eventpass.dto;

import com.eventpass.entity.Event;
import java.time.LocalDateTime;

public record EventResponse(
        Long id, String title, String description, String eventType,
        LocalDateTime startTime, LocalDateTime endTime, String venue,
        Integer capacity, Integer availableSeats, String status, Long organizerId
) {
    public static EventResponse from(Event e) {
        return new EventResponse(e.getId(), e.getTitle(), e.getDescription(), e.getEventType(),
                e.getStartTime(), e.getEndTime(), e.getVenue(), e.getCapacity(),
                e.getAvailableSeats(), e.getStatus().name(), e.getOrganizer().getId());
    }
}
