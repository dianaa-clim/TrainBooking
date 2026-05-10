package model;

import java.time.LocalDateTime;

public class EmailNotification {
    private Long id;
    private String recipientEmail;
    private String subject;
    private String body;
    private EmailType type;
    private EmailStatus status;
    private Long bookingId;
    private Long trainRunId;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public EmailNotification() {
    }

    public EmailNotification(Long id, String recipientEmail, String subject, String body,
                             EmailType type, EmailStatus status, Long bookingId, Long trainRunId,
                             LocalDateTime createdAt, LocalDateTime sentAt) {
        this.id = id;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.type = type;
        this.status = status;
        this.bookingId = bookingId;
        this.trainRunId = trainRunId;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public EmailType getType() {
        return type;
    }

    public void setType(EmailType type) {
        this.type = type;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}