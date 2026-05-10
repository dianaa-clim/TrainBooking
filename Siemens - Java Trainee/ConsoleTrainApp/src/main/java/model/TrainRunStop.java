package model;

import java.time.LocalDateTime;

public class TrainRunStop {
    private Long id;
    private Long trainRunId;
    private Long stationId;
    private int stopOrder;
    private LocalDateTime plannedArrival;
    private LocalDateTime plannedDeparture;

    public TrainRunStop() {
    }

    public TrainRunStop(Long id, Long trainRunId, Long stationId, int stopOrder,
                        LocalDateTime plannedArrival, LocalDateTime plannedDeparture) {
        this.id = id;
        this.trainRunId = trainRunId;
        this.stationId = stationId;
        this.stopOrder = stopOrder;
        this.plannedArrival = plannedArrival;
        this.plannedDeparture = plannedDeparture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }

    public LocalDateTime getPlannedArrival() {
        return plannedArrival;
    }

    public void setPlannedArrival(LocalDateTime plannedArrival) {
        this.plannedArrival = plannedArrival;
    }

    public LocalDateTime getPlannedDeparture() {
        return plannedDeparture;
    }

    public void setPlannedDeparture(LocalDateTime plannedDeparture) {
        this.plannedDeparture = plannedDeparture;
    }
}