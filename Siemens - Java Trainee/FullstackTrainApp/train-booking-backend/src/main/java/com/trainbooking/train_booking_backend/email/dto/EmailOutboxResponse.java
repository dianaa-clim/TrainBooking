package com.trainbooking.train_booking_backend.email.dto;

import java.time.LocalDateTime;

public class EmailOutboxResponse {

    private Long id;
    private String recipientEmail;
    private String subject;
    private String body;
    private String emailType;
    private String status;
    private LocalDateTime createdAt;

    public EmailOutboxResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getEmailType() {
        return emailType;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}