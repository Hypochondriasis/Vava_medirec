package com.example.projekt.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final HikariDataSource dataSource;

    static {
        HikariDataSource ds = null;
        try (InputStream input = new FileInputStream("config/db.properties")) {
            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            log.info("Initializing HikariCP DataSource: url={}, user={}", url, user);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(20);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30_000);
            config.setPoolName("MyHikariCP");

            ds = new HikariDataSource(config);

            // Log pool stats after startup
            log.info("HikariCP initialized. Pool name: {}, MaxPoolSize: {}, MinimumIdle: {}",
                    config.getPoolName(),
                    config.getMaximumPoolSize(),
                    config.getMinimumIdle());

        } catch (Exception e) {
            log.error("Failed to initialize HikariCP DataSource", e);
        }
        dataSource = ds;
    }

    private DatabaseConfig() {
        // Prevent instantiation
    }

    /**
     * Obtain a Connection from the pool.
     *
     * @return a valid JDBC Connection
     * @throws SQLException if the pool is not initialized or can't provide a connection
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            log.error("Attempt to getConnection() but DataSource is null");
            throw new IllegalStateException("DatabaseConfig initialization failed.");
        }
        log.debug("Requesting connection from HikariCP pool \"{}\"", dataSource.getPoolName());
        return dataSource.getConnection();
    }
}
