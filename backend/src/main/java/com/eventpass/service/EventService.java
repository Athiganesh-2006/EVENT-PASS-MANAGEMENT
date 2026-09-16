package com.eventpass.service;

import com.eventpass.dto.CreateEventRequest;
import com.eventpass.entity.Event;
import com.eventpass.entity.User;
import com.eventpass.repository.EventRepository;
import com.eventpass.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // Returns all events.
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    // Returns one event.
    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    // Creates a new event owned by the logged-in organizer.
    public Event create(CreateEventRequest request, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        if (request.capacity() <= 0) throw new RuntimeException("Capacity must be greater than zero");
        if (!request.endTime().isAfter(request.startTime())) throw new RuntimeException("End time must be after start time");

        Event event = Event.builder()
                .title(request.title())
                .description(request.description())
                .eventType(request.eventType())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .venue(request.venue())
                .capacity(request.capacity())
                .availableSeats(request.capacity())
                .status(Event.EventStatus.DRAFT)
                .organizer(organizer)
                .build();

        return eventRepository.save(event);
    }

    // Returns events created by the logged-in organizer.
    public List<Event> getMyEvents(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    // Changes DRAFT/CLOSED event to OPEN.
    public Event open(Long id, Long organizerId) {
        Event event = getOwnedEvent(id, organizerId);
        if (event.getStatus() == Event.EventStatus.CANCELLED) throw new RuntimeException("Cancelled event cannot be opened");
        event.setStatus(Event.EventStatus.OPEN);
        return eventRepository.save(event);
    }

    // Closes an event so new registrations cannot be created.
    public Event close(Long id, Long organizerId) {
        Event event = getOwnedEvent(id, organizerId);
        event.setStatus(Event.EventStatus.CLOSED);
        return eventRepository.save(event);
    }

    // Deletes an event owned by the organizer.
    public void delete(Long id, Long organizerId) {
        Event event = getOwnedEvent(id, organizerId);
        eventRepository.delete(event);
    }

    // Checks that the current organizer owns the event.
    private Event getOwnedEvent(Long id, Long organizerId) {
        Event event = getById(id);
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new RuntimeException("You do not own this event");
        }
        return event;
    }
}
