package service;

import config.MailConfig;
import dto.DelayNotificationRecipient;
import dto.TicketDetails;
import exception.EmailException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import model.Booking;
import model.Customer;
import model.EmailNotification;
import model.EmailStatus;
import model.EmailType;
import model.TrainRun;
import repository.EmailOutboxRepository;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class SmtpEmailService implements EmailService {
    private static final int EMAIL_TIMEOUT_SECONDS = 8;

    private final MailConfig mailConfig;
    private final EmailOutboxRepository emailOutboxRepository;

    public SmtpEmailService() {
        this.mailConfig = new MailConfig();
        this.emailOutboxRepository = new EmailOutboxRepository();
    }

    @Override
    public void sendBookingConfirmation(Connection connection,
                                        Booking booking,
                                        Customer customer,
                                        List<TicketDetails> ticketDetails) {
        String subject = "Booking confirmation - " + booking.getBookingCode();
        String body = buildBookingConfirmationBody(booking, customer, ticketDetails);

        sendAndSaveEmail(
                connection,
                customer.getEmail(),
                subject,
                body,
                EmailType.BOOKING_CONFIRMATION,
                booking.getId(),
                null
        );
    }

    @Override
    public void sendDelayNotification(Connection connection,
                                      TrainRun trainRun,
                                      DelayNotificationRecipient recipient,
                                      int delayMinutes,
                                      String reason) {
        String subject = "Train delay notification - " + trainRun.getRunCode();
        String body = buildDelayNotificationBody(trainRun, recipient, delayMinutes, reason);

        sendAndSaveEmail(
                connection,
                recipient.getCustomerEmail(),
                subject,
                body,
                EmailType.DELAY_NOTIFICATION,
                recipient.getBookingId(),
                trainRun.getId()
        );
    }

    private void sendAndSaveEmail(Connection connection,
                                  String recipientEmail,
                                  String subject,
                                  String body,
                                  EmailType emailType,
                                  Long bookingId,
                                  Long trainRunId) {
        EmailStatus status = EmailStatus.FAILED;
        LocalDateTime sentAt = null;

        try {
            sendEmailWithTimeout(recipientEmail, subject, body);

            status = EmailStatus.SENT;
            sentAt = LocalDateTime.now();

            System.out.println("\n--- REAL EMAIL SENT ---");
            System.out.println("To: " + recipientEmail);
            System.out.println("Subject: " + subject);
            System.out.println("--- END REAL EMAIL ---");

        } catch (Exception e) {
            System.out.println("\n--- REAL EMAIL FAILED ---");
            System.out.println("To: " + recipientEmail);
            System.out.println("Subject: " + subject);
            System.out.println("Reason: " + e.getMessage());
            System.out.println("--- END REAL EMAIL FAILED ---");
        }

        EmailNotification emailNotification = new EmailNotification(
                null,
                recipientEmail,
                subject,
                body,
                emailType,
                status,
                bookingId,
                trainRunId,
                null,
                sentAt
        );

        emailOutboxRepository.save(connection, emailNotification);
    }

    private void sendEmailWithTimeout(String recipientEmail, String subject, String body) {
        ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });

        Future<?> future = executor.submit(() -> sendEmail(recipientEmail, subject, body));

        try {
            future.get(EMAIL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new EmailException("SMTP timeout after " + EMAIL_TIMEOUT_SECONDS + " seconds.", e);
        } catch (ExecutionException e) {
            throw new EmailException("SMTP send failed.", e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EmailException("SMTP send was interrupted.", e);
        } finally {
            executor.shutdownNow();
        }
    }

    private void sendEmail(String recipientEmail, String subject, String body) {
        try {
            Properties properties = new Properties();

            properties.put("mail.smtp.host", mailConfig.getHost());
            properties.put("mail.smtp.port", mailConfig.getPort());
            properties.put("mail.smtp.auth", mailConfig.getAuth());
            properties.put("mail.smtp.starttls.enable", mailConfig.getStartTlsEnable());

            properties.put("mail.smtp.connectiontimeout", "5000");
            properties.put("mail.smtp.timeout", "5000");
            properties.put("mail.smtp.writetimeout", "5000");
            properties.put("mail.smtp.ssl.trust", mailConfig.getHost());

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            mailConfig.getSenderEmail(),
                            mailConfig.getSenderPassword()
                    );
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(
                    mailConfig.getSenderEmail(),
                    mailConfig.getSenderName()
            ));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );

            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

        } catch (Exception e) {
            throw new EmailException("Could not send email using SMTP.", e);
        }
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