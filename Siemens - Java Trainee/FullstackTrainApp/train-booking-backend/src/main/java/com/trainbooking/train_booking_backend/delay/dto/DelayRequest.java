package com.trainbooking.train_booking_backend.delay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class DelayRequest {

    @Min(value = 1, message = "Delay minutes must be at least 1")
    private int delayMinutes;

    @NotBlank(message = "Delay reason is required")
    private String reason;

    public DelayRequest() {
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public String getReason() {
        return reason;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}