// Shashvat
package boba.shop.database;

import boba.shop.model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shashvat
 */
public class EmployeeDAO {
    
    /**
     * @return a list of all employees
     * @throws SQLException if a database access error occurs
     */
    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT user_id, username, password, role FROM authentication ORDER BY user_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    /**
     * @param username employee username
     * @param password employee password
     * @param role employee role
     * @return true if the employee was added successfully
     * @throws SQLException if a database access error occurs
     */
    public static boolean addEmployee(String username, String password, String role) {
        String getMaxIdSql = "SELECT COALESCE(MAX(user_id), 0) + 1 FROM authentication";
        String sql = "INSERT INTO authentication (user_id, username, password, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            int nextId;
            try (PreparedStatement getIdStmt = conn.prepareStatement(getMaxIdSql);
                 ResultSet rs = getIdStmt.executeQuery()) {
                rs.next();
                nextId = rs.getInt(1);
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, nextId);
                stmt.setString(2, username);
                stmt.setString(3, password);
                stmt.setString(4, role);
                
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * @param userId employee ID
     * @param username updated username
     * @param password updated password
     * @param role updated role
     * @return true if the employee was updated successfully
     * @throws SQLException if a database access error occurs
     */
    public static boolean updateEmployee(int userId, String username, String password, String role) {
        String sql = "UPDATE authentication SET username = ?, password = ?, role = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setInt(4, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
