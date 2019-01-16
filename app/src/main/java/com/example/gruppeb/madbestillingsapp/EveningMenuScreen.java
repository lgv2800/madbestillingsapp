package com.example.gruppeb.madbestillingsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

public class EveningMenuScreen extends AppCompatActivity {

    Toolbar mToolbarOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evening_menu_screen);
        setTitle(getString(R.string.evening_menu_title));

        //HTML help screen
        /*
        String hjælpHtml = "<html><body>"
                + "<h1>Hj&aelig;lpesk&aelig;rm</h1>"
                + "<p>Her kunne st&aring; noget hj&aelig;lp.<br>Men den er ikke skrevet endnu.</p>";

        WebView wv = new WebView(this);
        wv.loadData(hjælpHtml, "text/html", null);
        setContentView(wv);*/

        mToolbarOrders = findViewById(R.id.my_OrdersToolbar);
        setSupportActionBar(mToolbarOrders);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
