package com.trainbooking.train_booking_backend.booking;

import com.trainbooking.train_booking_backend.booking.dto.BookingRequest;
import com.trainbooking.train_booking_backend.booking.dto.BookingResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/bookings")
public class CustomerBookingController {

    private final BookingService bookingService;

    public CustomerBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            Authentication authentication,
            @Valid @RequestBody BookingRequest request
    ) {
        return ResponseEntity.ok(
                bookingService.createBooking(authentication.getName(), request)
        );
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                bookingService.getMyBookings(authentication.getName())
        );
    }

    @GetMapping("/{bookingCode}")
    public ResponseEntity<BookingResponse> getMyBookingByCode(
            Authentication authentication,
            @PathVariable String bookingCode
    ) {
        return ResponseEntity.ok(
                bookingService.getMyBookingByCode(authentication.getName(), bookingCode)
        );
    }
}