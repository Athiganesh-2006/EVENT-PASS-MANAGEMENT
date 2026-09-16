package com.eventpass.service;

import com.eventpass.dto.CreateTimeSlotRequest;
import com.eventpass.entity.Event;
import com.eventpass.entity.TimeSlot;
import com.eventpass.repository.EventRepository;
import com.eventpass.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TimeSlotService {
    private final TimeSlotRepository repository;
    private final EventRepository eventRepository;

    public TimeSlotService(TimeSlotRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    public List<TimeSlot> getByEvent(Long eventId) { return repository.findByEventId(eventId); }

    public TimeSlot getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Time slot not found"));
    }

    public TimeSlot create(Long eventId, CreateTimeSlotRequest request, Long organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        if (!event.getOrganizer().getId().equals(organizerId)) throw new RuntimeException("You do not own this event");
        if (!request.endTime().isAfter(request.startTime())) throw new RuntimeException("End time must be after start time");
        if (request.capacity() <= 0) throw new RuntimeException("Capacity must be greater than zero");
        if (request.startTime().isBefore(event.getStartTime()) || request.endTime().isAfter(event.getEndTime())) {
            throw new RuntimeException("Time slot must be inside the event time range");
        }
        TimeSlot slot = TimeSlot.builder().event(event).startTime(request.startTime()).endTime(request.endTime())
                .capacity(request.capacity()).availableSeats(request.capacity()).build();
        return repository.save(slot);
    }
}
