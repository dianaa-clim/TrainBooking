package repository;

import exception.DatabaseException;
import model.BookingLeg;

import java.sql.*;

public class BookingLegRepository {

    public BookingLeg save(Connection connection, BookingLeg bookingLeg) {
        String sql = """
                INSERT INTO booking_legs
                (booking_id, train_run_id, origin_run_stop_id, destination_run_stop_id,
                 leg_order, passenger_count, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, bookingLeg.getBookingId());
            statement.setLong(2, bookingLeg.getTrainRunId());
            statement.setLong(3, bookingLeg.getOriginRunStopId());
            statement.setLong(4, bookingLeg.getDestinationRunStopId());
            statement.setInt(5, bookingLeg.getLegOrder());
            statement.setInt(6, bookingLeg.getPassengerCount());
            statement.setString(7, bookingLeg.getStatus().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bookingLeg.setId(generatedKeys.getLong(1));
                }
            }

            return bookingLeg;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save booking leg.", e);
        }
    }

    public int countPassengersOnSegment(Connection connection, Long trainRunId, int segmentStartOrder) {
        String sql = """
                SELECT COALESCE(SUM(bl.passenger_count), 0) AS occupied_seats
                FROM booking_legs bl
                JOIN train_run_stops origin_stop
                    ON bl.origin_run_stop_id = origin_stop.id
                JOIN train_run_stops destination_stop
                    ON bl.destination_run_stop_id = destination_stop.id
                WHERE bl.train_run_id = ?
                  AND bl.status = 'CONFIRMED'
                  AND origin_stop.stop_order <= ?
                  AND destination_stop.stop_order > ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, trainRunId);
            statement.setInt(2, segmentStartOrder);
            statement.setInt(3, segmentStartOrder);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("occupied_seats");
                }
            }

            return 0;

        } catch (SQLException e) {
            throw new DatabaseException("Could not count passengers on segment.", e);
        }
    }
}