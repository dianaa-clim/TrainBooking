package repository;

import exception.DatabaseException;
import model.EmailNotification;

import java.sql.*;
import java.time.LocalDateTime;

public class EmailOutboxRepository {

    public EmailNotification save(Connection connection, EmailNotification email) {
        String sql = """
                INSERT INTO email_outbox
                (recipient_email, subject, body, type, status, booking_id, train_run_id, sent_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, email.getRecipientEmail());
            statement.setString(2, email.getSubject());
            statement.setString(3, email.getBody());
            statement.setString(4, email.getType().name());
            statement.setString(5, email.getStatus().name());

            setNullableLong(statement, 6, email.getBookingId());
            setNullableLong(statement, 7, email.getTrainRunId());
            setNullableDateTime(statement, 8, email.getSentAt());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    email.setId(generatedKeys.getLong(1));
                }
            }

            return email;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save email notification.", e);
        }
    }

    private void setNullableLong(PreparedStatement statement, int index, Long value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.BIGINT);
        } else {
            statement.setLong(index, value);
        }
    }

    private void setNullableDateTime(PreparedStatement statement, int index, LocalDateTime value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.TIMESTAMP);
        } else {
            statement.setTimestamp(index, Timestamp.valueOf(value));
        }
    }
}