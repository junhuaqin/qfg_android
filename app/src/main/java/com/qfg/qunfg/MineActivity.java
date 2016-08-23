package com.qfg.qunfg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qfg.qunfg.service.LoginService;

public class MineActivity extends AppCompatActivity {

    private CustomBottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        bottomBar = new CustomBottomBar(2, this, savedInstanceState);
    }

    public void onLogout(View view) {
        LoginService.clearLoggedInStatus(getApplicationContext());
        Intent intent = new Intent(this, SaleActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomBar.selectTab();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }
}
