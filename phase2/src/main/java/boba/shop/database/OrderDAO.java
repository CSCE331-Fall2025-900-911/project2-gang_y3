package boba.shop.database;

import boba.shop.model.Order;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class OrderDAO {
    
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