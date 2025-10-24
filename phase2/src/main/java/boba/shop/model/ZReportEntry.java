package boba.shop.model;

import javafx.beans.property.*;

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

    public StringProperty hourProperty() {
        return hour;
    }

    public IntegerProperty salesProperty() {
        return sales;
    }

    public IntegerProperty returnsProperty() {
        return returns;
    }

    public IntegerProperty voidsProperty() {
        return voids;
    }

    public IntegerProperty discardsProperty() {
        return discards;
    }

    public DoubleProperty cashProperty() {
        return cash;
    }

    public DoubleProperty cardProperty() {
        return card;
    }

    public String getHour() {
        return hour.get();
    }

    public int getSales() {
        return sales.get();
    }

    public int getReturns() {
        return returns.get();
    }

    public int getVoids() {
        return voids.get();
    }

    public int getDiscards() {
        return discards.get();
    }

    public double getCash() {
        return cash.get();
    }

    public double getCard() {
        return card.get();
    }
    
    public IntegerProperty lowStockCountProperty() {
        return lowStockCount;
    }
    
    public IntegerProperty criticalStockCountProperty() {
        return criticalStockCount;
    }
    
    public int getLowStockCount() {
        return lowStockCount.get();
    }
    
    public int getCriticalStockCount() {
        return criticalStockCount.get();
    }
    
    public void setLowStockCount(int count) {
        this.lowStockCount.set(count);
    }
    
    public void setCriticalStockCount(int count) {
        this.criticalStockCount.set(count);
    }
}