// Sudeep

package boba.shop.model;

import boba.shop.database.OrderDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the state of the current customer order using the Singleton pattern.
 * This class holds the list of items being ordered, calculates the total price,
 * and handles the submission of the order to the database.
 */
public class OrderManager {
    private static OrderManager instance;
    private List<DrinkItem> currentOrder;
    private double totalPrice;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the order list and total price.
     */
    private OrderManager() {
        currentOrder = new ArrayList<>();
        totalPrice = 0.0;
    }

    /**
     * Provides the global single instance of the OrderManager.
     *
     * @return The singleton instance of OrderManager.
     */
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Adds a DrinkItem to the current order and recalculates the total price.
     *
     * @param item The DrinkItem to add.
     */
    public void addItem(DrinkItem item) {
        currentOrder.add(item);
        recalculateTotal();
    }

    /**
     * Removes a DrinkItem from the current order by its index
     * and recalculates the total price.
     *
     * @param index The index of the item to remove.
     */
    public void removeItem(int index) {
        if (index >= 0 && index < currentOrder.size()) {
            currentOrder.remove(index);
            recalculateTotal();
        }
    }

    /**
     * Gets a copy of the list of items in the current order.
     *
     * @return A new ArrayList containing all DrinkItems in the current order.
     */
    public List<DrinkItem> getCurrentOrder() {
        return new ArrayList<>(currentOrder); // Return a copy
    }

    /**
     * Gets the current total price of the order.
     *
     * @return The total price.
     */
    public double getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * Gets the number of items in the current order.
     *
     * @return The size of the current order list.
     */
    public int getOrderSize() {
        return currentOrder.size();
    }

    /**
     * Clears all items from the current order and resets the total price to 0.
     */
    public void clearOrder() {
        currentOrder.clear();
        totalPrice = 0.0;
    }

    /**
     * Private helper method to recalculate the total price of the order
     * by summing the prices of all DrinkItems.
     */
    private void recalculateTotal() {
        totalPrice = currentOrder.stream()
                .mapToDouble(DrinkItem::getTotalPrice)
                .sum();
    }

    /**
     * Submits the current order to the database.
     * This method aggregates item counts and customizations into JSON strings,
     * creates an Order object, and inserts it using OrderDAO.
     *
     * @param paymentMethod The payment method used (e.g., "cash", "card").
     * @return true if the order was successfully inserted, false otherwise.
     */
    public boolean submitOrder(String paymentMethod) {
        if (currentOrder.isEmpty()) return false;

    
        Map<Integer, Integer> itemCounts = new HashMap<>();
       
        Map<Integer, Map<String, String>> customizations = new HashMap<>();

        for (DrinkItem item : currentOrder) {
            int itemId = item.getMenuItem().getMenuItemId();
            itemCounts.put(itemId, itemCounts.getOrDefault(itemId, 0) + item.getQuantity());
            
            Map<String, String> custom = new HashMap<>();
            custom.put("ice", item.getIceLevel());
            custom.put("sugar", item.getSugarLevel());
            customizations.put(itemId, custom); 
        }

      
        String itemLink = createJsonString(itemCounts);
        String customId = createCustomJsonString(customizations);

   
        Order order = new Order(
            LocalDate.now(),
            LocalTime.now(),
            totalPrice,
            paymentMethod,
            "fulfilled", 
            1, 
            itemLink,
            customId
        );

        int orderId = OrderDAO.insertOrder(order);
        if (orderId > 0) {
            clearOrder(); 
            return true;
        }
        return false;
    }

    /**
     * Creates a simple JSON string from a map of item IDs to their quantities.
     * Example: {"1":2, "3":1}
     *
     * @param itemCounts A map where the key is menu_item_id and value is quantity.
     * @return A JSON-formatted string.
     */
    private String createJsonString(Map<Integer, Integer> itemCounts) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<Integer, Integer> entry : itemCounts.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    /**
     * Creates a simple JSON string from a map of item customizations.
     * Example: {"1":{"ice":"50%","sugar":"25%"}}
     *
     * @param customizations A map where the key is menu_item_id and value is another
     * map of customization types (e.g., "ice") to values (e.g., "50%").
     * @return A JSON-formatted string.
     */
    private String createCustomJsonString(Map<Integer, Map<String, String>> customizations) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<Integer, Map<String, String>> entry : customizations.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":{");
            
            Map<String, String> custom = entry.getValue();
            json.append("\"ice\":\"").append(custom.get("ice")).append("\",");
            json.append("\"sugar\":\"").append(custom.get("sugar")).append("\"");
            json.append("}");
            first = false;
        }
        json.append("}");
        return json.toString();
    }
}
