package com.trainbooking.train_booking_backend.exception;

public class OverbookingException extends RuntimeException {

    public OverbookingException(String message) {
        super(message);
    }
}