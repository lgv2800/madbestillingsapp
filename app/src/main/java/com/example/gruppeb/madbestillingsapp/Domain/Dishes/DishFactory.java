package com.example.gruppeb.madbestillingsapp.Domain.Dishes;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.gruppeb.madbestillingsapp.R;

public class DishFactory {
private SharedPreferences countPref;
private SharedPreferences.Editor orderPref, countEdit;

    public Dish createDish(int dishNumber, Context context){


        /*The order is assigned an unique count, which is incremented each time.
         *
         */
        countPref = context.getSharedPreferences(context.getString(R.string.current_order_count),Context.MODE_PRIVATE);
        countEdit = countPref.edit();

        int count = countPref.getInt("count",0);
        int temp = count;
        temp++;

        orderPref = context.getSharedPreferences(context.getString(R.string.current_order_pref),Context.MODE_PRIVATE).edit();
        Dish a;
        switch(dishNumber){
            case 0: orderPref.putString(context.getString(R.string.page1_food_title),context.getString(count)).apply(); a = new DishOne(); return a;
            case 1: orderPref.putString(context.getString(R.string.page2_food_title),context.getString(count)).apply(); a = new DishTwo(); return a;
            case 2: orderPref.putString(context.getString(R.string.page3_food_title),context.getString(count)).apply(); a = new DishThree(); return a;
            case 3: orderPref.putString(context.getString(R.string.page4_food_title),context.getString(count)).apply(); a = new DishFour(); return a;
            case 4: orderPref.putString(context.getString(R.string.page5_food_title),context.getString(count)).apply(); a = new DishFive(); return a;
        }

        countEdit.putInt("count", temp).apply();

        return null;
    }

}
