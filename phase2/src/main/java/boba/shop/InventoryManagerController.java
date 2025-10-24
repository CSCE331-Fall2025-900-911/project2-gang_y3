package boba.shop;

import java.io.IOException;
import java.util.List;

import boba.shop.database.InventoryDAO;
import boba.shop.model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * Controller class for managing and displaying inventory items in the Boba Shop
 * application. It organizes inventory data into categories (milk, boba,
 * utensils) and provides methods to refresh, sort, and navigate between views.
 *
 * @author Sonith
 */
public class InventoryManagerController {

    @FXML
    private ListView<String> milkInventory;
    @FXML
    private ListView<String> bobaInventory;
    @FXML
    private ListView<String> utensilsInventory;

    private ObservableList<String> milkList;
    private ObservableList<String> bobaList;
    private ObservableList<String> utensilsList;

    private List<Inventory> allInventoryItems;

    /**
     * Initializes the controller by loading the inventory data.
     */
    public void initialize() {
        loadInventoryData();
    }

    /**
     * Loads all inventory items from the database and categorizes them into
     * milk, boba, and utensil lists for display in the UI.
     */
    private void loadInventoryData() {
        try {
            allInventoryItems = InventoryDAO.getAllInventoryItems();

            milkList = FXCollections.observableArrayList();
            bobaList = FXCollections.observableArrayList();
            utensilsList = FXCollections.observableArrayList();

            for (Inventory item : allInventoryItems) {
                String itemName = item.getItemName().toLowerCase();
                String displayString = item.getDisplayString();

                if (itemName.contains("milk")) {
                    milkList.add(displayString);
                } else if (itemName.contains("boba") || itemName.contains("tapioca")
                        || itemName.contains("popping") || itemName.contains("flavoring")
                        || itemName.contains("puree")) {
                    bobaList.add(displayString);
                } else {
                    utensilsList.add(displayString);
                }
            }

            milkInventory.setItems(milkList);
            bobaInventory.setItems(bobaList);
            utensilsInventory.setItems(utensilsList);

        } catch (Exception e) {
            milkList = FXCollections.observableArrayList();
            bobaList = FXCollections.observableArrayList();
            utensilsList = FXCollections.observableArrayList();
            milkInventory.setItems(milkList);
            bobaInventory.setItems(bobaList);
            utensilsInventory.setItems(utensilsList);
        }
    }

    /**
     * Returns the user to the main POS (Point of Sale) screen.
     *
     * @throws IOException if the FXML file for the POS screen cannot be loaded
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
     * Refreshes the inventory data by reloading it from the database.
     */
    @FXML
    private void onRefreshInventory() {
        loadInventoryData();
    }

    /**
     * Sorts the milk inventory list in descending order based on quantity.
     */
    @FXML
    private void onSortMilkDescending() {
        milkList.sort((a, b) -> {
            int aCount = extractCount(a);
            int bCount = extractCount(b);
            return Integer.compare(bCount, aCount);
        });
    }

    /**
     * Sorts the boba inventory list in ascending order based on quantity.
     */
    @FXML
    private void onSortBobaAscending() {
        bobaList.sort((a, b) -> {
            int aCount = extractCount(a);
            int bCount = extractCount(b);
            return Integer.compare(aCount, bCount);
        });
    }

    /**
     * Sorts the utensils inventory list alphabetically.
     */
    @FXML
    private void onSortUtensilsAlphabetical() {
        utensilsList.sort(String::compareTo);
    }

    /**
     * Extracts the numerical quantity from a formatted inventory item string.
     *
     * @param inventoryItem the inventory item string (e.g., "Milk: 12 units")
     * @return the parsed quantity as an integer, or 0 if parsing fails
     */
    private int extractCount(String inventoryItem) {
        try {
            String[] parts = inventoryItem.split(":");
            if (parts.length > 1) {
                String countPart = parts[1].trim().split(" ")[0];
                return Integer.parseInt(countPart);
            }
        } catch (NumberFormatException e) {
            return 0;
        }
        return 0;
    }
}
