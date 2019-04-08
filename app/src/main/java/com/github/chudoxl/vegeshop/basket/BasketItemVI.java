package com.github.chudoxl.vegeshop.basket;

public class BasketItemVI {
    private long id;
    private String wareName;
    private String sum;
    private String price;
    private String calc;

    public BasketItemVI(long id, String wareName, String sum, String price, String calc) {
        this.id = id;
        this.wareName = wareName;
        this.sum = sum;
        this.price = price;
        this.calc = calc;
    }

    public long getId() {
        return id;
    }

    public String getWareName() {
        return wareName;
    }

    public String getSum() {
        return sum;
    }

    public String getPrice() {
        return price;
    }

    public String getCalc() {
        return calc;
    }
}
