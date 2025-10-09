package boba.shop;

import boba.shop.database.InventoryDAO;
import boba.shop.model.Inventory;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

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

    public void initialize() {
        loadInventoryData();
    }
    
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
                } else if (itemName.contains("boba") || itemName.contains("tapioca") || 
                          itemName.contains("popping") || itemName.contains("flavoring") ||
                          itemName.contains("puree")) {
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

    @FXML
    private void onBackToPOS() {
        try {
            App.setRoot("pos-main", 1000, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRefreshInventory() {
        loadInventoryData();
    }

    @FXML
    private void onSortMilkDescending() {
        milkList.sort((a, b) -> {
            int aCount = extractCount(a);
            int bCount = extractCount(b);
            return Integer.compare(bCount, aCount);
        });
    }

    @FXML
    private void onSortBobaAscending() {
        bobaList.sort((a, b) -> {
            int aCount = extractCount(a);
            int bCount = extractCount(b);
            return Integer.compare(aCount, bCount);
        });
    }

    @FXML
    private void onSortUtensilsAlphabetical() {
        utensilsList.sort(String::compareTo);
    }

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