package com.trainbooking.train_booking_backend.delay.dto;

import java.time.LocalDateTime;

public class DelayResponse {

    private Long id;
    private Long trainRunId;
    private String trainCode;
    private int delayMinutes;
    private String reason;
    private String createdByEmail;
    private LocalDateTime createdAt;

    public DelayResponse() {
    }

    public Long getId() {
        return id;
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public String getReason() {
        return reason;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}