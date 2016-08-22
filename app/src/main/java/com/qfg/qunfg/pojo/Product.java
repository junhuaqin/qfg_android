package com.qfg.qunfg.pojo;

/**
 * Created by rbtq on 8/17/16.
 */
public class Product{
    private int barCode;
    private String title;
    private int unitPrice;
    private int left;

    public int getBarCode() {
        return barCode;
    }

    public Product setBarCode(int barCode) {
        this.barCode = barCode;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Product setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public Product setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public int getLeft() {
        return left;
    }

    public Product setLeft(int left) {
        this.left = left;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%d-%s", getBarCode(), getTitle());
    }
}
