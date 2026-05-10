package model;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;
    private String ticketCode;
    private Long bookingLegId;
    private Long passengerId;
    private TicketStatus status;
    private LocalDateTime createdAt;

    public Ticket() {
    }

    public Ticket(Long id, String ticketCode, Long bookingLegId, Long passengerId,
                  TicketStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.ticketCode = ticketCode;
        this.bookingLegId = bookingLegId;
        this.passengerId = passengerId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingLegId() {
        return bookingLegId;
    }

    public void setBookingLegId(Long bookingLegId) {
        this.bookingLegId = bookingLegId;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}