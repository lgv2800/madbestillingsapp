package com.example.gruppeb.madbestillingsapp.Model;

public class MyOrdersScreenModel {

    private int orderDishID;
    private String orderDishName, orderDishBreadType, orderDishDate;

    public MyOrdersScreenModel(int orderDishID, String orderDishName, String orderDishBreadType, String orderDishDate) {
        this.orderDishID = orderDishID;
        this.orderDishName = orderDishName;
        this.orderDishBreadType = orderDishBreadType;
        this.orderDishDate = orderDishDate;
        //this.orderImageView = orderImageView;
    }

    public int getOrderDishID() {
        return orderDishID;
    }

    public String getOrderDishName() {
        return orderDishName;
    }

    public String getOrderDishDate() {
        return orderDishDate;
    }

    public String getOrderDishBreadType() {
        return orderDishBreadType;
    }
}
