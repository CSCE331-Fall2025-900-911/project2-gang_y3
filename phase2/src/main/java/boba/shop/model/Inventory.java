// Joshua

package boba.shop.model;

public class Inventory {
    private int itemId;
    private String itemName;
    private int quantityInStock;
    private String unit;
    private int redThreshold;
    private int yellowThreshold;

    public Inventory(int itemId, String itemName, int quantityInStock, String unit, 
                    int redThreshold, int yellowThreshold) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantityInStock = quantityInStock;
        this.unit = unit;
        this.redThreshold = redThreshold;
        this.yellowThreshold = yellowThreshold;
    }

    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantityInStock() { return quantityInStock; }
    public String getUnit() { return unit; }
    public int getRedThreshold() { return redThreshold; }
    public int getYellowThreshold() { return yellowThreshold; }

    public String getStatus() {
        if (quantityInStock <= redThreshold) return "Critical";
        else if (quantityInStock <= yellowThreshold) return "Low";
        else return "Normal";
    }

    public String getDisplayString() {
        return String.format("%s: %d %s (%s)", itemName, quantityInStock, unit, getStatus());
    }

    @Override
    public String toString() {
        return getDisplayString();
    }
}