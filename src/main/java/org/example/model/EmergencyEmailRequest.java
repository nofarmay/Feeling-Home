package org.example.model;

public class EmergencyEmailRequest {
    private String subject;
    private String message;

    // Constructor
    public EmergencyEmailRequest() {
    }

    public EmergencyEmailRequest(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    // Getters and Setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
