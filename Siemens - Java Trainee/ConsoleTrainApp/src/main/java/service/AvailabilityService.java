package service;

import dto.AvailabilityResult;
import dto.JourneyLeg;
import dto.JourneyOption;
import exception.DatabaseException;
import exception.OverbookingException;
import exception.ValidationException;
import repository.BookingLegRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvailabilityService {
    private final BookingLegRepository bookingLegRepository;

    public AvailabilityService() {
        this.bookingLegRepository = new BookingLegRepository();
    }

    public AvailabilityResult checkAvailability(Connection connection, JourneyOption journeyOption, int requestedSeats) {
        validateInput(journeyOption, requestedSeats);

        int minimumAvailableSeats = Integer.MAX_VALUE;

        for (JourneyLeg leg : journeyOption.getLegs()) {
            int trainCapacity = findTrainCapacity(connection, leg.getTrainRunId());

            int originOrder = findStopOrder(connection, leg.getOriginRunStopId());
            int destinationOrder = findStopOrder(connection, leg.getDestinationRunStopId());

            for (int segmentOrder = originOrder; segmentOrder < destinationOrder; segmentOrder++) {
                int occupiedSeats = bookingLegRepository.countPassengersOnSegment(
                        connection,
                        leg.getTrainRunId(),
                        segmentOrder
                );

                int availableSeats = trainCapacity - occupiedSeats;

                if (availableSeats < minimumAvailableSeats) {
                    minimumAvailableSeats = availableSeats;
                }

                if (occupiedSeats + requestedSeats > trainCapacity) {
                    String message = "Not enough seats for train " + leg.getTrainNumber()
                            + " on segment order " + segmentOrder
                            + ". Capacity: " + trainCapacity
                            + ", occupied: " + occupiedSeats
                            + ", requested: " + requestedSeats;

                    return new AvailabilityResult(false, requestedSeats, availableSeats, message);
                }
            }
        }

        return new AvailabilityResult(
                true,
                requestedSeats,
                minimumAvailableSeats,
                "Seats are available."
        );
    }

    public void ensureAvailable(Connection connection, JourneyOption journeyOption, int requestedSeats) {
        AvailabilityResult result = checkAvailability(connection, journeyOption, requestedSeats);

        if (!result.isAvailable()) {
            throw new OverbookingException(result.getMessage());
        }
    }

    private int findTrainCapacity(Connection connection, Long trainRunId) {
        String sql = """
                SELECT t.capacity
                FROM train_runs tr
                JOIN trains t ON tr.train_id = t.id
                WHERE tr.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, trainRunId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("capacity");
                }
            }

            throw new ValidationException("Train capacity not found for train_run_id: " + trainRunId);

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train capacity.", e);
        }
    }

    private int findStopOrder(Connection connection, Long trainRunStopId) {
        String sql = """
                SELECT stop_order
                FROM train_run_stops
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, trainRunStopId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("stop_order");
                }
            }

            throw new ValidationException("Train run stop not found: " + trainRunStopId);

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train run stop order.", e);
        }
    }

    private void validateInput(JourneyOption journeyOption, int requestedSeats) {
        if (journeyOption == null || journeyOption.getLegs() == null || journeyOption.getLegs().isEmpty()) {
            throw new ValidationException("Journey option is required.");
        }

        if (requestedSeats <= 0) {
            throw new ValidationException("Requested seats must be greater than zero.");
        }
    }
}