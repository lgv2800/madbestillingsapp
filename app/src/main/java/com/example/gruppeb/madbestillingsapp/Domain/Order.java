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
    private ArrayList<String> orderItemsBreadType;
    private DishCounter counter = new DishCounter();

    public Order(){
        orderItems = new ArrayList<>();
    }

    public void clearOrder(){
        orderItems.clear();
    }

    public void order(int a, boolean isLight, Context context){
        factory.createDish(a, isLight ,context);
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

    public ArrayList<String> getOrderItemsBreadType(Context context){
        SharedPreferences orderPref = context.getSharedPreferences("curent_order_pref",Context.MODE_PRIVATE);
        orderItemsBreadType = new ArrayList<>();

        int count = orderPref.getInt("count",0);

        /** increments through the order, if the order is true (meaning it's light bread), a String res will be added to the arraylist
         *
         */
        for (int i =0; i < count; i++){
            if (orderPref.getBoolean("light_bread"+i, false)){
                orderItemsBreadType.add(context.getString(R.string.breadtype_light));
            }
            else if (!orderPref.getBoolean("light_bread"+i, false)){
                orderItemsBreadType.add(context.getString(R.string.breadtype_dark));
            }
        }

        return orderItemsBreadType;
    }

}
