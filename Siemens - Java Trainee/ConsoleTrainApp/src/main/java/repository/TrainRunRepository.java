package repository;

import config.ConnectionFactory;
import exception.DatabaseException;
import model.TrainRun;
import model.TrainRunStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainRunRepository {

    public List<TrainRun> findAll() {
        String sql = """
                SELECT id, train_id, route_id, run_code, service_date, status, delay_minutes, created_at
                FROM train_runs
                ORDER BY service_date, run_code
                """;

        List<TrainRun> trainRuns = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                trainRuns.add(mapResultSetToTrainRun(resultSet));
            }

            return trainRuns;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train runs.", e);
        }
    }

    public List<TrainRun> findByServiceDate(LocalDate serviceDate) {
        String sql = """
                SELECT id, train_id, route_id, run_code, service_date, status, delay_minutes, created_at
                FROM train_runs
                WHERE service_date = ?
                ORDER BY run_code
                """;

        List<TrainRun> trainRuns = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(serviceDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    trainRuns.add(mapResultSetToTrainRun(resultSet));
                }
            }

            return trainRuns;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train runs by service date.", e);
        }
    }

    public Optional<TrainRun> findById(Long id) {
        String sql = """
                SELECT id, train_id, route_id, run_code, service_date, status, delay_minutes, created_at
                FROM train_runs
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTrainRun(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train run by id.", e);
        }
    }

    public Optional<TrainRun> findByRunCode(String runCode) {
        String sql = """
                SELECT id, train_id, route_id, run_code, service_date, status, delay_minutes, created_at
                FROM train_runs
                WHERE run_code = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, runCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTrainRun(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train run by run code.", e);
        }
    }

    public TrainRun save(TrainRun trainRun) {
        String sql = """
                INSERT INTO train_runs (train_id, route_id, run_code, service_date, status, delay_minutes)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, trainRun.getTrainId());
            statement.setLong(2, trainRun.getRouteId());
            statement.setString(3, trainRun.getRunCode());
            statement.setDate(4, Date.valueOf(trainRun.getServiceDate()));
            statement.setString(5, trainRun.getStatus().name());
            statement.setInt(6, trainRun.getDelayMinutes());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trainRun.setId(generatedKeys.getLong(1));
                }
            }

            return trainRun;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save train run.", e);
        }
    }

    public void updateStatusAndDelay(Long id, TrainRunStatus status, int delayMinutes) {
        String sql = """
                UPDATE train_runs
                SET status = ?, delay_minutes = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.name());
            statement.setInt(2, delayMinutes);
            statement.setLong(3, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update train run status and delay.", e);
        }
    }

    public void update(TrainRun trainRun) {
        String sql = """
                UPDATE train_runs
                SET train_id = ?, route_id = ?, run_code = ?, service_date = ?, status = ?, delay_minutes = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, trainRun.getTrainId());
            statement.setLong(2, trainRun.getRouteId());
            statement.setString(3, trainRun.getRunCode());
            statement.setDate(4, Date.valueOf(trainRun.getServiceDate()));
            statement.setString(5, trainRun.getStatus().name());
            statement.setInt(6, trainRun.getDelayMinutes());
            statement.setLong(7, trainRun.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update train run.", e);
        }
    }

    public void updateStatusAndDelay(Connection connection, Long id, TrainRunStatus status, int delayMinutes) {
        String sql = """
            UPDATE train_runs
            SET status = ?, delay_minutes = ?
            WHERE id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, delayMinutes);
            statement.setLong(3, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update train run status and delay.", e);
        }
    }

    private TrainRun mapResultSetToTrainRun(ResultSet resultSet) throws SQLException {
        return new TrainRun(
                resultSet.getLong("id"),
                resultSet.getLong("train_id"),
                resultSet.getLong("route_id"),
                resultSet.getString("run_code"),
                resultSet.getDate("service_date").toLocalDate(),
                TrainRunStatus.valueOf(resultSet.getString("status")),
                resultSet.getInt("delay_minutes"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}