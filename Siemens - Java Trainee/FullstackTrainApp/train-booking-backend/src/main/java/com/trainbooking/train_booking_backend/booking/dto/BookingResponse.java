package com.trainbooking.train_booking_backend.booking.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingResponse {

    private Long id;
    private String bookingCode;
    private String status;
    private LocalDateTime createdAt;

    private Long customerId;
    private String customerEmail;
    private String customerFullName;

    private List<TicketResponse> tickets = new ArrayList<>();

    public BookingResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public List<TicketResponse> getTickets() {
        return tickets;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public void setTickets(List<TicketResponse> tickets) {
        this.tickets = tickets;
    }
}