package boba.shop.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportsDAO {
    
    public static Map<String, Object> getDailyReports() {
        Map<String, Object> reports = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            reports.put("todayOrders", getTodayOrderCount(conn));
            reports.put("todayRevenue", getTodayRevenue(conn));
            reports.put("popularItems", getMostPopularItem(conn));
            reports.put("popularHour", getMostPopularHour(conn));
            reports.put("lowInventory", getLowInventoryCount(conn));
            reports.put("totalEmployees", getTotalEmployees(conn));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    private static int getTodayOrderCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_date = (SELECT MAX(order_date) FROM orders)";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    private static double getTodayRevenue(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE order_date = (SELECT MAX(order_date) FROM orders)";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }
    
    private static String getMostPopularItem(Connection conn) throws SQLException {
        String sql = "SELECT m.item_name, COUNT(*) as count FROM orders o " +
                    "JOIN menu m ON o.item_link::text LIKE '%\"' || m.menu_item_id || '\"%' " +
                    "GROUP BY m.item_name ORDER BY count DESC LIMIT 1";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("item_name") + " (" + rs.getInt("count") + " orders)";
            }
        }
        return "Nothing";
    }
    
    private static String getMostPopularHour(Connection conn) throws SQLException {
        String sql = "SELECT EXTRACT(HOUR FROM order_time) as hour, COUNT(*) as count " +
                    "FROM orders " +
                    "GROUP BY EXTRACT(HOUR FROM order_time) ORDER BY count DESC LIMIT 1";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int hour = rs.getInt("hour");
                int count = rs.getInt("count");
                return hour + ":00 (" + count + " orders)";
            }
        }
        return "Nothing";
    }
    
    private static int getLowInventoryCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inventory WHERE quantity_in_stock <= red_threshold";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    private static int getTotalEmployees(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM authentication";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}