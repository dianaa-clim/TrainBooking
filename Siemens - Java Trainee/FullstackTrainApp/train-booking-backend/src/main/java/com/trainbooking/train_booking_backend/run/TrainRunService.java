package com.trainbooking.train_booking_backend.run;

import com.trainbooking.train_booking_backend.booking.BookingLegRepository;
import com.trainbooking.train_booking_backend.exception.BadRequestException;
import com.trainbooking.train_booking_backend.exception.ResourceNotFoundException;
import com.trainbooking.train_booking_backend.route.Route;
import com.trainbooking.train_booking_backend.route.RouteStop;
import com.trainbooking.train_booking_backend.route.RouteStopRepository;
import com.trainbooking.train_booking_backend.run.dto.TrainRunRequest;
import com.trainbooking.train_booking_backend.run.dto.TrainRunResponse;
import com.trainbooking.train_booking_backend.run.mapper.TrainRunMapper;
import com.trainbooking.train_booking_backend.train.Train;
import com.trainbooking.train_booking_backend.train.TrainRepository;
import com.trainbooking.train_booking_backend.route.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TrainRunService {

    private final TrainRunRepository trainRunRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final TrainRepository trainRepository;
    private final TrainRunMapper trainRunMapper;
    private final BookingLegRepository bookingLegRepository;
    private final TrainRunStopRepository trainRunStopRepository;

    public TrainRunService(
            TrainRunRepository trainRunRepository,
            RouteRepository routeRepository,
            RouteStopRepository routeStopRepository,
            TrainRepository trainRepository,
            TrainRunMapper trainRunMapper,
            BookingLegRepository bookingLegRepository,
            TrainRunStopRepository trainRunStopRepository
    ) {
        this.trainRunRepository = trainRunRepository;
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
        this.trainRepository = trainRepository;
        this.trainRunMapper = trainRunMapper;
        this.bookingLegRepository = bookingLegRepository;
        this.trainRunStopRepository = trainRunStopRepository;
    }

    @Transactional(readOnly = true)
    public List<TrainRunResponse> getAllTrainRuns() {
        return trainRunRepository.findAll()
                .stream()
                .map(trainRunMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TrainRunResponse getTrainRunById(Long id) {
        TrainRun trainRun = findTrainRunById(id);

        return trainRunMapper.toResponse(trainRun);
    }

    @Transactional
    public TrainRunResponse createTrainRun(TrainRunRequest request) {
        Train train = findTrainById(request.getTrainId());
        Route route = findRouteById(request.getRouteId());

        validateTrainAndRoute(train, route);

        List<RouteStop> routeStops = getActiveRouteStops(route.getId());

        if (routeStops.size() < 2) {
            throw new BadRequestException("A route must have at least two active stops.");
        }

        TrainRun trainRun = new TrainRun();
        trainRun.setTrain(train);
        trainRun.setRoute(route);
        trainRun.setRunDate(request.getRunDate());
        trainRun.setStatus(TrainRunStatus.SCHEDULED);
        trainRun.setActive(true);

        addGeneratedTrainRunStops(trainRun, routeStops, request.getRunDate());

        TrainRun savedTrainRun = trainRunRepository.save(trainRun);

        return trainRunMapper.toResponse(savedTrainRun);
    }

    @Transactional
    public TrainRunResponse updateTrainRun(Long id, TrainRunRequest request) {
        TrainRun trainRun = findTrainRunById(id);

        if (!bookingLegRepository.findByTrainRunId(id).isEmpty()) {
            throw new BadRequestException(
                    "Cannot update this train run because it already has bookings. Create a new train run instead."
            );
        }

        Train train = findTrainById(request.getTrainId());
        Route route = findRouteById(request.getRouteId());

        validateTrainAndRoute(train, route);

        List<RouteStop> routeStops = getActiveRouteStops(route.getId());

        if (routeStops.size() < 2) {
            throw new BadRequestException("A route must have at least two active stops.");
        }

        trainRun.getStops().clear();
        trainRunRepository.flush();

        trainRun.setTrain(train);
        trainRun.setRoute(route);
        trainRun.setRunDate(request.getRunDate());

        addGeneratedTrainRunStops(trainRun, routeStops, request.getRunDate());

        TrainRun updatedTrainRun = trainRunRepository.save(trainRun);

        return trainRunMapper.toResponse(updatedTrainRun);
    }

    @Transactional
    public TrainRunResponse deactivateTrainRun(Long id) {
        TrainRun trainRun = findTrainRunById(id);

        trainRun.setActive(false);

        TrainRun updatedTrainRun = trainRunRepository.save(trainRun);

        return trainRunMapper.toResponse(updatedTrainRun);
    }

    @Transactional
    public TrainRunResponse activateTrainRun(Long id) {
        TrainRun trainRun = findTrainRunById(id);

        trainRun.setActive(true);

        TrainRun updatedTrainRun = trainRunRepository.save(trainRun);

        return trainRunMapper.toResponse(updatedTrainRun);
    }

    public TrainRun findTrainRunById(Long id) {
        return trainRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train run not found with id: " + id));
    }

    private Train findTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
    }

    private Route findRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
    }

    private List<RouteStop> getActiveRouteStops(Long routeId) {
        return routeStopRepository.findByRouteIdAndActiveTrueOrderByStopOrderAsc(routeId);
    }

    private void validateTrainAndRoute(Train train, Route route) {
        if (!train.isActive()) {
            throw new BadRequestException("Cannot create a train run for an inactive train.");
        }

        if (!route.isActive()) {
            throw new BadRequestException("Cannot create a train run for an inactive route.");
        }
    }

    private void addGeneratedTrainRunStops(
            TrainRun trainRun,
            List<RouteStop> routeStops,
            LocalDate runDate
    ) {
        int dayOffset = 0;
        LocalTime previousTime = null;

        for (RouteStop routeStop : routeStops) {
            TrainRunStop trainRunStop = new TrainRunStop();

            trainRunStop.setTrainRun(trainRun);
            trainRunStop.setStation(routeStop.getStation());
            trainRunStop.setStopOrder(routeStop.getStopOrder());

            LocalTime arrivalTime = routeStop.getArrivalTime();
            LocalTime departureTime = routeStop.getDepartureTime();

            if (arrivalTime != null) {
                if (previousTime != null && arrivalTime.isBefore(previousTime)) {
                    dayOffset++;
                }

                trainRunStop.setPlannedArrivalTime(
                        LocalDateTime.of(runDate.plusDays(dayOffset), arrivalTime)
                );

                previousTime = arrivalTime;
            }

            if (departureTime != null) {
                if (previousTime != null && departureTime.isBefore(previousTime)) {
                    dayOffset++;
                }

                trainRunStop.setPlannedDepartureTime(
                        LocalDateTime.of(runDate.plusDays(dayOffset), departureTime)
                );

                previousTime = departureTime;
            }

            trainRun.getStops().add(trainRunStop);
        }
    }
}