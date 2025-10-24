// Sudeep
package boba.shop.model;

/**
 * Represents an Employee entity.
 * This class is a simple data model (POJO) to hold employee information
 * such as their ID, username, password, and role.
 */
public class Employee {
    private int userId;
    private String username;
    private String password;
    private String role;
    
    /**
     * Constructs a new Employee object.
     *
     * @param userId The unique identifier for the employee.
     * @param username The employee's login name.
     * @param password The employee's password (hashed or plain text).
     * @param role The employee's assigned role (e.g., "manager", "cashier").
     */
    public Employee(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    /**
     * Gets the employee's unique user ID.
     * @return The user ID.
     */
    public int getUserId() { return userId; }
    
    /**
     * Sets the employee's user ID.
     * @param userId The new user ID.
     */
    public void setUserId(int userId) { this.userId = userId; }
    
    /**
     * Gets the employee's username.
     * @return The username.
     */
    public String getUsername() { return username; }
    
    /**
     * Sets the employee's username.
     * @param username The new username.
     */
    public void setUsername(String username) { this.username = username; }
    
    /**
     * Gets the employee's password.
     * @return The password.
     */
    public String getPassword() { return password; }
    
    /**
     * Sets the employee's password.
     * @param password The new password.
     */
    public void setPassword(String password) { this.password = password; }
    
    /**
     * Gets the employee's role.
     * @return The role.
     */
    public String getRole() { return role; }
    
    /**
     * Sets the employee's role.
     * @param role The new role.
     */
    public void setRole(String role) { this.role = role; }
    
    /**
     * Generates a formatted string for display in UI lists.
     * Example: "sudeep (manager) - ID: 1"
     *
     * @return A formatted display string.
     */
    public String getDisplayString() {
        return String.format("%s (%s) - ID: %d", username, role, userId);
    }
}
