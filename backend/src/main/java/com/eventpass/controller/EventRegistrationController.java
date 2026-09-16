package com.eventpass.controller;

import com.eventpass.dto.RegistrationResponse;
import com.eventpass.service.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRegistrationController {
    private final RegistrationService service;
    public EventRegistrationController(RegistrationService service) { this.service = service; }

    // GET /api/events/{eventId}/registrations
    @GetMapping("/{eventId}/registrations")
    public List<RegistrationResponse> registrations(@PathVariable Long eventId, Authentication auth) {
        return service.getForEvent(eventId, (Long) auth.getPrincipal())
                .stream().map(RegistrationResponse::from).toList();
    }
}
