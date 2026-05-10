package com.trainbooking.train_booking_backend.booking.mapper;

import com.trainbooking.train_booking_backend.booking.Booking;
import com.trainbooking.train_booking_backend.booking.Ticket;
import com.trainbooking.train_booking_backend.booking.dto.BookingResponse;
import com.trainbooking.train_booking_backend.booking.dto.TicketResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();

        response.setId(booking.getId());
        response.setBookingCode(booking.getBookingCode());
        response.setStatus(booking.getStatus().name());
        response.setCreatedAt(booking.getCreatedAt());

        response.setCustomerId(booking.getCustomer().getId());
        response.setCustomerEmail(booking.getCustomer().getUser().getEmail());
        response.setCustomerFullName(
                booking.getCustomer().getUser().getFirstName()
                        + " "
                        + booking.getCustomer().getUser().getLastName()
        );

        List<TicketResponse> tickets = booking.getTickets()
                .stream()
                .map(this::toTicketResponse)
                .toList();

        response.setTickets(tickets);

        return response;
    }

    public TicketResponse toTicketResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();

        response.setId(ticket.getId());
        response.setTicketCode(ticket.getTicketCode());
        response.setPassengerName(ticket.getPassenger().getFullName());

        response.setTrainCode(ticket.getLeg().getTrainRun().getTrain().getCode());
        response.setTrainName(ticket.getLeg().getTrainRun().getTrain().getName());

        response.setRouteName(ticket.getLeg().getTrainRun().getRoute().getName());

        response.setOriginStationCode(ticket.getLeg().getOriginStop().getStation().getCode());
        response.setOriginStationName(ticket.getLeg().getOriginStop().getStation().getName());

        response.setDestinationStationCode(ticket.getLeg().getDestinationStop().getStation().getCode());
        response.setDestinationStationName(ticket.getLeg().getDestinationStop().getStation().getName());

        response.setDepartureTime(ticket.getLeg().getOriginStop().getPlannedDepartureTime());
        response.setArrivalTime(ticket.getLeg().getDestinationStop().getPlannedArrivalTime());

        return response;
    }
}