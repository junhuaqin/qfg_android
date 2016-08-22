package com.qfg.qunfg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.qfg.qunfg.pojo.Order;
import com.qfg.qunfg.pojo.SaleItem;
import com.qfg.qunfg.util.Formatter;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rbtq on 8/22/16.
 */
public class OrdersListAdapter extends BaseExpandableListAdapter {
    private List<Order> orders = new LinkedList<>();

    public OrdersListAdapter() {

    }

    public OrdersListAdapter(Collection<Order> orders) {
        this.orders.addAll(orders);
    }

    public void addAll(Collection<Order> orders) {
        this.orders.addAll(orders);
    }

    public void clear() {
        this.orders.clear();
    }

    @Override
    public int getGroupCount() {
        return orders.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return orders.get(i).getItems().size();
    }

    @Override
    public Object getGroup(int i) {
        return orders.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return orders.get(i).getItems().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Order order = (Order)getGroup(i);
        if (null == view) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_list_group, null);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);
        lblListHeader.setText(String.format("%s %s %s", format.format(order.getCreatedAt()),
                order.getSale(),
                Formatter.formatCurrency(Formatter.currency2fg(order.getTotalPrice()))));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        SaleItem saleItem = (SaleItem)getChild(i, i1);
        if (null == view) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_list_item, null);
        }
        TextView txtListChild = (TextView) view.findViewById(R.id.lblListItem);

        txtListChild.setText(String.format("%s %s*%d", saleItem.getTitle(),
                Formatter.formatCurrency(Formatter.currency2fg(saleItem.getUnitPrice())),
                saleItem.getCount()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
