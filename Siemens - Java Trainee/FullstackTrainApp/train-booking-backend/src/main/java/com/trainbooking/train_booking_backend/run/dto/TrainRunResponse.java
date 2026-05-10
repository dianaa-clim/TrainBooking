package com.trainbooking.train_booking_backend.run.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainRunResponse {

    private Long id;
    private Long trainId;
    private String trainCode;
    private String trainName;
    private int trainCapacity;

    private Long routeId;
    private String routeCode;
    private String routeName;

    private LocalDate runDate;
    private String status;
    private boolean active;

    private List<TrainRunStopResponse> stops = new ArrayList<>();

    public TrainRunResponse() {
    }

    public TrainRunResponse(
            Long id,
            Long trainId,
            String trainCode,
            String trainName,
            int trainCapacity,
            Long routeId,
            String routeCode,
            String routeName,
            LocalDate runDate,
            String status,
            boolean active,
            List<TrainRunStopResponse> stops
    ) {
        this.id = id;
        this.trainId = trainId;
        this.trainCode = trainCode;
        this.trainName = trainName;
        this.trainCapacity = trainCapacity;
        this.routeId = routeId;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.runDate = runDate;
        this.status = status;
        this.active = active;
        this.stops = stops;
    }

    public Long getId() {
        return id;
    }

    public Long getTrainId() {
        return trainId;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public String getTrainName() {
        return trainName;
    }

    public int getTrainCapacity() {
        return trainCapacity;
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

    public LocalDate getRunDate() {
        return runDate;
    }

    public String getStatus() {
        return status;
    }

    public boolean isActive() {
        return active;
    }

    public List<TrainRunStopResponse> getStops() {
        return stops;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setTrainCapacity(int trainCapacity) {
        this.trainCapacity = trainCapacity;
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

    public void setRunDate(LocalDate runDate) {
        this.runDate = runDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setStops(List<TrainRunStopResponse> stops) {
        this.stops = stops;
    }
}