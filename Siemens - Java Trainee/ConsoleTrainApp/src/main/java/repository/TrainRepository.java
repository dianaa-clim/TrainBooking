package repository;

import config.ConnectionFactory;
import exception.DatabaseException;
import model.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainRepository {

    public List<Train> findAll() {
        String sql = """
                SELECT id, train_number, name, capacity, active
                FROM trains
                ORDER BY train_number
                """;

        List<Train> trains = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                trains.add(mapResultSetToTrain(resultSet));
            }

            return trains;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find trains.", e);
        }
    }

    public List<Train> findAllActive() {
        String sql = """
                SELECT id, train_number, name, capacity, active
                FROM trains
                WHERE active = TRUE
                ORDER BY train_number
                """;

        List<Train> trains = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                trains.add(mapResultSetToTrain(resultSet));
            }

            return trains;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find active trains.", e);
        }
    }

    public Optional<Train> findById(Long id) {
        String sql = """
                SELECT id, train_number, name, capacity, active
                FROM trains
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTrain(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train by id.", e);
        }
    }

    public Optional<Train> findByTrainNumber(String trainNumber) {
        String sql = """
                SELECT id, train_number, name, capacity, active
                FROM trains
                WHERE train_number = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, trainNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTrain(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find train by train number.", e);
        }
    }

    public Train save(Train train) {
        String sql = """
                INSERT INTO trains (train_number, name, capacity, active)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, train.getTrainNumber());
            statement.setString(2, train.getName());
            statement.setInt(3, train.getCapacity());
            statement.setBoolean(4, train.isActive());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    train.setId(generatedKeys.getLong(1));
                }
            }

            return train;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save train.", e);
        }
    }

    public void update(Train train) {
        String sql = """
                UPDATE trains
                SET train_number = ?, name = ?, capacity = ?, active = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, train.getTrainNumber());
            statement.setString(2, train.getName());
            statement.setInt(3, train.getCapacity());
            statement.setBoolean(4, train.isActive());
            statement.setLong(5, train.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update train.", e);
        }
    }

    public void deactivate(Long id) {
        String sql = """
                UPDATE trains
                SET active = FALSE
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not deactivate train.", e);
        }
    }

    private Train mapResultSetToTrain(ResultSet resultSet) throws SQLException {
        return new Train(
                resultSet.getLong("id"),
                resultSet.getString("train_number"),
                resultSet.getString("name"),
                resultSet.getInt("capacity"),
                resultSet.getBoolean("active")
        );
    }
}