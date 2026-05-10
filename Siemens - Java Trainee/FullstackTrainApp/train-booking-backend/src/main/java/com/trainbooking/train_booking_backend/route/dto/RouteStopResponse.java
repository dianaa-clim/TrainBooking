package com.trainbooking.train_booking_backend.route.dto;

import java.time.LocalTime;

public class RouteStopResponse {

    private Long id;
    private Long stationId;
    private String stationCode;
    private String stationName;
    private String city;
    private int stopOrder;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private boolean active;

    public RouteStopResponse() {
    }

    public RouteStopResponse(
            Long id,
            Long stationId,
            String stationCode,
            String stationName,
            String city,
            int stopOrder,
            LocalTime arrivalTime,
            LocalTime departureTime,
            boolean active
    ) {
        this.id = id;
        this.stationId = stationId;
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.city = city;
        this.stopOrder = stopOrder;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public String getCity() {
        return city;
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

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setCity(String city) {
        this.city = city;
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

    public void setActive(boolean active) {
        this.active = active;
    }
}