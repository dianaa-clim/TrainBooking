package com.trainbooking.train_booking_backend.run.dto;

import java.time.LocalDateTime;

public class TrainRunStopResponse {

    private Long id;
    private Long stationId;
    private String stationCode;
    private String stationName;
    private String city;
    private int stopOrder;
    private LocalDateTime plannedArrivalTime;
    private LocalDateTime plannedDepartureTime;
    private LocalDateTime actualArrivalTime;
    private LocalDateTime actualDepartureTime;

    public TrainRunStopResponse() {
    }

    public TrainRunStopResponse(
            Long id,
            Long stationId,
            String stationCode,
            String stationName,
            String city,
            int stopOrder,
            LocalDateTime plannedArrivalTime,
            LocalDateTime plannedDepartureTime,
            LocalDateTime actualArrivalTime,
            LocalDateTime actualDepartureTime
    ) {
        this.id = id;
        this.stationId = stationId;
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.city = city;
        this.stopOrder = stopOrder;
        this.plannedArrivalTime = plannedArrivalTime;
        this.plannedDepartureTime = plannedDepartureTime;
        this.actualArrivalTime = actualArrivalTime;
        this.actualDepartureTime = actualDepartureTime;
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

    public LocalDateTime getPlannedArrivalTime() {
        return plannedArrivalTime;
    }

    public LocalDateTime getPlannedDepartureTime() {
        return plannedDepartureTime;
    }

    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public LocalDateTime getActualDepartureTime() {
        return actualDepartureTime;
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

    public void setPlannedArrivalTime(LocalDateTime plannedArrivalTime) {
        this.plannedArrivalTime = plannedArrivalTime;
    }

    public void setPlannedDepartureTime(LocalDateTime plannedDepartureTime) {
        this.plannedDepartureTime = plannedDepartureTime;
    }

    public void setActualArrivalTime(LocalDateTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public void setActualDepartureTime(LocalDateTime actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }
}