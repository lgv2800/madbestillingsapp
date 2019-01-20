package com.example.gruppeb.madbestillingsapp.Domain;

public class MyOrdersScreenModel {

    private int orderDishID;
    private String orderDishName, orderDishDscription, orderDishBreadType, orderDishDate;
    private int orderImageView;

    public MyOrdersScreenModel(int orderDishID, String orderDishName, String orderDishDscription, String orderDishBreadType, String orderDishDate, int orderImageView) {
        this.orderDishID = orderDishID;
        this.orderDishName = orderDishName;
        this.orderDishDscription = orderDishDscription;
        this.orderDishBreadType = orderDishBreadType;
        this.orderDishDate = orderDishDate;
        this.orderImageView = orderImageView;
    }

    public int getOrderDishID() {
        return orderDishID;
    }

    public String getOrderDishName() {
        return orderDishName;
    }

    public String getOrderDishDscription() {
        return orderDishDscription;
    }

    public String getOrderDishDate() {
        return orderDishDate;
    }

    public String getOrderDishBreadType() {
        return orderDishBreadType;
    }

    public int getOrderImageView() {
        return orderImageView;
    }
}
