package boba.shop.database;

import boba.shop.model.Inventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    
    public static List<Inventory> getAllInventoryItems() {
        List<Inventory> inventoryItems = new ArrayList<>();
        String sql = "SELECT item_id, item_name, quantity_in_stock, unit, red_threshold, yellow_threshold " +
                    "FROM inventory ORDER BY item_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Inventory item = new Inventory(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("quantity_in_stock"),
                    rs.getString("unit"),
                    rs.getInt("red_threshold"),
                    rs.getInt("yellow_threshold")
                );
                inventoryItems.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return inventoryItems;
    }
    
    public static boolean addInventoryItem(String itemName, int quantity, String unit, 
                                         int redThreshold, int yellowThreshold) {
        String getMaxIdSql = "SELECT COALESCE(MAX(item_id), 0) + 1 FROM inventory";
        String sql = "INSERT INTO inventory (item_id, item_name, quantity_in_stock, unit, red_threshold, yellow_threshold) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            int nextId;
            try (PreparedStatement getIdStmt = conn.prepareStatement(getMaxIdSql);
                 ResultSet rs = getIdStmt.executeQuery()) {
                rs.next();
                nextId = rs.getInt(1);
            }
            
            // Insert with the calculated ID
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, nextId);
                stmt.setString(2, itemName);
                stmt.setInt(3, quantity);
                stmt.setString(4, unit);
                stmt.setInt(5, redThreshold);
                stmt.setInt(6, yellowThreshold);
                
                return stmt.executeUpdate() > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateInventoryItem(int itemId, String itemName, int quantity, String unit,
                                            int redThreshold, int yellowThreshold) {
        String sql = "UPDATE inventory SET item_name = ?, quantity_in_stock = ?, unit = ?, " +
                    "red_threshold = ?, yellow_threshold = ? WHERE item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, itemName);
            stmt.setInt(2, quantity);
            stmt.setString(3, unit);
            stmt.setInt(4, redThreshold);
            stmt.setInt(5, yellowThreshold);
            stmt.setInt(6, itemId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}