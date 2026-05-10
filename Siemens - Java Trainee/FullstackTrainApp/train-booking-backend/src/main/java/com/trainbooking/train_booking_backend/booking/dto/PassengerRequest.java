package com.trainbooking.train_booking_backend.booking.dto;

import jakarta.validation.constraints.NotBlank;

public class PassengerRequest {

    @NotBlank(message = "Passenger full name is required")
    private String fullName;

    public PassengerRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}