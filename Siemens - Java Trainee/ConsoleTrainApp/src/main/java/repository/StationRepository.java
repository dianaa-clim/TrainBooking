package repository;

import config.ConnectionFactory;
import exception.DatabaseException;
import model.Station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StationRepository {

    public List<Station> findAll() {
        String sql = """
                SELECT id, code, name, city, active
                FROM stations
                ORDER BY name
                """;

        List<Station> stations = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                stations.add(mapResultSetToStation(resultSet));
            }

            return stations;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find stations.", e);
        }
    }

    public List<Station> findAllActive() {
        String sql = """
                SELECT id, code, name, city, active
                FROM stations
                WHERE active = TRUE
                ORDER BY name
                """;

        List<Station> stations = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                stations.add(mapResultSetToStation(resultSet));
            }

            return stations;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find active stations.", e);
        }
    }

    public Optional<Station> findById(Long id) {
        String sql = """
                SELECT id, code, name, city, active
                FROM stations
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToStation(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find station by id.", e);
        }
    }

    public Optional<Station> findByCode(String code) {
        String sql = """
                SELECT id, code, name, city, active
                FROM stations
                WHERE code = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToStation(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find station by code.", e);
        }
    }

    public Station save(Station station) {
        String sql = """
                INSERT INTO stations (code, name, city, active)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, station.getCode());
            statement.setString(2, station.getName());
            statement.setString(3, station.getCity());
            statement.setBoolean(4, station.isActive());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    station.setId(generatedKeys.getLong(1));
                }
            }

            return station;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save station.", e);
        }
    }

    public void update(Station station) {
        String sql = """
                UPDATE stations
                SET code = ?, name = ?, city = ?, active = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, station.getCode());
            statement.setString(2, station.getName());
            statement.setString(3, station.getCity());
            statement.setBoolean(4, station.isActive());
            statement.setLong(5, station.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update station.", e);
        }
    }

    public void deactivate(Long id) {
        String sql = """
                UPDATE stations
                SET active = FALSE
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not deactivate station.", e);
        }
    }

    private Station mapResultSetToStation(ResultSet resultSet) throws SQLException {
        return new Station(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("name"),
                resultSet.getString("city"),
                resultSet.getBoolean("active")
        );
    }
}