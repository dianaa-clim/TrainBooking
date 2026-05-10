package service;

import config.ConnectionFactory;
import dto.DelayNotificationRecipient;
import dto.TrainRunBookingView;
import exception.DatabaseException;
import exception.NotFoundException;
import exception.ValidationException;
import model.*;
import repository.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final TrainRunRepository trainRunRepository;
    private final BookingRepository bookingRepository;
    private final DelayEventRepository delayEventRepository;
    private final EmailService emailService;

    public AdminService() {
        this.stationRepository = new StationRepository();
        this.trainRepository = new TrainRepository();
        this.routeRepository = new RouteRepository();
        this.routeStopRepository = new RouteStopRepository();
        this.trainRunRepository = new TrainRunRepository();
        this.bookingRepository = new BookingRepository();
        this.delayEventRepository = new DelayEventRepository();
        this.emailService = new SmtpEmailService();
    }

    public Station addStation(String code, String name, String city) {
        validateText(code, "Station code");
        validateText(name, "Station name");
        validateText(city, "Station city");

        String normalizedCode = code.trim().toUpperCase();

        stationRepository.findByCode(normalizedCode).ifPresent(existing -> {
            throw new ValidationException("Station with code " + normalizedCode + " already exists.");
        });

        Station station = new Station(
                null,
                normalizedCode,
                name.trim(),
                city.trim(),
                true
        );

        return stationRepository.save(station);
    }

    public void updateStation(Long id, String code, String name, String city, boolean active) {
        validateId(id, "Station id");
        validateText(code, "Station code");
        validateText(name, "Station name");
        validateText(city, "Station city");

        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Station not found with id: " + id));

        station.setCode(code.trim().toUpperCase());
        station.setName(name.trim());
        station.setCity(city.trim());
        station.setActive(active);

        stationRepository.update(station);
    }

    public void deactivateStation(Long id) {
        validateId(id, "Station id");

        stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Station not found with id: " + id));

        stationRepository.deactivate(id);
    }

    public Train addTrain(String trainNumber, String name, int capacity) {
        validateText(trainNumber, "Train number");
        validateText(name, "Train name");

        if (capacity <= 0) {
            throw new ValidationException("Train capacity must be greater than zero.");
        }

        String normalizedTrainNumber = trainNumber.trim().toUpperCase();

        trainRepository.findByTrainNumber(normalizedTrainNumber).ifPresent(existing -> {
            throw new ValidationException("Train with number " + normalizedTrainNumber + " already exists.");
        });

        Train train = new Train(
                null,
                normalizedTrainNumber,
                name.trim(),
                capacity,
                true
        );

        return trainRepository.save(train);
    }

    public void updateTrain(Long id, String trainNumber, String name, int capacity, boolean active) {
        validateId(id, "Train id");
        validateText(trainNumber, "Train number");
        validateText(name, "Train name");

        if (capacity <= 0) {
            throw new ValidationException("Train capacity must be greater than zero.");
        }

        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Train not found with id: " + id));

        train.setTrainNumber(trainNumber.trim().toUpperCase());
        train.setName(name.trim());
        train.setCapacity(capacity);
        train.setActive(active);

        trainRepository.update(train);
    }

    public void deactivateTrain(Long id) {
        validateId(id, "Train id");

        trainRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Train not found with id: " + id));

        trainRepository.deactivate(id);
    }

    public Route addRoute(String code, String name) {
        validateText(code, "Route code");
        validateText(name, "Route name");

        String normalizedCode = code.trim().toUpperCase();

        routeRepository.findByCode(normalizedCode).ifPresent(existing -> {
            throw new ValidationException("Route with code " + normalizedCode + " already exists.");
        });

        Route route = new Route(
                null,
                normalizedCode,
                name.trim(),
                true
        );

        return routeRepository.save(route);
    }

    public void updateRoute(Long id, String code, String name, boolean active) {
        validateId(id, "Route id");
        validateText(code, "Route code");
        validateText(name, "Route name");

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Route not found with id: " + id));

        route.setCode(code.trim().toUpperCase());
        route.setName(name.trim());
        route.setActive(active);

        routeRepository.update(route);
    }

    public void deactivateRoute(Long id) {
        validateId(id, "Route id");

        routeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Route not found with id: " + id));

        routeRepository.deactivate(id);
    }

    public RouteStop addStationToRoute(Long routeId, Long stationId, int stopOrder, BigDecimal distanceFromStartKm) {
        validateId(routeId, "Route id");
        validateId(stationId, "Station id");

        if (stopOrder <= 0) {
            throw new ValidationException("Stop order must be greater than zero.");
        }

        if (distanceFromStartKm == null || distanceFromStartKm.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Distance from start must be positive or zero.");
        }

        routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException("Route not found with id: " + routeId));

        stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("Station not found with id: " + stationId));

        RouteStop routeStop = new RouteStop(
                null,
                routeId,
                stationId,
                stopOrder,
                distanceFromStartKm
        );

        return routeStopRepository.save(routeStop);
    }

    public void updateRouteStop(Long routeStopId, Long routeId, Long stationId,
                                int stopOrder, BigDecimal distanceFromStartKm) {
        validateId(routeStopId, "Route stop id");
        validateId(routeId, "Route id");
        validateId(stationId, "Station id");

        if (stopOrder <= 0) {
            throw new ValidationException("Stop order must be greater than zero.");
        }

        if (distanceFromStartKm == null || distanceFromStartKm.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Distance from start must be positive or zero.");
        }

        routeStopRepository.findById(routeStopId)
                .orElseThrow(() -> new NotFoundException("Route stop not found with id: " + routeStopId));

        RouteStop routeStop = new RouteStop(
                routeStopId,
                routeId,
                stationId,
                stopOrder,
                distanceFromStartKm
        );

        routeStopRepository.update(routeStop);
    }

    public void removeStationFromRoute(Long routeStopId) {
        validateId(routeStopId, "Route stop id");

        routeStopRepository.findById(routeStopId)
                .orElseThrow(() -> new NotFoundException("Route stop not found with id: " + routeStopId));

        routeStopRepository.deleteById(routeStopId);
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    private void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new ValidationException(fieldName + " must be a positive number.");
        }
    }

    public List<TrainRunBookingView> showBookingsForTrainRun(Long trainRunId) {
        validateId(trainRunId, "Train run id");

        trainRunRepository.findById(trainRunId)
                .orElseThrow(() -> new NotFoundException("Train run not found with id: " + trainRunId));

        try (Connection connection = ConnectionFactory.getConnection()) {
            return bookingRepository.findBookingsByTrainRunId(connection, trainRunId);
        } catch (SQLException e) {
            throw new DatabaseException("Could not open connection for reading bookings.", e);
        }
    }

    public int registerDelayAndNotifyCustomers(Long trainRunId, int delayMinutes, String reason) {
        validateId(trainRunId, "Train run id");

        if (delayMinutes <= 0) {
            throw new ValidationException("Delay minutes must be greater than zero.");
        }

        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);

                TrainRun trainRun = trainRunRepository.findById(trainRunId)
                        .orElseThrow(() -> new NotFoundException("Train run not found with id: " + trainRunId));

                trainRunRepository.updateStatusAndDelay(
                        connection,
                        trainRunId,
                        TrainRunStatus.DELAYED,
                        delayMinutes
                );

                trainRun.setStatus(TrainRunStatus.DELAYED);
                trainRun.setDelayMinutes(delayMinutes);

                DelayEvent delayEvent = new DelayEvent(
                        null,
                        trainRunId,
                        delayMinutes,
                        reason,
                        false,
                        null
                );

                delayEventRepository.save(connection, delayEvent);

                List<DelayNotificationRecipient> recipients =
                        bookingRepository.findDelayNotificationRecipients(connection, trainRunId);

                for (DelayNotificationRecipient recipient : recipients) {
                    emailService.sendDelayNotification(
                            connection,
                            trainRun,
                            recipient,
                            delayMinutes,
                            reason
                    );
                }

                delayEventRepository.markNotifiedCustomers(connection, delayEvent.getId(), true);

                connection.commit();

                return recipients.size();

            } catch (RuntimeException e) {
                rollback(connection);
                throw e;
            } catch (SQLException e) {
                rollback(connection);
                throw new DatabaseException("Could not complete delay notification transaction.", e);
            } finally {
                restoreAutoCommit(connection);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not open connection for delay registration.", e);
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DatabaseException("Could not rollback admin transaction.", e);
        }
    }

    private void restoreAutoCommit(Connection connection) {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseException("Could not restore auto commit.", e);
        }
    }
}