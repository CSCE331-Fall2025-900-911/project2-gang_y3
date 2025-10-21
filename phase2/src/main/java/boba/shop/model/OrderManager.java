// Sudeep

package boba.shop.model;

import boba.shop.database.OrderDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;
    private List<DrinkItem> currentOrder;
    private double totalPrice;

    private OrderManager() {
        currentOrder = new ArrayList<>();
        totalPrice = 0.0;
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addItem(DrinkItem item) {
        currentOrder.add(item);
        recalculateTotal();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < currentOrder.size()) {
            currentOrder.remove(index);
            recalculateTotal();
        }
    }

    public List<DrinkItem> getCurrentOrder() {
        return new ArrayList<>(currentOrder);
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    
    public int getOrderSize() {
        return currentOrder.size();
    }

    public void clearOrder() {
        currentOrder.clear();
        totalPrice = 0.0;
    }

    private void recalculateTotal() {
        totalPrice = currentOrder.stream()
                .mapToDouble(DrinkItem::getTotalPrice)
                .sum();
    }

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