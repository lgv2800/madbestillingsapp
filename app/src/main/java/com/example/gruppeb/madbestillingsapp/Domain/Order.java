package com.example.gruppeb.madbestillingsapp.Domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gruppeb.madbestillingsapp.Domain.Dishes.DishFactory;
import com.example.gruppeb.madbestillingsapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private DishFactory factory = new DishFactory();
    private ArrayList<String> orderItems;
    private ArrayList<String> orderItemsBreadType;
    ArrayList<Map<String, String>> orderMap;

    public Order(){
        orderItems = new ArrayList<>();
    }

    public void clearOrder(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.current_order_pref),0).edit();
        editor.clear().apply();
    }

    public void order(int a, boolean isLight, Context context){
        factory.createDish(a, isLight ,context);
    }

    public ArrayList<String> getOrder(Context context){
        createOrderList(context);
        return orderItems;
    }

    public ArrayList<Map<String, String>> getMap(Context context){
        createOrderList(context);
        getOrderItemsBreadType(context);
        orderMap = new ArrayList<>();
        generateMap();
        return orderMap;
    }

    private void createOrderList(Context context){
        SharedPreferences orderPref = context.getSharedPreferences(context.getString(R.string.current_order_pref), Context.MODE_PRIVATE);
        orderItems = new ArrayList<>();

        int count = orderPref.getInt("count", 0);

        for (int i = 0; i < count; i++){
            orderItems.add(orderPref.getString("id"+i, "No order placed"));
        }

    }

    private void getOrderItemsBreadType(Context context){
        SharedPreferences orderPref = context.getSharedPreferences(context.getString(R.string.current_order_pref),Context.MODE_PRIVATE);
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
    }

    private void generateMap(){
        for (int i = 0; i < orderItems.size(); i++){
            Map<String, String> listMap = new HashMap<>();
            listMap.put("title", orderItems.get(i));
            listMap.put("breadtype", orderItemsBreadType.get(i));
            orderMap.add(listMap);
        }
    }

}
