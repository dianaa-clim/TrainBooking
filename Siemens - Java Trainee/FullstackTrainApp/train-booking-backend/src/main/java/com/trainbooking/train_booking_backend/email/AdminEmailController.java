package com.trainbooking.train_booking_backend.email;

import com.trainbooking.train_booking_backend.email.dto.EmailOutboxResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/emails")
public class AdminEmailController {

    private final EmailService emailService;

    public AdminEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<EmailOutboxResponse>> getAllEmails() {
        return ResponseEntity.ok(emailService.getAllEmails());
    }
}