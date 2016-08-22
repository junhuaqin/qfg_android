package com.qfg.qunfg.pojo;

import java.util.List;

/**
 * Created by rbtq on 8/18/16.
 */
public class Order{
    private int id;
    private String sale;
    private long createdAt;
    private List<SaleItem> items;
    private int totalPrice;

    public int getId() {
        return id;
    }

    public Order setId(int id) {
        this.id = id;
        return this;
    }

    public String getSale() {
        return sale;
    }

    public Order setSale(String sale) {
        this.sale = sale;
        return this;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public Order setItems(List<SaleItem> items) {
        this.items = items;
        return this;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Order setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "sale='" + sale + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public static class Statics {
        private long curMonth;
        private long curDay;

        public long getCurMonth() {
            return curMonth;
        }

        public long getCurDay() {
            return curDay;
        }
    }
}
