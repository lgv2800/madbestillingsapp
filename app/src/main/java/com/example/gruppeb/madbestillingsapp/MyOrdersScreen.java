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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gruppeb.madbestillingsapp.Connector.Connector;
import com.example.gruppeb.madbestillingsapp.Domain.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class MyOrdersScreen extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbarOrders;
    TextView mNumberOfOrdersCountTextView;

    private String strDate;
    private String roomNumberFromIntent;
    private String roomNumberStringFromExtra;
    private int roomNumberStringFromExtraToInt;
    private int numberOfOrdersInDB;

    private ImageView page1_image;

    ListView mMyOrdersListView;
    ArrayList<String> dishOrderMenusByRoomNumber;

    Context mContext = MyOrdersScreen.this;
    ProgressDialog progressDialog;

    Connector mConnector; //Database connector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_screen);
        setTitle(getString(R.string.my_orders_title));

        mToolbarOrders = findViewById(R.id.my_OrdersToolbar);
        setSupportActionBar(mToolbarOrders);

        mMyOrdersListView = findViewById(R.id.listView_MyOrdersListView);

        mNumberOfOrdersCountTextView = findViewById(R.id.textView_numberOfOrdersCount);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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

        dishOrderMenusByRoomNumber = new ArrayList<String>();

        //Database
        mConnector = new Connector();

        MyOrdersScreen.MyOrdersListAsyncTaskStatement mMyOrdersListAsyncTaskStatement = new MyOrdersScreen.MyOrdersListAsyncTaskStatement();
        mMyOrdersListAsyncTaskStatement.execute();

    }

    //@Override
    public void onClick(View v) {

    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //https://gist.githubusercontent.com/pacificregmi/00927e29c4c0f9eae414/raw/eff50094306248331fdf570e3c5f13e57fe85b69/MainActivity.java
    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-d");
        strDate = mdformat.format(calendar.getTime());
        strDate += "%";
        System.out.print(strDate);
    }

    private class MyOrdersListAsyncTaskStatement extends AsyncTask<String, String, String> {
        private String numberOfOrdersInDBStringFromQuery;
        private String orderMenuInDBStringFromQuery;
        private String errorMessage = "";
        private boolean isSuccess = false;

        private int conditionInt = 1;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Indlæser");
            progressDialog.show();
            getCurrentDate();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (conditionInt < 0)
                errorMessage = getString(R.string.login_login_message_errorhashappend);
            else {
                try {
                    Connection con = mConnector.CONN();
                    if (con == null) {
                        errorMessage = getString(R.string.login_login_message_checkinternet);
                    } else {

                        System.out.println("Forbindelse til DB er aktiv.");

                        //String query = " SELECT COUNT(*) FROM Orders WHERE roomNumber='" + Order.ROOM_NUMBER + "'";
                        //

                        //String query = " SELECT * FROM Orders WHERE roomNumber='" + Order.ROOM_NUMBER + "'";

                        String query = " SELECT * FROM Orders WHERE roomNumber='" + Order.ROOM_NUMBER + "' AND orderDate LIKE '" + strDate + "'";

                        //String query = " SELECT * FROM Orders WHERE roomNumber='" + Order.ROOM_NUMBER + "'";

                        Statement stmt = con.createStatement();

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            //i = Placement of column in table
                            //roomNumberQuery = rs.getString(2);
                            orderMenuInDBStringFromQuery = rs.getString(3); //orderMenu
                            dishOrderMenusByRoomNumber.add(orderMenuInDBStringFromQuery);
                            //numberOfOrdersInDBStringFromQuery = rs.getString(4); //breadType

                            isSuccess = true;

                        }

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
                setAdapter();
            }

            progressDialog.hide();
        }
    }

    private void setAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_orders_list, dishOrderMenusByRoomNumber);
        mMyOrdersListView.setAdapter(adapter);
    }

}
