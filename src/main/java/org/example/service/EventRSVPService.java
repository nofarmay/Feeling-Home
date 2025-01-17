package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Event;
import org.example.model.EventRSVP;
import org.example.repository.EventRSVPRepository;
import org.example.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EventRSVPService {
    private final EventRSVPRepository eventRSVPRepository;
    private final EventRepository eventRepository;

    @Autowired
    public EventRSVPService(EventRSVPRepository eventRSVPRepository, EventRepository eventRepository) {
        this.eventRSVPRepository = eventRSVPRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public EventRSVP registerForEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // בדיקה אם יש מקום וסטטוס האירוע פתוח
        if (!event.canRegister()) {
            throw new RuntimeException("Event is full or closed");
        }

        // בדיקה אם כבר רשום
        Optional<EventRSVP> existingRSVP = eventRSVPRepository.findByEvent_EventIdAndUserId(eventId, userId);
        if (existingRSVP.isPresent()) {
            throw new RuntimeException("Already registered");
        }

        EventRSVP rsvp = new EventRSVP(event, userId);
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        eventRepository.save(event);

        return eventRSVPRepository.save(rsvp);
    }

    @Transactional
    public void cancelRegistration(Long eventId, Long userId) {
        Optional<EventRSVP> rsvp = eventRSVPRepository.findByEvent_EventIdAndUserId(eventId, userId);
        if (rsvp.isPresent()) {
            Event event = rsvp.get().getEvent();
            if (event.getCurrentParticipants() > 0) {  // בדיקה נוספת
                event.setCurrentParticipants(event.getCurrentParticipants() - 1);
                eventRepository.save(event);
            }
            eventRSVPRepository.delete(rsvp.get());
        }
    }

    public Optional<EventRSVP> getRegistrationByEventAndUser(Long eventId, Long userId) {
        return eventRSVPRepository.findByEvent_EventIdAndUserId(eventId, userId);
    }
}