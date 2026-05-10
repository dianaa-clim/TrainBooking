package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TrainRun {
    private Long id;
    private Long trainId;
    private Long routeId;
    private String runCode;
    private LocalDate serviceDate;
    private TrainRunStatus status;
    private int delayMinutes;
    private LocalDateTime createdAt;

    public TrainRun() {
    }

    public TrainRun(Long id, Long trainId, Long routeId, String runCode, LocalDate serviceDate,
                    TrainRunStatus status, int delayMinutes, LocalDateTime createdAt) {
        this.id = id;
        this.trainId = trainId;
        this.routeId = routeId;
        this.runCode = runCode;
        this.serviceDate = serviceDate;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getRunCode() {
        return runCode;
    }

    public void setRunCode(String runCode) {
        this.runCode = runCode;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public TrainRunStatus getStatus() {
        return status;
    }

    public void setStatus(TrainRunStatus status) {
        this.status = status;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}