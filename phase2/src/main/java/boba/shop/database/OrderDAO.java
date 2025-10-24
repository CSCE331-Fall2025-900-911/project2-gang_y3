

package boba.shop.database;

import boba.shop.model.Order;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Access Object for handling database operations related to Orders.
 * This class provides static methods for inserting new orders into the 'orders' table.
 * 
 * @author Joshua Samuel and Shashvat Chathapuram
 */
public class OrderDAO {
    
    /**
     * Inserts a new order into the database.
     * This method first retrieves the next available order ID and then inserts the new order record.
     *
     * @param order The Order object containing all data for the new order.
     * @return The new order_id if the insertion was successful, or -1 if it failed.
     */
    public static int insertOrder(Order order) {
        int nextOrderId = getNextOrderId();
        if (nextOrderId == -1) return -1;
        
        String sql = "INSERT INTO orders (order_id, order_date, order_time, total_amount, payment_method, " +
                    "order_status, customer_id, item_link, custom_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nextOrderId);
            stmt.setDate(2, Date.valueOf(order.getOrderDate()));
            stmt.setTime(3, Time.valueOf(order.getOrderTime()));
            stmt.setDouble(4, order.getTotalAmount());
            stmt.setString(5, order.getPaymentMethod());
            stmt.setString(6, order.getOrderStatus());
            stmt.setInt(7, order.getCustomerId());
            stmt.setString(8, order.getItemLink());
            stmt.setString(9, order.getCustomId());
            
            return stmt.executeUpdate() > 0 ? nextOrderId : -1;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Retrieves the next available order_id by finding the current maximum order_id and adding 1.
     *
     * @return The next integer ID to be used, or -1 if a database error occurs.
     */
    private static int getNextOrderId() {
        String sql = "SELECT COALESCE(MAX(order_id), 0) + 1 FROM orders";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
    }
}
