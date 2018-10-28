package com.example.gruppeb.madbestillingsapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CartScreen extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbarCart;
    private Button mOrderFoodButton;
    private TextView mFoodClearTextView;
    private ImageView mBinOneImageView;
    private ImageView mBinTwoImageView;
    private ImageView mBinThreeImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);
        setTitle("Kurv");

        mToolbarCart = findViewById(R.id.my_CartToolbar);
        setSupportActionBar(mToolbarCart);

        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mOrderFoodButton = (Button) findViewById(R.id.button_order);
        mOrderFoodButton.setOnClickListener(this);

        mBinOneImageView = (ImageView) findViewById(R.id.imageView);
        mBinOneImageView.setImageResource(R.drawable.ic_delete_black_24dp);
        mBinTwoImageView = (ImageView) findViewById(R.id.imageView2);
        mBinTwoImageView.setImageResource(R.drawable.ic_delete_black_24dp);
        mBinThreeImageView = (ImageView) findViewById(R.id.imageView3);
        mBinThreeImageView.setImageResource(R.drawable.ic_delete_black_24dp);

        mFoodClearTextView = (TextView) findViewById(R.id.textView_Clear);

    }

    //@Override
    public void onClick(View v) {
        if (v == mOrderFoodButton) {
            mOrderFoodButton.setBackgroundColor(Color.GRAY);
            mOrderFoodButton.setText("ER BESTILT");
            mBinOneImageView.setVisibility(View.INVISIBLE);
            mBinTwoImageView.setVisibility(View.INVISIBLE);
            mBinThreeImageView.setVisibility(View.INVISIBLE);
            mFoodClearTextView.setVisibility(View.INVISIBLE);
        }
    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
