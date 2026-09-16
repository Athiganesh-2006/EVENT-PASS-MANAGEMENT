package com.eventpass.repository;

import com.eventpass.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserId(Long userId);
    List<Registration> findByEventId(Long eventId);
    Optional<Registration> findByUserIdAndEventIdAndTimeSlotId(Long userId, Long eventId, Long timeSlotId);
}
