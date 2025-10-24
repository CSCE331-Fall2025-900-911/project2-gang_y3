


package boba.shop.model;


/**
 * Represents an item on the menu with its details and availability.
 * @author Joshua Samuel and Sudeep Sirivella
 */
public class MenuItem {
    private int menuItemId;
    private String itemName;
    private String category;
    private double price;
    private boolean availability;
    private String inventoryLink;


    /**
     * Constructs a MenuItem object.
     * @param menuItemId Unique ID of the menu item
     * @param itemName Name of the menu item
     * @param category Category of the menu item
     * @param price Price of the menu item
     * @param availability True if available, false otherwise
     * @param inventoryLink Link to related inventory item
     */
    public MenuItem(int menuItemId, String itemName, String category, double price, boolean availability, String inventoryLink) {
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.availability = availability;
        this.inventoryLink = inventoryLink;
    }


    /** @return Unique ID of the menu item */
    public int getMenuItemId() { return menuItemId; }


    /** @return Name of the menu item */
    public String getItemName() { return itemName; }


    /** @return Category of the menu item */
    public String getCategory() { return category; }


    /** @return Price of the menu item */
    public double getPrice() { return price; }


    /** @return True if available, false otherwise */
    public boolean isAvailability() { return availability; }


    /** @return Inventory link related to the menu item */
    public String getInventoryLink() { return inventoryLink; }
}



