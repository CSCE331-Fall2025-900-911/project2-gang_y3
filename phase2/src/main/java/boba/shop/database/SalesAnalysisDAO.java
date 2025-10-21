package boba.shop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SalesAnalysisDAO {

    public static Map<String, Object> getProductSalesAnalysis(int menuItemId, LocalDate startDate, LocalDate endDate) 
            throws SQLException {
        
        Map<String, Object> results = new HashMap<>();
        results.put("totalUnits", 0);
        results.put("totalRevenue", 0.0);
        
        
        try (Connection conn = DatabaseConnection.getConnection()) {
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