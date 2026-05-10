package repository;

import config.ConnectionFactory;
import exception.DatabaseException;
import model.TrainRunStop;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainRunStopRepository {

    public List<TrainRunStop> findByTrainRunId(Long trainRunId) {
        String sql = """
                SELECT id, train_run_id, station_id, stop_order, planned_arrival, planned_departure
                FROM train_run_stops
                WHERE train_run_id = ?
                ORDER BY stop_order
                """;

        List<TrainRunStop> stops = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, trainRunId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stops.add(mapResultSetToTrainRunStop(resultSet));
                }
            }

            return stops;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train run stops by train run id.", e);
        }
    }

    public Optional<TrainRunStop> findById(Long id) {
        String sql = """
                SELECT id, train_run_id, station_id, stop_order, planned_arrival, planned_departure
                FROM train_run_stops
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTrainRunStop(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train run stop by id.", e);
        }
    }

    public Optional<TrainRunStop> findByTrainRunIdAndStationId(Long trainRunId, Long stationId) {
        String sql = """
                SELECT id, train_run_id, station_id, stop_order, planned_arrival, planned_departure
                FROM train_run_stops
                WHERE train_run_id = ? AND station_id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, trainRunId);
            statement.setLong(2, stationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTrainRunStop(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train run stop by train run id and station id.", e);
        }
    }

    public TrainRunStop save(TrainRunStop trainRunStop) {
        String sql = """
                INSERT INTO train_run_stops
                (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, trainRunStop.getTrainRunId());
            statement.setLong(2, trainRunStop.getStationId());
            statement.setInt(3, trainRunStop.getStopOrder());
            setNullableDateTime(statement, 4, trainRunStop.getPlannedArrival());
            setNullableDateTime(statement, 5, trainRunStop.getPlannedDeparture());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trainRunStop.setId(generatedKeys.getLong(1));
                }
            }

            return trainRunStop;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save train run stop.", e);
        }
    }

    public void update(TrainRunStop trainRunStop) {
        String sql = """
                UPDATE train_run_stops
                SET train_run_id = ?, station_id = ?, stop_order = ?, planned_arrival = ?, planned_departure = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, trainRunStop.getTrainRunId());
            statement.setLong(2, trainRunStop.getStationId());
            statement.setInt(3, trainRunStop.getStopOrder());
            setNullableDateTime(statement, 4, trainRunStop.getPlannedArrival());
            setNullableDateTime(statement, 5, trainRunStop.getPlannedDeparture());
            statement.setLong(6, trainRunStop.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update train run stop.", e);
        }
    }

    public void deleteById(Long id) {
        String sql = """
                DELETE FROM train_run_stops
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete train run stop.", e);
        }
    }

    public void deleteByTrainRunId(Long trainRunId) {
        String sql = """
                DELETE FROM train_run_stops
                WHERE train_run_id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, trainRunId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete train run stops by train run id.", e);
        }
    }

    private TrainRunStop mapResultSetToTrainRunStop(ResultSet resultSet) throws SQLException {
        return new TrainRunStop(
                resultSet.getLong("id"),
                resultSet.getLong("train_run_id"),
                resultSet.getLong("station_id"),
                resultSet.getInt("stop_order"),
                getNullableDateTime(resultSet, "planned_arrival"),
                getNullableDateTime(resultSet, "planned_departure")
        );
    }

    private LocalDateTime getNullableDateTime(ResultSet resultSet, String columnName) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);

        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }

    private void setNullableDateTime(PreparedStatement statement, int parameterIndex, LocalDateTime value)
            throws SQLException {

        if (value == null) {
            statement.setNull(parameterIndex, Types.TIMESTAMP);
        } else {
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(value));
        }
    }
}