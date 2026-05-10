package com.trainbooking.train_booking_backend.email;

import com.trainbooking.train_booking_backend.booking.Booking;
import com.trainbooking.train_booking_backend.booking.Ticket;
import com.trainbooking.train_booking_backend.delay.TrainDelayEvent;
import com.trainbooking.train_booking_backend.email.dto.EmailOutboxResponse;
import com.trainbooking.train_booking_backend.email.mapper.EmailMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ConditionalOnProperty(name = "app.email.mode", havingValue = "simulated", matchIfMissing = true)
public class SimulatedEmailService implements EmailService {

    private final EmailOutboxRepository emailOutboxRepository;
    private final EmailMapper emailMapper;


    public SimulatedEmailService(
            EmailOutboxRepository emailOutboxRepository,
            EmailMapper emailMapper
    ) {
        this.emailOutboxRepository = emailOutboxRepository;
        this.emailMapper = emailMapper;
    }

    @Override
    @Transactional
    public void sendBookingConfirmation(Booking booking) {
        EmailOutbox email = new EmailOutbox();

        email.setRecipientEmail(booking.getCustomer().getUser().getEmail());
        email.setSubject("Booking confirmation - " + booking.getBookingCode());
        email.setBody(buildBookingConfirmationBody(booking));
        email.setEmailType(EmailType.BOOKING_CONFIRMATION);
        email.setStatus(EmailStatus.SIMULATED);

        emailOutboxRepository.save(email);
    }

    @Override
    @Transactional
    public void sendDelayNotification(Booking booking, TrainDelayEvent delayEvent) {
        EmailOutbox email = new EmailOutbox();

        email.setRecipientEmail(booking.getCustomer().getUser().getEmail());
        email.setSubject("Delay notification - " + booking.getBookingCode());
        email.setBody(buildDelayNotificationBody(booking, delayEvent));
        email.setEmailType(EmailType.DELAY_NOTIFICATION);
        email.setStatus(EmailStatus.SIMULATED);

        emailOutboxRepository.save(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailOutboxResponse> getAllEmails() {
        return emailOutboxRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(emailMapper::toResponse)
                .toList();
    }

    private String buildBookingConfirmationBody(Booking booking) {
        StringBuilder body = new StringBuilder();

        body.append("Hello ")
                .append(booking.getCustomer().getUser().getFirstName())
                .append(" ")
                .append(booking.getCustomer().getUser().getLastName())
                .append(",\n\n");

        body.append("Your booking has been confirmed.\n\n");
        body.append("Booking code: ").append(booking.getBookingCode()).append("\n");
        body.append("Status: ").append(booking.getStatus().name()).append("\n\n");

        body.append("Tickets:\n");

        for (Ticket ticket : booking.getTickets()) {
            body.append("\nTicket code: ").append(ticket.getTicketCode()).append("\n");
            body.append("Passenger: ").append(ticket.getPassenger().getFullName()).append("\n");
            body.append("Train: ")
                    .append(ticket.getLeg().getTrainRun().getTrain().getCode())
                    .append(" - ")
                    .append(ticket.getLeg().getTrainRun().getTrain().getName())
                    .append("\n");
            body.append("Route: ")
                    .append(ticket.getLeg().getTrainRun().getRoute().getName())
                    .append("\n");
            body.append("From: ")
                    .append(ticket.getLeg().getOriginStop().getStation().getCode())
                    .append(" - ")
                    .append(ticket.getLeg().getOriginStop().getStation().getName())
                    .append("\n");
            body.append("To: ")
                    .append(ticket.getLeg().getDestinationStop().getStation().getCode())
                    .append(" - ")
                    .append(ticket.getLeg().getDestinationStop().getStation().getName())
                    .append("\n");
            body.append("Departure: ")
                    .append(ticket.getLeg().getOriginStop().getPlannedDepartureTime())
                    .append("\n");
            body.append("Arrival: ")
                    .append(ticket.getLeg().getDestinationStop().getPlannedArrivalTime())
                    .append("\n");
        }

        body.append("\nThis is a simulated email. No real email was sent.");

        return body.toString();
    }

    private String buildDelayNotificationBody(Booking booking, TrainDelayEvent delayEvent) {
        StringBuilder body = new StringBuilder();

        body.append("Hello ")
                .append(booking.getCustomer().getUser().getFirstName())
                .append(" ")
                .append(booking.getCustomer().getUser().getLastName())
                .append(",\n\n");

        body.append("A delay has been reported for one of the trains in your booking.\n\n");
        body.append("Booking code: ").append(booking.getBookingCode()).append("\n");
        body.append("Train: ")
                .append(delayEvent.getTrainRun().getTrain().getCode())
                .append(" - ")
                .append(delayEvent.getTrainRun().getTrain().getName())
                .append("\n");
        body.append("Route: ")
                .append(delayEvent.getTrainRun().getRoute().getName())
                .append("\n");
        body.append("Delay: ")
                .append(delayEvent.getDelayMinutes())
                .append(" minutes\n");
        body.append("Reason: ")
                .append(delayEvent.getReason())
                .append("\n\n");

        body.append("This is a simulated email. No real email was sent.");

        return body.toString();
    }
}