package com.example.gruppeb.madbestillingsapp.FoodFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gruppeb.madbestillingsapp.R;

public class Page1 extends Fragment implements View.OnClickListener {


    public Page1() {
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_page1, container, false);

        return view;
    }
    @Override
    public void onClick(final View v){

    }
}
