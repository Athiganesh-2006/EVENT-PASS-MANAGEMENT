package com.eventpass.controller;

import com.eventpass.dto.*;
import com.eventpass.entity.Event;
import com.eventpass.service.EventService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;
    public EventController(EventService service) { this.service = service; }

    // GET /api/events
    @GetMapping
    public List<EventResponse> getAll() {
        return service.getAll().stream().map(EventResponse::from).toList();
    }

    // GET /api/events/{eventId}
    @GetMapping("/{eventId}")
    public EventResponse get(@PathVariable Long eventId) {
        return EventResponse.from(service.getById(eventId));
    }

    // POST /api/events
    @PostMapping
    public EventResponse create(@Valid @RequestBody CreateEventRequest request, Authentication auth) {
        return EventResponse.from(service.create(request, (Long) auth.getPrincipal()));
    }

    // GET /api/events/my
    @GetMapping("/my")
    public List<EventResponse> myEvents(Authentication auth) {
        return service.getMyEvents((Long) auth.getPrincipal()).stream().map(EventResponse::from).toList();
    }

    // PUT /api/events/{eventId}/open
    @PutMapping("/{eventId}/open")
    public EventResponse open(@PathVariable Long eventId, Authentication auth) {
        return EventResponse.from(service.open(eventId, (Long) auth.getPrincipal()));
    }

    // PUT /api/events/{eventId}/close
    @PutMapping("/{eventId}/close")
    public EventResponse close(@PathVariable Long eventId, Authentication auth) {
        return EventResponse.from(service.close(eventId, (Long) auth.getPrincipal()));
    }

    // DELETE /api/events/{eventId}
    @DeleteMapping("/{eventId}")
    public void delete(@PathVariable Long eventId, Authentication auth) {
        service.delete(eventId, (Long) auth.getPrincipal());
    }
}
