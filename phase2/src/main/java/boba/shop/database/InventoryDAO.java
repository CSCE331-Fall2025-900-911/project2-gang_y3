
package boba.shop.database;

import boba.shop.model.Inventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling database operations related to Inventory items.
 * This class provides static methods for reading, adding, and updating items in the 'inventory' table.
 * 
 * @author Joshua Samuel and Sonith Guduru
 */
public class InventoryDAO {
    
    /**
     * Retrieves all inventory items from the database, ordered by item name.
     *
     * @return A List of Inventory objects. Returns an empty list if no items are found or a database error occurs.
     */
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
    
    /**
     * Adds a new inventory item to the database.
     * This method first calculates the next available item_id by finding the current maximum ID and adding 1.
     *
     * @param itemName The name of the new inventory item.
     * @param quantity The starting quantity in stock.
     * @param unit The unit of measurement (e.g., "kg", "liters", "units").
     * @param redThreshold The critical low-stock threshold.
     * @param yellowThreshold The warning low-stock threshold.
     * @return true if the item was successfully added, false otherwise.
     */
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
    
    /**
     * Updates an existing inventory item in the database, identified by its ID.
     *
     * @param itemId The unique ID of the item to update.
     * @param itemName The new name for the item.
     * @param quantity The new quantity in stock.
     * @param unit The new unit of measurement.
     * @param redThreshold The new critical low-stock threshold.
     * @param yellowThreshold The new warning low-stock threshold.
     * @return true if the item was successfully updated, false otherwise.
     */
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
