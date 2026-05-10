package repository;

import config.ConnectionFactory;
import exception.DatabaseException;
import model.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RouteRepository {

    public List<Route> findAll() {
        String sql = """
                SELECT id, code, name, active
                FROM routes
                ORDER BY code
                """;

        List<Route> routes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                routes.add(mapResultSetToRoute(resultSet));
            }

            return routes;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find routes.", e);
        }
    }

    public List<Route> findAllActive() {
        String sql = """
                SELECT id, code, name, active
                FROM routes
                WHERE active = TRUE
                ORDER BY code
                """;

        List<Route> routes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                routes.add(mapResultSetToRoute(resultSet));
            }

            return routes;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find active routes.", e);
        }
    }

    public Optional<Route> findById(Long id) {
        String sql = """
                SELECT id, code, name, active
                FROM routes
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToRoute(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find route by id.", e);
        }
    }

    public Optional<Route> findByCode(String code) {
        String sql = """
                SELECT id, code, name, active
                FROM routes
                WHERE code = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToRoute(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find route by code.", e);
        }
    }

    public Route save(Route route) {
        String sql = """
                INSERT INTO routes (code, name, active)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, route.getCode());
            statement.setString(2, route.getName());
            statement.setBoolean(3, route.isActive());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    route.setId(generatedKeys.getLong(1));
                }
            }

            return route;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save route.", e);
        }
    }

    public void update(Route route) {
        String sql = """
                UPDATE routes
                SET code = ?, name = ?, active = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, route.getCode());
            statement.setString(2, route.getName());
            statement.setBoolean(3, route.isActive());
            statement.setLong(4, route.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update route.", e);
        }
    }

    public void deactivate(Long id) {
        String sql = """
                UPDATE routes
                SET active = FALSE
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not deactivate route.", e);
        }
    }

    private Route mapResultSetToRoute(ResultSet resultSet) throws SQLException {
        return new Route(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("name"),
                resultSet.getBoolean("active")
        );
    }
}