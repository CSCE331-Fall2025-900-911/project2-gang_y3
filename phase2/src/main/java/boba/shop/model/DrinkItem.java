package boba.shop.model;

public class DrinkItem {
    private MenuItem menuItem;
    private String iceLevel;
    private String sugarLevel;
    private int quantity;

    public DrinkItem(MenuItem menuItem, String iceLevel, String sugarLevel, int quantity) {
        this.menuItem = menuItem;
        this.iceLevel = iceLevel;
        this.sugarLevel = sugarLevel;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public String getIceLevel() { return iceLevel; }
    public String getSugarLevel() { return sugarLevel; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%dx %s (Ice: %s, Sugar: %s) - $%.2f", 
                           quantity, menuItem.getItemName(), iceLevel, sugarLevel, getTotalPrice());
    }
}