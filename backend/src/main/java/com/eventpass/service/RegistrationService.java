package com.eventpass.service;

import com.eventpass.dto.CreateRegistrationRequest;
import com.eventpass.entity.*;
import com.eventpass.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TimeSlotRepository timeSlotRepository;

    public RegistrationService(RegistrationRepository registrationRepository, UserRepository userRepository,
                               EventRepository eventRepository, TimeSlotRepository timeSlotRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    // Creates a registration for the currently logged-in participant.
    @Transactional
    public Registration create(Long userId, CreateRegistrationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        TimeSlot slot = timeSlotRepository.findById(request.timeSlotId())
                .orElseThrow(() -> new RuntimeException("Time slot not found"));

        if (!slot.getEvent().getId().equals(event.getId())) throw new RuntimeException("Time slot does not belong to event");
        if (event.getStatus() != Event.EventStatus.OPEN) throw new RuntimeException("Event is not open");
        if (event.getAvailableSeats() <= 0) throw new RuntimeException("Event is full");
        if (slot.getAvailableSeats() <= 0) throw new RuntimeException("Time slot is full");

        if (registrationRepository.findByUserIdAndEventIdAndTimeSlotId(userId, event.getId(), slot.getId()).isPresent()) {
            throw new RuntimeException("You are already registered for this time slot");
        }

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .timeSlot(slot)
                .status(Registration.RegistrationStatus.REGISTERED)
                .registeredAt(LocalDateTime.now())
                .build();

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        slot.setAvailableSeats(slot.getAvailableSeats() - 1);
        eventRepository.save(event);
        timeSlotRepository.save(slot);

        return registrationRepository.save(registration);
    }

    // Returns registrations belonging to the current user.
    public List<Registration> getMy(Long userId) {
        return registrationRepository.findByUserId(userId);
    }

    // Gets one registration.
    public Registration getById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    // Cancels a registration and releases the seat.
    @Transactional
    public Registration cancel(Long id, Long userId) {
        Registration registration = getById(id);
        if (!registration.getUser().getId().equals(userId)) throw new RuntimeException("You cannot cancel this registration");
        if (registration.getStatus() == Registration.RegistrationStatus.CANCELLED) throw new RuntimeException("Registration already cancelled");

        registration.setStatus(Registration.RegistrationStatus.CANCELLED);
        Event event = registration.getEvent();
        TimeSlot slot = registration.getTimeSlot();
        event.setAvailableSeats(Math.min(event.getCapacity(), event.getAvailableSeats() + 1));
        slot.setAvailableSeats(Math.min(slot.getCapacity(), slot.getAvailableSeats() + 1));

        eventRepository.save(event);
        timeSlotRepository.save(slot);
        return registrationRepository.save(registration);
    }

    // Gets registrations for an organizer's event.
    public List<Registration> getForEvent(Long eventId, Long organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        if (!event.getOrganizer().getId().equals(organizerId)) throw new RuntimeException("You do not own this event");
        return registrationRepository.findByEventId(eventId);
    }
}
