package repository;

import exception.DatabaseException;
import model.DelayEvent;

import java.sql.*;

public class DelayEventRepository {

    public DelayEvent save(Connection connection, DelayEvent delayEvent) {
        String sql = """
                INSERT INTO train_delay_events
                (train_run_id, delay_minutes, reason, notified_customers)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, delayEvent.getTrainRunId());
            statement.setInt(2, delayEvent.getDelayMinutes());
            statement.setString(3, delayEvent.getReason());
            statement.setBoolean(4, delayEvent.isNotifiedCustomers());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    delayEvent.setId(generatedKeys.getLong(1));
                }
            }

            return delayEvent;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save delay event.", e);
        }
    }

    public void markNotifiedCustomers(Connection connection, Long delayEventId, boolean notifiedCustomers) {
        String sql = """
                UPDATE train_delay_events
                SET notified_customers = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, notifiedCustomers);
            statement.setLong(2, delayEventId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update delay event notification status.", e);
        }
    }
}