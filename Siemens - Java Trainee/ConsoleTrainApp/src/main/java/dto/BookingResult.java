package dto;

import java.util.ArrayList;
import java.util.List;

public class BookingResult {
    private String bookingCode;
    private List<String> ticketCodes = new ArrayList<>();
    private List<TicketDetails> ticketDetails = new ArrayList<>();

    public BookingResult() {
    }

    public BookingResult(String bookingCode, List<String> ticketCodes) {
        this.bookingCode = bookingCode;
        this.ticketCodes = ticketCodes;
    }

    public BookingResult(String bookingCode, List<String> ticketCodes, List<TicketDetails> ticketDetails) {
        this.bookingCode = bookingCode;
        this.ticketCodes = ticketCodes;
        this.ticketDetails = ticketDetails;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public List<String> getTicketCodes() {
        return ticketCodes;
    }

    public void setTicketCodes(List<String> ticketCodes) {
        this.ticketCodes = ticketCodes;
    }

    public List<TicketDetails> getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(List<TicketDetails> ticketDetails) {
        this.ticketDetails = ticketDetails;
    }
}