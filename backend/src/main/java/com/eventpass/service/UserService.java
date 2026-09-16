package com.eventpass.service;

import com.eventpass.entity.User;
import com.eventpass.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository repository) { this.repository = repository; }

    // Finds one user by id.
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
