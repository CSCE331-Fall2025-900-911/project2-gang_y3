package boba.shop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import boba.shop.model.ZReportEntry;

/**
 * Data Access Object (DAO) for generating Z-Reports (end-of-day sales summaries).
 * This class queries the Orders and Inventory tables to aggregate sales data
 * and inventory status for the current day.
 */
public class ZReportDAO {

    /**
     * Retrieves and aggregates sales and inventory data for the current day (LocalDate.now()).
     *
     * This method queries all orders, filters for those matching the current date,
     * and aggregates total sales, voids, discards, and payment types (cash/card).
     * It also queries the inventory table to get counts of items at "low" and
     * "critical" stock levels.
     *
     * Note: This method processes all orders in the database and filters them in Java,
     * which may be inefficient for large datasets.
     *
     * @return A List containing a single ZReportEntry object populated with
     * the day's total figures.
     * @throws SQLException if a database access error occurs.
     */
    public static List<ZReportEntry> getZReportData() throws SQLException {
        LocalDate targetDate = LocalDate.now();
        List<ZReportEntry> result = new ArrayList<>();
        System.out.println("Generating Z Report for today: " + targetDate);
        ZReportEntry dailyTotals = new ZReportEntry("Daily Totals", 0, 0, 0, 0, 0.0, 0.0);
        
        int totalSales = 0;
        double totalCash = 0.0;
        double totalCard = 0.0;
        int totalVoids = 0;
        int totalDiscards = 0;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            String sql = "SELECT order_id, order_date, order_time, total_amount, payment_method, order_status " +
                         "FROM Orders";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                
                int matchCount = 0;
                int totalRecords = 0;
                
                while (rs.next()) {
                    totalRecords++;
                    
                    String dateStr = rs.getString("order_date");
                    String paymentMethod = rs.getString("payment_method");
                    String orderStatus = rs.getString("order_status");
                    double totalAmount = rs.getDouble("total_amount");
                    
                   
                    LocalDate orderDate = parseDate(dateStr);
                    if (orderDate == null) {
                        continue; 
                    }
                    
                  
                    if (orderDate.equals(targetDate)) {
                        matchCount++;
                        
                        
                        int salesIncrement = orderStatus.equalsIgnoreCase("fulfilled") ? 1 : 0;
                        totalSales += salesIncrement;
                        
                        int voidIncrement = orderStatus.equalsIgnoreCase("canceled") ? 1 : 0;
                        totalVoids += voidIncrement;
                        
                        int discardIncrement = orderStatus.equalsIgnoreCase("discarded") ? 1 : 0;
                        totalDiscards += discardIncrement;
                        
                        double cashIncrement = paymentMethod.equalsIgnoreCase("cash") ? totalAmount : 0;
                        totalCash += cashIncrement;
                        
                        double cardIncrement = paymentMethod.equalsIgnoreCase("card") ? totalAmount : 0;
                        totalCard += cardIncrement;
                    }
                }
                
            }
            
       
            int lowStockCount = 0;
            int criticalStockCount = 0;
            
           
            String invSql = "SELECT COUNT(*) as low_count FROM inventory WHERE quantity_in_stock <= yellow_threshold AND quantity_in_stock > red_threshold";
            try (PreparedStatement invStmt = conn.prepareStatement(invSql);
                 ResultSet invRs = invStmt.executeQuery()) {
                if (invRs.next()) {
                    lowStockCount = invRs.getInt("low_count");
                }
            }
            
          
            String criticalSql = "SELECT COUNT(*) as critical_count FROM inventory WHERE quantity_in_stock <= red_threshold";
            try (PreparedStatement critStmt = conn.prepareStatement(criticalSql);
                 ResultSet critRs = critStmt.executeQuery()) {
                if (critRs.next()) {
                    criticalStockCount = critRs.getInt("critical_count");
                }
            }
            
           
            dailyTotals = new ZReportEntry(
                "Daily Totals",
                totalSales,
                0, 
                totalVoids,
                totalDiscards,
                totalCash,
                totalCard
            );
            
            result.add(dailyTotals);
        
            dailyTotals.setLowStockCount(lowStockCount);
            dailyTotals.setCriticalStockCount(criticalStockCount);
            }
        
        return result;
    }
    
    /**
     * A robust parser for various date formats stored in the database.
     *
     * Attempts to parse dates in several common formats (M/d/yy, MM/dd/yy, yyyy-MM-dd)
     * including a custom parser that correctly handles two-digit years
     * by assuming the 2000s.
     *
     * @param dateStr The date string to parse.
     * @return A LocalDate object, or null if parsing fails.
     */
    private static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
 
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
   
        String[] formats = {
            "M/d/yy",  
            "MM/dd/yy",
            "yyyy-MM-dd" 
        };
        
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
          
            }
        }
        
        System.err.println("Failed to parse date: " + dateStr);
        return null;
    }
    
}

