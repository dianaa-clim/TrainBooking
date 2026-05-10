package com.trainbooking.train_booking_backend.booking;

import com.trainbooking.train_booking_backend.booking.dto.BookingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminBookingController {

    private final BookingService bookingService;

    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/train-runs/{trainRunId}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsForTrainRun(
            @PathVariable Long trainRunId
    ) {
        return ResponseEntity.ok(
                bookingService.getBookingsForTrainRun(trainRunId)
        );
    }
}