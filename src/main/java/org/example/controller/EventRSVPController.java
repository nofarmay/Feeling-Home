package org.example.controller;

import org.example.model.EventRSVP;
import org.example.service.EventRSVPService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rsvp")
public class EventRSVPController {
    private final EventRSVPService eventRSVPService;

    public EventRSVPController(EventRSVPService eventRSVPService) {
        this.eventRSVPService = eventRSVPService;
    }

    @PostMapping
    public ResponseEntity<EventRSVP> registerForEvent(@RequestBody Map<String, Long> data) {
        try {
            Long eventId = data.get("eventId");
            Long userId = data.get("userId");
            return ResponseEntity.ok(eventRSVPService.registerForEvent(eventId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelRegistration(@RequestParam Long eventId, @RequestParam Long userId) {
        eventRSVPService.cancelRegistration(eventId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Optional<EventRSVP>> getRegistration(@RequestParam Long eventId, @RequestParam Long userId) {
        return ResponseEntity.ok(eventRSVPService.getRegistrationByEventAndUser(eventId, userId));
    }
}