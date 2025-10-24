package boba.shop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object for performing sales analysis.
 * This class provides methods to query and aggregate sales data from the Orders table.
 * 
 * @author Joshua Samuel and Sudeep Sirivella
 */
public class SalesAnalysisDAO {

    /**
     * Analyzes sales for a specific product (MenuItem) within a given date range.
     *
     * This method searches the 'item_link' JSON string in the Orders table for a specific menu item ID. It only includes orders marked as 'fulfilled' and within the specified start and end dates.
     *
     * 
     *
     * @param menuItemId The ID of the menu item to analyze.
     * @param startDate The start date of the analysis period (inclusive).
     * @param endDate The end date of the analysis period (inclusive).
     * @return A Map containing:
     * - "totalUnits" (Integer): Total number of orders containing the item.
     * - "totalRevenue" (Double): Total revenue from those orders.
     * - "unitPrice" (Double): The price of the menu item (if found).
     * @throws SQLException if a database access error occurs.
     */
    public static Map<String, Object> getProductSalesAnalysis(int menuItemId, LocalDate startDate, LocalDate endDate) 
            throws SQLException {
        
        Map<String, Object> results = new HashMap<>();
        results.put("totalUnits", 0);
        results.put("totalRevenue", 0.0);
        
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // SQL to find orders containing the item
            String sql = "SELECT o.order_id, o.order_date, o.total_amount, o.item_link " +
                         "FROM Orders o " +
                         "WHERE o.item_link LIKE ? " +
                         "AND o.order_status = 'fulfilled'";
            String menuItemPattern = "%\"" + menuItemId + "\":%"; 
            
            int totalUnits = 0;
            double totalRevenue = 0.0;
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, menuItemPattern);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String orderDate = rs.getString("order_date");
                        try {
                          
                            LocalDate orderLocalDate = parseDate(orderDate);
                            
                           
                            if ((orderLocalDate.isEqual(startDate) || orderLocalDate.isAfter(startDate)) &&
                                (orderLocalDate.isEqual(endDate) || orderLocalDate.isBefore(endDate))) {
                                totalUnits++;
                                totalRevenue += rs.getDouble("total_amount");
                            }
                        } catch (Exception e) {
                            System.err.println("Error parsing date: " + orderDate);
                        }
                    }
                }
            }
            
            results.put("totalUnits", totalUnits);
            results.put("totalRevenue", totalRevenue);
            
            
            String menuSql = "SELECT price FROM Menu m WHERE m.menu_item_id = ?";
            try (PreparedStatement menuStmt = conn.prepareStatement(menuSql)) {
                menuStmt.setInt(1, menuItemId);
                
                try (ResultSet rs = menuStmt.executeQuery()) {
                    if (rs.next()) {
                        double price = rs.getDouble("price");
                        results.put("unitPrice", price);
                    }
                }
            }
        }
        
        return results;
    }
    
    /**
     * A robust parser for various date formats stored in the database.
     *
     * Attempts to parse dates in several common formats (M/d/yy, M/d/yyyy, yyyy-MM-dd) including a custom parser that correctly handles two-digit years by assuming the 2000s.
     *
     * @param dateStr The date string to parse.
     * @return A LocalDate object.
     * @throws IllegalArgumentException if the date string cannot be parsed by any known format.
     */
    private static LocalDate parseDate(String dateStr) {
       
        try {
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                int month = Integer.parseInt(parts[0]);
                int day = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                if (year < 100) {
                    year += 2000; 
                }
                return LocalDate.of(year, month, day);
            }
        } catch (Exception e) {
            System.err.println("Custom date parser failed for: " + dateStr);
        }
        
      
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("M/d/yy"));
        } catch (Exception e1) {
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("M/d/yyyy"));
            } catch (Exception e2) {
                try {
                    return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e3) {
                    
                    throw new IllegalArgumentException("Cannot parse date: " + dateStr);
                }
            }
        }
    }
}

