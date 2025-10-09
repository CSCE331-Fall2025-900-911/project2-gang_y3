package boba.shop.model;

public class MenuItem {
    private int menuItemId;
    private String itemName;
    private String category;
    private double price;
    private boolean availability;
    private String inventoryLink;

    public MenuItem(int menuItemId, String itemName, String category, double price, boolean availability, String inventoryLink) {
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.availability = availability;
        this.inventoryLink = inventoryLink;
    }

    public int getMenuItemId() { return menuItemId; }
    public String getItemName() { return itemName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public boolean isAvailability() { return availability; }
    public String getInventoryLink() { return inventoryLink; }
}