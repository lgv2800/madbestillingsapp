package com.example.gruppeb.madbestillingsapp.Domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gruppeb.madbestillingsapp.Domain.Dishes.Dish;
import com.example.gruppeb.madbestillingsapp.Domain.Dishes.DishCounter;
import com.example.gruppeb.madbestillingsapp.Domain.Dishes.DishFactory;
import com.example.gruppeb.madbestillingsapp.R;

import java.util.ArrayList;

public class Order {

    private DishFactory factory = new DishFactory();
    private ArrayList<String> orderItems;
    private DishCounter counter = new DishCounter();

    public Order(){
        orderItems = new ArrayList<>();
    }

    public void clearOrder(){
        orderItems.clear();
    }

    public void order(int a, Context context){
        factory.createDish(a, context);
        counter.addDish(a);
    }

    public ArrayList<String> getOrder(Context context){
        createOrderList(context);
        return orderItems;
    }

    private void createOrderList(Context context){
        SharedPreferences orderPref = context.getSharedPreferences(context.getString(R.string.current_order_pref), Context.MODE_PRIVATE);
        orderItems = new ArrayList<>();

        int count = orderPref.getInt("count", 0);

        for (int i = 0; i < count; i++){
            orderItems.add(orderPref.getString("id"+i, "No order placed"));
        }

    }

}
