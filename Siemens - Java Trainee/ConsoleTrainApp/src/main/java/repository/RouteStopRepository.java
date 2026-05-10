package repository;

import config.ConnectionFactory;
import exception.DatabaseException;
import model.RouteStop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RouteStopRepository {

    public List<RouteStop> findByRouteId(Long routeId) {
        String sql = """
                SELECT id, route_id, station_id, stop_order, distance_from_start_km
                FROM route_stops
                WHERE route_id = ?
                ORDER BY stop_order
                """;

        List<RouteStop> routeStops = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, routeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    routeStops.add(mapResultSetToRouteStop(resultSet));
                }
            }

            return routeStops;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find route stops by route id.", e);
        }
    }

    public Optional<RouteStop> findById(Long id) {
        String sql = """
                SELECT id, route_id, station_id, stop_order, distance_from_start_km
                FROM route_stops
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToRouteStop(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find route stop by id.", e);
        }
    }

    public Optional<RouteStop> findByRouteIdAndStationId(Long routeId, Long stationId) {
        String sql = """
                SELECT id, route_id, station_id, stop_order, distance_from_start_km
                FROM route_stops
                WHERE route_id = ? AND station_id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, routeId);
            statement.setLong(2, stationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToRouteStop(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find route stop by route id and station id.", e);
        }
    }

    public RouteStop save(RouteStop routeStop) {
        String sql = """
                INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, routeStop.getRouteId());
            statement.setLong(2, routeStop.getStationId());
            statement.setInt(3, routeStop.getStopOrder());
            statement.setBigDecimal(4, routeStop.getDistanceFromStartKm());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    routeStop.setId(generatedKeys.getLong(1));
                }
            }

            return routeStop;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save route stop.", e);
        }
    }

    public void update(RouteStop routeStop) {
        String sql = """
                UPDATE route_stops
                SET route_id = ?, station_id = ?, stop_order = ?, distance_from_start_km = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, routeStop.getRouteId());
            statement.setLong(2, routeStop.getStationId());
            statement.setInt(3, routeStop.getStopOrder());
            statement.setBigDecimal(4, routeStop.getDistanceFromStartKm());
            statement.setLong(5, routeStop.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update route stop.", e);
        }
    }

    public void deleteById(Long id) {
        String sql = """
                DELETE FROM route_stops
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete route stop.", e);
        }
    }

    public void deleteByRouteId(Long routeId) {
        String sql = """
                DELETE FROM route_stops
                WHERE route_id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, routeId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete route stops by route id.", e);
        }
    }

    private RouteStop mapResultSetToRouteStop(ResultSet resultSet) throws SQLException {
        return new RouteStop(
                resultSet.getLong("id"),
                resultSet.getLong("route_id"),
                resultSet.getLong("station_id"),
                resultSet.getInt("stop_order"),
                resultSet.getBigDecimal("distance_from_start_km")
        );
    }
}