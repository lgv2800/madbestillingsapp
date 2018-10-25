package com.example.gruppeb.madbestillingsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        String i = getIntent().getExtras().getString("credentials");
        Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();

    }
}
