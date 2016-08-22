package com.qfg.qunfg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qfg.qunfg.pojo.Product;
import com.qfg.qunfg.service.HttpService;
import com.qfg.qunfg.service.ProductService;
import com.qfg.qunfg.util.Constants;
import com.qfg.qunfg.util.Utils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by rbtq on 8/22/16.
 */
public class StoresActivity extends AppCompatActivity {

    private BottomBar bottomBar;
    private CoordinatorLayout layout;
    private ProgressBar refreshPB;
    private WeakReference<RefreshStore> refreshStoreWeakReference;
    private ProductsListAdapter productArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        layout = (CoordinatorLayout) findViewById(R.id.store_tab);
        refreshPB = (ProgressBar) findViewById(R.id.progressBar);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItems(R.menu.bottom_navagation);
        bottomBar.setActiveTabColor(ContextCompat.getColor(this, R.color.colorBlue));
        bottomBar.selectTabAtPosition(1, true);

        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int itemId) {
                switch (itemId) {
                    case R.id.sale_item:
                        Intent intent = new Intent(StoresActivity.this, SaleActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.store_item:
                        break;
                    case R.id.my_item:
//                        Snackbar.make(layout, "Location Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        productArrayAdapter = new ProductsListAdapter(this, android.R.layout.simple_list_item_1);
        ((ListView)findViewById(R.id.productStore)).setAdapter(productArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (productArrayAdapter.getCount() == 0) {
            refreshStore();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

    private void refreshStore() {
        if (null == refreshStoreWeakReference || null == refreshStoreWeakReference.get()) {
            if (!HttpService.isOnline(this)) {
                Snackbar.make(layout, getResources().getString(R.string.notConnectInternet), Snackbar.LENGTH_LONG).show();
                return;
            }

            RefreshStore refreshStore = new RefreshStore();
            refreshStoreWeakReference = new WeakReference<RefreshStore>(refreshStore);
            refreshStore.execute();
        }
    }

    private void showStores() {
        productArrayAdapter.clear();
        productArrayAdapter.addAll(ProductService.getInstance().getAll());
        productArrayAdapter.notifyDataSetChanged();
    }

    private class RefreshStore extends AsyncTask<Void, Void, Message> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected Message doInBackground(Void... orders) {
            Message msg = new Message();

            try {
                String result = HttpService.get("/products");
                Gson gson = new Gson();
                msg.what = Constants.SUCCESS;
                msg.obj = gson.fromJson(result, new TypeToken<List<Product>>(){}.getType());
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
                ProductService.getInstance().setAll((List<Product>) s.obj);
                showStores();
            } else {
                Snackbar.make(layout, (String)s.obj, Snackbar.LENGTH_LONG).show();
            }

            refreshPB.setVisibility(View.GONE);
        }
    }
}