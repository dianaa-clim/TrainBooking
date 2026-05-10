package config;

import exception.EmailException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailConfig {
    private static final String CONFIG_FILE = "application.properties";

    private final String host;
    private final String port;
    private final String auth;
    private final String startTlsEnable;
    private final String senderEmail;
    private final String senderPassword;
    private final String senderName;

    public MailConfig() {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new EmailException("Could not find " + CONFIG_FILE);
            }

            properties.load(inputStream);

            this.host = properties.getProperty("mail.smtp.host");
            this.port = properties.getProperty("mail.smtp.port");
            this.auth = properties.getProperty("mail.smtp.auth");
            this.startTlsEnable = properties.getProperty("mail.smtp.starttls.enable");
            this.senderEmail = properties.getProperty("mail.sender.email");
            this.senderPassword = properties.getProperty("mail.sender.password");
            this.senderName = properties.getProperty("mail.sender.name");

            validateConfig();

        } catch (IOException e) {
            throw new EmailException("Could not load mail configuration.", e);
        }
    }

    private void validateConfig() {
        if (host == null || host.isBlank()) {
            throw new EmailException("Missing mail.smtp.host in application.properties");
        }

        if (port == null || port.isBlank()) {
            throw new EmailException("Missing mail.smtp.port in application.properties");
        }

        if (senderEmail == null || senderEmail.isBlank()) {
            throw new EmailException("Missing mail.sender.email in application.properties");
        }

        if (senderPassword == null || senderPassword.isBlank()) {
            throw new EmailException("Missing mail.sender.password in application.properties");
        }
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getAuth() {
        return auth;
    }

    public String getStartTlsEnable() {
        return startTlsEnable;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public String getSenderName() {
        return senderName;
    }
}