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
import android.widget.TextView;
import android.widget.Toast;

import com.example.gruppeb.madbestillingsapp.Connector.Connector;
import com.example.gruppeb.madbestillingsapp.Domain.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyOrdersScreen extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbarOrders;
    TextView mNumberOfOrdersCountTextView;

    private String roomNumberFromIntent;
    private String roomNumberStringFromExtra;
    private int roomNumberStringFromExtraToInt;
    private int numberOfOrdersInDB;

    Context mContext;
    AlertDialog statusAlertDialog;
    ProgressDialog progressDialog;

    Connector mConnector; //Database connector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_screen);
        setTitle(getString(R.string.my_orders_title));

        mToolbarOrders = findViewById(R.id.my_OrdersToolbar);
        setSupportActionBar(mToolbarOrders);

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

        //Database
        mConnector = new Connector();

        MyOrdersScreen.NumOfOrdersByRoomNumAsyncStatement mNumOfOrdersByRoomNumAsyncStatement = new MyOrdersScreen.NumOfOrdersByRoomNumAsyncStatement();
        mNumOfOrdersByRoomNumAsyncStatement.execute();

        MyOrdersScreen.MyOrdersListAsyncTaskStatement mMyOrdersListAsyncTaskStatement = new MyOrdersScreen.MyOrdersListAsyncTaskStatement();
        mMyOrdersListAsyncTaskStatement.execute();
    }
    
    //@Override
    public void onClick(View v) {
        //if (v == mDeleteAll) {
        //}
    }

    //https://developer.android.com/training/appbar/actions#java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class NumOfOrdersByRoomNumAsyncStatement extends AsyncTask<String, String, String> {
        private String numberOfOrdersInDBStringFromQuery;
        private String errorMessage = "";
        private boolean isSuccess = false;

        private int conditionInt = 1;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Indlæser");
            progressDialog.show();

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

                        String query = " SELECT COUNT(*) FROM Orders WHERE roomNumber='" + Order.ROOM_NUMBER + "'";

                        Statement stmt = con.createStatement();

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next())

                        {
                            //i = Placement of column in table
                            numberOfOrdersInDBStringFromQuery = rs.getString(1);
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
                ;
                //numberOfOrdersInDBStringFromQuery =
                numberOfOrdersInDB = Integer.parseInt(numberOfOrdersInDBStringFromQuery);
                mNumberOfOrdersCountTextView.setText("Antallet af ordre på rum-nr: " + Order.ROOM_NUMBER + " er: " + numberOfOrdersInDB);
            }

            progressDialog.hide();
        }
    }

    private class MyOrdersListAsyncTaskStatement extends AsyncTask<String, String, String> {
        //Database
        private boolean isSuccess = false;

        private String roomNumberQuery;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Indlæser");
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (numberOfOrdersInDB < 0)
                errorMessage = "Du har ingen retter bestilt.";
            else {
                try {
                    Connection con = mConnector.CONN();
                    if (con == null) {
                        errorMessage = getString(R.string.login_login_message_checkinternet);
                    } else {

                        System.out.println("Forbindelse til DB er aktiv.");

                        String query = " SELECT * FROM Orders WHERE roomNumber='" + Order.ROOM_NUMBER + "'";

                        Statement stmt = con.createStatement();

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next())

                        {
                            //i = Placement of column in table
                            roomNumberQuery = rs.getString(2);


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

            }

            progressDialog.hide();

        }
    }

}
