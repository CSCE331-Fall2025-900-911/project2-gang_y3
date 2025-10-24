package boba.shop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boba.shop.model.XReportEntry;

/**
 * Data Access Object for generating X-Reports (hourly sales summaries).
 * This class queries the Orders table to aggregate sales data for a specific day, broken down by the hour.
 * 
 * @author Sonith Guduru and Shashvat Chathapuram
 */
public class XReportDAO {
    
    /**
     * Retrieves and aggregates sales data for a specified date, broken down by hour.
     *
     * This method initializes a list of XReportEntry objects for each hour from 8:00 AM to 7:00 PM (19:00). It then queries the Orders table, filters for orders matching the targetDate, and aggregates sales, voids, discards, and payment types (cash/card) into the corresponding hour.
     *
     * 
     *
     * @param targetDate The specific date for which to generate the report.
     * @return A List of XReportEntry objects, one for each hour, populated with that hour's sales data.
     * @throws SQLException if a database access error occurs.
     */
    public static List<XReportEntry> getXReportData(LocalDate targetDate) throws SQLException {
        List<XReportEntry> result = new ArrayList<>();
        Map<Integer, XReportEntry> hourMap = new HashMap<>();
        
        System.out.println("Looking for data for date: " + targetDate);
        
        
        for (int i = 8; i <= 19; i++) {
            String hourDisplay = (i <= 12) ? i + ":00 AM" : (i - 12) + ":00 PM";
            if (i == 12) hourDisplay = "12:00 PM";
            XReportEntry entry = new XReportEntry(hourDisplay, 0, 0, 0, 0, 0.0, 0.0);
            result.add(entry);
            hourMap.put(i, entry); 
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
         
            String sampleSQL = "SELECT order_id, order_date, order_time FROM Orders LIMIT 5";
            try (PreparedStatement sampleStmt = conn.prepareStatement(sampleSQL);
                 ResultSet sampleRs = sampleStmt.executeQuery()) {
                
                System.out.println("Sample orders (to verify date format):");
                while (sampleRs.next()) {
                    String orderId = sampleRs.getString("order_id");
                    String dbDate = sampleRs.getString("order_date");
                    String dbTime = sampleRs.getString("order_time");
                    System.out.println(" - Order #" + orderId + ": date=" + dbDate + ", time=" + dbTime);
                }
            }
            
           
            String sql = "SELECT order_id, order_date, order_time, total_amount, payment_method, order_status " +
                         "FROM Orders";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                
                int matchCount = 0;
                int totalRecords = 0;
                
                while (rs.next()) {
                    totalRecords++;
                    
                    String dateStr = rs.getString("order_date");
                    String timeStr = rs.getString("order_time");
                    String paymentMethod = rs.getString("payment_method");
                    String orderStatus = rs.getString("order_status");
                    double totalAmount = rs.getDouble("total_amount");
                    
                
                    LocalDate orderDate = parseDate(dateStr);
                    if (orderDate == null) {
                        continue; 
                    }
                    
                  
                    if (orderDate.equals(targetDate)) {
                        matchCount++;
                        
                      
                        int hourOfDay = extractHour(timeStr);
                        if (hourOfDay < 8 || hourOfDay > 19) {
                            continue; 
                        }
                        
                        XReportEntry entry = hourMap.get(hourOfDay);
                        if (entry != null) {
                         
                            int salesIncrement = orderStatus.equalsIgnoreCase("fulfilled") ? 1 : 0;
                            int voidIncrement = orderStatus.equalsIgnoreCase("canceled") ? 1 : 0;
                            int discardIncrement = orderStatus.equalsIgnoreCase("discarded") ? 1 : 0;
                            double cashIncrement = paymentMethod.equalsIgnoreCase("cash") ? totalAmount : 0;
                            double cardIncrement = paymentMethod.equalsIgnoreCase("card") ? totalAmount : 0;
                            
                  
                            XReportEntry updatedEntry = new XReportEntry(
                                entry.getHour(),
                                entry.getSales() + salesIncrement,
                                0, 
                                entry.getVoids() + voidIncrement,
                                entry.getDiscards() + discardIncrement,
                                entry.getCash() + cashIncrement,
                                entry.getCard() + cardIncrement
                            );
                            
                          
                            int index = result.indexOf(entry);
                            if (index != -1) {
                                result.set(index, updatedEntry);
                            }
                            hourMap.put(hourOfDay, updatedEntry);
                        }
                    }
                }
                
                System.out.println("Total records processed: " + totalRecords);
                System.out.println("Records matching target date: " + matchCount);
            }
        }
        return result;
    }

    /**
     * A robust parser for various date formats stored in the database.
     *
     * Attempts to parse dates in several common formats (M/d/yy, MM/dd/yy, yyyy-MM-dd) including a custom parser that correctly handles two-digit years by assuming the 2000s.
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
    
    /**
     * Extracts the hour (0-23) from a time string (e.g., "14:30").
     *
     * @param timeStr The time string to parse.
     * @return The hour as an integer, or 0 if parsing fails.
     */
    private static int extractHour(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return 0;
        }
        
        try {
            String[] parts = timeStr.split(":");
            if (parts.length > 0) {
                return Integer.parseInt(parts[0]); 
            }
        } catch (Exception e) {
            System.err.println("Error parsing time: " + timeStr);
        }
        
        return 0;
    }
}

