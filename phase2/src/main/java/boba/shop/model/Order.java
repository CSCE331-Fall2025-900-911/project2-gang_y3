// Sonith

package boba.shop.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
    private LocalDate orderDate;
    private LocalTime orderTime;
    private double totalAmount;
    private String paymentMethod;
    private String orderStatus;
    private int customerId;
    private String itemLink;
    private String customId;

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

    public LocalDate getOrderDate() { return orderDate; }
    public LocalTime getOrderTime() { return orderTime; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getOrderStatus() { return orderStatus; }
    public int getCustomerId() { return customerId; }
    public String getItemLink() { return itemLink; }
    public String getCustomId() { return customId; }
}