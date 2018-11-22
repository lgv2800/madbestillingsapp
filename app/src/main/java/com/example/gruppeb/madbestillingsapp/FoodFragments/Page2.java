package com.example.gruppeb.madbestillingsapp.FoodFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gruppeb.madbestillingsapp.Domain.BreadType;
import com.example.gruppeb.madbestillingsapp.R;

public class Page2 extends Fragment implements View.OnClickListener{

    Chip mLight, mDark;
    private BreadType listener;

    public Page2() {
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_page2, container, false);
        mLight = view.findViewById(R.id.page2_lightbread);
        mDark = view.findViewById(R.id.page2_darkbread);

        mLight.setOnClickListener(this);
        mDark.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(final View v){
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

    /**Gør det muligt at implementere et interface, som kan anvendes i Mainscreen.java, således der er lav coupling.
     *
     * https://stackoverflow.com/questions/12659747/call-an-activity-method-from-a-fragment
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
