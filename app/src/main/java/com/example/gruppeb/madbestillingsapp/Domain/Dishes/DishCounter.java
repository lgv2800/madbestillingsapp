package com.example.gruppeb.madbestillingsapp.Domain.Dishes;

import android.content.Context;

public class DishCounter {
    private int dishOneAmount, dishTwoAmount, dishThreeAmount, dishFourAmount, dishFiveAmount;

    public int getTotalDishAmount(){
        return dishOneAmount+dishTwoAmount+dishThreeAmount+dishFourAmount+dishFiveAmount;
    }

    public void addDish(int dish){
        switch (dish){
            case 0: dishOneAmount++; break;
            case 1: dishTwoAmount++; break;
            case 2: dishThreeAmount++; break;
            case 3: dishFourAmount++; break;
            case 4: dishFiveAmount++; break;

        }
    }

}
