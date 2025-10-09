package boba.shop;

import boba.shop.database.*;
import boba.shop.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class ManagerDashboardSimpleController {

    @FXML private TabPane managerTabPane;
    @FXML private ListView<String> menuListView;
    @FXML private ListView<String> inventoryListView;
    @FXML private ListView<String> employeeListView;
    @FXML private Label todayOrdersLabel;
    @FXML private Label todayRevenueLabel;
    @FXML private Label popularItemsLabel;
    @FXML private Label popularHourLabel;
    @FXML private Label lowInventoryLabel;
    @FXML private Label totalEmployeesLabel;
    
    @FXML private TextField menuNameField;
    @FXML private TextField menuCategoryField;
    @FXML private TextField menuPriceField;
    @FXML private TextField menuInventoryLinkField;
    @FXML private CheckBox menuAvailableCheck;
    
    @FXML private TextField invNameField;
    @FXML private TextField invQuantityField;
    @FXML private TextField invUnitField;
    @FXML private TextField invRedThresholdField;
    @FXML private TextField invYellowThresholdField;
    
    @FXML private TextField empUsernameField;
    @FXML private TextField empPasswordField;
    @FXML private TextField empRoleField;

    private ObservableList<String> menuDisplayList;
    private ObservableList<String> inventoryDisplayList;
    private ObservableList<String> employeeDisplayList;
    private List<boba.shop.model.MenuItem> menuItems;
    private List<Inventory> inventoryItems;
    private List<Employee> employees;

    public void initialize() {
        menuDisplayList = FXCollections.observableArrayList();
        inventoryDisplayList = FXCollections.observableArrayList();
        employeeDisplayList = FXCollections.observableArrayList();
        
        menuListView.setItems(menuDisplayList);
        inventoryListView.setItems(inventoryDisplayList);
        employeeListView.setItems(employeeDisplayList);
        
        loadAllData();
        loadReports();
    }

    private void loadAllData() {
        loadMenuData();
        loadInventoryData();
        loadEmployeeData();
    }

    private void loadMenuData() {
        try {
            menuItems = MenuDAO.getAllMenuItems();
            menuDisplayList.clear();
            for (boba.shop.model.MenuItem item : menuItems) {
                menuDisplayList.add(String.format("%s - $%.2f (%s)", 
                    item.getItemName(), item.getPrice(), item.getCategory()));
            }
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    private void loadInventoryData() {
        try {
            inventoryItems = InventoryDAO.getAllInventoryItems();
            inventoryDisplayList.clear();
            for (Inventory item : inventoryItems) {
                inventoryDisplayList.add(item.getDisplayString());
            }
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    private void loadEmployeeData() {
        try {
            employees = EmployeeDAO.getAllEmployees();
            employeeDisplayList.clear();
            for (Employee emp : employees) {
                employeeDisplayList.add(emp.getDisplayString());
            }
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    private void loadReports() {
        try {
            Map<String, Object> reports = ReportsDAO.getDailyReports();
            todayOrdersLabel.setText(String.valueOf(reports.get("todayOrders")));
            todayRevenueLabel.setText(String.format("$%.2f", reports.get("todayRevenue")));
            popularItemsLabel.setText((String) reports.get("popularItems"));
            popularHourLabel.setText((String) reports.get("popularHour"));
            lowInventoryLabel.setText(String.valueOf(reports.get("lowInventory")));
            totalEmployeesLabel.setText(String.valueOf(reports.get("totalEmployees")));
        } catch (Exception e) {
            todayOrdersLabel.setText("Error");
            todayRevenueLabel.setText("Error");
            popularItemsLabel.setText("Error");
            popularHourLabel.setText("Error");
            lowInventoryLabel.setText("Error");
            totalEmployeesLabel.setText("Error");
        }
    }

    private int getUnderThresholdCount() {
        int count = 0;
        for (Inventory item : inventoryItems) {
            if (item.getQuantityInStock() <= item.getRedThreshold()) {
                count++;
            }
        }
        return count;
    }
    @FXML
    private void onAddMenuItem() {
        try {
            String name = menuNameField.getText().trim();
            String category = menuCategoryField.getText().trim();
            double price = Double.parseDouble(menuPriceField.getText().trim());
            String inventoryLink = menuInventoryLinkField.getText().trim();
            
            if (name.isEmpty() || category.isEmpty()) {
                showAlert("Error", "Need name and category");
                return;
            }
            
            if (MenuDAO.addMenuItem(name, category, price, inventoryLink)) {
                clearMenuFields();
                loadMenuData();
                showAlert("Good", "Added Successfully");
            } else {
                showAlert("Error", "Failed to add menu item");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Bad price format");
        } catch (Exception e) {
            showAlert("Error", "Failed to add menu item: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdateMenuItem() {
        int selectedIndex = menuListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("Error", "Select a menu item to update");
            return;
        }
        
        try {
            boba.shop.model.MenuItem selectedItem = menuItems.get(selectedIndex);
            String name = menuNameField.getText().trim();
            String category = menuCategoryField.getText().trim();
            double price = Double.parseDouble(menuPriceField.getText().trim());
            String inventoryLink = menuInventoryLinkField.getText().trim();
            boolean available = menuAvailableCheck.isSelected();
            
            if (MenuDAO.updateMenuItem(selectedItem.getMenuItemId(), name, category, price, available, inventoryLink)) {
                clearMenuFields();
                loadMenuData();
                showAlert("Success", "Successfully updated menu item");
            } else {
                showAlert("Error", "Failed to update menu item");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Error in number format");
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void onAddInventoryItem() {
        try {
            String name = invNameField.getText().trim();
            int quantity = Integer.parseInt(invQuantityField.getText().trim());
            String unit = invUnitField.getText().trim();
            int redThreshold = Integer.parseInt(invRedThresholdField.getText().trim());
            int yellowThreshold = Integer.parseInt(invYellowThresholdField.getText().trim());
            
            if (name.isEmpty() || unit.isEmpty()) {
                showAlert("Error", "Need name and unit");
                return;
            }
            
            if (InventoryDAO.addInventoryItem(name, quantity, unit, redThreshold, yellowThreshold)) {
                clearInventoryFields();
                loadInventoryData();
                showAlert("Success", "Successfully added inventory item");
            } else {
                showAlert("Error", "Error in adding inventory item");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Error in number format");
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdateInventoryItem() {
        int selectedIndex = inventoryListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("Error", "Error: Select an inventory item to update");
            return;
        }
        
        try {
            Inventory selectedItem = inventoryItems.get(selectedIndex);
            String name = invNameField.getText().trim();
            int quantity = Integer.parseInt(invQuantityField.getText().trim());
            String unit = invUnitField.getText().trim();
            int redThreshold = Integer.parseInt(invRedThresholdField.getText().trim());
            int yellowThreshold = Integer.parseInt(invYellowThresholdField.getText().trim());
            
            if (InventoryDAO.updateInventoryItem(selectedItem.getItemId(), name, quantity, unit, redThreshold, yellowThreshold)) {
                clearInventoryFields();
                loadInventoryData();
                showAlert("Success", "Successfully updated inventory item");
            } else {
                showAlert("Error", "Error in updating inventory item");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Error in number format");
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void onAddEmployee() {
        try {
            String username = empUsernameField.getText().trim();
            String password = empPasswordField.getText().trim();
            String role = empRoleField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                showAlert("Error", "Error: Need username, password, and role");
                return;
            }
            
            if (EmployeeDAO.addEmployee(username, password, role)) {
                clearEmployeeFields();
                loadEmployeeData();
                showAlert("Success", "Successfully added employee");
            } else {
                showAlert("Error", "Error in adding employee");
            }
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdateEmployee() {
        int selectedIndex = employeeListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("Error", "Error: Select an employee to update");
            return;
        }
        
        try {
            Employee selectedEmployee = employees.get(selectedIndex);
            String username = empUsernameField.getText().trim();
            String password = empPasswordField.getText().trim();
            String role = empRoleField.getText().trim();
            
            if (EmployeeDAO.updateEmployee(selectedEmployee.getUserId(), username, password, role)) {
                clearEmployeeFields();
                loadEmployeeData();
                showAlert("Success", "Successfully updated employee");
            } else {
                showAlert("Error", "Error in updating employee");
            }
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void onRefreshReports() {
        loadReports();
    }

    @FXML
    private void onRefreshAll() {
        loadAllData();
        loadReports();
    }

    @FXML
    private void onBackToPOS() {
        try {
            App.setRoot("pos-main", 1000, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearMenuFields() {
        menuNameField.clear();
        menuCategoryField.clear();
        menuPriceField.clear();
        menuInventoryLinkField.clear();
        menuAvailableCheck.setSelected(true);
    }

    private void clearInventoryFields() {
        invNameField.clear();
        invQuantityField.clear();
        invUnitField.clear();
        invRedThresholdField.clear();
        invYellowThresholdField.clear();
    }

    private void clearEmployeeFields() {
        empUsernameField.clear();
        empPasswordField.clear();
        empRoleField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}