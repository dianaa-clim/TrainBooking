package com.trainbooking.train_booking_backend.delay;

import com.trainbooking.train_booking_backend.booking.Booking;
import com.trainbooking.train_booking_backend.booking.BookingLeg;
import com.trainbooking.train_booking_backend.booking.BookingLegRepository;
import com.trainbooking.train_booking_backend.booking.BookingStatus;
import com.trainbooking.train_booking_backend.delay.dto.DelayRequest;
import com.trainbooking.train_booking_backend.delay.dto.DelayResponse;
import com.trainbooking.train_booking_backend.delay.mapper.DelayMapper;
import com.trainbooking.train_booking_backend.email.EmailService;
import com.trainbooking.train_booking_backend.exception.ResourceNotFoundException;
import com.trainbooking.train_booking_backend.run.TrainRun;
import com.trainbooking.train_booking_backend.run.TrainRunRepository;
import com.trainbooking.train_booking_backend.run.TrainRunStatus;
import com.trainbooking.train_booking_backend.run.TrainRunStop;
import com.trainbooking.train_booking_backend.user.User;
import com.trainbooking.train_booking_backend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainDelayService {

    private final TrainDelayEventRepository trainDelayEventRepository;
    private final TrainRunRepository trainRunRepository;
    private final BookingLegRepository bookingLegRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final DelayMapper delayMapper;

    public TrainDelayService(
            TrainDelayEventRepository trainDelayEventRepository,
            TrainRunRepository trainRunRepository,
            BookingLegRepository bookingLegRepository,
            UserRepository userRepository,
            EmailService emailService,
            DelayMapper delayMapper
    ) {
        this.trainDelayEventRepository = trainDelayEventRepository;
        this.trainRunRepository = trainRunRepository;
        this.bookingLegRepository = bookingLegRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.delayMapper = delayMapper;
    }

    @Transactional
    public DelayResponse registerDelay(
            Long trainRunId,
            DelayRequest request,
            String adminEmail
    ) {
        TrainRun trainRun = findTrainRunById(trainRunId);
        User admin = findUserByEmail(adminEmail);

        TrainDelayEvent delayEvent = new TrainDelayEvent();
        delayEvent.setTrainRun(trainRun);
        delayEvent.setDelayMinutes(request.getDelayMinutes());
        delayEvent.setReason(request.getReason().trim());
        delayEvent.setCreatedBy(admin);

        trainRun.setStatus(TrainRunStatus.DELAYED);
        updateActualTimes(trainRun, request.getDelayMinutes());

        TrainDelayEvent savedDelayEvent = trainDelayEventRepository.save(delayEvent);

        notifyAffectedCustomers(trainRunId, savedDelayEvent);

        return delayMapper.toResponse(savedDelayEvent);
    }

    @Transactional(readOnly = true)
    public List<DelayResponse> getDelaysForTrainRun(Long trainRunId) {
        findTrainRunById(trainRunId);

        return trainDelayEventRepository.findByTrainRunIdOrderByCreatedAtDesc(trainRunId)
                .stream()
                .map(delayMapper::toResponse)
                .toList();
    }

    private void updateActualTimes(TrainRun trainRun, int delayMinutes) {
        for (TrainRunStop stop : trainRun.getStops()) {
            if (stop.getPlannedArrivalTime() != null) {
                LocalDateTime baseArrival = stop.getActualArrivalTime() != null
                        ? stop.getActualArrivalTime()
                        : stop.getPlannedArrivalTime();

                stop.setActualArrivalTime(baseArrival.plusMinutes(delayMinutes));
            }

            if (stop.getPlannedDepartureTime() != null) {
                LocalDateTime baseDeparture = stop.getActualDepartureTime() != null
                        ? stop.getActualDepartureTime()
                        : stop.getPlannedDepartureTime();

                stop.setActualDepartureTime(baseDeparture.plusMinutes(delayMinutes));
            }
        }
    }

    private void notifyAffectedCustomers(Long trainRunId, TrainDelayEvent delayEvent) {
        List<BookingLeg> affectedLegs = bookingLegRepository.findByTrainRunId(trainRunId);

        Map<Long, Booking> uniqueBookings = new LinkedHashMap<>();

        for (BookingLeg leg : affectedLegs) {
            Booking booking = leg.getBooking();

            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                uniqueBookings.put(booking.getId(), booking);
            }
        }

        for (Booking booking : uniqueBookings.values()) {
            emailService.sendDelayNotification(booking, delayEvent);
        }
    }

    private TrainRun findTrainRunById(Long trainRunId) {
        return trainRunRepository.findById(trainRunId)
                .orElseThrow(() -> new ResourceNotFoundException("Train run not found with id: " + trainRunId));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public List<DelayResponse> getAllDelays() {
        return trainDelayEventRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(delayMapper::toResponse)
                .toList();
    }
}