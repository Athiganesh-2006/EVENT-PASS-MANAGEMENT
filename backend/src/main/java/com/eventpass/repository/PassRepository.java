package com.eventpass.repository;

import com.eventpass.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PassRepository extends JpaRepository<Pass, Long> {
    Optional<Pass> findByRegistrationId(Long registrationId);
}
