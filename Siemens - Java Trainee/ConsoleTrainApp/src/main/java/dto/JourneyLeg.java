package dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class JourneyLeg {
    private int legOrder;

    private Long trainRunId;
    private String runCode;
    private String trainNumber;
    private String trainName;

    private Long originRunStopId;
    private Long destinationRunStopId;

    private String originStationCode;
    private String originStationName;
    private String destinationStationCode;
    private String destinationStationName;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private int delayMinutes;

    public JourneyLeg() {
    }

    public JourneyLeg(int legOrder, Long trainRunId, String runCode, String trainNumber, String trainName,
                      Long originRunStopId, Long destinationRunStopId,
                      String originStationCode, String originStationName,
                      String destinationStationCode, String destinationStationName,
                      LocalDateTime departureTime, LocalDateTime arrivalTime, int delayMinutes) {
        this.legOrder = legOrder;
        this.trainRunId = trainRunId;
        this.runCode = runCode;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.originRunStopId = originRunStopId;
        this.destinationRunStopId = destinationRunStopId;
        this.originStationCode = originStationCode;
        this.originStationName = originStationName;
        this.destinationStationCode = destinationStationCode;
        this.destinationStationName = destinationStationName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.delayMinutes = delayMinutes;
    }

    public long getDurationMinutes() {
        return Duration.between(departureTime, arrivalTime).toMinutes();
    }

    public int getLegOrder() {
        return legOrder;
    }

    public void setLegOrder(int legOrder) {
        this.legOrder = legOrder;
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
    }

    public String getRunCode() {
        return runCode;
    }

    public void setRunCode(String runCode) {
        this.runCode = runCode;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public Long getOriginRunStopId() {
        return originRunStopId;
    }

    public void setOriginRunStopId(Long originRunStopId) {
        this.originRunStopId = originRunStopId;
    }

    public Long getDestinationRunStopId() {
        return destinationRunStopId;
    }

    public void setDestinationRunStopId(Long destinationRunStopId) {
        this.destinationRunStopId = destinationRunStopId;
    }

    public String getOriginStationCode() {
        return originStationCode;
    }

    public void setOriginStationCode(String originStationCode) {
        this.originStationCode = originStationCode;
    }

    public String getOriginStationName() {
        return originStationName;
    }

    public void setOriginStationName(String originStationName) {
        this.originStationName = originStationName;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public void setDestinationStationCode(String destinationStationCode) {
        this.destinationStationCode = destinationStationCode;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public void setDestinationStationName(String destinationStationName) {
        this.destinationStationName = destinationStationName;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }
}