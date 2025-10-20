// Joshua
package boba.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://csce-315-db.engr.tamu.edu:5432/gang_y3_db";
    private static final String USERNAME = "gang_y3";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
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