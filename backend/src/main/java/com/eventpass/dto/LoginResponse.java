package com.eventpass.dto;

public record LoginResponse(
        String token,
        Long id,
        String name,
        String email,
        String role
) {}
