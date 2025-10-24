// Joshua
package boba.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static boolean configLoaded = false;
    
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
    
    public static Connection getConnection() throws SQLException {
        if (!configLoaded) {
            throw new SQLException("Database configuration not loaded");
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
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