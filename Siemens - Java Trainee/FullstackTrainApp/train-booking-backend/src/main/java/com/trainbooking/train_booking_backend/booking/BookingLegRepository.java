package com.trainbooking.train_booking_backend.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingLegRepository extends JpaRepository<BookingLeg, Long> {

    List<BookingLeg> findByBookingIdOrderByLegOrderAsc(Long bookingId);

    List<BookingLeg> findByTrainRunId(Long trainRunId);
}