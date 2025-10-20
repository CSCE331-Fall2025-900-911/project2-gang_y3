// Sudeep
package boba.shop.database;

import boba.shop.model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@author Sudeep
public class MenuDAO {
    
    @param
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
    
    @return
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
    
    @throws
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