package com.qfg.qunfg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karics.library.zxing.android.CaptureActivity;
import com.qfg.qunfg.pojo.Order;
import com.qfg.qunfg.pojo.SaleItem;
import com.qfg.qunfg.service.HttpService;
import com.qfg.qunfg.service.LoginService;
import com.qfg.qunfg.service.SaleItemService;
import com.qfg.qunfg.util.Constants;
import com.qfg.qunfg.util.Formatter;
import com.qfg.qunfg.util.Utils;

import java.lang.ref.WeakReference;

public class SaleActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN = 0;
    private static final int PICK_SALE_ITEM = 1;

    private RecyclerView mRecyclerView;
    private KeyValueAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomBottomBar bottomBar;
    private TextView totalPrice;
    private Button submit;
    private CoordinatorLayout layout;
    private ProgressBar progressBar;
    private ProgressBar staticsPB;
    private TextView curMon;
    private TextView curDay;
    private TextView showOrders;

    private WeakReference<RefreshStatics> refreshStaticsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        totalPrice = (TextView) findViewById(R.id.total_price);
        submit = (Button) findViewById(R.id.submit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        staticsPB = (ProgressBar) findViewById(R.id.staticsProgressBar);
        layout = (CoordinatorLayout) findViewById(R.id.sale_tab);
        curMon = (TextView) findViewById(R.id.curMon);
        curDay = (TextView) findViewById(R.id.curDay);
        showOrders = (TextView) findViewById(R.id.showOrders);

        bottomBar = new CustomBottomBar(0, this, savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new KeyValueAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void gotoLogin() {
        Intent intent = new Intent(SaleActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void refreshStatic() {
        if (null == refreshStaticsReference || null == refreshStaticsReference.get()) {
            if (!HttpService.isOnline(this)) {
                Snackbar.make(layout, getResources().getString(R.string.notConnectInternet), Snackbar.LENGTH_LONG).show();
                return;
            }

            RefreshStatics statics = new RefreshStatics();
            refreshStaticsReference = new WeakReference<>(statics);
            statics.execute();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!LoginService.isLoggedIn(getApplicationContext())) {
            gotoLogin();
        } else {
            bottomBar.selectTab();
            mAdapter.setData(SaleItemService.getInstance().getAll());
            mAdapter.notifyDataSetChanged();
            submit.setEnabled(mAdapter.getItemCount() > 0);

            totalPrice.setText(getResources().getString(R.string.totalPrice,
                    Formatter.formatCurrency(Formatter.currency2fg(SaleItemService.getInstance().getTotalPrice()))));
            refreshStatic();
        }
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

    public void onScan(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    public void onAddSaleItem(View v) {
        Intent intent = new Intent(this, AddSaleItem.class);
        startActivityForResult(intent, PICK_SALE_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_SALE_ITEM && resultCode == RESULT_OK) {
            SaleItem saleItem = new SaleItem();
            Bundle bundle = data.getBundleExtra(AddSaleItem.SALE_ITEM);
            saleItem.setBarCode(bundle.getInt(AddSaleItem.SALE_ITEM_BARCODE))
                    .setTitle(bundle.getString(AddSaleItem.SALE_ITEM_TITLE))
                    .setUnitPrice(bundle.getInt(AddSaleItem.SALE_ITEM_UNIT_PRICE))
                    .setCount(bundle.getInt(AddSaleItem.SALE_ITEM_AMOUNT));
            SaleItemService.getInstance().add(saleItem);
        } else if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
                // 扫描二维码/条码回传
                if (data != null) {
                    String content = data.getStringExtra(CaptureActivity.CODED_CONTENT_KEY);
                    int index = content.lastIndexOf('/');
                    if (index <= 0) {
                        Toast.makeText(this, "无效的二维码", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(this, AddSaleItem.class);
                        intent.putExtra(AddSaleItem.QR_CODE, content.substring(index+1));
                        startActivityForResult(intent, PICK_SALE_ITEM);
                    }
                }
            }
    }

    public void onSubmit(View view) {
        if (!HttpService.isOnline(this)) {
            Snackbar.make(layout, getResources().getString(R.string.notConnectInternet), Snackbar.LENGTH_LONG).show();
            return;
        }
        Order order = new Order();
        order.setTotalPrice(SaleItemService.getInstance().getTotalPrice());
        order.setItems(SaleItemService.getInstance().getAll());
        (new SubmitSale()).execute(order);
    }

    private void clearSaleItems() {
        SaleItemService.getInstance().clear();
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        totalPrice.setText(getResources().getString(R.string.totalPrice, Formatter.formatCurrency(0)));
        submit.setEnabled(false);
    }

    public void onCancel(View view) {
        clearSaleItems();
    }

    public void onShowOrders(View view) {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }

    private class SubmitSale extends AsyncTask<Order, Void, Message> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            submit.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Message doInBackground(Order... collections) {
            Message msg = new Message();
            Gson gson = new Gson();
            String json = gson.toJson(collections[0]);
            try {
                HttpService.post("/orders/add", json);
                msg.what = Constants.SUCCESS;
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
            submit.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            if (Constants.SUCCESS == s.what) {
                Snackbar.make(layout, getResources().getString(R.string.submitSuccess), Snackbar.LENGTH_LONG).show();
                clearSaleItems();
                refreshStatic();
            } else {
                Snackbar.make(layout, (String)s.obj, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class RefreshStatics extends AsyncTask<Void, Void, Message> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            curMon.setVisibility(View.GONE);
            curDay.setVisibility(View.GONE);
            showOrders.setVisibility(View.GONE);
            staticsPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected Message doInBackground(Void... orders) {
            Message msg = new Message();

            try {
                String result = HttpService.get("/orders/statics");
                Gson gson = new Gson();
                msg.what = Constants.SUCCESS;
                msg.obj = gson.fromJson(result, Order.Statics.class);
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
                Order.Statics statics = (Order.Statics)s.obj;
                curMon.setText(Formatter.formatCurrency(Formatter.currency2fg(statics.getCurMonth())));
                curDay.setText(getResources().getString(R.string.curDayTotalSale,
                               Formatter.formatCurrency(Formatter.currency2fg(statics.getCurDay()))));
            } else {
                Snackbar.make(layout, (String)s.obj, Snackbar.LENGTH_LONG).show();
            }

            staticsPB.setVisibility(View.GONE);
            curMon.setVisibility(View.VISIBLE);
            curDay.setVisibility(View.VISIBLE);
            showOrders.setVisibility(View.VISIBLE);
        }
    }
}
