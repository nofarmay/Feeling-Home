package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.EmergencyEmailRequest;
import org.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/api/email")
    public class EmailController {
        @Autowired
        private EmailService emailService;

        @PostMapping("/emergency")
        public ResponseEntity<String> sendEmergencyEmail(@Valid @RequestBody EmergencyEmailRequest request) {
            if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("נושא ההודעה לא יכול להיות ריק");
            }
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("תוכן ההודעה לא יכול להיות ריק");
            }

            try {
                emailService.sendEmergencyEmailToAllUsers(request.getSubject(), request.getMessage());
                return ResponseEntity.ok("המייל נשלח בהצלחה");
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body("שגיאה בשליחת המייל: " + e.getMessage());
            }
        }
    }

