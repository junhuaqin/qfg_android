package com.qfg.qunfg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.qfg.qunfg.pojo.Product;
import com.qfg.qunfg.service.ProductService;
import com.qfg.qunfg.util.Constants;
import com.qfg.qunfg.util.Formatter;

import java.util.List;

/**
 * Created by rbtq on 8/12/16.
 */
public class AddSaleItem extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String SALE_ITEM = "saleItem";
    public static final String SALE_ITEM_BARCODE = "barCode";
    public static final String SALE_ITEM_TITLE = "title";
    public static final String SALE_ITEM_UNIT_PRICE = "unitPrice";
    public static final String SALE_ITEM_AMOUNT = "amount";

    private TextView barCode;
    private TextView title;
    private TextView unitPrice;
    private TextView amount;

    private ArrayAdapter<Product> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sale_item);

//        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        barCode = (TextView) findViewById(R.id.barCode);
        title = (TextView) findViewById(R.id.itemTitle);
        unitPrice = (TextView) findViewById(R.id.itemUnitPrice);
        amount = (TextView) findViewById(R.id.itemAmount);

        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        AutoCompleteTextView productList = (AutoCompleteTextView) findViewById(R.id.productList);

        productList.setAdapter(adapter);
        productList.setOnItemClickListener(this);

        ProductService.getInstance().getAll(handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constants.LIST_OF_PRODUCT) {
                adapter.clear();
                adapter.addAll((List<Product>)msg.obj);
                adapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_sale_item_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (validate()) {
                    onOK();
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean isEmpty(TextView view) {
        return view.getText().toString().isEmpty();
    }

    private boolean validate() {
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.addSaleItem);

        if (isEmpty(barCode)) {
            String strBC = getResources().getString(R.string.barCode);
//            barCode.setError(getResources().getString(R.string.nullNotAllowed, strBC));
            Snackbar.make(layout, getResources().getString(R.string.nullNotAllowed, strBC), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (isEmpty(title)) {
            String strTitle = getResources().getString(R.string.itemTitle);
//            title.setError(getResources().getString(R.string.nullNotAllowed, strTitle));
            Snackbar.make(layout, getResources().getString(R.string.nullNotAllowed, strTitle), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (isEmpty(unitPrice) || Formatter.currency2bg(Float.valueOf(unitPrice.getText().toString())) <= 0) {
            String strUP = getResources().getString(R.string.unitPrice);
            unitPrice.setError(getResources().getString(R.string.nullNotAllowed, strUP));
            unitPrice.requestFocus();
//            Snackbar.make(layout, getResources().getString(R.string.nullNotAllowed, strUP), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (isEmpty(amount) || Integer.valueOf(amount.getText().toString()) <= 0) {
            String strAmount = getResources().getString(R.string.amount);
            amount.setError(getResources().getString(R.string.nullNotAllowed, strAmount));
            amount.requestFocus();
//            Snackbar.make(layout, getResources().getString(R.string.nullNotAllowed, strAmount), Snackbar.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    private void onOK() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(SALE_ITEM_BARCODE, Integer.valueOf(barCode.getText().toString()));
        bundle.putString(SALE_ITEM_TITLE, title.getText().toString());
        bundle.putInt(SALE_ITEM_UNIT_PRICE, Formatter.currency2bg(Float.valueOf(unitPrice.getText().toString())));
        bundle.putInt(SALE_ITEM_AMOUNT, Integer.valueOf(amount.getText().toString()));
        intent.putExtra(SALE_ITEM, bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Product product = (Product) adapterView.getItemAtPosition(i);
            updateShow(product);
    }

    private void updateShow(Product product) {
        if (null == product) {
            barCode.setText("");
            title.setText("");
            unitPrice.setText("");
        } else {
            barCode.setText(String.format("%d", product.getBarCode()));
            title.setText(product.getTitle());
            unitPrice.setText(String.format("%.2f", Formatter.currency2fg(product.getUnitPrice())));
        }
    }
}
