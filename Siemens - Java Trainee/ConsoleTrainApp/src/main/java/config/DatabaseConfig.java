package config;

import exception.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "application.properties";

    private final String url;
    private final String username;
    private final String password;

    public DatabaseConfig() {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new DatabaseException("Could not find " + CONFIG_FILE);
            }

            properties.load(inputStream);

            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");

            validateConfig();

        } catch (IOException e) {
            throw new DatabaseException("Could not load database configuration.", e);
        }
    }

    private void validateConfig() {
        if (url == null || url.isBlank()) {
            throw new DatabaseException("Missing db.url in application.properties");
        }

        if (username == null || username.isBlank()) {
            throw new DatabaseException("Missing db.username in application.properties");
        }

        if (password == null) {
            throw new DatabaseException("Missing db.password in application.properties");
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}