package com.qfg.qunfg.pojo;

import com.qfg.qunfg.util.Formatter;

/**
 * Created by rbtq on 8/12/16.
 */
public class SaleItem implements KeyValuePOJO {
    private int barCode;
    private String title;
    private int unitPrice;
    private int count;

    public SaleItem() {

    }

    public SaleItem(int barCode, String title, int unitPrice, int count) {
        setBarCode(barCode)
        .setTitle(title)
        .setUnitPrice(unitPrice)
        .setCount(count);
    }

    public int getBarCode() {
        return barCode;
    }

    public SaleItem setBarCode(int barCode) {
        this.barCode = barCode;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SaleItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public SaleItem setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public int getCount() {
        return count;
    }

    public SaleItem setCount(int count) {
        this.count = count;
        return this;
    }

    public SaleItem addCount(int count) {
        this.count += count;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return getBarCode() == ((SaleItem)obj).getBarCode()
                    && getUnitPrice() == ((SaleItem)obj).getUnitPrice();
        } else {
            return true;
        }
    }

    @Override
    public String getKey() {
        return title;
    }

    @Override
    public String getValue() {
        return String.format("%s * %d", Formatter.formatCurrency(Formatter.currency2fg(unitPrice)), count);
    }
}
