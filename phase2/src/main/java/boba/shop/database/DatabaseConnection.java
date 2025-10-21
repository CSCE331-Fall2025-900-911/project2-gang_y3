// Joshua
package boba.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    
    static {
        Properties props = new Properties();
        String envFile = System.getProperty("user.dir") + "/.env";
        
        try (FileInputStream fis = new FileInputStream(envFile)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
        
        URL = props.getProperty("DB_URL", "jdbc:postgresql://csce-315-db.engr.tamu.edu:5432/gang_y3_db");
        USERNAME = props.getProperty("DB_USERNAME", "gang_y3");
        PASSWORD = props.getProperty("DB_PASSWORD", "YzPoF3aR");
    }
    
    public static Connection getConnection() throws SQLException {
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