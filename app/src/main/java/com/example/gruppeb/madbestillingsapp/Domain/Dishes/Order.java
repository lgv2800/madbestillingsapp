package com.example.gruppeb.madbestillingsapp.Domain.Dishes;

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
    private ArrayList<Map<String, String>> orderMap;

    public static String ROOM_NUMBER;

    public Order(){
        orderItems = new ArrayList<>();
    }

    public void clearOrder(Context context, String preference){
        SharedPreferences.Editor editor = context.getSharedPreferences(preference,0).edit();
        editor.clear().apply();
    }

    public void order(String dish, String dishDanish, boolean isLight, Context context){
        factory.createDish(dish, dishDanish, isLight ,context);
    }

    public ArrayList<String> getOrder(Context context, String preference){
        createOrderList(context, preference);
        return orderItems;
    }

    public ArrayList<String> getBreadType(Context context, String preference){
        getOrderItemsBreadType(context, preference);
        return orderItemsBreadType;
    }

    public ArrayList<Map<String, String>> getMap(Context context, String preference){
        createOrderList(context, preference);
        getOrderItemsBreadType(context, preference);
        orderMap = new ArrayList<>();
        generateMap();
        return orderMap;
    }

    private void createOrderList(Context context, String preference){
        SharedPreferences orderPref = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        orderItems = new ArrayList<>();

        int count = orderPref.getInt("count", 0);

        for (int i = 0; i < count; i++){
            orderItems.add(orderPref.getString("id"+i, "No order placed"));
        }

    }

    private void getOrderItemsBreadType(Context context, String preference){
        SharedPreferences orderPref = context.getSharedPreferences(preference,Context.MODE_PRIVATE);
        orderItemsBreadType = new ArrayList<>();

        int count = orderPref.getInt("count",0);

        /** increments through the order, if the order is true (meaning it's light bread), a String res will be added to the arraylist
         *
         */
        if (preference == context.getString(R.string.current_database_order)){
            for (int i = 0; i < count; i++) {
                if (orderPref.getBoolean("light_bread" + i, false)) {
                    orderItemsBreadType.add("LYS");
                } else if (!orderPref.getBoolean("light_bread" + i, false)) {
                    orderItemsBreadType.add("MØRK");
                }
            }
        }
        else {
            for (int i = 0; i < count; i++) {
                if (orderPref.getBoolean("light_bread" + i, false)) {
                    orderItemsBreadType.add(context.getString(R.string.breadtype_light));
                } else if (!orderPref.getBoolean("light_bread" + i, false)) {
                    orderItemsBreadType.add(context.getString(R.string.breadtype_dark));
                }
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

    public int getCount(Context context){
        SharedPreferences orderPref = context.getSharedPreferences(context.getString(R.string.current_order_pref),Context.MODE_PRIVATE);
        return orderPref.getInt("count",0);
    }

}
