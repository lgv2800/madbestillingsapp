package com.example.gruppeb.madbestillingsapp.Domain.Dishes;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.gruppeb.madbestillingsapp.R;

public class DishFactory {

    public void createDish(String dish, boolean isLight, Context context){


        /**The order is assigned an unique count, which is incremented each time.
         *
         */
        SharedPreferences orderPref =  context.getSharedPreferences(context.getString(R.string.current_order_pref),Context.MODE_PRIVATE);
        SharedPreferences.Editor orderEdit = orderPref.edit();

        int count = orderPref.getInt("count",0);
        int temp = count;
        temp++;

        orderEdit.putString("id"+count, dish).putBoolean("light_bread"+count, isLight).apply();

        orderEdit.putInt("count", temp).apply();

    }
}
