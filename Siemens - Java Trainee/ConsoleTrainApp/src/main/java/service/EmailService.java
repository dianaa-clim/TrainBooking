package service;

import dto.DelayNotificationRecipient;
import dto.TicketDetails;
import model.Booking;
import model.Customer;
import model.TrainRun;

import java.sql.Connection;
import java.util.List;

public interface EmailService {
    void sendBookingConfirmation(Connection connection,
                                 Booking booking,
                                 Customer customer,
                                 List<TicketDetails> ticketDetails);

    void sendDelayNotification(Connection connection,
                               TrainRun trainRun,
                               DelayNotificationRecipient recipient,
                               int delayMinutes,
                               String reason);
}