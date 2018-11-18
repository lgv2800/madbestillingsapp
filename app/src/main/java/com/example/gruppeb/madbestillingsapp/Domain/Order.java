package com.example.gruppeb.madbestillingsapp.Domain;

import android.content.Context;

import com.example.gruppeb.madbestillingsapp.Domain.Dishes.Dish;
import com.example.gruppeb.madbestillingsapp.Domain.Dishes.DishCounter;
import com.example.gruppeb.madbestillingsapp.Domain.Dishes.DishFactory;

import java.util.ArrayList;

public class Order {

    DishFactory factory = new DishFactory();
    private ArrayList<Dish> orderItems;
    DishCounter counter = new DishCounter();

    public Order(){
        orderItems = new ArrayList<>();
    }

    public void deleteDish(Dish a){
        removeDish(a);
    }

    public void clearOrder(){
        orderItems.clear();
    }

    public void order(int a, Context context){
        Dish dish = factory.createDish(a, context);
        counter.addDish(a);
        addDish(dish);
    }

    private void addDish(Dish a){
        orderItems.add(a);
    }

    private void removeDish(Dish a){
        orderItems.remove(a);
    }

    public ArrayList<Dish> getOrder(){
        return orderItems;
    }

}
