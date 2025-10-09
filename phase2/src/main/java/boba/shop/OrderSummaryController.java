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

public class OrderSummaryController {

    @FXML
    private ListView<String> orderItemsList;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private ChoiceBox<String> paymentMethodChoice;

    private OrderManager orderManager;
    private ObservableList<String> orderDisplayList;

    public void initialize() {
        orderManager = OrderManager.getInstance();
        orderDisplayList = FXCollections.observableArrayList();
        orderItemsList.setItems(orderDisplayList);
        
        // Setup payment method choice box
        if (paymentMethodChoice != null) {
            paymentMethodChoice.setItems(FXCollections.observableArrayList("cash", "card"));
            paymentMethodChoice.setValue("card");
        }
        
        updateOrderDisplay();
    }

    private void updateOrderDisplay() {
        orderDisplayList.clear();
        List<DrinkItem> order = orderManager.getCurrentOrder();
        
        for (DrinkItem item : order) {
            orderDisplayList.add(item.toString());
        }
        
        totalPriceLabel.setText(String.format("Total: $%.2f", orderManager.getTotalPrice()));
    }

    @FXML
    private void onCheckout() {
        if (orderManager.getOrderSize() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty");
            alert.setHeaderText(null);
            alert.setContentText("Make sure to add items to your order before checking out.");
            alert.showAndWait();
            return;
        }

        // Get selected payment method
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

    @FXML
    private void onCancel() {
        Stage stage = (Stage) orderItemsList.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onRemoveSelected() {
        int selectedIndex = orderItemsList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            orderManager.removeItem(selectedIndex);
            updateOrderDisplay();
        }
    }
}