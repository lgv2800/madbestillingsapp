package com.example.gruppeb.madbestillingsapp.FoodFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gruppeb.madbestillingsapp.Domain.BreadType;
import com.example.gruppeb.madbestillingsapp.MainScreen;
import com.example.gruppeb.madbestillingsapp.R;

public class Page1 extends Fragment implements View.OnClickListener {

    Chip mLight, mDark;
    private BreadType listener;

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

        //default breadtype set checked
        mDark.setChecked(true);

        return view;

    }

    @Override
    public void onClick(final View v) {
        if (v == mLight) {
            listener.setBreadType(true);
            mLight.setChecked(true);
            mDark.setChecked(false);
        }
        if (v == mDark) {
            listener.setBreadType(false);
            mDark.setChecked(true);
            mLight.setChecked(false);
        }

    }

    /**
     * Gør det muligt at implementere et interface, som kan anvendes i Mainscreen.java, således der er lav coupling.
     * <p>
     * https://stackoverflow.com/questions/12659747/call-an-activity-method-from-a-fragment
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (BreadType) context;
        } catch (ClassCastException castException) {
            //
        }
    }


}
