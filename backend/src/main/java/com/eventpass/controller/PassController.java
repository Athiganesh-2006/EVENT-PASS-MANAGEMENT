package com.eventpass.controller;

import com.eventpass.dto.PassResponse;
import com.eventpass.service.PassService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PassController {
    private final PassService service;
    public PassController(PassService service) { this.service = service; }

    // POST /api/registrations/{registrationId}/pass
    @PostMapping("/registrations/{registrationId}/pass")
    public PassResponse create(@PathVariable Long registrationId, Authentication auth) {
        return PassResponse.from(service.create(registrationId, (Long) auth.getPrincipal()));
    }

    // GET /api/passes/{passId}
    @GetMapping("/passes/{passId}")
    public PassResponse get(@PathVariable Long passId, Authentication auth) {
        return PassResponse.from(service.getById(passId, (Long) auth.getPrincipal()));
    }

    // PUT /api/passes/{passId}/check-in
    @PutMapping("/passes/{passId}/check-in")
    public PassResponse checkIn(@PathVariable Long passId, Authentication auth) {
        return PassResponse.from(service.checkIn(passId, (Long) auth.getPrincipal()));
    }

    // PUT /api/passes/{passId}/check-out
    @PutMapping("/passes/{passId}/check-out")
    public PassResponse checkOut(@PathVariable Long passId, Authentication auth) {
        return PassResponse.from(service.checkOut(passId, (Long) auth.getPrincipal()));
    }
}
