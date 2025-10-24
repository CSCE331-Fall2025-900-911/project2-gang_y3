package boba.shop.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Handles generating daily reports from the database, including orders, revenue, and inventory data.
 * Uses DatabaseConnection for PostgreSQL access.
 * @author Shashvat
 */
public class ReportsDAO {


    /**
     * Retrieves daily report data, including order count, revenue, popular items, peak hour,
     * low inventory, and total employees.
     *
     * @return a map of report data
     * @throws SQLException if a database access error occurs
     */
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


    /**
     * Gets today's total number of orders.
     *
     * @param conn the active database connection
     * @return total orders for today
     * @throws SQLException if a database access error occurs
     */
    private static int getTodayOrderCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_date = (SELECT MAX(order_date) FROM orders)";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }


    /**
     * Gets today's total revenue.
     *
     * @param conn the active database connection
     * @return total revenue for today
     * @throws SQLException if a database access error occurs
     */
    private static double getTodayRevenue(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE order_date = (SELECT MAX(order_date) FROM orders)";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }


    /**
     * Gets the most popular menu item and its order count.
     *
     * @param conn the active database connection
     * @return popular item and count
     * @throws SQLException if a database access error occurs
     */
    private static String getMostPopularItem(Connection conn) throws SQLException {
        String sql = "SELECT m.item_name, COUNT(*) AS count FROM orders o " +
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


    /**
     * Gets the most active hour of the day for orders.
     *
     * @param conn the active database connection
     * @return hour and order count
     * @throws SQLException if a database access error occurs
     */
    private static String getMostPopularHour(Connection conn) throws SQLException {
        String sql = "SELECT EXTRACT(HOUR FROM order_time) AS hour, COUNT(*) AS count " +
                     "FROM orders GROUP BY EXTRACT(HOUR FROM order_time) " +
                     "ORDER BY count DESC LIMIT 1";


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


    /**
     * Gets the number of inventory items at or below the red threshold.
     *
     * @param conn the active database connection
     * @return count of low inventory items
     * @throws SQLException if a database access error occurs
     */
    private static int getLowInventoryCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inventory WHERE quantity_in_stock <= red_threshold";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }


    /**
     * Gets the total number of employees.
     *
     * @param conn the active database connection
     * @return total employee count
     * @throws SQLException if a database access error occurs
     */
    private static int getTotalEmployees(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM authentication";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}



