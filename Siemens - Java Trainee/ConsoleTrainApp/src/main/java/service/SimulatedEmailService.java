package service;

import dto.DelayNotificationRecipient;
import dto.TicketDetails;
import model.*;
import repository.EmailOutboxRepository;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class SimulatedEmailService implements EmailService {
    private final EmailOutboxRepository emailOutboxRepository;

    public SimulatedEmailService() {
        this.emailOutboxRepository = new EmailOutboxRepository();
    }

    @Override
    public void sendBookingConfirmation(Connection connection,
                                        Booking booking,
                                        Customer customer,
                                        List<TicketDetails> ticketDetails) {
        String subject = "Booking confirmation - " + booking.getBookingCode();
        String body = buildBookingConfirmationBody(booking, customer, ticketDetails);

        EmailNotification email = new EmailNotification(
                null,
                customer.getEmail(),
                subject,
                body,
                EmailType.BOOKING_CONFIRMATION,
                EmailStatus.SIMULATED,
                booking.getId(),
                null,
                null,
                LocalDateTime.now()
        );

        emailOutboxRepository.save(connection, email);

        System.out.println("\n--- SIMULATED EMAIL SENT ---");
        System.out.println("To: " + customer.getEmail());
        System.out.println("Subject: " + subject);
        System.out.println(body);
        System.out.println("--- END SIMULATED EMAIL ---");
    }

    private String buildBookingConfirmationBody(Booking booking,
                                                Customer customer,
                                                List<TicketDetails> ticketDetails) {
        StringBuilder body = new StringBuilder();

        body.append("Hello, ").append(customer.getFullName()).append("\n\n");
        body.append("Your train booking has been confirmed.\n");
        body.append("Booking code: ").append(booking.getBookingCode()).append("\n\n");
        body.append("Tickets:\n");

        for (TicketDetails ticket : ticketDetails) {
            body.append("\n");
            body.append("Ticket: ").append(ticket.getTicketCode()).append("\n");
            body.append("Passenger: ").append(ticket.getPassengerName()).append("\n");
            body.append("Train: ")
                    .append(ticket.getTrainNumber())
                    .append(" - ")
                    .append(ticket.getTrainName())
                    .append("\n");
            body.append("Route: ")
                    .append(ticket.getOriginStationCode())
                    .append(" ")
                    .append(ticket.getOriginStationName())
                    .append(" -> ")
                    .append(ticket.getDestinationStationCode())
                    .append(" ")
                    .append(ticket.getDestinationStationName())
                    .append("\n");
            body.append("Departure: ").append(ticket.getDepartureTime()).append("\n");
            body.append("Arrival: ").append(ticket.getArrivalTime()).append("\n");
        }

        body.append("\nThank you for using Console Train Booking App.");

        return body.toString();
    }

    @Override
    public void sendDelayNotification(Connection connection,
                                      TrainRun trainRun,
                                      DelayNotificationRecipient recipient,
                                      int delayMinutes,
                                      String reason) {
        String subject = "Train delay notification - " + trainRun.getRunCode();

        String body = buildDelayNotificationBody(trainRun, recipient, delayMinutes, reason);

        EmailNotification email = new EmailNotification(
                null,
                recipient.getCustomerEmail(),
                subject,
                body,
                EmailType.DELAY_NOTIFICATION,
                EmailStatus.SIMULATED,
                recipient.getBookingId(),
                trainRun.getId(),
                null,
                LocalDateTime.now()
        );

        emailOutboxRepository.save(connection, email);

        System.out.println("\n--- SIMULATED DELAY EMAIL SENT ---");
        System.out.println("To: " + recipient.getCustomerEmail());
        System.out.println("Subject: " + subject);
        System.out.println(body);
        System.out.println("--- END SIMULATED DELAY EMAIL ---");
    }

    private String buildDelayNotificationBody(TrainRun trainRun,
                                              DelayNotificationRecipient recipient,
                                              int delayMinutes,
                                              String reason) {
        StringBuilder body = new StringBuilder();

        body.append("Hello, ").append(recipient.getCustomerName()).append("\n\n");
        body.append("We inform you that your booked train has a delay.\n\n");
        body.append("Booking code: ").append(recipient.getBookingCode()).append("\n");
        body.append("Train run: ").append(trainRun.getRunCode()).append("\n");
        body.append("Delay: ").append(delayMinutes).append(" minutes\n");

        if (reason != null && !reason.isBlank()) {
            body.append("Reason: ").append(reason).append("\n");
        }

        body.append("\nThank you for understanding.");

        return body.toString();
    }
}