package com.example.gruppeb.madbestillingsapp.Domain;

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

    public void order(int a){
        Dish b = factory.createDish(a);
        counter.addDish(a);
        addDish(b);
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
