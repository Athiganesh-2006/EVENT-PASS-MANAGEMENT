package com.eventpass.repository;

import com.eventpass.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Database operations for events.
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizerId(Long organizerId);
}
