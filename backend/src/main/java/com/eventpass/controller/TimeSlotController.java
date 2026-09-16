package com.eventpass.controller;

import com.eventpass.dto.CreateTimeSlotRequest;
import com.eventpass.dto.TimeSlotResponse;
import com.eventpass.service.TimeSlotService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TimeSlotController {
    private final TimeSlotService service;
    public TimeSlotController(TimeSlotService service) { this.service = service; }

    @GetMapping("/api/events/{eventId}/timeslots")
    public List<TimeSlotResponse> getByEvent(@PathVariable Long eventId) {
        return service.getByEvent(eventId).stream().map(TimeSlotResponse::from).toList();
    }

    @PostMapping("/api/events/{eventId}/timeslots")
    public TimeSlotResponse create(@PathVariable Long eventId,
                                   @Valid @RequestBody CreateTimeSlotRequest request,
                                   Authentication auth) {
        return TimeSlotResponse.from(service.create(eventId, request, (Long) auth.getPrincipal()));
    }

    @GetMapping("/api/timeslots/{slotId}")
    public TimeSlotResponse get(@PathVariable Long slotId) {
        return TimeSlotResponse.from(service.getById(slotId));
    }
}
