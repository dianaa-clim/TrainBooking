package com.trainbooking.train_booking_backend.run.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TrainRunRequest {

    @NotNull(message = "Train id is required")
    private Long trainId;

    @NotNull(message = "Route id is required")
    private Long routeId;

    @NotNull(message = "Run date is required")
    private LocalDate runDate;

    public TrainRunRequest() {
    }

    public Long getTrainId() {
        return trainId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public LocalDate getRunDate() {
        return runDate;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setRunDate(LocalDate runDate) {
        this.runDate = runDate;
    }
}