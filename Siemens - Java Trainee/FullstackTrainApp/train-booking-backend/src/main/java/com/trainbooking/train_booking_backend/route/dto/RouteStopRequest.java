package com.trainbooking.train_booking_backend.route.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class RouteStopRequest {

    @NotNull(message = "Station id is required")
    private Long stationId;

    @Min(value = 1, message = "Stop order must be at least 1")
    private int stopOrder;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    public RouteStopRequest() {
    }

    public Long getStationId() {
        return stationId;
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
}