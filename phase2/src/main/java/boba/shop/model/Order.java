package boba.shop.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an order in the Boba Shop system. Each order contains details such
 * as the date, time, total amount, payment method, order status, customer ID,
 * and item information.
 *
 * @author Sonith
 */
public class Order {

    private LocalDate orderDate;
    private LocalTime orderTime;
    private double totalAmount;
    private String paymentMethod;
    private String orderStatus;
    private int customerId;
    private String itemLink;
    private String customId;

    /**
     * Constructs a new Order with all relevant details.
     *
     * @param orderDate the date when the order was placed
     * @param orderTime the time when the order was placed
     * @param totalAmount the total amount of the order
     * @param paymentMethod the payment method used (e.g., "Cash", "Card")
     * @param orderStatus the current status of the order (e.g., "Pending",
     * "Completed")
     * @param customerId the ID of the customer who placed the order
     * @param itemLink a reference or link to the ordered item details
     * @param customId a unique identifier assigned to the order
     */
    public Order(LocalDate orderDate, LocalTime orderTime, double totalAmount,
            String paymentMethod, String orderStatus, int customerId,
            String itemLink, String customId) {
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.itemLink = itemLink;
        this.customId = customId;
    }

    /**
     * Returns the date of the order.
     *
     * @return the order date
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }

    /**
     * Returns the time of the order.
     *
     * @return the order time
     */
    public LocalTime getOrderTime() {
        return orderTime;
    }

    /**
     * Returns the total amount of the order.
     *
     * @return the total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Returns the payment method used for the order.
     *
     * @return the payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Returns the current status of the order.
     *
     * @return the order status
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * Returns the customer ID associated with the order.
     *
     * @return the customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Returns a link or reference to the ordered items.
     *
     * @return the item link
     */
    public String getItemLink() {
        return itemLink;
    }

    /**
     * Returns the unique custom ID assigned to the order.
     *
     * @return the custom ID
     */
    public String getCustomId() {
        return customId;
    }
}
