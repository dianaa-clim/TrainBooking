package com.trainbooking.train_booking_backend.exception;

public class NoJourneyFoundException extends RuntimeException {

    public NoJourneyFoundException(String message) {
        super(message);
    }
}