package com.example.gruppeb.madbestillingsapp.Domain.Dishes;


public class DishFactory {

    public Dish createDish(int dishNumber){

        Dish a;
        switch(dishNumber){
            case 0: a = new DishOne(); return a;
            case 1: a = new DishTwo(); return a;
            case 2: a = new DishThree(); return a;
            case 3: a = new DishFour(); return a;
            case 4: a = new DishFive(); return a;
        }

        return null;
    }

}
