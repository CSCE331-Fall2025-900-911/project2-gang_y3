

package boba.shop.model;

/**
 * Represents a single inventory item in the boba shop.
 * This class models the data stored in the 'inventory' table, including stock levels and thresholds for alerts.
 * 
 * @author Sudeep Sirivella and Sonith Guduru
 */
public class Inventory {
    private int itemId;
    private String itemName;
    private int quantityInStock;
    private String unit;
    private int redThreshold;
    private int yellowThreshold;

    /**
     * Constructs a new Inventory item.
     *
     * @param itemId The unique identifier for the item.
     * @param itemName The name of the item (e.g., "Tapioca Pearls", "Oolong Tea").
     * @param quantityInStock The current quantity in stock.
     * @param unit The unit of measurement (e.g., "kg", "liters", "units").
     * @param redThreshold The critical stock level (critical alert).
     * @param yellowThreshold The low stock level (warning alert).
     */
    public Inventory(int itemId, String itemName, int quantityInStock, String unit, 
                    int redThreshold, int yellowThreshold) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantityInStock = quantityInStock;
        this.unit = unit;
        this.redThreshold = redThreshold;
        this.yellowThreshold = yellowThreshold;
    }

    /**
     * @return The item's unique ID.
     */
    public int getItemId() { return itemId; }
    
    /**
     * @return The item's name.
     */
    public String getItemName() { return itemName; }
    
    /**
     * @return The current quantity in stock.
     */
    public int getQuantityInStock() { return quantityInStock; }
    
    /**
     * @return The unit of measurement.
     */
    public String getUnit() { return unit; }
    
    /**
     * @return The critical stock threshold.
     */
    public int getRedThreshold() { return redThreshold; }
    
    /**
     * @return The low stock (warning) threshold.
     */
    public int getYellowThreshold() { return yellowThreshold; }

    /**
     * Determines the stock status based on current quantity and thresholds.
     *
     * @return "Critical" if stock is at or below redThreshold, "Low" if stock is at or below yellowThreshold, or "Normal" otherwise.
     */
    public String getStatus() {
        if (quantityInStock <= redThreshold) return "Critical";
        else if (quantityInStock <= yellowThreshold) return "Low";
        else return "Normal";
    }

    /**
     * Generates a human-readable string for display in UI lists.
     *
     * @return A formatted display string.
     */
    public String getDisplayString() {
        return String.format("%s: %d %s (%s)", itemName, quantityInStock, unit, getStatus());
    }

    /**
     * Overrides the default toString method to return the display string.
     * This is useful for populating JavaFX controls like ComboBoxes or ListViews.
     *
     * 
     * @return The formatted display string from getDisplayString().
     */
    @Override
    public String toString() {
        return getDisplayString();
    }
}
