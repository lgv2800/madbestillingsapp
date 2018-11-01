package com.example.gruppeb.madbestillingsapp.FoodFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gruppeb.madbestillingsapp.R;

public class Page1 extends Fragment implements View.OnClickListener {

    Chip mLight, mDark;

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
        mLight = view.findViewById(R.id.page1_lightbread);
        mDark = view.findViewById(R.id.page1_darkbread);

        mLight.setOnClickListener(this);
        mDark.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(final View v){
        if (v == mLight) {
            mLight.setChecked(true);
            mDark.setChecked(false);
        }
        if (v == mDark) {
            mDark.setChecked(true);
            mLight.setChecked(false);
        }

    }
}
