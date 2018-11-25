package com.example.gruppeb.madbestillingsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.example.gruppeb.madbestillingsapp.Domain.Dishes.Dish;
import com.example.gruppeb.madbestillingsapp.Domain.Order;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartScreen extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbarCart;
    ImageView mMainImage;
    ArrayList<String> orderItems;
    ArrayList<String> orderItemsBreadType;
    ListView mListView;

    ArrayList<Map<String, String>> orderMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);
        setTitle("Kurv");
        mListView = findViewById(R.id.cart_list);

        populateItemList();

        mMainImage = findViewById(R.id.cart_mainimage);
        mMainImage.setOnClickListener(this);

        mToolbarCart = findViewById(R.id.my_CartToolbar);
        setSupportActionBar(mToolbarCart);


        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    //@Override
    public void onClick(View v) {
    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateItemList(){
        Order mOrder = new Order();

        orderMap = mOrder.getMap(this);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, orderMap, R.layout.order_list, new String[]{"title", "breadtype"}, new int[]{R.id.cart_list_title, R.id.cart_list_breadtype});

        mListView.setAdapter(simpleAdapter);
    }

}
