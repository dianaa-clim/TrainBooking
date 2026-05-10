package com.trainbooking.train_booking_backend.booking.dto;

import jakarta.validation.constraints.NotNull;

public class BookingLegRequest {

    @NotNull(message = "Train run id is required")
    private Long trainRunId;

    @NotNull(message = "Origin stop id is required")
    private Long originStopId;

    @NotNull(message = "Destination stop id is required")
    private Long destinationStopId;

    public BookingLegRequest() {
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public Long getOriginStopId() {
        return originStopId;
    }

    public Long getDestinationStopId() {
        return destinationStopId;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
    }

    public void setOriginStopId(Long originStopId) {
        this.originStopId = originStopId;
    }

    public void setDestinationStopId(Long destinationStopId) {
        this.destinationStopId = destinationStopId;
    }
}