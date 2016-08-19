package com.qfg.qunfg.service;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qfg.qunfg.pojo.Product;
import com.qfg.qunfg.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by rbtq on 8/17/16.
 */
public class ProductService extends BaseService {
    private final static ProductService instance = new ProductService();
    private List<Product> products = new ArrayList<>();

    private ProductService() {
//        products.add(new Product().setBarCode(12356).setTitle("纤巧套装").setUnitPrice(21900).setLeft(10));
//        products.add(new Product().setBarCode(12357).setTitle("高压锅套装").setUnitPrice(540000).setLeft(10));
    }

    public static ProductService getInstance() {
        return instance;
    }

    @Override
    public void getAll(final Handler handler) {
        final Message msg = new Message();
        msg.what = Constants.LIST_OF_PRODUCT;
        if (products.isEmpty()) {
            BgExecutorService.getInstance().submit(new Callable<String>(

            ) {
                @Override
                public String call() throws Exception {
                    try {
                        String result = HttpService.get("/products");
                        Gson gson = new Gson();
                        products = gson.fromJson(result, new TypeToken<List<Product>>(){}.getType());
                        msg.obj = products;
                        handler.sendMessage(msg);
                        return "";
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            });
        } else {
            msg.obj = products;
            handler.sendMessage(msg);
        }
    }

    public Product getByBarCode(Integer barCode) {
        for (Product product : products) {
            if (barCode.equals(product.getBarCode())) {
                return product;
            }
        }

        return null;
    }
}
