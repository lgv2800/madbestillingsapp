package com.example.gruppeb.madbestillingsapp.Domain.Dishes;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.gruppeb.madbestillingsapp.R;

public class DishFactory {

    public void createDish(int dishNumber, boolean isLight, Context context){


        /**The order is assigned an unique count, which is incremented each time.
         *
         */
        SharedPreferences orderPref =  context.getSharedPreferences(context.getString(R.string.current_order_pref),Context.MODE_PRIVATE);
        SharedPreferences.Editor orderEdit = orderPref.edit();

        int count = orderPref.getInt("count",0);
        int temp = count;
        temp++;

        switch(dishNumber){
            case 0: orderEdit.putString("id"+count, context.getString(R.string.page1_food_title)).putBoolean("light_bread"+count, isLight).apply(); break;
            case 1: orderEdit.putString("id"+count, context.getString(R.string.page2_food_title)).putBoolean("light_bread"+count, isLight).apply(); break;
            case 2: orderEdit.putString("id"+count, context.getString(R.string.page3_food_title)).putBoolean("light_bread"+count, isLight).apply(); break;
            case 3: orderEdit.putString("id"+count, context.getString(R.string.page4_food_title)).putBoolean("light_bread"+count, isLight).apply(); break;
            case 4: orderEdit.putString("id"+count, context.getString(R.string.page5_food_title)).putBoolean("light_bread"+count, isLight).apply(); break;
        }

        orderEdit.putInt("count", temp).apply();

    }
}
