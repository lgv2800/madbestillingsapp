package com.example.gruppeb.madbestillingsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

//import com.example.gruppeb.madbestillingsapp.Connector.BackgroundWorker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.gruppeb.madbestillingsapp.Connector.Connector;
import com.example.gruppeb.madbestillingsapp.Domain.Dish;
import com.example.gruppeb.madbestillingsapp.Domain.DishOne;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    Button mLoginButton;
    EditText mRoomNumberEnterField;

    Context mContext;
    AlertDialog statusAlertDialog;
    ProgressDialog progressDialog;

    private String roomNumber;
    private String type;
    private Toast mToast;
    private String recResult;

    //Database class
    Connector mConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);

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
        mConnector = new Connector();
    }

    private class loginAsyncTaskStatement extends AsyncTask<String, String, String> {
        //Database
        private String roomNumberString = mRoomNumberEnterField.getText().toString();
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
            if (roomNumberString.trim().equals(""))
                errorMessage = "Udfyld venligst feltet";
            else {
                try {
                    Connection con = mConnector.CONN();
                    if (con == null) {
                        errorMessage = "Tjek venligst din internet forbindelse";
                    } else {

                        System.out.println("Forbindelse til DB er aktiv.");

                        String query = " SELECT * FROM Orders WHERE roomNumber='" + roomNumberString + "'";

                        Statement stmt = con.createStatement();
                        // stmt.executeUpdate(query);

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next())

                        {
                            //i = Placement of column in table
                            roomNumberQuery = rs.getString(2);

                            if (roomNumberQuery.equals(roomNumberString)) {

                                isSuccess = true;
                                errorMessage = "Logget ind med rum-nr: " + roomNumberString;

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

            if (isSuccess) {

                Intent intent = new Intent(LoginScreen.this, MainScreen.class);

                intent.putExtra("roomNumber", roomNumberString);

                startActivity(intent);
            }

            progressDialog.hide();

        }
    }


    @SuppressLint("StaticFieldLeak")
    public void loginAsyncTask() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String type = params[0];
                //String login_url = "http://localhost:8888/Projekter/Madbestillingsapp%20-%20Webportal/userLogin.php";
                String login_url = "http://192.168.0.129:8888/Projekter/Madbestillingsapp%20-%20Webportal/userLogin.php";
                //String login_url = "http://10.16.161.90:8888/Projekter/Madbestillingsapp%20-%20Webportal/userLogin.php";

                if (type.equals("login")) {
                    try {
                        String roomNumber = params[1];
                        URL url = new URL(login_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);

                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String postData = URLEncoder.encode("roomNumber", "UTF-8") + "=" + URLEncoder.encode(roomNumber, "UTF-8");
                        bufferedWriter.write(postData);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();

                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                        recResult = "";
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            recResult += line;
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                        return recResult;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                statusAlertDialog = new AlertDialog.Builder(mContext).create();
                statusAlertDialog.setTitle("Login status");
            }

            @Override
            protected void onPostExecute(final String recResult) {
                statusAlertDialog.setMessage(recResult);
                statusAlertDialog.show();
                System.out.println(recResult);
            }

        }.execute(type, roomNumber);
    }

    public void loginWithRoomNumber() {
        roomNumber = mRoomNumberEnterField.getText().toString();

        type = "login";

        loginAsyncTask();

        /*
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, roomNumber);*/

        /*
        if (roomNumber.equals("333")) {
            recResult = "Rum-nr fundet";
        } else if (roomNumber.equals("444")) {
            recResult = "Rum-nr ej fundet";
        }*/

        //String recResult = "Rum-nr ej fundet";

        System.out.println("LoginScreen før equals: " + recResult);

        if (recResult.equals("Rum-nr fundet")) {
            Intent i = new Intent(LoginScreen.this, MainScreen.class);
            i.putExtra("credentials", roomNumber);
            startActivity(i);
        } else if (recResult.equals("Rum-nr ej fundet")) {
            mToast.makeText(this, "Rum-nr ej fundet. Prøv igen", Toast.LENGTH_SHORT).show();
        }

        System.out.println("LoginScreen efter equals: " + recResult);

    }

    @Override
    public void onClick(View v) {

        if (v == mLoginButton) {
            //loginWithRoomNumber();
            loginAsyncTaskStatement mloginAsyncTaskStatement = new loginAsyncTaskStatement();
            mloginAsyncTaskStatement.execute();
        }
    }

}
