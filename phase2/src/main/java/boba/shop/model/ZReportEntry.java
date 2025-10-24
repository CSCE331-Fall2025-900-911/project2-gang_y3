package boba.shop.model;

import javafx.beans.property.*;

/**
 * Represents a single row of data for a Z-Report (end-of-day summary).
 * This class is a JavaFX data model (POJO) that uses JavaFX Properties to allow easy binding with TableView columns. It also includes fields for inventory status.
 * 
 * @author Joshua Samuel and Shashvat Chathapuram
 */
public class ZReportEntry {
    private final StringProperty hour; 
    private final IntegerProperty sales;
    private final IntegerProperty returns;
    private final IntegerProperty voids;
    private final IntegerProperty discards;
    private final DoubleProperty cash;
    private final DoubleProperty card;
    private final IntegerProperty lowStockCount;
    private final IntegerProperty criticalStockCount;

    /**
     * Constructs a new ZReportEntry.
     *
     * @param hour The display string for the report row (e.g., "Daily Totals").
     * @param sales The total number of sales for the day.
     * @param returns The total number of returns for the day.
     * @param voids The total number of voided orders for the day.
     * @param discards The total number of discarded items for the day.
     * @param cash The total cash revenue for the day.
     * @param card The total card revenue for the day.
     */
    public ZReportEntry(String hour, int sales, int returns, int voids, int discards, double cash, double card) {
        this.hour = new SimpleStringProperty(hour);
        this.sales = new SimpleIntegerProperty(sales);
        this.returns = new SimpleIntegerProperty(returns);
        this.voids = new SimpleIntegerProperty(voids);
        this.discards = new SimpleIntegerProperty(discards);
        this.cash = new SimpleDoubleProperty(cash);
        this.card = new SimpleDoubleProperty(card);
        
        this.lowStockCount = new SimpleIntegerProperty(0);
        this.criticalStockCount = new SimpleIntegerProperty(0);
    }

    /**
     * The JavaFX property for the hour/label string.
     * @return The StringProperty for the hour.
     */
    public StringProperty hourProperty() {
        return hour;
    }

    /**
     * The JavaFX property for the sales count.
     * @return The IntegerProperty for sales.
     */
    public IntegerProperty salesProperty() {
        return sales;
    }

    /**
     * The JavaFX property for the returns count.
     * @return The IntegerProperty for returns.
     */
    public IntegerProperty returnsProperty() {
        return returns;
    }

    /**
     * The JavaFX property for the voids count.
     * @return The IntegerProperty for voids.
     */
    public IntegerProperty voidsProperty() {
        return voids;
    }

    /**
     * The JavaFX property for the discards count.
     * @return The IntegerProperty for discards.
     */
    public IntegerProperty discardsProperty() {
        return discards;
    }

    /**
     * The JavaFX property for the total cash amount.
     * @return The DoubleProperty for cash.
     */
    public DoubleProperty cashProperty() {
        return cash;
    }

    /**
     * The JavaFX property for the total card amount.
     * @return The DoubleProperty for card.
     */
    public DoubleProperty cardProperty() {
        return card;
    }

    /**
     * Standard getter for the hour/label.
     * @return The hour string.
     */
    public String getHour() {
        return hour.get();
    }

    /**
     * Standard getter for the sales count.
     * @return The number of sales.
     */
    public int getSales() {
        return sales.get();
    }

    /**
     * Standard getter for the returns count.
     * @return The number of returns.
     */
    public int getReturns() {
        return returns.get();
    }

    /**
     * Standard getter for the voids count.
     * @return The number of voids.
     */
    public int getVoids() {
        return voids.get();
    }

    /**
     * Standard getter for the discards count.
     * @return The number of discards.
     */
    public int getDiscards() {
        return discards.get();
    }

    /**
     * Standard getter for the cash total.
     * @return The total cash amount.
     */
    public double getCash() {
        return cash.get();
    }

    /**
     * Standard getter for the card total.
     * @return The total card amount.
     */
    public double getCard() {
        return card.get();
    }
    
    /**
     * The JavaFX property for the low stock item count.
     * @return The IntegerProperty for the low stock count.
     */
    public IntegerProperty lowStockCountProperty() {
        return lowStockCount;
    }
    
    /**
     * The JavaFX property for the critical stock item count.
     * @return The IntegerProperty for the critical stock count.
     */
    public IntegerProperty criticalStockCountProperty() {
        return criticalStockCount;
    }
    
    /**
     * Standard getter for the low stock count.
     * @return The number of items at low stock.
     */
    public int getLowStockCount() {
        return lowStockCount.get();
    }
    
    /**
     * Standard getter for the critical stock count.
     * @return The number of items at critical stock.
     */
    public int getCriticalStockCount() {
        return criticalStockCount.get();
    }
    
    /**
     * Standard setter for the low stock count.
     * @param count The new low stock count.
     */
    public void setLowStockCount(int count) {
        this.lowStockCount.set(count);
    }
    
    /**
     * Standard setter for the critical stock count.
     * @param count The new critical stock count.
     */
    public void setCriticalStockCount(int count) {
        this.criticalStockCount.set(count);
    }
}

