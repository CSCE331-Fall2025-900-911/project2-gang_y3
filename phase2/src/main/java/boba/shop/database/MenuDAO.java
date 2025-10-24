// Sudeep
package boba.shop.database;

import boba.shop.model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling all database operations related to MenuItems.
 * This class provides static methods for CRUD (Create, Read, Update) operations
 * on the 'menu' table.
 */
public class MenuDAO {
    
    /**
     * Retrieves all available menu items from the database.
     * Items are considered available if their 'availability' column is true.
     * The list is sorted by category, then by item name.
     *
     * @return A List of MenuItem objects, or an empty list if no items are found or an error occurs.
     */
    public static List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT menu_item_id, item_name, category, price, availability, inventory_link " +
                    "FROM menu WHERE availability = true ORDER BY category, item_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem(
                    rs.getInt("menu_item_id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getBoolean("availability"),
                    rs.getString("inventory_link")
                );
                menuItems.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menuItems;
    }
    

    /**
     * Retrieves a single menu item from the database based on its unique ID.
     *
     * @param menuItemId The unique identifier (menu_item_id) of the item to fetch.
     * @return A MenuItem object if an item with the given ID is found, otherwise null.
     */
    public static MenuItem getMenuItemById(int menuItemId) {
        String sql = "SELECT menu_item_id, item_name, category, price, availability, inventory_link " +
                    "FROM menu WHERE menu_item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, menuItemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new MenuItem(
                    rs.getInt("menu_item_id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getBoolean("availability"),
                    rs.getString("inventory_link")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    

    /**
     * Adds a new menu item to the database.
     * This method first determines the next available menu_item_id by finding the
     * current maximum ID and adding 1. The new item is set to 'available' by default.
     *
     * @param itemName The name of the new item (e.g., "Classic Milk Tea").
     * @param category The category of the item (e.g., "Milk Teas", "Specials").
     * @param price The price of the item.
     * @param inventoryLink A string identifier linking to an inventory record.
     * @return true if the item was successfully added, false otherwise.
     */
    public static boolean addMenuItem(String itemName, String category, double price, String inventoryLink) {
        String getMaxIdSql = "SELECT COALESCE(MAX(menu_item_id), 0) + 1 FROM menu";
        String sql = "INSERT INTO menu (menu_item_id, item_name, category, price, availability, inventory_link) VALUES (?, ?, ?, ?, true, ?)";
        
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
                stmt.setString(3, category);
                stmt.setDouble(4, price);
                stmt.setString(5, inventoryLink);
                
                return stmt.executeUpdate() > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing menu item in the database, identified by its ID.
     *
     * @param menuItemId The unique identifier of the menu item to update.
     * @param itemName The new name for the item.
     * @param category The new category for the item.
     * @param price The new price for the item.
     * @param availability The new availability status (true for available, false for out of stock).
     * @param inventoryLink The new inventory link string.
     * @return true if the item was successfully updated, false if the item was not found or an error occurred.
     */
    public static boolean updateMenuItem(int menuItemId, String itemName, String category, 
                                       double price, boolean availability, String inventoryLink) {
        String sql = "UPDATE menu SET item_name = ?, category = ?, price = ?, " +
                    "availability = ?, inventory_link = ? WHERE menu_item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, itemName);
            stmt.setString(2, category);
            stmt.setDouble(3, price);
            stmt.setBoolean(4, availability);
            stmt.setString(5, inventoryLink);
            stmt.setInt(6, menuItemId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
