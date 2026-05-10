package model;

public class BookingLeg {
    private Long id;
    private Long bookingId;
    private Long trainRunId;
    private Long originRunStopId;
    private Long destinationRunStopId;
    private int legOrder;
    private int passengerCount;
    private BookingStatus status;

    public BookingLeg() {
    }

    public BookingLeg(Long id, Long bookingId, Long trainRunId, Long originRunStopId,
                      Long destinationRunStopId, int legOrder, int passengerCount, BookingStatus status) {
        this.id = id;
        this.bookingId = bookingId;
        this.trainRunId = trainRunId;
        this.originRunStopId = originRunStopId;
        this.destinationRunStopId = destinationRunStopId;
        this.legOrder = legOrder;
        this.passengerCount = passengerCount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
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

    public int getLegOrder() {
        return legOrder;
    }

    public void setLegOrder(int legOrder) {
        this.legOrder = legOrder;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}