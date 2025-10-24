
package boba.shop;

import boba.shop.database.*;
import boba.shop.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Controller for the Manager Dashboard (manager-dashboard-simple.fxml).
 * This class handles all UI logic for the manager's view, including:
 * - Managing menu items (Add/Update)
 * - Managing inventory (Add/Update)
 * - Managing employees (Add/Update)
 * - Viewing high-level reports
 * - Running product sales analysis
 * - Generating X (hourly) and Z (daily) reports.
 * 
 * @author Joshua Samuel and Sonith Guduru
 */
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
    
    
    @FXML private ComboBox<boba.shop.model.MenuItem> productComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button runAnalysisButton;
    @FXML private Label totalUnitsSoldLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label averageDailySalesLabel;
    @FXML private Label analysisStatusLabel;
    
    
    @FXML private DatePicker xReportDatePicker;
    @FXML private Button generateXReportButton;
    @FXML private TableView<boba.shop.model.XReportEntry> xReportTable;
    @FXML private TableColumn<boba.shop.model.XReportEntry, String> hourColumn;
    @FXML private TableColumn<boba.shop.model.XReportEntry, Integer> salesColumn;
    @FXML private TableColumn<boba.shop.model.XReportEntry, Integer> returnsColumn;
    @FXML private TableColumn<boba.shop.model.XReportEntry, Integer> voidsColumn;
    @FXML private TableColumn<boba.shop.model.XReportEntry, Integer> discardsColumn;
    @FXML private TableColumn<boba.shop.model.XReportEntry, Double> cashColumn;
    @FXML private TableColumn<boba.shop.model.XReportEntry, Double> cardColumn;
    
    
    @FXML private Button generateZReportButton;
    @FXML private TableView<boba.shop.model.ZReportEntry> zReportTable;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, String> zHourColumn;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, Integer> zSalesColumn;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, Integer> zVoidsColumn;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, Integer> zDiscardsColumn;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, Double> zCashColumn;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, Double> zCardColumn;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, Integer> zLowStockColumn;
    @FXML private TableColumn<boba.shop.model.ZReportEntry, Integer> zCriticalStockColumn;

    
    private ObservableList<String> menuDisplayList;
    private ObservableList<String> inventoryDisplayList;
    private ObservableList<String> employeeDisplayList;
    private ObservableList<boba.shop.model.XReportEntry> xReportEntries;
    private ObservableList<boba.shop.model.ZReportEntry> zReportEntries;
    
    
    private List<boba.shop.model.MenuItem> menuItems;
    private List<Inventory> inventoryItems;
    private List<Employee> employees;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded. It sets up ListView/TableView items, cell factories for custom display, and loads all initial data.
     */
    public void initialize() {
        menuDisplayList = FXCollections.observableArrayList();
        inventoryDisplayList = FXCollections.observableArrayList();
        employeeDisplayList = FXCollections.observableArrayList();
        xReportEntries = FXCollections.observableArrayList();
        zReportEntries = FXCollections.observableArrayList();
        
        menuListView.setItems(menuDisplayList);
        inventoryListView.setItems(inventoryDisplayList);
        employeeListView.setItems(employeeDisplayList);
        
        if (productComboBox != null) {
            productComboBox.setCellFactory(param -> new ListCell<boba.shop.model.MenuItem>() {
                @Override
                protected void updateItem(boba.shop.model.MenuItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getItemName());
                    }
                }
            });
            
            productComboBox.setButtonCell(new ListCell<boba.shop.model.MenuItem>() {
                @Override
                protected void updateItem(boba.shop.model.MenuItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getItemName());
                    }
                }
            });
        }
        
        if (xReportTable != null) {
            hourColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));
            salesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));
            returnsColumn.setCellValueFactory(new PropertyValueFactory<>("returns"));
            voidsColumn.setCellValueFactory(new PropertyValueFactory<>("voids"));
            discardsColumn.setCellValueFactory(new PropertyValueFactory<>("discards"));
            cashColumn.setCellValueFactory(new PropertyValueFactory<>("cash"));
            cardColumn.setCellValueFactory(new PropertyValueFactory<>("card"));
            
            cashColumn.setCellFactory(col -> new TableCell<boba.shop.model.XReportEntry, Double>() {
                @Override
                protected void updateItem(Double amount, boolean empty) {
                    super.updateItem(amount, empty);
                    if (empty || amount == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", amount));
                    }
                }
            });
            
            cardColumn.setCellFactory(col -> new TableCell<boba.shop.model.XReportEntry, Double>() {
                @Override
                protected void updateItem(Double amount, boolean empty) {
                    super.updateItem(amount, empty);
                    if (empty || amount == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", amount));
                    }
                }
            });
            
            discardsColumn.setCellFactory(col -> new TableCell<boba.shop.model.XReportEntry, Integer>() {
                @Override
                protected void updateItem(Integer count, boolean empty) {
                    super.updateItem(count, empty);
                    if (empty || count == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(count.toString());
                        if (count > 0) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });
        }
        
        if (zReportTable != null) {
            zHourColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));
            zSalesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));
            zVoidsColumn.setCellValueFactory(new PropertyValueFactory<>("voids"));
            zDiscardsColumn.setCellValueFactory(new PropertyValueFactory<>("discards"));
            zCashColumn.setCellValueFactory(new PropertyValueFactory<>("cash"));
            zCardColumn.setCellValueFactory(new PropertyValueFactory<>("card"));
            zLowStockColumn.setCellValueFactory(new PropertyValueFactory<>("lowStockCount"));
            zCriticalStockColumn.setCellValueFactory(new PropertyValueFactory<>("criticalStockCount"));
            
            zCashColumn.setCellFactory(col -> new TableCell<boba.shop.model.ZReportEntry, Double>() {
                @Override
                protected void updateItem(Double amount, boolean empty) {
                    super.updateItem(amount, empty);
                    if (empty || amount == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", amount));
                    }
                }
            });
            
            zCardColumn.setCellFactory(col -> new TableCell<boba.shop.model.ZReportEntry, Double>() {
                @Override
                protected void updateItem(Double amount, boolean empty) {
                    super.updateItem(amount, empty);
                    if (empty || amount == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", amount));
                    }
                }
            });
            
            zDiscardsColumn.setCellFactory(col -> new TableCell<boba.shop.model.ZReportEntry, Integer>() {
                @Override
                protected void updateItem(Integer count, boolean empty) {
                    super.updateItem(count, empty);
                    if (empty || count == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(count.toString());
                        if (count > 0) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });
            
            zLowStockColumn.setCellFactory(col -> new TableCell<boba.shop.model.ZReportEntry, Integer>() {
                @Override
                protected void updateItem(Integer count, boolean empty) {
                    super.updateItem(count, empty);
                    if (empty || count == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(count.toString());
                        if (count > 0) {
                            setStyle("-fx-text-fill: #FF8C00; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });
            
            zCriticalStockColumn.setCellFactory(col -> new TableCell<boba.shop.model.ZReportEntry, Integer>() {
                @Override
                protected void updateItem(Integer count, boolean empty) {
                    super.updateItem(count, empty);
                    if (empty || count == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(count.toString());
                        if (count > 0) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });
            
            if (xReportDatePicker != null) {
                xReportDatePicker.setVisible(false);
                xReportDatePicker.setManaged(false);
            }
        }
        
        loadAllData();
        loadReports();
    }

    /**
     * Helper method to load data for Menu, Inventory, and Employees.
     */
    private void loadAllData() {
        loadMenuData();
        loadInventoryData();
        loadEmployeeData();
    }

    /**
     * Fetches menu items from MenuDAO and populates the menu ListView and the product analysis ComboBox.
     */
    private void loadMenuData() {
        try {
            menuItems = MenuDAO.getAllMenuItems();
            menuDisplayList.clear();
            
            for (boba.shop.model.MenuItem item : menuItems) {
                menuDisplayList.add(String.format("%s - $%.2f (%s)", 
                    item.getItemName(), item.getPrice(), item.getCategory()));
            }
            
            if (productComboBox != null) {
                ObservableList<boba.shop.model.MenuItem> productItems = FXCollections.observableArrayList(menuItems);
                productComboBox.setItems(productItems);
                
                if (!productItems.isEmpty()) {
                    productComboBox.getSelectionModel().selectFirst();
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage());
        }
    }

    /**
     * Fetches inventory items from InventoryDAO and populates the inventory ListView.
     */
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

    /**
     * Fetches employees from EmployeeDAO and populates the employee ListView.
     */
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

    /**
     * Fetches daily report summaries from ReportsDAO and populates the labels on the main dashboard tab.
     */
    private void loadReports() {
        try {
            Map<String, Object> reports = ReportsDAO.getDailyReports();
            todayOrdersLabel.setText(String.valueOf(reports.get("todayOrders")));
            todayRevenueLabel.setText(String.format("$%.2f", reports.get("todayRevenue")));
            popularItemsLabel.setText((String) reports.get("popularItems"));
            popularHourLabel.setText((String) reports.get("popularHour"));
            
            if (inventoryItems != null && !inventoryItems.isEmpty()) {
                lowInventoryLabel.setText(String.valueOf(getUnderThresholdCount()));
            } else {
                lowInventoryLabel.setText(String.valueOf(reports.get("lowInventory")));
            }
            
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

    /**
     * Calculates the number of inventory items that are at or below their red (critical) threshold.
     *
     * @return The count of items at or below the red threshold.
     */
    private int getUnderThresholdCount() {
        int count = 0;
        for (Inventory item : inventoryItems) {
            if (item.getQuantityInStock() <= item.getRedThreshold()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * FXML Action: Handles adding a new menu item.
     * Reads data from the menu text fields and calls MenuDAO.addMenuItem.
     */
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

    /**
     * FXML Action: Handles updating an existing menu item.
     * Reads data from fields and updates the item selected in the menu ListView.
     */
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

    /**
     * FXML Action: Handles adding a new inventory item.
     * Reads data from inventory text fields and calls InventoryDAO.addInventoryItem.
     */
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

    /**
     * FXML Action: Handles updating an existing inventory item.
     * Reads data from fields and updates the item selected in the inventory ListView.
     */
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

    /**
     * FXML Action: Handles adding a new employee.
     * Reads data from employee text fields and calls EmployeeDAO.addEmployee.
     */
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

    /**
     * FXML Action: Handles updating an existing employee.
     * Reads data from fields and updates the employee selected in the employee ListView.
     */
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

    /**
     * FXML Action: Refreshes the data on the "Dashboard" tab.
     */
    @FXML
    private void onRefreshReports() {
        loadReports();
    }

    /**
     * FXML Action: Refreshes all data across all tabs.
     */
    @FXML
    private void onRefreshAll() {
        loadAllData();
        loadReports();
    }

    /**
     * FXML Action: Navigates back to the main Point-of-Sale (POS) screen.
     */
    @FXML
    private void onBackToPOS() {
        try {
            App.setRoot("pos-main", 1000, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears all text fields in the "Menu" management tab.
     */
    private void clearMenuFields() {
        menuNameField.clear();
        menuCategoryField.clear();
        menuPriceField.clear();
        menuInventoryLinkField.clear();
        menuAvailableCheck.setSelected(true);
    }

    /**
     * Clears all text fields in the "Inventory" management tab.
     */
    private void clearInventoryFields() {
        invNameField.clear();
        invQuantityField.clear();
        invUnitField.clear();
        invRedThresholdField.clear();
        invYellowThresholdField.clear();
    }

    /**
     * Clears all text fields in the "Employees" management tab.
     */
    private void clearEmployeeFields() {
        empUsernameField.clear();
        empPasswordField.clear();
        empRoleField.clear();
    }

    /**
     * Utility method to show a simple information alert dialog.
     *
     * @param title The title for the alert window.
     * @param message The content message for the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * FXML Action: Runs the product sales analysis based on selected product and date range.
     */
    @FXML
    private void onRunProductAnalysis() {
        try {
            analysisStatusLabel.setText("");
            
            boba.shop.model.MenuItem selectedProduct = productComboBox.getValue();
            if (selectedProduct == null) {
                analysisStatusLabel.setText("Please select a product");
                return;
            }
            
            int productId = selectedProduct.getMenuItemId();
            String productName = selectedProduct.getItemName();
            
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            
            if (startDate == null || endDate == null) {
                analysisStatusLabel.setText("Please select both start and end dates");
                return;
            }
            
            if (endDate.isBefore(startDate)) {
                analysisStatusLabel.setText("End date must be after start date");
                return;
            }
            
            try {
                Map<String, Object> analysisResults = SalesAnalysisDAO.getProductSalesAnalysis(
                    productId, startDate, endDate);
                int totalUnitsSold = (int) analysisResults.get("totalUnits");
                double unitPrice = analysisResults.containsKey("unitPrice") ? (double) analysisResults.get("unitPrice") : 0.0;
                double estimatedProductRevenue;
                if (analysisResults.containsKey("totalRevenue")) {
                    estimatedProductRevenue = (double) analysisResults.get("totalRevenue");
                } else {
                    estimatedProductRevenue = totalUnitsSold * unitPrice;
                }
                
                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                double avgDailySales = daysBetween > 0 ? totalUnitsSold / (double) daysBetween : 0;
                
                totalUnitsSoldLabel.setText(String.valueOf(totalUnitsSold));
                totalRevenueLabel.setText(String.format("$%.2f", estimatedProductRevenue));
                averageDailySalesLabel.setText(String.format("%.2f units/day", avgDailySales));
                
                analysisStatusLabel.setText("Showing results for: " + productName);
                
                if (totalUnitsSold == 0) {
                    analysisStatusLabel.setText("No sales data found for " + productName + " in the selected date range");
                }
            } catch (SQLException e) {
                resetAnalysisResults();
                analysisStatusLabel.setText("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            resetAnalysisResults();
            analysisStatusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Resets the product analysis result labels to their default state.
     */
    private void resetAnalysisResults() {
        totalUnitsSoldLabel.setText("--");
        totalRevenueLabel.setText("--");
        averageDailySalesLabel.setText("--");
    }
    
    /**
     * FXML Action: Generates the X-Report (hourly summary) for the current day.
     */
    @FXML
    private void onGenerateXReport() {
        LocalDate currentDate = LocalDate.now();
        
        try {
            System.out.println("Generating X Report for today's date: " + currentDate);
            
            List<boba.shop.model.XReportEntry> reportData;
            
            reportData = XReportDAO.getXReportData(currentDate);
            
            xReportEntries = FXCollections.observableArrayList(reportData);
            xReportTable.setItems(xReportEntries);
            
            int totalSales = 0;
            double totalCash = 0.0;
            double totalCard = 0.0;
            int totalVoids = 0;
            int totalDiscards = 0;
            
            for (boba.shop.model.XReportEntry entry : reportData) {
                totalSales += entry.getSales();
                totalCash += entry.getCash();
                totalCard += entry.getCard();
                totalVoids += entry.getVoids();
                totalDiscards += entry.getDiscards();
            }
            
            showAlert("X Report Summary", String.format(
                "X Report for Today (%s)\n\n" +
                "SALES SUMMARY\n" +
                "Total Sales: %d\n" +
                "Total Voids: %d\n" +
                "Total Discards: %d\n\n" +
                "PAYMENT SUMMARY\n" +
                "Total Cash: $%.2f\n" +
                "Total Card: $%.2f\n" +
                "Total Revenue: $%.2f",
                currentDate.toString(), totalSales, totalVoids, totalDiscards, 
                totalCash, totalCard, totalCash + totalCard));
            
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * FXML Action: Generates the Z-Report (end-of-day summary).
     */
    @FXML
    private void onGenerateZReport() {
        generateZReport();
    }
    
    /**
     * Core logic for generating the Z-Report. Fetches data from ZReportDAO, populates the Z-Report TableView, and displays a summary alert.
     */
    private void generateZReport() {
        try {
            
            List<boba.shop.model.ZReportEntry> reportData;
            
            reportData = ZReportDAO.getZReportData();
            
            zReportEntries = FXCollections.observableArrayList(reportData);
            zReportTable.setItems(zReportEntries);
            
            if (reportData.isEmpty()) {
                showAlert("Error", "No data available for Z Report");
                return;
            }
            
            boba.shop.model.ZReportEntry totals = reportData.get(0);
            int totalSales = totals.getSales();
            double totalCash = totals.getCash();
            double totalCard = totals.getCard();
            int totalVoids = totals.getVoids();
            int totalDiscards = totals.getDiscards();
            int lowStockCount = totals.getLowStockCount();
            int criticalStockCount = totals.getCriticalStockCount();
            
            LocalDate currentDate = LocalDate.now();
            showAlert("Z Report Summary", String.format(
                "Z REPORT (End of Day)\n" +
                "Date: %s\n" +
                "Time: %s\n\n" +
                "SALES SUMMARY\n" +
                "Total Sales: %d\n" +
                "Total Voids: %d\n" +
                "Total Discards: %d\n\n" +
                "PAYMENT SUMMARY\n" +
                "Cash Payments: $%.2f\n" +
                "Card Payments: $%.2f\n" +
                "Total Revenue: $%.2f\n\n" +
                "INVENTORY METRICS\n" +
                "Low Stock Items: %d\n" +
                "Critical Stock Items: %d\n\n" +
                "Daily totals are reset.",
                currentDate.toString(),
                java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                totalSales,
                totalVoids,
                totalDiscards,
                totalCash,
                totalCard,
                totalCash + totalCard,
                lowStockCount,
                criticalStockCount
            ));
            
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a TableCell for formatting Double values as currency ($0.00).
     * For XReportEntry.
     * @return A new TableCell for currency formatting.
     */
    private TableCell<boba.shop.model.XReportEntry, Double> createCurrencyCell() {
        return new TableCell<boba.shop.model.XReportEntry, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        };
    }

    /**
     * Creates a TableCell for formatting Double values as currency ($0.00).
     * For ZReportEntry.
     * @return A new TableCell for currency formatting.
     */
    private TableCell<boba.shop.model.ZReportEntry, Double> createCurrencyCellZ() {
        return new TableCell<boba.shop.model.ZReportEntry, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        };
    }

    /**
     * Creates a TableCell that formats an Integer and styles the text red if the value is greater than zero. For XReportEntry.
     * @return A new TableCell with conditional red styling.
     */
    private TableCell<boba.shop.model.XReportEntry, Integer> createRedOnPositiveCell() {
        return new TableCell<boba.shop.model.XReportEntry, Integer>() {
            @Override
            protected void updateItem(Integer count, boolean empty) {
                super.updateItem(count, empty);
                if (empty || count == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(count.toString());
                    if (count > 0) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        };
    }

    /**
     * Creates a TableCell that formats an Integer and styles the text red if the value is greater than zero. For ZReportEntry.
     * @return A new TableCell with conditional red styling.
     */
    private TableCell<boba.shop.model.ZReportEntry, Integer> createRedOnPositiveCellZ() {
        return new TableCell<boba.shop.model.ZReportEntry, Integer>() {
            @Override
            protected void updateItem(Integer count, boolean empty) {
                super.updateItem(count, empty);
                if (empty || count == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(count.toString());
                    if (count > 0) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        };
    }

    /**
     * Creates a TableCell that formats an Integer and styles the text with a specific color if the value is greater than zero.
     * @param color The CSS color string (e.g., "red" or "#FF8C00").
     * @return A new TableCell with conditional styling.
     */
    private TableCell<boba.shop.model.ZReportEntry, Integer> createColoredStockCell(String color) {
        return new TableCell<boba.shop.model.ZReportEntry, Integer>() {
            @Override
            protected void updateItem(Integer count, boolean empty) {
                super.updateItem(count, empty);
                if (empty || count == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(count.toString());
                    if (count > 0) {
                        setStyle(String.format("-fx-text-fill: %s; -fx-font-weight: bold;", color));
                    } else {
                        setStyle("");
                    }
                }
            }
        };
    }
}

