package com.trainbooking.train_booking_backend.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class BookingRequest {

    @Valid
    @NotEmpty(message = "At least one passenger is required")
    private List<PassengerRequest> passengers = new ArrayList<>();

    @Valid
    @NotEmpty(message = "At least one booking leg is required")
    private List<BookingLegRequest> legs = new ArrayList<>();

    public BookingRequest() {
    }

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public List<BookingLegRequest> getLegs() {
        return legs;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
        this.passengers = passengers;
    }

    public void setLegs(List<BookingLegRequest> legs) {
        this.legs = legs;
    }
}