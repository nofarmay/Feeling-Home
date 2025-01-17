package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "event_participants")
public class EventRSVP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "user_id", nullable = false)
    private Long userId;

//    @Column(nullable = false)
//    private String registrationCode;

public EventRSVP() {}

    public EventRSVP(Event event, Long userId) {
        this.event = event;
        this.userId = userId;
//        this.registrationCode = generateRegistrationCode();
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // תיקון שמות המתודות
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

//    public String getRegistrationCode() {
//        return registrationCode;
//    }
//
//    public void setRegistrationCode(String registrationCode) {
//        this.registrationCode = registrationCode;
//    }
}