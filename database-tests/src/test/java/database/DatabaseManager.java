package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public final class DatabaseManager {
    private static final String CONFIG_FILE = "config-qa.properties";
    private static HikariDataSource dataSource;

    private DatabaseManager() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (dataSource == null) {
            dataSource = new HikariDataSource(buildConfig());
        }

        return dataSource.getConnection();
    }

    public static boolean isConfigured() {
        Properties properties = loadProperties();
        String url = readConfigValue(properties, "db.url", "DB_URL").orElse("");
        String username = readConfigValue(properties, "db.username", "DB_USERNAME").orElse("");

        return !url.isBlank()
                && !username.isBlank();
    }

    public static synchronized void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private static HikariConfig buildConfig() {
        Properties properties = loadProperties();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(requiredConfig(properties, "db.url", "DB_URL"));
        config.setUsername(requiredConfig(properties, "db.username", "DB_USERNAME"));
        config.setPassword(readConfigValue(properties, "db.password", "DB_PASSWORD").orElse(""));

        // Keep the pool small for test automation. Increase only if your tests truly run in parallel.
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(10_000);
        config.setPoolName("amp-database-tests");

        return config;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = DatabaseManager.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not load " + CONFIG_FILE, e);
        }

        return properties;
    }

    private static String requiredConfig(Properties properties, String propertyName, String environmentName) {
        return readConfigValue(properties, propertyName, environmentName)
                .filter(value -> !value.isBlank())
                .orElseThrow(() -> new IllegalStateException(
                        "Missing database config. Fill " + propertyName + " or set " + environmentName + "."));
    }

    private static Optional<String> readConfigValue(Properties properties, String propertyName, String environmentName) {
        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return Optional.of(environmentValue.trim());
        }

        return Optional.ofNullable(properties.getProperty(propertyName))
                .map(String::trim);
    }
}
