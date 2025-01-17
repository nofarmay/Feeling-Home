package org.example.service;

import org.example.model.Event;
import org.example.model.EventRSVP;
import org.example.model.EventStatus;
import org.example.repository.CommunityActivityScoreRepository;
import org.example.repository.EventRSVPRepository;
import org.example.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventRSVPRepository rsvpRepository;

    @Autowired
    public EventService(EventRepository eventRepository,
                        EventRSVPRepository rsvpRepository,
                        CommunityActivityScoreRepository scoreRepository) {
        this.eventRepository = eventRepository;
        this.rsvpRepository = rsvpRepository;
//        CommunityActivityScoreRepository scoreRepository
    }

    // מתודות ניהול אירועים בסיסיות נשארות כפי שהן
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(Event event) {
        if (event.getStartDate().before(new Date())) {
            throw new IllegalArgumentException("לא ניתן ליצור אירוע בתאריך עבר");
        }
        event.setStatus(EventStatus.OPEN);
        event.setCurrentParticipants(0);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            updatedEvent.setEventId(id);
            return eventRepository.save(updatedEvent);
        }
        return null;
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // לוגיקת RSVP   קשורה לעדכון האירוע עצמו
    public boolean registerForEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.canRegister()) {
            return false;
        }

        // בדיקה אם כבר רשום
        Optional<EventRSVP> existingRSVP = rsvpRepository.findByEvent_EventIdAndUserId(eventId, userId);
        if (existingRSVP.isPresent()) {
            return false;
        }

        EventRSVP rsvp = new EventRSVP(event, userId);
        rsvpRepository.save(rsvp);
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        eventRepository.save(event);
        return true;
    }

    public void cancelRegistration(Long eventId, Long userId) {
        Optional<EventRSVP> rsvp = rsvpRepository.findByEvent_EventIdAndUserId(eventId, userId);
        if (rsvp.isPresent()) {
            Event event = rsvp.get().getEvent();
            event.setCurrentParticipants(event.getCurrentParticipants() - 1);
            eventRepository.save(event);
            rsvpRepository.delete(rsvp.get());
        }
    }

    public Optional<EventRSVP> getRegistrationByEventAndUser(Long eventId, Long userId) {
        return rsvpRepository.findByEvent_EventIdAndUserId(eventId, userId);
    }
}