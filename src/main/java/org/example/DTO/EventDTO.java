package org.example.DTO;

import org.example.model.EventStatus;

import java.util.Date;

public class EventDTO {
    private Long eventId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String location;
    private EventStatus status;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Long userId;

    // Constructors, Getters and Setters
}
