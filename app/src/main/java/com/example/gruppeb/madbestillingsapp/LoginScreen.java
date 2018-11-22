package com.example.gruppeb.madbestillingsapp;

import android.content.Intent;
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

import com.example.gruppeb.madbestillingsapp.Connector.BackgroundWorker;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener{

Button mLoginButton;
EditText mRoomNumberEnterField;

private String roomNumber;
private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mLoginButton = findViewById(R.id.login_button);
        mRoomNumberEnterField = findViewById(R.id.login_number);
        mLoginButton.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {

        if (v == mLoginButton){
            loginWithRoomNumber();
        }
    }

    public void loginWithRoomNumber() {
        roomNumber = mRoomNumberEnterField.getText().toString();

        type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, roomNumber);

        Intent i = new Intent(LoginScreen.this, MainScreen.class);
        i.putExtra("credentials", roomNumber);
        startActivity(i);

    }

}
