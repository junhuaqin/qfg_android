package com.qfg.qunfg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qfg.qunfg.pojo.Order;
import com.qfg.qunfg.service.HttpService;
import com.qfg.qunfg.service.OrderService;
import com.qfg.qunfg.util.Constants;
import com.qfg.qunfg.util.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by rbtq on 8/19/16.
 */
public class OrdersActivity extends AppCompatActivity {
    private OrdersListAdapter mAdapter;
    private ProgressBar forwardPB;
    private WeakReference<RefreshOrder> refreshOrderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        forwardPB = (ProgressBar) findViewById(R.id.forwardProgressBar);

        mAdapter = new OrdersListAdapter();
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.showOrders);
        listView.setAdapter(mAdapter);
        refreshOrders();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pick:
                Toast.makeText(getApplicationContext(), "pick", Toast.LENGTH_SHORT)
                        .show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void refreshOrders() {
        if (null == refreshOrderReference || null == refreshOrderReference.get()) {
            if (!HttpService.isOnline(this)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.notConnectInternet), Toast.LENGTH_LONG)
                        .show();
                return;
            }

            RefreshOrder refreshOrder = new RefreshOrder();
            refreshOrderReference = new WeakReference<>(refreshOrder);
            refreshOrder.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOrders();
    }

    private void showOrders() {
        mAdapter.clear();
        mAdapter.addAll(OrderService.getInstance().getAll());
        mAdapter.notifyDataSetChanged();
    }

    private class RefreshOrder extends AsyncTask<Void, Void, Message> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            forwardPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected Message doInBackground(Void... orders) {
            Message msg = new Message();

            try {

                String result = HttpService.get("/orders/0/"+System.currentTimeMillis());
                Gson gson = new Gson();
                msg.what = Constants.SUCCESS;
                msg.obj = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
            } catch (Exception e) {
                e.printStackTrace();
                msg.what = Constants.FAILED;
                msg.obj = Utils.convertExceptionToString(e);
            }

            return msg;
        }

        @Override
        protected void onPostExecute(Message s) {
            super.onPostExecute(s);

            if (Constants.SUCCESS == s.what) {
                OrderService.getInstance().addAll((List<Order>)s.obj);
                showOrders();
            } else {
                Toast.makeText(getApplicationContext(), (String)s.obj, Toast.LENGTH_LONG)
                        .show();
            }

            forwardPB.setVisibility(View.GONE);
        }
    }
}
