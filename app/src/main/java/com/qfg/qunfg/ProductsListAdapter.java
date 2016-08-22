package com.qfg.qunfg;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qfg.qunfg.pojo.Product;
import com.qfg.qunfg.util.Formatter;

/**
 * Created by rbtq on 8/22/16.
 */
public class ProductsListAdapter extends ArrayAdapter<Product> {
    public ProductsListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Product product = super.getItem(position);

        ((TextView)view).setText(String.format("%d %s %s %d", product.getBarCode(), product.getTitle(),
                Formatter.formatCurrency(Formatter.currency2fg(product.getUnitPrice())), product.getLeft()));
        return view;
    }
}
