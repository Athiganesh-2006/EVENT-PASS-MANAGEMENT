package com.eventpass.service;

import com.eventpass.dto.*;
import com.eventpass.entity.User;
import com.eventpass.repository.UserRepository;
import com.eventpass.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Login flow:
    // 1. Find user by email (username).
    // 2. Compare plain password with stored BCrypt hash.
    // 3. Generate JWT containing the verified user's role.
    // 4. Return token + basic user information.
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
