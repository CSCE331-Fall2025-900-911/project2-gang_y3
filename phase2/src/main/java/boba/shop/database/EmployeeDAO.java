package boba.shop.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import boba.shop.model.Employee;


/**
 * Handles employee database operations such as retrieving, adding, and updating records.
 * Uses the DatabaseConnection class for connecting to PostgreSQL.
 * 
 * @author Shashvat Chathapuram and Sonith Guduru
 */
public class EmployeeDAO {


    /**
     * Retrieves all employees from the database.
     *
     * @return a list of Employee objects
     * 
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
     * Adds a new employee to the database.
     *
     * @param username the employee's username
     * @param password the employee's password
     * @param role the employee's role
     * @return true if the employee was added successfully, false otherwise
     * 
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
     * Updates an existing employee's information.
     *
     * @param userId the employee's ID
     * @param username the updated username
     * @param password the updated password
     * @param role the updated role
     * @return true if the employee was updated successfully, false otherwise
     * 
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



