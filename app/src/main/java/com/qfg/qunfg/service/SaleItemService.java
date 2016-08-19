package com.qfg.qunfg.service;

import com.qfg.qunfg.pojo.SaleItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rbtq on 8/18/16.
 */
public class SaleItemService {
    private final static SaleItemService instance = new SaleItemService();
    private List<SaleItem> saleItems = new ArrayList<>();

    private SaleItemService() {

    }

    public static SaleItemService getInstance() {
        return instance;
    }

    public void addAll(Collection<SaleItem> c) {
        for (SaleItem item : c) {
            add(item);
        }
    }

    public void add(SaleItem s) {
        boolean bFind = false;

        for (SaleItem item : saleItems) {
            if (item.equals(s)) {
                item.addCount(s.getCount());
                bFind = true;
                break;
            }
        }

        if (!bFind) {
            saleItems.add(s);
        }
    }

    public void delete(SaleItem s) {
        saleItems.remove(s);
    }

    public void clear() {
        saleItems.clear();
    }

    public List<SaleItem> getAll() {
        return saleItems;
    }

    public int getTotalPrice() {
        int total = 0;
        for (SaleItem item : saleItems) {
            total += item.getCount()*item.getUnitPrice();
        }

        return total;
    }


}
