package repository;

import exception.DatabaseException;
import model.Customer;

import java.sql.*;
import java.util.Optional;

public class CustomerRepository {

    public Optional<Customer> findByEmail(Connection connection, String email) {
        String sql = """
                SELECT id, full_name, email, created_at
                FROM customers
                WHERE email = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToCustomer(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Could not find customer by email.", e);
        }
    }

    public Customer save(Connection connection, Customer customer) {
        String sql = """
                INSERT INTO customers (full_name, email)
                VALUES (?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getEmail());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getLong(1));
                }
            }

            return customer;

        } catch (SQLException e) {
            throw new DatabaseException("Could not save customer.", e);
        }
    }

    private Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getLong("id"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}