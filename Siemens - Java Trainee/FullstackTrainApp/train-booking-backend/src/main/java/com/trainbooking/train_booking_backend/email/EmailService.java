package com.trainbooking.train_booking_backend.email;

import com.trainbooking.train_booking_backend.booking.Booking;
import com.trainbooking.train_booking_backend.delay.TrainDelayEvent;
import com.trainbooking.train_booking_backend.email.dto.EmailOutboxResponse;

import java.util.List;

public interface EmailService {

    void sendBookingConfirmation(Booking booking);

    void sendDelayNotification(Booking booking, TrainDelayEvent delayEvent);

    List<EmailOutboxResponse> getAllEmails();
}