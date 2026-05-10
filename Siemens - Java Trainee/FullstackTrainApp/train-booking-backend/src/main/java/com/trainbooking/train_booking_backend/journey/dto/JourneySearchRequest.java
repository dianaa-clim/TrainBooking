package com.trainbooking.train_booking_backend.journey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class JourneySearchRequest {

    @NotBlank(message = "Departure station code is required")
    private String fromStationCode;

    @NotBlank(message = "Arrival station code is required")
    private String toStationCode;

    @NotNull(message = "Date is required")
    private LocalDate date;

    public JourneySearchRequest() {
    }

    public String getFromStationCode() {
        return fromStationCode;
    }

    public String getToStationCode() {
        return toStationCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setFromStationCode(String fromStationCode) {
        this.fromStationCode = fromStationCode;
    }

    public void setToStationCode(String toStationCode) {
        this.toStationCode = toStationCode;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}