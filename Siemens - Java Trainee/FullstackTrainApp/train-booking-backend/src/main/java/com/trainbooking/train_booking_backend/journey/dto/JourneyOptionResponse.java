package com.trainbooking.train_booking_backend.journey.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JourneyOptionResponse {

    private String type;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String transferStationCode;
    private String transferStationName;
    private List<JourneyLegResponse> legs = new ArrayList<>();

    public JourneyOptionResponse() {
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public String getTransferStationCode() {
        return transferStationCode;
    }

    public String getTransferStationName() {
        return transferStationName;
    }

    public List<JourneyLegResponse> getLegs() {
        return legs;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setTransferStationCode(String transferStationCode) {
        this.transferStationCode = transferStationCode;
    }

    public void setTransferStationName(String transferStationName) {
        this.transferStationName = transferStationName;
    }

    public void setLegs(List<JourneyLegResponse> legs) {
        this.legs = legs;
    }
}