package repository;

import dto.DelayNotificationRecipient;
import dto.TrainRunBookingView;
import exception.DatabaseException;
import model.Booking;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public Booking save(Connection connection, Booking booking) {
        String sql = """
                INSERT INTO bookings (booking_code, customer_id, status)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, booking.getBookingCode());
            statement.setLong(2, booking.getCustomerId());
            statement.setString(3, booking.getStatus().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getLong(1));
                }
            }

            return booking;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save booking.", e);
        }
    }

    public List<TrainRunBookingView> findBookingsByTrainRunId(Connection connection, Long trainRunId) {
        String sql = """
                SELECT
                    b.id AS booking_id,
                    b.booking_code,
                    c.full_name AS customer_name,
                    c.email AS customer_email,
                    bp.full_name AS passenger_name,
                    t.ticket_code,
                    os.code AS origin_station_code,
                    os.name AS origin_station_name,
                    ds.code AS destination_station_code,
                    ds.name AS destination_station_name,
                    origin_stop.planned_departure AS departure_time,
                    destination_stop.planned_arrival AS arrival_time,
                    b.created_at AS booking_created_at
                FROM booking_legs bl
                JOIN bookings b ON bl.booking_id = b.id
                JOIN customers c ON b.customer_id = c.id
                JOIN tickets t ON t.booking_leg_id = bl.id
                JOIN booking_passengers bp ON t.passenger_id = bp.id
                JOIN train_run_stops origin_stop ON bl.origin_run_stop_id = origin_stop.id
                JOIN train_run_stops destination_stop ON bl.destination_run_stop_id = destination_stop.id
                JOIN stations os ON origin_stop.station_id = os.id
                JOIN stations ds ON destination_stop.station_id = ds.id
                WHERE bl.train_run_id = ?
                  AND bl.status = 'CONFIRMED'
                  AND b.status = 'CONFIRMED'
                  AND t.status = 'VALID'
                ORDER BY b.created_at, b.booking_code, bp.full_name
                """;

        List<TrainRunBookingView> bookings = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, trainRunId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapResultSetToTrainRunBookingView(resultSet));
                }
            }

            return bookings;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find bookings by train run id.", e);
        }
    }

    public List<DelayNotificationRecipient> findDelayNotificationRecipients(Connection connection, Long trainRunId) {
        String sql = """
                SELECT DISTINCT
                    b.id AS booking_id,
                    b.booking_code,
                    c.full_name AS customer_name,
                    c.email AS customer_email
                FROM booking_legs bl
                JOIN bookings b ON bl.booking_id = b.id
                JOIN customers c ON b.customer_id = c.id
                WHERE bl.train_run_id = ?
                  AND bl.status = 'CONFIRMED'
                  AND b.status = 'CONFIRMED'
                ORDER BY c.email
                """;

        List<DelayNotificationRecipient> recipients = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, trainRunId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    recipients.add(new DelayNotificationRecipient(
                            resultSet.getLong("booking_id"),
                            resultSet.getString("booking_code"),
                            resultSet.getString("customer_name"),
                            resultSet.getString("customer_email")
                    ));
                }
            }

            return recipients;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find delay notification recipients.", e);
        }
    }

    private TrainRunBookingView mapResultSetToTrainRunBookingView(ResultSet resultSet) throws SQLException {
        return new TrainRunBookingView(
                resultSet.getLong("booking_id"),
                resultSet.getString("booking_code"),
                resultSet.getString("customer_name"),
                resultSet.getString("customer_email"),
                resultSet.getString("passenger_name"),
                resultSet.getString("ticket_code"),
                resultSet.getString("origin_station_code"),
                resultSet.getString("origin_station_name"),
                resultSet.getString("destination_station_code"),
                resultSet.getString("destination_station_name"),
                getNullableDateTime(resultSet, "departure_time"),
                getNullableDateTime(resultSet, "arrival_time"),
                getNullableDateTime(resultSet, "booking_created_at")
        );
    }

    private LocalDateTime getNullableDateTime(ResultSet resultSet, String columnName) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);

        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }
}