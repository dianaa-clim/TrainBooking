package com.trainbooking.train_booking_backend.booking.dto;

import java.time.LocalDateTime;

public class TicketResponse {

    private Long id;
    private String ticketCode;
    private String passengerName;

    private String trainCode;
    private String trainName;

    private String routeName;

    private String originStationCode;
    private String originStationName;

    private String destinationStationCode;
    private String destinationStationName;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public TicketResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getOriginStationCode() {
        return originStationCode;
    }

    public String getOriginStationName() {
        return originStationName;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setOriginStationCode(String originStationCode) {
        this.originStationCode = originStationCode;
    }

    public void setOriginStationName(String originStationName) {
        this.originStationName = originStationName;
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