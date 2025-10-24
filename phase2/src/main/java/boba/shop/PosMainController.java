


package boba.shop;


import java.io.IOException;
import java.util.List;


import boba.shop.database.DatabaseConnection;
import boba.shop.database.MenuDAO;
import boba.shop.model.DrinkItem;
import boba.shop.model.MenuItem;
import boba.shop.model.OrderManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Controller for the main POS interface. Handles menu loading, order updates, and navigation.
 * @author Joshua Samuel and Shashvat Chathapuram
 */
public class PosMainController {


    @FXML
    private ListView<String> orderListView;


    @FXML
    private Button viewOrderButton;


    @FXML
    private GridPane menuGridPane;


    private OrderManager orderManager;
    private ObservableList<String> orderDisplayList;
    private List<MenuItem> menuItems;


    /** Initializes the POS view, tests the database connection, and loads menu items. */
    public void initialize() {
        if (!DatabaseConnection.testConnection()) {
            showAlert("Error", "Error connecting to database.");
            return;
        }


        orderManager = OrderManager.getInstance();
        orderDisplayList = FXCollections.observableArrayList();
        orderListView.setItems(orderDisplayList);
        loadMenuItems();
        updateOrderDisplay();
    }


    /** Loads menu items from the database and displays them as buttons. */
    private void loadMenuItems() {
        try {
            menuItems = MenuDAO.getAllMenuItems();


            if (menuGridPane != null) {
                menuGridPane.getChildren().clear();


                int row = 0, col = 0, maxCols = 3;
                for (MenuItem item : menuItems) {
                    Button button = createMenuButton(item);
                    menuGridPane.add(button, col, row);
                    if (++col >= maxCols) {
                        col = 0;
                        row++;
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Error loading menu items from database!");
        }
    }


    /**
     * @param item the menu item to display
     * @return a styled button representing the menu item
     */
    private Button createMenuButton(MenuItem item) {
        Button button = new Button();
        button.setText(item.getItemName() + "\n$" + String.format("%.2f", item.getPrice()));
        button.setPrefSize(200, 100);
        button.setStyle("-fx-background-color: #ddd; -fx-font-size: 18px;");
        button.setOnAction(e -> openCustomizeWindow(item));
        return button;
    }


    /**
     * Opens the order summary window.
     */
    @FXML
    private void onViewOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order-summary.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);


            Stage stage = new Stage();
            stage.setTitle("Summary");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            updateOrderDisplay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** Switches to the manager dashboard view. */
    @FXML
    private void onManagerView() {
        try {
            App.setRoot("manager-dashboard-simple", 1200, 800);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Opens a customization window for the selected menu item.
     * @param menuItem the selected menu item
     */
    private void openCustomizeWindow(MenuItem menuItem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customize-drink.fxml"));
            Scene scene = new Scene(loader.load(), 400, 500);


            CustomizeDrinkController controller = loader.getController();
            controller.setMenuItem(menuItem);


            Stage stage = new Stage();
            stage.setTitle("Custom " + menuItem.getItemName());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            updateOrderDisplay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** Updates the displayed list of current order items. */
    public void updateOrderDisplay() {
        orderDisplayList.clear();
        List<DrinkItem> order = orderManager.getCurrentOrder();
        for (DrinkItem item : order) {
            orderDisplayList.add(item.toString());
        }
    }


    /**
     * Displays an alert dialog with a given title and message.
     * @param title alert title
     * @param message alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



