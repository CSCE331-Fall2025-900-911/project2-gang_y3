// Joshua
package boba.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static boolean isConfigured = false;

    static {
        Properties props = new Properties();
        String envFile = System.getProperty("user.dir") + "/.env";

        try (FileInputStream fis = new FileInputStream(envFile)) {
            props.load(fis);
            URL = props.getProperty("DB_URL");
            USERNAME = props.getProperty("DB_USERNAME");
            PASSWORD = props.getProperty("DB_PASSWORD");
            isConfigured = true;
        } catch (IOException e) {
            System.err.println("Error loading .env file: " + e.getMessage());
            isConfigured = false;
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!isConfigured) {
            throw new SQLException("Database connection not configured. Please check .env file.");
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Connected");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}