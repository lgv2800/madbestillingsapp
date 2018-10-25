package com.example.gruppeb.madbestillingsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener{

Button mLoginButton;
EditText mEnterField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mLoginButton = findViewById(R.id.login_button);
        mEnterField = findViewById(R.id.login_number);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == mLoginButton){
            String enteredValue = mEnterField.getText().toString();

            Intent i = new Intent(LoginScreen.this, MainScreen.class);
            i.putExtra("credentials", enteredValue);
            startActivity(i);
        }
    }
}
