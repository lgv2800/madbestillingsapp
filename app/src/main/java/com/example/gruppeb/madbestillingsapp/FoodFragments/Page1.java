package com.example.gruppeb.madbestillingsapp.FoodFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        Button lystBrød = (Button) view.findViewById(R.id.button2);
        lystBrød.setOnClickListener(this);
        Button mørktBrød = (Button) view.findViewById(R.id.button3);
        mørktBrød.setOnClickListener(this);


        return view;
    }
    @Override
    public void onClick(final View v){
        Button lystBrød = getView().findViewById(R.id.button2);
        Button mørktBrød = getView().findViewById(R.id.button3);
        switch (v.getId()){
            case R.id.button2:
                lystBrød.setTextColor(Color.WHITE);
                lystBrød.setBackgroundColor(Color.BLACK);
                mørktBrød.setTextColor(Color.BLACK);
                mørktBrød.setBackgroundColor(Color.WHITE);
                break;
            case R.id.button3:
                mørktBrød.setTextColor(Color.WHITE);
                mørktBrød.setBackgroundColor(Color.BLACK);
                lystBrød.setTextColor(Color.BLACK);
                lystBrød.setBackgroundColor(Color.WHITE);
                break;
        }
    }
}
