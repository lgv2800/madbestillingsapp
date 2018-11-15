package com.example.gruppeb.madbestillingsapp.Domain;

import java.util.ArrayList;

public class Order {
    private ArrayList<Dish> orderItems;

    public Order(){
        orderItems = new ArrayList<>();
    }

    public void addDish(Dish a){
        orderItems.add(a);
    }

    public void removeDish(Dish a){
        orderItems.remove(a);
    }

    public ArrayList<Dish> getOrder(){
        return orderItems;
    }

}
