package com.eventpass.config;

import com.eventpass.entity.User;
import com.eventpass.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

// Creates simple demo users on first run.
// Change/remove these for your interview database if users already exist.
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(UserRepository repository, PasswordEncoder encoder) {
        return args -> {
            createIfMissing(repository, encoder, "Admin", "admin@example.com", "admin123", User.Role.ADMIN);
            createIfMissing(repository, encoder, "Organizer", "organizer@example.com", "organizer123", User.Role.ORGANIZER);
            createIfMissing(repository, encoder, "Participant", "participant@example.com", "participant123", User.Role.PARTICIPANT);
        };
    }

    // Creates a BCrypt-hashed demo user only when the email does not exist.
    private void createIfMissing(UserRepository repository, PasswordEncoder encoder,
                                  String name, String email, String password, User.Role role) {
        if (repository.findByEmail(email).isEmpty()) {
            repository.save(User.builder()
                    .name(name)
                    .email(email)
                    .password(encoder.encode(password))
                    .role(role)
                    .build());
        }
    }
}
