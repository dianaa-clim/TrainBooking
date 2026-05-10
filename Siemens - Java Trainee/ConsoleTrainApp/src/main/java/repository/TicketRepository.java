package repository;

import exception.DatabaseException;
import model.Ticket;

import java.sql.*;

public class TicketRepository {

    public Ticket save(Connection connection, Ticket ticket) {
        String sql = """
                INSERT INTO tickets (ticket_code, booking_leg_id, passenger_id, status)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ticket.getTicketCode());
            statement.setLong(2, ticket.getBookingLegId());
            statement.setLong(3, ticket.getPassengerId());
            statement.setString(4, ticket.getStatus().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setId(generatedKeys.getLong(1));
                }
            }

            return ticket;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save ticket.", e);
        }
    }
}