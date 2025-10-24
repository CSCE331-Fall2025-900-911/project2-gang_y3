
package boba.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Utility class for managing the database connection.
 * It reads the database credentials URL, USERNAME, and PASSWORD from the .env file
 * This class uses a static initializer to load the configuration once.
 * 
 * @author Joshua Samuel and Sudeep Sirivella
 */
public class DatabaseConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static boolean configLoaded = false;
    
    /**
     * Static initializer block.
     * This block runs once when the class is loaded.
     * It attempts to find and read the .env file from the project's root directory, load the database properties, and set the configLoaded flag.
     */
    static {
        Properties props = new Properties();
        String envFile = System.getProperty("user.dir") + "/.env";
        
        try {
            FileInputStream fis = new FileInputStream(envFile);
            props.load(fis);
            fis.close();
            
            URL = props.getProperty("DB_URL");
            USERNAME = props.getProperty("DB_USERNAME");
            PASSWORD = props.getProperty("DB_PASSWORD");
            
            if (URL != null && USERNAME != null && PASSWORD != null) {
                configLoaded = true;
            } else {
                System.out.println("Missing database credentials in .env file");
                configLoaded = false;
            }
        } catch (Exception e) {
            System.out.println("Failed to load .env file");
            configLoaded = false;
        }
    }
    
    /**
     * Attempts to create and return a new connection to the database.
     *
     * @return A Connection object to the database.
     * @throws SQLException if the database configuration was not loaded or if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (!configLoaded) {
            throw new SQLException("Database configuration not loaded");
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    /**
     * Tests the database connection by attempting to establish one.
     * Prints the connection status to the console.
     *
     * @return true if the connection is successful, false otherwise.
     */
    public static boolean testConnection() {
        if (!configLoaded) {
            System.out.println("Connection failed: Missing database configuration");
            return false;
        }
        
        try {
            Connection conn = getConnection();
            System.out.println("Connection passed");
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Connection failed");
            return false;
        }
    }
}
