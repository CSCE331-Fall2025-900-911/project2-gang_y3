package boba.shop.model;

/**
 * Represents a customized drink item in the Boba Shop system. Each drink includes details such as the base menu item, ice level, sugar level, and quantity. It also provides methods to calculate the total price and return a readable string representation.
 *
 * @author Sudeep Sirivella and Shashvat Chathapuram
 */
public class DrinkItem {

    private MenuItem menuItem;
    private String iceLevel;
    private String sugarLevel;
    private int quantity;

    /**
     * Constructs a new DrinkItem with the given menu item, ice level, sugar level, and quantity.
     *
     * @param menuItem the base menu item selected
     * @param iceLevel the chosen ice level (e.g., "No Ice", "Less Ice")
     * @param sugarLevel the chosen sugar level (e.g., "50%", "100%")
     * @param quantity the number of drinks ordered
     */
    public DrinkItem(MenuItem menuItem, String iceLevel, String sugarLevel, int quantity) {
        this.menuItem = menuItem;
        this.iceLevel = iceLevel;
        this.sugarLevel = sugarLevel;
        this.quantity = quantity;
    }

    /**
     * Returns the base menu item for this drink.
     *
     * @return the MenuItem object associated with this drink
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * Returns the ice level of the drink.
     *
     * @return the ice level as a String
     */
    public String getIceLevel() {
        return iceLevel;
    }

    /**
     * Returns the sugar level of the drink.
     *
     * @return the sugar level as a String
     */
    public String getSugarLevel() {
        return sugarLevel;
    }

    /**
     * Returns the quantity ordered.
     *
     * @return the drink quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity of the drink ordered.
     *
     * @param quantity the new quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the total price for this drink order.
     *
     * @return the total cost as a double value
     */
    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;
    }

    /**
     * Returns a string representation of the drink item, including quantity, name, ice level, sugar level, and total price.
     *
     * @return a formatted string describing the drink order
     */
    @Override
    public String toString() {
        return String.format("%dx %s (Ice: %s, Sugar: %s) - $%.2f",
                quantity, menuItem.getItemName(), iceLevel, sugarLevel, getTotalPrice());
    }
}
