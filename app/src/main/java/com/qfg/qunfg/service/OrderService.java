package com.qfg.qunfg.service;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.qfg.qunfg.pojo.Order;
import com.qfg.qunfg.util.Constants;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rbtq on 8/19/16.
 */
public class OrderService {
    private final static OrderService instance = new OrderService();
    private List<Order> orders = new LinkedList<>();

    private OrderService() {
    }

    public static OrderService getInstance() {
        return instance;
    }

    public void addAll(Collection<Order> orders) {
        this.orders.addAll(orders);
    }

    public List<Order> getAll() {
        return orders;
    }

    public void getStatics(final Handler handler) {
        final Message msg = new Message();
        msg.what = Constants.LIST_OF_ORDER;
        BgExecutorService.getInstance().submit(new Runnable(

        ) {
            @Override
            public void run() {
                try {
                    String result = HttpService.get("/orders/statics");
                    Gson gson = new Gson();
                    Order.Statics statics = gson.fromJson(result, Order.Statics.class);
                    msg.obj = statics;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        handler.sendMessage(msg);
    }
}
