package dto;

import java.time.LocalDateTime;

public class TicketDetails {
    private String ticketCode;
    private String passengerName;

    private String trainNumber;
    private String trainName;

    private String originStationCode;
    private String originStationName;
    private String destinationStationCode;
    private String destinationStationName;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public TicketDetails() {
    }

    public TicketDetails(String ticketCode, String passengerName,
                         String trainNumber, String trainName,
                         String originStationCode, String originStationName,
                         String destinationStationCode, String destinationStationName,
                         LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.ticketCode = ticketCode;
        this.passengerName = passengerName;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.originStationCode = originStationCode;
        this.originStationName = originStationName;
        this.destinationStationCode = destinationStationCode;
        this.destinationStationName = destinationStationName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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
}