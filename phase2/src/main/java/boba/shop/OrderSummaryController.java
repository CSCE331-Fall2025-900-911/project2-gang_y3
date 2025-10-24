

package boba.shop;

import boba.shop.model.DrinkItem;
import boba.shop.model.OrderManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.util.List;

/**
 * Controller for the Order Summary modal dialog (order-summary.fxml).
 * This class handles displaying the current order, total price, removing items, and submitting the order.
 * 
 * @author Sudeep Sirivella and Shashvat Chathapuram
 */
public class OrderSummaryController {

    @FXML
    private ListView<String> orderItemsList;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private ChoiceBox<String> paymentMethodChoice;

    private OrderManager orderManager;
    private ObservableList<String> orderDisplayList;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * It gets the OrderManager instance, sets up the ListView, and populates the payment method ChoiceBox.
     */
    public void initialize() {
        orderManager = OrderManager.getInstance();
        orderDisplayList = FXCollections.observableArrayList();
        orderItemsList.setItems(orderDisplayList);
        
        
        if (paymentMethodChoice != null) {
            paymentMethodChoice.setItems(FXCollections.observableArrayList("cash", "card"));
            paymentMethodChoice.setValue("card");
        }
        
        updateOrderDisplay();
    }

    /**
     * Updates the ListView and total price label to reflect the current state of the order in OrderManager.
     */
    private void updateOrderDisplay() {
        orderDisplayList.clear();
        List<DrinkItem> order = orderManager.getCurrentOrder();
        
        for (DrinkItem item : order) {
            orderDisplayList.add(item.toString());
        }
        
        totalPriceLabel.setText(String.format("Total: $%.2f", orderManager.getTotalPrice()));
    }

    /**
     * FXML Action: Handles the checkout process.
     * Validates that the order is not empty, gets the selected payment method, and submits the order via OrderManager.
     * Shows a confirmation or error alert and closes the dialog on success.
     */
    @FXML
    private void onCheckout() {
        if (orderManager.getOrderSize() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Order");
            alert.setHeaderText(null);
            alert.setContentText("Please add items to your order before checking out.");
            alert.showAndWait();
            return;
        }

        String paymentMethod = (paymentMethodChoice != null) ? paymentMethodChoice.getValue() : "card";
        if (paymentMethod == null) {
            paymentMethod = "card";
        }

        boolean success = orderManager.submitOrder(paymentMethod);
        
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed");
            alert.setHeaderText(null);
            alert.setContentText("Order placed successfully!");
            alert.showAndWait();
            Stage stage = (Stage) orderItemsList.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Order Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to place order.");
            alert.showAndWait();
        }
    }

    /**
     * FXML Action: Cancels the order summary and closes the dialog window.
     */
    @FXML
    private void onCancel() {
        Stage stage = (Stage) orderItemsList.getScene().getWindow();
        stage.close();
    }

    /**
     * FXML Action: Removes the selected item from the order.
     * Gets the selected index from the ListView and calls
     * OrderManager.removeItem(), then updates the display.
     */
    @FXML
    private void onRemoveSelected() {
        int selectedIndex = orderItemsList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            orderManager.removeItem(selectedIndex);
            updateOrderDisplay();
        }
    }
}
