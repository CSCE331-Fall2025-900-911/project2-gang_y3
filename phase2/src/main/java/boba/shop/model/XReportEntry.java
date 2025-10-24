package boba.shop.model;

import javafx.beans.property.*;

/**
 * Represents a single row of data for an X-Report (hourly sales summary).
 * This class is a JavaFX data model (POJO) that uses JavaFX Properties
 * to allow easy binding with TableView columns.
 */
public class XReportEntry {
    private final StringProperty hour;
    private final IntegerProperty sales;
    private final IntegerProperty returns;
    private final IntegerProperty voids;
    private final IntegerProperty discards;
    private final DoubleProperty cash;
    private final DoubleProperty card;

    /**
     * Constructs a new XReportEntry.
     *
     * @param hour The display string for the hour (e.g., "9:00 AM").
     * @param sales The total number of sales in that hour.
     * @param returns The total number of returns in that hour.
     * @param voids The total number of voided orders in that hour.
     * @param discards The total number of discarded items in that hour.
     * @param cash The total cash revenue in that hour.
     * @param card The total card revenue in that hour.
     */
    public XReportEntry(String hour, int sales, int returns, int voids, int discards, double cash, double card) {
        this.hour = new SimpleStringProperty(hour);
        this.sales = new SimpleIntegerProperty(sales);
        this.returns = new SimpleIntegerProperty(returns);
        this.voids = new SimpleIntegerProperty(voids);
        this.discards = new SimpleIntegerProperty(discards);
        this.cash = new SimpleDoubleProperty(cash);
        this.card = new SimpleDoubleProperty(card);
    }

    /**
     * The JavaFX property for the hour string.
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
     * Standard getter for the hour.
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
}

