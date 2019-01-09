package com.example.gruppeb.madbestillingsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import com.example.gruppeb.madbestillingsapp.Connector.Connector;
import com.example.gruppeb.madbestillingsapp.Domain.Dishes.Dish;
import com.example.gruppeb.madbestillingsapp.Domain.Dishes.DishOne;
import com.example.gruppeb.madbestillingsapp.Domain.Order;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    Button mLoginButton;
    EditText mRoomNumberEnterField;

    com.airbnb.lottie.LottieAnimationView loadingAnimation;

    Context mContext;
    AlertDialog statusAlertDialog;
    ProgressDialog progressDialog;

    Connector mConnector; //Database connector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        //https://blog.lokalise.co/android-app-localization/
        // Create a new Locale object
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        // Create a new configuration object
        Configuration config = new Configuration();
        // Set the locale of the new configuration
        config.locale = locale;
        // Update the configuration of the Accplication context
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );*/

        setContentView(R.layout.activity_login_screen);

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);

        loadingAnimation = findViewById(R.id.login_animation_loading);

        mRoomNumberEnterField = findViewById(R.id.login_number);

        //Allows for enter taps on keyboard to play round.
        mRoomNumberEnterField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mLoginButton.performClick();
                    return true;
                }
                return false;
            }
        });

        progressDialog = new ProgressDialog(this);

        //Database
        mConnector = new     Connector();
    }

    private class loginAsyncTaskStatement extends AsyncTask<String, String, String> {
        //Database
        private String roomNumberString = mRoomNumberEnterField.getText().toString();
        private boolean isSuccess = false;

        private String roomNumberQuery;
        private String errorMessage = "";

        @Override
        protected String doInBackground(String... params) {

            if (roomNumberString.trim().equals(""))
                errorMessage = getString(R.string.login_login_message_fillvalue);
            else {
                try {
                    Connection con = mConnector.CONN();
                    if (con == null) {
                        errorMessage = getString(R.string.login_login_message_checkinternet);
                    } else {

                        System.out.println("Forbindelse til DB er aktiv.");

                        //String query = " SELECT * FROM Orders WHERE roomNumber='" + roomNumberString + "'";
                        String query = " SELECT * FROM roomNumber WHERE roomNumber='" + roomNumberString + "'";

                        Statement stmt = con.createStatement();
                        // stmt.executeUpdate(query);

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next())

                        {
                            //i = Placement of column in table
                            roomNumberQuery = rs.getString(1);

                            if (roomNumberQuery.equals(roomNumberString)) {

                                isSuccess = true;
                                errorMessage = getString(R.string.login_login_message_loginsuccesswithroomnumber) + roomNumberString;

                            } else

                                isSuccess = false;
                            //errorMessage = "Loginoplysninger er ukorrekt.";

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
            Order.ROOM_NUMBER = roomNumberString;
            if (isSuccess) {

                Intent intent = new Intent(LoginScreen.this, MainScreen.class);

                startActivity(intent);
            }

            progressDialog.hide();
            setAnimation(false);
        }
    }


    @Override
    public void onClick(View v) {

        if (v == mLoginButton) {
            setAnimation(true);
            loginAsyncTaskStatement mloginAsyncTaskStatement = new loginAsyncTaskStatement();
            mloginAsyncTaskStatement.execute();
        }
    }

    private void setAnimation(Boolean a){
        if (a){
            mLoginButton.setVisibility(View.INVISIBLE);
            loadingAnimation.setVisibility(View.VISIBLE);
        }
        if (!a){
            mLoginButton.setVisibility(View.VISIBLE);
            loadingAnimation.setVisibility(View.INVISIBLE);
        }
    }

}
