package com.example.gruppeb.madbestillingsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gruppeb.madbestillingsapp.Domain.Dishes.Dish;
import com.example.gruppeb.madbestillingsapp.Domain.Order;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Database import statements
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.gruppeb.madbestillingsapp.Connector.Connector;

public class CartScreen extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbarCart;
    ImageView mMainImage;
    ListView mListView;
    Button mOrderCart;
    TextView mDeleteAll;
    ArrayList<Map<String, String>> orderMap;
    Order mOrder;

    private String roomNumberFromIntent;
    private String roomNumberStringFromExtra;
    private int roomNumberStringFromExtraToInt;

    ProgressDialog progressDialog;

    Connector mConnector; //Database connector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrder = new Order();
        if (mOrder.getCount(this)==0){
            setEmptyView();
        }

        else {
            setContentView(R.layout.activity_cart_screen);
            setTitle("Kurv");

            findViewAndClickListener();

            populateItemList();
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                roomNumberFromIntent = null;
            } else {
                roomNumberFromIntent = extras.getString("roomNumber");
                roomNumberStringFromExtra = roomNumberFromIntent;
                roomNumberStringFromExtraToInt = Integer.parseInt(roomNumberStringFromExtra);
            }
        } else {
            roomNumberFromIntent = (String) savedInstanceState.getSerializable("roomNumber");
        }

        progressDialog = new ProgressDialog(this);

        //Database
        mConnector = new Connector();

    }

    //@Override
    public void onClick(View v) {
        if (v == mDeleteAll) {
            mOrder.clearOrder(this);
            setEmptyView();
        } else if (v == mOrderCart) {
            CartToDBAsyncTaskStatement mCartToDBAsyncTaskStatement = new CartToDBAsyncTaskStatement();
            mCartToDBAsyncTaskStatement.execute();
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

    private void populateItemList() {
        orderMap = mOrder.getMap(this);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, orderMap, R.layout.order_list, new String[]{"title", "breadtype"}, new int[]{R.id.cart_list_title, R.id.cart_list_breadtype});

        mListView.setAdapter(simpleAdapter);
    }

    public class CartToDBAsyncTaskStatement extends AsyncTask<String, String, String> {
        private boolean isSuccess = false;
        private String errorMessage = "";

        /*public CartToDBAsyncTaskStatement(Context mContext) {
            //mContext = this.Context;
        }*/

        //Variables of dish
        //private int dishAmount = Order.getCount(mContext);
        private int dishAmount = 1;
        private String breadType = "Lys";
        private String orderMenu = "Laks";

        /*ArrayList <String> itemOrdered = new ArrayList<String>();
        ArrayList <String> breadType = new ArrayList<String>();*/

        //private String itemOrdered = new String[]{"Lys", "Mørk"};


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Indlæser");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            /*
            for (int i = 0; i <= dishAmount,i++){
                //Insert code for inseting into DB, by dish amount.
            }*/

            /*for (String i : itemOrdered) {
                String itemOrdered = itemOrdered.get(i);
                String breadType = breadType.get(i);
            }*/

            if (dishAmount < 0)
                errorMessage = "Bestil venligst mindst én ret.";
            else {
                try {
                    Connection con = mConnector.CONN();
                    if (con == null) {
                        errorMessage = "Tjek venligst din internet forbindelse";
                    } else {
                        String query = " INSERT INTO Orders (roomNumber, orderMenu, breadType) values('" + roomNumberStringFromExtraToInt + "','" + orderMenu + "','" + breadType + "')";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);

                        errorMessage = "Oprettelse af ordre var successfuldt";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    errorMessage = "Exceptions" + ex;
                }
            }
            return errorMessage;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(), "" + errorMessage, Toast.LENGTH_LONG).show();

            if (isSuccess) {
                finish();
            }

            progressDialog.hide();
        }
    }

    private void findViewAndClickListener(){
        mMainImage = findViewById(R.id.cart_mainimage);
        mMainImage.setOnClickListener(this);
        mListView = findViewById(R.id.cart_list);
        mDeleteAll = findViewById(R.id.cart_delete_all);
        mDeleteAll.setOnClickListener(this);
        mOrderCart = findViewById(R.id.button_order);
        mOrderCart.setOnClickListener(this);

        createToolBar();
    }

    private void setEmptyView(){
        setContentView(R.layout.activity_cart_screen_empty);
        com.airbnb.lottie.LottieAnimationView animation = findViewById(R.id.cart_empty_animation);
        animation.setOnClickListener(v-> Toast.makeText(this, "Du har ikke bestilt noget", Toast.LENGTH_SHORT).show());
        createToolBar();
    }

    private void createToolBar(){
        mToolbarCart = findViewById(R.id.my_CartToolbar);
        setSupportActionBar(mToolbarCart);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

}
