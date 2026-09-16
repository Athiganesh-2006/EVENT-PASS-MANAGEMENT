package com.eventpass.controller;

import com.eventpass.dto.*;
import com.eventpass.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService service;
    public RegistrationController(RegistrationService service) { this.service = service; }

    // POST /api/registrations
    @PostMapping
    public RegistrationResponse create(@Valid @RequestBody CreateRegistrationRequest request, Authentication auth) {
        return RegistrationResponse.from(service.create((Long) auth.getPrincipal(), request));
    }

    // GET /api/registrations/my
    @GetMapping("/my")
    public List<RegistrationResponse> my(Authentication auth) {
        return service.getMy((Long) auth.getPrincipal()).stream().map(RegistrationResponse::from).toList();
    }

    // GET /api/registrations/{registrationId}
    @GetMapping("/{registrationId}")
    public RegistrationResponse get(@PathVariable Long registrationId, Authentication auth) {
        var r = service.getById(registrationId);
        if (!r.getUser().getId().equals((Long) auth.getPrincipal())) throw new RuntimeException("You cannot view this registration");
        return RegistrationResponse.from(r);
    }

    // PUT /api/registrations/{registrationId}/cancel
    @PutMapping("/{registrationId}/cancel")
    public RegistrationResponse cancel(@PathVariable Long registrationId, Authentication auth) {
        return RegistrationResponse.from(service.cancel(registrationId, (Long) auth.getPrincipal()));
    }
}
