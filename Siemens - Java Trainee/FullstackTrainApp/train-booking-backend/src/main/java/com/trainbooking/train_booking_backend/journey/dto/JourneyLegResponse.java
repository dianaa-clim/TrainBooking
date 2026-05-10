package com.trainbooking.train_booking_backend.journey.dto;

import java.time.LocalDateTime;

public class JourneyLegResponse {

    private int legOrder;

    private Long trainRunId;
    private String trainCode;
    private String trainName;

    private Long routeId;
    private String routeCode;
    private String routeName;

    private Long originStopId;
    private String originStationCode;
    private String originStationName;

    private Long destinationStopId;
    private String destinationStationCode;
    private String destinationStationName;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public JourneyLegResponse() {
    }

    public int getLegOrder() {
        return legOrder;
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public String getTrainName() {
        return trainName;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public Long getOriginStopId() {
        return originStopId;
    }

    public String getOriginStationCode() {
        return originStationCode;
    }

    public String getOriginStationName() {
        return originStationName;
    }

    public Long getDestinationStopId() {
        return destinationStopId;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setLegOrder(int legOrder) {
        this.legOrder = legOrder;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setOriginStopId(Long originStopId) {
        this.originStopId = originStopId;
    }

    public void setOriginStationCode(String originStationCode) {
        this.originStationCode = originStationCode;
    }

    public void setOriginStationName(String originStationName) {
        this.originStationName = originStationName;
    }

    public void setDestinationStopId(Long destinationStopId) {
        this.destinationStopId = destinationStopId;
    }

    public void setDestinationStationCode(String destinationStationCode) {
        this.destinationStationCode = destinationStationCode;
    }

    public void setDestinationStationName(String destinationStationName) {
        this.destinationStationName = destinationStationName;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}