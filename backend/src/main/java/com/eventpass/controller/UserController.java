package com.eventpass.controller;

import com.eventpass.entity.User;
import com.eventpass.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable Long id, Authentication authentication) {
        Long loggedInId = (Long) authentication.getPrincipal();
        if (!loggedInId.equals(id)) throw new RuntimeException("You can only view your own profile");

        User user = service.getById(id);
        return Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail(), "role", user.getRole().name());
    }
}
