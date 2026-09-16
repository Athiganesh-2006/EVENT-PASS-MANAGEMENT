package com.eventpass.repository;

import com.eventpass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Database operations for users.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
