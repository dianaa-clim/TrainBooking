package repository;

import exception.DatabaseException;
import model.BookingPassenger;

import java.sql.*;

public class BookingPassengerRepository {

    public BookingPassenger save(Connection connection, BookingPassenger passenger) {
        String sql = """
                INSERT INTO booking_passengers (booking_id, full_name)
                VALUES (?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, passenger.getBookingId());
            statement.setString(2, passenger.getFullName());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    passenger.setId(generatedKeys.getLong(1));
                }
            }

            return passenger;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save booking passenger.", e);
        }
    }
}