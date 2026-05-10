package com.trainbooking.train_booking_backend.email.mapper;

import com.trainbooking.train_booking_backend.email.EmailOutbox;
import com.trainbooking.train_booking_backend.email.dto.EmailOutboxResponse;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public EmailOutboxResponse toResponse(EmailOutbox email) {
        EmailOutboxResponse response = new EmailOutboxResponse();

        response.setId(email.getId());
        response.setRecipientEmail(email.getRecipientEmail());
        response.setSubject(email.getSubject());
        response.setBody(email.getBody());
        response.setEmailType(email.getEmailType().name());
        response.setStatus(email.getStatus().name());
        response.setCreatedAt(email.getCreatedAt());

        return response;
    }
}