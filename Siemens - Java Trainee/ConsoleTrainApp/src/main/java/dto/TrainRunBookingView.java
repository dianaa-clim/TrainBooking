package dto;

import java.time.LocalDateTime;

public class TrainRunBookingView {
    private Long bookingId;
    private String bookingCode;
    private String customerName;
    private String customerEmail;
    private String passengerName;
    private String ticketCode;

    private String originStationCode;
    private String originStationName;
    private String destinationStationCode;
    private String destinationStationName;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime bookingCreatedAt;

    public TrainRunBookingView() {
    }

    public TrainRunBookingView(Long bookingId, String bookingCode, String customerName, String customerEmail,
                               String passengerName, String ticketCode,
                               String originStationCode, String originStationName,
                               String destinationStationCode, String destinationStationName,
                               LocalDateTime departureTime, LocalDateTime arrivalTime,
                               LocalDateTime bookingCreatedAt) {
        this.bookingId = bookingId;
        this.bookingCode = bookingCode;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.passengerName = passengerName;
        this.ticketCode = ticketCode;
        this.originStationCode = originStationCode;
        this.originStationName = originStationName;
        this.destinationStationCode = destinationStationCode;
        this.destinationStationName = destinationStationName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.bookingCreatedAt = bookingCreatedAt;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getTicketCode() {
        return ticketCode;
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

    public LocalDateTime getBookingCreatedAt() {
        return bookingCreatedAt;
    }
}