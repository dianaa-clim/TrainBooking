package config;

import exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final DatabaseConfig databaseConfig = new DatabaseConfig();

    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    databaseConfig.getUrl(),
                    databaseConfig.getUsername(),
                    databaseConfig.getPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("MySQL JDBC driver was not found.", e);
        } catch (SQLException e) {
            throw new DatabaseException("Could not connect to the database.", e);
        }
    }
}